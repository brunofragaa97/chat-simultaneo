package org.chat_simultaneo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;

public class JwtUtil {

    private static final byte[] SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
    private static final long EXPIRATION_TIME = 864_000_000; // 10 dias em milissegundos

    // Gera o token JWT
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY), SignatureAlgorithm.HS256) // Usa SECRET_KEY diretamente
                .compact();
    }

    // Valida o token e retorna o username
    public static String validateToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY)) // Usa SECRET_KEY diretamente
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}