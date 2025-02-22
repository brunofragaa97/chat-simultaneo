package org.chat_simultaneo.service;


import org.chat_simultaneo.models.Usuario;
import org.chat_simultaneo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario salvarUsuario (Usuario usuario){
        return usuarioRepository.save(usuario);
    }
}
