package org.chat_simultaneo.DTO;

public class AuthResponseDto {

    private String message;
    private boolean sucesso;

    public AuthResponseDto(String message, boolean sucesso){
        this.message = message;
        this.sucesso = sucesso;
    }

    public String getMessage(){
        return message;
    }
    public boolean isSucesso(){
        return sucesso;
    }
}
