package org.chat_simultaneo.models;


import jakarta.persistence.*;

//Informa que a classe é uma entidade JPA-hibernate
@Entity
@Table(name = "usuarios")

public class Usuario {
    //cria um id unico automatico no banco
    @Id
    @GeneratedValue
    private long id;

    private String nome;
    private String senha;
    private String email;

    //Construtor vazio (é necessario para utilizar JPA)
    public Usuario() {

    }

    //Construtor com Parametros
    public Usuario(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    //Getters and Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
