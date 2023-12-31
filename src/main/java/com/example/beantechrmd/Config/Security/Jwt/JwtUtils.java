package com.example.beantechrmd.Config.Security.Jwt;

import java.security.Key;
import java.util.Date;

import com.example.beantechrmd.Model.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/*
    La classe di utility del Jwt ha tre diverse competenze:
    1. Raccogliere i jwt dai cookie
    2. Elaborare dei nuovi cookie con jwt pulito
    3. Validare il jwt
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("UniUd")
    private String jwtCookieName;
    @Value("1000000")
    private int jwtExpirationMs;
    @Value("HotSecretStuff")
    private String jwtSecret;

    //Creazione JWT
    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        return ResponseCookie.from(jwtCookieName, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
    }

    //Pulizia JWT
    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookieName, null).path("/api").build();
    }

    //Raccolta JWT
    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    //Prendere il nome utente dal JWT
    public String getUserNameFromJwtToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //Decodifica JWT
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    //Creazione token
    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    //Validazione token
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("JWT invalido {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT scaduto {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT non supportato: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT vuoto: {}", e.getMessage());
        }
        return false;
    }
}
