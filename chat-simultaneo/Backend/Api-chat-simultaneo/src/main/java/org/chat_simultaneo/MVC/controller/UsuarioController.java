package org.chat_simultaneo.MVC.controller;


import org.chat_simultaneo.MVC.models.Usuario;
import org.chat_simultaneo.MVC.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastrarUsuario")
    public Usuario registrarUsuario(@RequestBody Usuario usuario){
        return usuarioService.salvarUsuario(usuario);
    }


}
