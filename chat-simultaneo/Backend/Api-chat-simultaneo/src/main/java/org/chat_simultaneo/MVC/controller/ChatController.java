package org.chat_simultaneo.MVC.controller;

import org.chat_simultaneo.MVC.models.Chat;
import org.chat_simultaneo.MVC.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // Endpoint GET pra buscar o histórico de mensagens
    @GetMapping
    public List<Chat> getMessages() {
        return chatService.findAll();
    }
}