package com.diabetes.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * Endpoint GET pour vérifier l'authentification via Basic Auth.
     */
    @GetMapping("/login")
    public ResponseEntity<String> loginGet() {
        return ResponseEntity.ok("Authentifié avec Basic Auth via GET");
    }

    /**
     * Endpoint POST pour vérifier l'authentification via Basic Auth.
     * Lorsque vous faites un POST sur /auth/login, l'authentification Basic sera vérifiée
     * par Spring Security avant d'atteindre cet endpoint.
     */
    @PostMapping("/login")
    public ResponseEntity<String> loginPost() {
        return ResponseEntity.ok("Authentifié avec Basic Auth via POST");
    }
}
