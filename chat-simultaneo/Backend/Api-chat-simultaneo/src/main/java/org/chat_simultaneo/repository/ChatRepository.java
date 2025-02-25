package org.chat_simultaneo.repository;

import org.chat_simultaneo.MVC.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}