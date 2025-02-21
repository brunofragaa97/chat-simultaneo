package org.chat_simultaneo.service;


import org.chat_simultaneo.DTO.UsuarioDto;
import org.chat_simultaneo.models.Usuario;
import org.chat_simultaneo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario salvarUsuario (Usuario usuario){
        return usuarioRepository.save(usuario);
    }
    public Usuario findByEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado:" + email));
    }

}
