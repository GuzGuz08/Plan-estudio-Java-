package application.service;

import application.dto.PersonaDTO;
import org.springframework.stereotype.Service;

@Service
public class SanitizacionService {

    /**
     * Sanitiza todos los campos de texto del DTO para prevenir XSS.
     * Elimina etiquetas HTML, caracteres de control y espacios innecesarios.
     */
    public PersonaDTO sanitizar(PersonaDTO dto) {
        dto.nombre = sanitizarTexto(dto.nombre);
        dto.apellido = sanitizarTexto(dto.apellido);
        dto.email = sanitizarEmail(dto.email);
        return dto;
    }

    public String sanitizarTexto(String valor) {
        if (valor == null) {
            return null;
        }
        // 1. Eliminar etiquetas HTML/XML para prevenir XSS
        String limpio = valor.replaceAll("<[^>]*>", "");
        // 2. Eliminar caracteres de control (excepto espacios normales)
        limpio = limpio.replaceAll("[\\p{Cntrl}&&[^\t\n\r]]", "");
        // 3. Escapar caracteres peligrosos para prevenir inyecciones
        limpio = limpio.replace("&", "&amp;")
                       .replace("\"", "&quot;")
                       .replace("'", "&#x27;");
        // 4. Normalizar espacios en blanco
        return limpio.trim();
    }

    public String sanitizarEmail(String email) {
        if (email == null) {
            return null;
        }
        // Para email solo eliminar HTML, control chars y trim
        String limpio = email.replaceAll("<[^>]*>", "");
        limpio = limpio.replaceAll("[\\p{Cntrl}&&[^\t\n\r]]", "");
        return limpio.trim().toLowerCase();
    }
}
