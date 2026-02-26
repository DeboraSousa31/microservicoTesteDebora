package br.com.foxinline.shared.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys; 

public class CryptoUtils {

    // CHAVE SECRETA ORIGINAL
    private static final String SECRET_KEY = "cGO/TG^X)1BGlR@)0|Hnd%(]IhfzT{&&uaN=Xw6k[nx:MhPlM;{%HOS!*r>Z,b>%";
    private static final SecretKey SECRET_KEY_OBJECT = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
                                                                                                                       
                                                                                                                      
    // private static final long EXPIRATION_TIME_MS = TimeUnit.HOURS.toMillis(1);

    private static SecretKey getKey() {
        return SECRET_KEY_OBJECT;
    }

    public static String encriptografar(String caminho) {
        if (caminho == null || caminho.trim().isEmpty()) {
            throw new IllegalArgumentException("Caminho não pode ser nulo ou vazio");
        }

        if (caminho.startsWith("/")) {
            caminho = caminho.substring(1);
        }

        return Jwts.builder()
                .claim("data", caminho)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public static String descriptografar(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("data", String.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Token inválido", e);
        }
    }

    // --- NOVOS MÉTODOS (PARA AUTENTICAÇÃO DE USUÁRIO) ---

    /**
     * Gera um JWT contendo o nome de usuário (subject) e um tempo de expiração.
     * Este é o token de autenticação usado para o SSO entre serviços.
     * 
     * @param username O nome de usuário a ser incluído no token.
     * @return O JWT assinado.
     */
    public static String generateAuthToken(String username, Long userId, Long credenciadaUsuarioId) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username não pode ser nulo ou vazio para gerar o token.");
        }
        if (userId == null) {
            throw new IllegalArgumentException("UserId não pode ser nulo para gerar o token.");
        }
        if (credenciadaUsuarioId == null) {
            throw new IllegalArgumentException("credenciadaUsuarioId não pode ser nulo para gerar o token.");
        }

        Date now = new Date();
        // Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME_MS);

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("credenciadaId", credenciadaUsuarioId) // ⚡ aqui adiciona a credenciada
                .setIssuedAt(now)
                // .setExpiration(expiryDate)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Valida o token e extrai o nome de usuário.
     * 
     * @param token O JWT do usuário.
     * @return O username.
     * @throws SecurityException Se o token for inválido, expirado ou a assinatura
     *                           não coincidir.
     */
    public static String getUsernameFromAuthToken(String token) {
        try {
            Claims claims = Jwts.parser() // Usando parserBuilder (padrão JJWT moderno)
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject(); // Extrai o nome de usuário do 'subject'

        } catch (Exception e) {
            // Lançar SecurityException é padrão para falhas de autenticação de token
            throw new SecurityException("Token de autenticação inválido, expirado ou com assinatura incorreta.", e);
        }
    }
}
