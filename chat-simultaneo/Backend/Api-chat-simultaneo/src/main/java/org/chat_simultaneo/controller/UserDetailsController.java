package org.chat_simultaneo.controller;

import org.chat_simultaneo.DTO.UsuarioDto;
import org.chat_simultaneo.models.Usuario;
import org.chat_simultaneo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserDetailsController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/userDetails")
    public ResponseEntity<UsuarioDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("UserDetails recebido: " + userDetails);
        if (userDetails == null) {
            System.out.println("UserDetails é null - usuário não autenticado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            String email = userDetails.getUsername();
            System.out.println("Buscando usuário com email: " + email);
            Usuario usuario = usuarioService.findByEmail(email);
            UsuarioDto usuarioDto = new UsuarioDto(usuario.getNome(), usuario.getEmail(), usuario.getImgPerfil(), "Online");
            System.out.println("Dados User: => " + usuarioDto);
            return ResponseEntity.ok(usuarioDto);
        } catch (UsernameNotFoundException e) {
            System.out.println("Usuário não encontrado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            System.out.println("Erro ao processar userDetails: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}