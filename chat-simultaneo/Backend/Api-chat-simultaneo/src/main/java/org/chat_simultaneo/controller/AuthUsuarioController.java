package org.chat_simultaneo.controller;


import org.chat_simultaneo.DTO.AuthResponseDto;
import org.chat_simultaneo.models.Usuario;
import org.chat_simultaneo.service.AuthUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthUsuarioController {

    @Autowired
    private AuthUsuarioService authUsuarioService;

    @PostMapping("authUser")
    public ResponseEntity<AuthResponseDto> logarUsuario(@RequestBody Usuario usuario) {

        boolean estaAutenticado = authUsuarioService.autenticarUsuario(usuario.getEmail(), usuario.getSenha());

        if (estaAutenticado) {
            System.out.println("Usuario: " + usuario.getEmail() + " Autenticado com sucesso");
            return ResponseEntity.ok(new AuthResponseDto("Usuario Autenticado com SUCESSO", true));

        } else {
            System.out.println("Usuario: " + usuario.getEmail() + "  Não autorizado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponseDto("Usuario ou senha invalidos", false));
        }
    }

}
