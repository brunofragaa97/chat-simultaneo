package org.chat_simultaneo.DTO;

import java.time.LocalDateTime;

public class ErrorResponse {
    private String codigo;
    private String mensagem;
    private LocalDateTime timeStamp;


    public ErrorResponse(String codigo, String mensagem, LocalDateTime timeStamp){
        this.codigo = codigo;
        this.mensagem = mensagem;
        this.timeStamp = timeStamp;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
}


