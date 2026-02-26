package br.com.foxinline.shared.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class CriptografiaUtils {

    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "foxinlineTechnologiesCerubEdshow";
    private static final SecretKey FIXED_KEY = getFixedKey();

    // ========================================
    // 🔐 AES ENCRYPT / DECRYPT
    // ========================================
    public static SecretKey getFixedKey() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length != 32) {
            byte[] newKey = new byte[32];
            System.arraycopy(keyBytes, 0, newKey, 0, Math.min(keyBytes.length, 32));
            keyBytes = newKey;
        }
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static String encrypt(String token) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, FIXED_KEY);
        byte[] encryptedBytes = cipher.doFinal(token.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encryptedBytes);
    }

    public static String decrypt(String encryptedToken) throws Exception {
        byte[] encryptedBytes = hexToBytes(encryptedToken);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, FIXED_KEY);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static byte[] hexToBytes(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return bytes;
    }

    public static String gerarToken(Long id) {
        try {
            return encrypt(String.valueOf(id));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ========================================
    // 🔒 JWT (TOKEN SEGURO)
    // ========================================
    private static SecretKey getJwtKey() {
        // Gera uma chave HMAC-SHA com base na mesma senha (ou variável de ambiente)
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

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

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("credenciadaId", credenciadaUsuarioId)
                .setIssuedAt(now)
                .signWith(getJwtKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims validateToken(String tokenJWT) {
        return Jwts.parser()
                .setSigningKey(getJwtKey())
                .build()
                .parseClaimsJws(tokenJWT)
                .getBody();
    }
}