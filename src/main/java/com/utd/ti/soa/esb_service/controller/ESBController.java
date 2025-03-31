package com.utd.ti.soa.esb_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpStatus;
import com.utd.ti.soa.esb_service.model.User;
import java.util.List;

@RestController
@RequestMapping("http://user-service.railway.internal:6000/api/v1/esb")
public class ESBController {
    private final WebClient webClient = WebClient.builder()
            .baseUrl("user-service.railway.internal/api/v1/esb")
            .defaultHeader("Content-Type", "application/json")
            .build();

    // Obtener todos los usuarios 
    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        try {
            List<User> users = webClient.get()
                .uri("/all")
                .retrieve()
                .bodyToFlux(User.class)
                .collectList()
                .block();

            return ResponseEntity.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener usuarios: " + e.getMessage());
        }
    }

    // Crear un nuevo usuario
    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            if (user.getUsername() == null || user.getPassword() == null || user.getPhone() == null) {
                return ResponseEntity.badRequest().body("Teléfono, correo y contraseña son obligatorios");
            }

            String response = webClient.post()
                .bodyValue(user)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear usuario: " + e.getMessage());
        }
    }

    // Actualizar usuario por ID 
    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User user) {
        try {
            String response = webClient.put()
                .uri("/{id}", id)
                .bodyValue(user)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar usuario: " + e.getMessage());
        }
    }

    // Desactivar usuario por ID 
    @PutMapping("/user/desactivate/{id}")
    public ResponseEntity<?> desactivateUser(@PathVariable String id) {
        try {
            String response = webClient.put()
                .uri("/desactivate/{id}", id)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al desactivar usuario: " + e.getMessage());
        }
    }

    // Iniciar sesión 
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            String response = webClient.post()
                .uri("/login")
                .bodyValue(user)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al iniciar sesión: " + e.getMessage());
        }
    }
}
