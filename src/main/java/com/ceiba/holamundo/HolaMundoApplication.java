package com.ceiba.holamundo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal - Punto de entrada de la aplicación.
 * 
 * @SpringBootApplication hace 3 cosas:
 * 1. Marca esta clase como configuración de Spring
 * 2. Activa la auto-configuración (Spring configura cosas por nosotros)
 * 3. Escanea este paquete buscando clases con anotaciones (@RestController, etc.)
 */
@SpringBootApplication
public class HolaMundoApplication {

    public static void main(String[] args) {
        // Esto arranca toda la aplicación Spring Boot
        SpringApplication.run(HolaMundoApplication.class, args);
        System.out.println("✅ Aplicación corriendo en http://localhost:8080");
    }
}
