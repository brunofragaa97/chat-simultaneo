package org.chat_simultaneo.MVC.service;

import org.chat_simultaneo.MVC.models.Chat;
import org.chat_simultaneo.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    // Salva uma mensagem no banco
    public Chat save(Chat chat) {
        return chatRepository.save(chat);
    }

    // Busca todas as mensagens do banco
    public List<Chat> findAll() {
        return chatRepository.findAll();
    }
}