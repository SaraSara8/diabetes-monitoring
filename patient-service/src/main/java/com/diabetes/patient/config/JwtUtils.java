package com.diabetes.patient.config;



import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;
import java.util.Date;

public class JwtUtils {

    private static final String SECRET = "mySuperLongSecretOfAtLeast32+characters123456";
    // Important: Doit être le même que dans la gateway si on veut valider la signature

    public static Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
