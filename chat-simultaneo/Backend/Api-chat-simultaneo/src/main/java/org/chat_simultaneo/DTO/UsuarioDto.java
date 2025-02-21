package org.chat_simultaneo.DTO;

public class UsuarioDto {
    private long id;

    private String nome;
    private String email;
    private String imgPerfil;
    private String status;


    //Construtor vazio (é necessario para utilizar JPA)
    public UsuarioDto() {

    }

    public long getId() {
        return id;
    }


    //Construtor com Parametros
    public UsuarioDto(String nome, String email, String imgPerfil, String status) {
        this.nome = nome;
        this.email = email;
        this.imgPerfil = imgPerfil;
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

    public String getImgPerfil() {
        return imgPerfil;
    }

    public void setImgPerfil(String imgPerfil) {
        this.imgPerfil = imgPerfil;
    }

    public void setStatus(String status) { this.status = status; }

    public String getStatus() { return status; }

    @Override
    public String toString() {
        return "UsuarioDto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", Caminho imagem='" + imgPerfil + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

