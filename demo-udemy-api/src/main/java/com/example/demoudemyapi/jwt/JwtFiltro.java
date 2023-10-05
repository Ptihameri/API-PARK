package com.example.demoudemyapi.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtFiltro extends OncePerRequestFilter {

    @Autowired
    private JwtDetalhesDoUsuarioServico detaislsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String token = request.getHeader(JwtUtils.JWT_AUTORIZACAO);
        if (token == null || !token.startsWith(JwtUtils.JWT_BEARER)) {
            log.info("Token inexistente ou inválido t001");
            filterChain.doFilter(request, response);
            return;
        }
        if (!JwtUtils.isTokenValido(token)){
            log.warn("Token inválido ou expirado t002");
            filterChain.doFilter(request, response);
            return;
        }
        String username = JwtUtils.getUsuarioPeloToken(token);

        toAuthentication(request, username);

        filterChain.doFilter(request, response);

    }
    private void toAuthentication(HttpServletRequest request, String username){
        UserDetails userDetails = detaislsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authenticationToken =
                UsernamePasswordAuthenticationToken.authenticated(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    }
}
