package com.example.demoudemyapi.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class JwtUtils {

    public static final String JWT_BEARER = "Bearer ";

    public static final String JWT_AUTORIZACAO = "Authorization";

    public static final String CHAVE_SECRETA = "0123456789-0123456789-0123456789";

    public static final long EXPIRE_DAYS = 0;
    public static final long EXPIRE_HOURS = 0;
    public static final long EXPIRE_MINUTES = 20;

    private JwtUtils() {
    }

    private static Key gerarChave() {
        return Keys.hmacShaKeyFor(CHAVE_SECRETA.getBytes(StandardCharsets.UTF_8));
    }

    private static Date toExpirarData(Date start) {
        LocalDateTime tempo = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime fim = tempo.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);
        return Date.from(fim.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static JwtToken createToken(String username, String role) {
        Date agora = new Date();
        Date expirar = toExpirarData(agora);

        String token = Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setSubject(username)
                .setIssuedAt(agora)
                .setExpiration(expirar)
                .signWith(gerarChave(), SignatureAlgorithm.HS256)
                .claim("role", role)
                .compact();
        return new JwtToken(token);
    }
    private static Claims getClaimsPeloToken(String token) {
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(gerarChave())
                    .build()
                    .parseClaimsJws(refatorarToken(token))
                    .getBody();

        }catch (JwtException ex){
            log.error(String.format("Token Invalido %s", ex.getMessage()));
        }
        return null;
    }
    public static String getUsuarioPeloToken(String token) {
        return getClaimsPeloToken(token).getSubject();
    }
    public static boolean isTokenValido(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(gerarChave())
                    .build()
                    .parseClaimsJws(refatorarToken(token));
            return true;
        } catch (JwtException e) {
            log.error(String.format("Token expirado ou invalido %s", e.getMessage()));
        }
        return false;

    }
    private static String refatorarToken (String token){
        if (token.contains(JWT_BEARER)) {
            return token.substring(JWT_BEARER.length());
        }
        return token;
    }
}
