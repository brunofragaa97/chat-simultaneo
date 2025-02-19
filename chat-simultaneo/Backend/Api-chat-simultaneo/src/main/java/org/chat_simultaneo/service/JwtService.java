package org.chat_simultaneo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    // A chave secreta usada para assinar e validar o JWT
    private static final String SECRET_KEY = "MINHA_CHAVE_SECRETA_AQUI_MINHA_CHAVE_SECRETA_AQUI"; // Deve ter pelo menos 256 bits

    // Método para gerar a chave de assinatura a partir da SECRET_KEY
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // Decodifica a chave secreta base64
        return Keys.hmacShaKeyFor(keyBytes); // Gera a chave HMAC
    }

    // Método para gerar um token JWT
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Adiciona o nome de usuário como assunto do token
                .setIssuedAt(new Date(System.currentTimeMillis())) // Data de emissão
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Validade do token (1 hora)
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // Assina o token com a chave secreta
                .compact();
    }

    // Método para validar o token JWT
    public boolean validateToken(String token, String username) {
        // Verifica se o username do token corresponde ao que é passado e se o token não está expirado
        return extractUsername(token).equals(username) && !isTokenExpired(token);
    }

    // Método para extrair o nome de usuário do token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // Extrai o nome de usuário (subject) do token
    }

    // Método genérico para extrair uma claim (informação) do token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); // Extrai todas as claims do token
        return claimsResolver.apply(claims); // Aplica a função na claim (ex: obter o nome de usuário)
    }

    // Método para extrair todas as claims do token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey()) // Define a chave de assinatura
                .build()
                .parseClaimsJws(token) // Faz o parse do JWT
                .getBody(); // Retorna o corpo das claims
    }

    // Método para verificar se o token está expirado
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date()); // Verifica a data de expiração
    }
}
