package org.chat_simultaneo.service;

import org.chat_simultaneo.models.Usuario;
import org.chat_simultaneo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class AuthUsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Boolean autenticarUsuario(String email, String senha){
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        if(usuarioOptional.isPresent()){
            Usuario usuario = usuarioOptional.get();
            return usuario.getSenha().equals(senha);
        }
        return false;
    }
}
