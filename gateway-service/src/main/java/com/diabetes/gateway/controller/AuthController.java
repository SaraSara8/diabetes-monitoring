package com.diabetes.gateway.controller;

import com.diabetes.gateway.config.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * Endpoint pour se "logger" : attend un username + password.
     * En cas de succès, renvoie un token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        // Vérification minimaliste: admin / password
        // (Pour un vrai usage, on ferait appel à un UserDetailsService, Bcrypt, etc.)
        if ("admin".equals(request.getUsername()) && "1234".equals(request.getPassword())) {
            // Générer un token
            String token = JwtUtils.generateToken(request.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    // DTO pour la requête
    static class LoginRequest {
        private String username;
        private String password;
        // getters / setters
        public String getUsername() {return username;}
        public void setUsername(String u) {this.username = u;}
        public String getPassword() {return password;}
        public void setPassword(String p) {this.password = p;}
    }

    // DTO pour la réponse
    static class AuthResponse {
        private String token;
        public AuthResponse(String token) {this.token = token;}
        public String getToken() {return token;}
        public void setToken(String token) {this.token = token;}
    }
}
