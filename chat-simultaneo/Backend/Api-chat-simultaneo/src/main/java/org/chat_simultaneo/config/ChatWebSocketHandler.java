package org.chat_simultaneo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.chat_simultaneo.security.JwtUtil;
import org.chat_simultaneo.service.UsuarioService;
import org.chat_simultaneo.models.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String query = session.getUri().getQuery();
        String token = null;
        if (query != null && query.startsWith("token=")) {
            token = query.substring(6); // Extrai o token da URL
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
                    System.out.println("Usuário não encontrado: " + email);
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
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String email = findEmailBySession(session);
        if (email != null) {
            sessions.remove(email);
            broadcastUserList();
            System.out.println("Conexão WebSocket fechada para: " + email);
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
                System.out.println("Erro ao buscar usuário: " + email);
            }
        }
        String json = objectMapper.writeValueAsString(userList);
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(json));
            }
        }
    }
}