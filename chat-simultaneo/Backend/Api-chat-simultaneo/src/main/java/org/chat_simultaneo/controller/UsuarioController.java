package org.chat_simultaneo.controller;


import org.chat_simultaneo.DTO.UsuarioDto;
import org.chat_simultaneo.models.Usuario;
import org.chat_simultaneo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
