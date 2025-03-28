package com.utd.ti.soa.esb_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.utd.ti.soa.esb_service.model.Client;
import reactor.core.publisher.Mono;
import java.util.List;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/v1/esb")
public class ClientController {  

    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:5000/api/clients")
            .defaultHeader("Content-Type", "application/json")
            .build();

    // ✅ Obtener todos los clientes
    @GetMapping("/clients")
    public ResponseEntity<?> getClients() {
        try {
            List<Client> clients = webClient.get()
                .uri("/all")
                .retrieve()
                .bodyToFlux(Client.class)
                .collectList()
                .block();

            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener clientes: " + e.getMessage());
        }
    }

    // ✅ Obtener cliente por ID
    @GetMapping("/client/{id}")
    public ResponseEntity<?> getClientById(@PathVariable int id) {
        try {
            Client client = webClient.get()
                .uri("/" + id)
                .retrieve()
                .bodyToMono(Client.class)
                .block();

            return ResponseEntity.ok(client);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado: " + e.getMessage());
        }
    }

    // ✅ Crear nuevo cliente
    @PostMapping("/client")
    public ResponseEntity<?> createClient(@RequestBody Client client) {
        try {
            String response = webClient.post()
                .bodyValue(client)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear cliente: " + e.getMessage());
        }
    }

    // ✅ Actualizar cliente por ID
    @PutMapping("/client/{id}")
    public ResponseEntity<?> updateClient(@PathVariable int id, @RequestBody Client client) {
        try {
            String response = webClient.put()
                .uri("/" + id)
                .bodyValue(client)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar cliente: " + e.getMessage());
        }
    }

    // ✅ Desactivar cliente
    @PutMapping("/client/desactivate/{id}")
    public ResponseEntity<?> desactivateClient(@PathVariable int id) {
        try {
            String response = webClient.put()
                .uri("/desactivate/" + id)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error al desactivar cliente: " + e.getMessage());
        }
    }
}
