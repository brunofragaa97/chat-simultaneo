package org.chat_simultaneo.controller;


//CLASSE CRIADA PARA RESPONDER AS REQUISIÇÕES DE INFORMAÇÕES DO USUARIO

import org.chat_simultaneo.DTO.UsuarioDto;
import org.chat_simultaneo.models.Usuario;
import org.chat_simultaneo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserDetailsController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/userDetails")
    public ResponseEntity<UsuarioDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails){
        String email = userDetails.getUsername();
        Usuario usuario = usuarioService.findByEmail(email);
        if (usuario == null) {
            return ResponseEntity.notFound().build(); // Retorna 404 caso o usuário não seja encontrado
        }
        UsuarioDto usuarioDto = new UsuarioDto(usuario.getNome(), usuario.getEmail(),  "Online");

        return ResponseEntity.ok(usuarioDto);
    }


}
