package org.chat_simultaneo.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

import io.jsonwebtoken.Jwts;

// Supondo que você tenha uma maneira de extrair informações do token, como:
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;

    public JwtAuthenticationToken(String token) {
        super(Collections.emptyList());
        this.token = token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        try {
            // Aqui, você decodifica o token para obter o nome do usuário ou outra informação de identificação
            String username = Jwts.parser().setSigningKey("your-secret-key").parseClaimsJws(token).getBody().getSubject();
            return username; // ou qualquer outra informação relevante presente no token
        } catch (Exception e) {
            // Se o token for inválido ou não puder ser decodificado
            return "unknown";
        }
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        // Como antes
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}