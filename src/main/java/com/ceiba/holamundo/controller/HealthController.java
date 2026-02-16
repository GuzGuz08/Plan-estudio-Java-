package com.ceiba.holamundo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller para verificar que la API está funcionando.
 * 
 * @RestController = le dice a Spring: "esta clase maneja peticiones HTTP y responde JSON"
 * @RequestMapping("/api") = todas las URLs de esta clase empiezan con /api
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    /**
     * Endpoint: GET http://localhost:8080/api/health
     * 
     * Retorna: {"status": "OK", "message": "API funcionando"}
     * 
     * @GetMapping = este método responde cuando alguien hace GET a /api/health
     */
    @GetMapping("/health")
    public Map<String, String> health() {
        // Creamos un Map (como un diccionario clave-valor)
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("status", "OK");
        respuesta.put("message", "API funcionando");
        
        // Spring convierte automáticamente este Map a JSON
        return respuesta;
    }
}
