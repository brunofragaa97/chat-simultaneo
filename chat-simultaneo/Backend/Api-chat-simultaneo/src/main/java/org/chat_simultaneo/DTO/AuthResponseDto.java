package org.chat_simultaneo.DTO;

public class AuthResponseDto {

    private String message;
    private boolean sucesso;
    private String token;

    public AuthResponseDto(String message, boolean sucesso, String token){
        this.message = message;
        this.sucesso = sucesso;
        this.token = token;
    }
    public String getMessage(){
        return message;
    }
    public String getToken(){
        return token;
    }
    public boolean isSucesso(){
        return sucesso;
    }
}
