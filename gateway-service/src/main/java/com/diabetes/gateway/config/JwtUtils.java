package com.diabetes.gateway.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;

import java.util.Date;

/**
 * Classe utilitaire pour générer et valider des tokens JWT.
 */
public class JwtUtils {

    // Au moins 32 caractères (>= 256 bits) pour HS256
    // Ici, 36 caractères par exemple
     private static final String SECRET = "mySuperLongSecretOfAtLeast32+characters123456";


    // Durée de validité en millisecondes (ex: 1 heure)
    private static final long EXPIRATION = 3600000L;

    /**
     * Génère un token JWT pour un username donné.
     *
     * @param username l'identifiant de l'utilisateur
     * @return un token JWT signé HS256
     */
    public static String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION);

        return Jwts.builder()
                .setSubject(username)        // Le "nom" ou identifiant
                .setIssuedAt(now)           // Date d'émission
                .setExpiration(expiryDate)  // Date d'expiration
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    /**
     * Valide un token : parse le token, vérifie la signature HS256
     * et renvoie les claims si tout est OK.
     *
     * @param token le token JWT à valider
     * @return un objet Claims (contenant le "sub", la date d'expiration, etc.)
     * @throws io.jsonwebtoken.JwtException si le token est invalide ou expiré
     */
    public static Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
