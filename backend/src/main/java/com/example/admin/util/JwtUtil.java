package com.example.admin.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    /**
     * 获取签名密钥
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * 从token中获取用户名
     */
    public String getUsernameFromToken(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                System.err.println("Token为空");
                return null;
            }
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                System.err.println("Claims为空");
                return null;
            }
            return claims.getSubject();
        } catch (Exception e) {
            System.err.println("解析token失败: " + e.getMessage());
            System.err.println("Token内容: " + token);
            return null;
        }
    }
    
    /**
     * 从token中获取Claims
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            if (token == null || token.trim().isEmpty()) {
                return null;
            }
            claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.err.println("解析JWT Claims失败: " + e.getMessage());
        }
        return claims;
    }
    
    /**
     * 生成token
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        claims.put("created", new Date());
        return generateToken(claims);
    }
    
    /**
     * 根据Claims生成token
     */
    private String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiration);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * 验证token是否有效
     */
    public Boolean validateToken(String token, String username) {
        String usernameFromToken = getUsernameFromToken(token);
        return (usernameFromToken != null && usernameFromToken.equals(username) && !isTokenExpired(token));
    }
    
    /**
     * 判断token是否已过期
     */
    private Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
    
    /**
     * 判断token是否即将过期（30分钟内）
     */
    public Boolean isTokenNearExpiry(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            long diff = expiration.getTime() - System.currentTimeMillis();
            // 如果剩余时间少于30分钟，返回true
            return diff < 30 * 60 * 1000;
        } catch (Exception e) {
            return true;
        }
    }
    
    /**
     * 刷新token
     */
    public String refreshToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims != null) {
                String username = claims.getSubject();
                return generateToken(username);
            }
        } catch (Exception e) {
            System.err.println("刷新token失败: " + e.getMessage());
        }
        return null;
    }
}
