package org.chat_simultaneo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.*;
import java.util.Date;

public class JwtUtil {

    private static final byte[] SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
    private static final long EXPIRATION_TIME = 864_000_000; // 10 dias em milissegundos

    // Gera o token JWT
    public static String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY), SignatureAlgorithm.HS256) // Usa SECRET_KEY diretamente
                .compact();
    }
    public static String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Valida o token e retorna o username
    public static String validateToken(String token) {
        try {
            return getEmailFromToken(token);
        } catch (ExpiredJwtException e) {
            System.out.println("Token expirado!");
        } catch (UnsupportedJwtException e) {
            System.out.println("Token não suportado!");
        } catch (MalformedJwtException e) {
            System.out.println("Token malformado!");
        } catch (SignatureException e) {
            System.out.println("Assinatura inválida!");
        } catch (IllegalArgumentException e) {
            System.out.println("Token inválido!");
        }
        return null; // Retorna null se o token for inválido
    }
}