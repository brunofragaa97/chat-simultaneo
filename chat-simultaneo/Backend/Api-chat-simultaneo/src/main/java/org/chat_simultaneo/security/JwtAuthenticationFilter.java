package org.chat_simultaneo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        System.out.println("Header Authorization: " + header);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            System.out.println("Token recebido: " + token);
            try {
                String email = JwtUtil.validateToken(token);
                System.out.println("Email extraído do token: " + email);
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    if (userDetailsService == null) {
                        System.out.println("UserDetailsService não injetado!");
                        throw new IllegalStateException("UserDetailsService is null");
                    }
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    System.out.println("UserDetails carregado: " + userDetails.getUsername());
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("Autenticação configurada para: " + email);
                } else {
                    System.out.println("Email nulo ou autenticação já existente");
                }
            } catch (Exception e) {
                System.out.println("Erro ao processar token: " + e.getMessage());
                e.printStackTrace();
                SecurityContextHolder.clearContext();
            }
        } else {
            System.out.println("Nenhum token Bearer encontrado no header");
        }
        filterChain.doFilter(request, response);
    }
}