package com.example.Gestione_eventi_con_prenotazione.security;


import com.example.Gestione_eventi_con_prenotazione.exception.NotFoundException;
import com.example.Gestione_eventi_con_prenotazione.exception.UnAuthorizedException;
import com.example.Gestione_eventi_con_prenotazione.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTool jwtTool;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                jwtTool.validateToken(token);
                User user = jwtTool.getUserFromToken(token);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (NotFoundException | UnAuthorizedException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
                return;
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Errore durante la validazione del token: " + e.getMessage());
                return;
            }
        }


        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
