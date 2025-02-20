package org.chat_simultaneo.controller;


import org.chat_simultaneo.DTO.AuthResponseDto;
import org.chat_simultaneo.models.Usuario;
import org.chat_simultaneo.security.JwtUtil;
import org.chat_simultaneo.service.AuthUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginUserController {

    @Autowired
    private AuthUsuarioService authUsuarioService;

    @PostMapping("authUser")
    public ResponseEntity<AuthResponseDto> logarUsuario(@RequestBody Usuario usuario) {

        boolean estaAutenticado = authUsuarioService.autenticarUsuario(usuario.getEmail(), usuario.getSenha());

        if (estaAutenticado) {

            String token = JwtUtil.generateToken(usuario.getEmail());
            System.out.println("Usuario: " + usuario.getEmail() + " Autenticado com sucesso" + "\nToken: " + token);
            return ResponseEntity.ok(new AuthResponseDto("Usuario Autenticado com SUCESSO", true, token));

        } else {
            System.out.println("Usuario: " + usuario.getEmail() + "  Não autorizado");
            String token = "Token não gerado";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponseDto("Usuario ou senha invalidos", false, token));
        }
    }

}
