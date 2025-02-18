package org.chat_simultaneo.controller;


import org.chat_simultaneo.service.AuthUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/auth")
public class AuthUsuarioController {

    @Autowired
    private AuthUsuarioService authUsuarioService;

    @PostMapping ("authUser")
    public String logarUsuario(@RequestParam String email, @RequestParam String senha) {

        boolean estaAutenticado = authUsuarioService.autenticarUsuario(email, senha);

        if(estaAutenticado){
            return "Usuario Autenticado";

        }
        return "Usuario ou senha invalidos";
    }

}
