package org.chat_simultaneo.DTO;

public class UsuarioDto {
    private long id;

    private String nome;
    private String email;
    private String status;


    //Construtor vazio (é necessario para utilizar JPA)
    public UsuarioDto() {

    }

    public long getId() {
        return id;
    }


    //Construtor com Parametros
    public UsuarioDto(String nome, String email, String status) {
        this.nome = nome;
        this.email = email;
        this.status = status;
    }

    //Getters and Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(String status) { this.status = status; }

    public String getStatus() { return status; }
}

