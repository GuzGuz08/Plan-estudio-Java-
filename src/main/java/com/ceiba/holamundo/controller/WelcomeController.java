package com.ceiba.holamundo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller para el saludo de bienvenida.
 */
@RestController
@RequestMapping("/api")
public class WelcomeController {

    /**
     * Endpoint: GET http://localhost:8080/api/welcome/Andres
     * 
     * Retorna: {"message": "¡Hola Andres! Bienvenido a la API"}
     * 
     * @PathVariable = toma el valor de la URL. 
     *   Si la URL es /api/welcome/Andres, entonces nombre = "Andres"
     */
    @GetMapping("/welcome/{nombre}")
    public Map<String, String> welcome(@PathVariable String nombre) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("message", "¡Hola " + nombre + "! Bienvenido a la API");
        respuesta.put("nombre", nombre);
        
        return respuesta;
    }
}
