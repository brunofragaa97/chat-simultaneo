package org.chat_simultaneo.WebSocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.chat_simultaneo.JWT.JwtUtil;
import org.chat_simultaneo.MVC.models.Chat;
import org.chat_simultaneo.MVC.models.Usuario;
import org.chat_simultaneo.MVC.service.ChatService;
import org.chat_simultaneo.MVC.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String query = session.getUri().getQuery();
        String token = null;
        if (query != null && query.startsWith("token=")) {
            token = query.substring(6);
        }

        if (token != null) {
            String email = JwtUtil.validateToken(token);
            if (email != null) {
                try {
                    Usuario usuario = usuarioService.findByEmail(email);
                    sessions.put(email, session);
                    broadcastUserList();
                    System.out.println("Conexão WebSocket estabelecida para: " + email);
                } catch (Exception e) {
                    System.out.println("Erro ao buscar usuário: " + email + " - " + e.getMessage());
                    session.close(CloseStatus.BAD_DATA);
                }
            } else {
                System.out.println("Token inválido: " + token);
                session.close(CloseStatus.BAD_DATA);
            }
        } else {
            System.out.println("Nenhum token fornecido na URL: " + session.getUri());
            session.close(CloseStatus.BAD_DATA);
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String email = findEmailBySession(session);
        if (email == null) {
            System.out.println("Sessão não encontrada para: " + session.getId());
            return;
        }

        String mensagem = message.getPayload();
        System.out.println("Mensagem recebida de " + email + ": " + mensagem + " | Tamanho: " + mensagem.length());

        // Verifica se a mensagem é muito longa (limite arbitrário de 4096 caracteres)
        if (mensagem.length() > 9000) {
            System.out.println("Mensagem muito longa (" + mensagem.length() + " caracteres). Rejeitando.");
            session.sendMessage(new TextMessage("Erro: Mensagem excede o limite de 4096 caracteres"));
            return;
        }

        try {
            Usuario remetente = usuarioService.findByEmail(email);
            Chat chat = new Chat(remetente, mensagem, LocalDateTime.now());
            chatService.save(chat);

            Map<String, Object> messageData = Map.of(
                    "remetente", remetente.getNome(),
                    "mensagem", mensagem,
                    "time", chat.getTime().toString()
            );

            String json = objectMapper.writeValueAsString(messageData);
            System.out.println("Enviando JSON para todos: " + json);

            for (WebSocketSession s : sessions.values()) {
                if (s.isOpen()) {
                    s.sendMessage(new TextMessage(json));
                } else {
                    System.out.println("Sessão fechada detectada: " + s.getId());
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao processar mensagem de " + email + ": " + e.getMessage());
            e.printStackTrace(); // Mostra stacktrace pra debugging
            session.sendMessage(new TextMessage("Erro interno ao processar a mensagem"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String email = findEmailBySession(session);
        if (email != null) {
            sessions.remove(email);
            broadcastUserList();
            System.out.println("Conexão WebSocket fechada para: " + email + " | Status: " + status);
        }
    }

    private String findEmailBySession(WebSocketSession session) {
        for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
            if (entry.getValue().equals(session)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void broadcastUserList() throws Exception {
        Map<String, Object> userList = new ConcurrentHashMap<>();
        for (String email : sessions.keySet()) {
            try {
                Usuario usuario = usuarioService.findByEmail(email);
                userList.put(email, Map.of(
                        "name", usuario.getNome(),
                        "photo", usuario.getImgPerfil() != null ? usuario.getImgPerfil() : "default_photo_url",
                        "status", "online"
                ));
            } catch (Exception e) {
                System.out.println("Erro ao buscar usuário para lista: " + email + " - " + e.getMessage());
            }
        }
        String json = objectMapper.writeValueAsString(userList);
        System.out.println("Enviando lista de usuários: " + json);

        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(json));
            }
        }
    }
}