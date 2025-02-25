package org.chat_simultaneo.MVC.models;


import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "remetente_id", nullable = false)
    private Usuario remetente;

    @Column(nullable = false)
    private String mensagem;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime time;

    public Chat() {

    }

    public Chat(Usuario remetente, String mensagem, LocalDateTime time) {
        this.remetente = remetente;
        this.mensagem = mensagem;
        this.time = time;


    }

    public Usuario getRemetente() {
        return remetente;
    }

    public void setRemetente(Usuario remetente) {
        this.remetente = remetente;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
