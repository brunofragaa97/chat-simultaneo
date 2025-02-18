package org.chat_simultaneo.repository;
import java.util.Optional;
import org.chat_simultaneo.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //Indica que esta classe é um repositorio JPA
public interface UsuarioRepository extends JpaRepository <Usuario, Long>{

    Optional<Usuario> findByEmail(String email);

}
