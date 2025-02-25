package org.chat_simultaneo.repository;
import java.util.Optional;
import org.chat_simultaneo.MVC.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //Indica que esta classe é um repositorio JPA
public interface UsuarioRepository extends JpaRepository <Usuario, Long>{

    Optional<Usuario> findByEmail(String email);

}
