package application.service;

import application.dto.PersonaDTO;
import org.springframework.stereotype.Service;

@Service
public class SanitizacionService {

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
        String limpio = valor.replaceAll("<[^>]*>", "");
        limpio = limpio.replaceAll("[\\p{Cntrl}&&[^\t\n\r]]", "");
        limpio = limpio.replace("&", "&amp;")
                       .replace("\"", "&quot;")
                       .replace("'", "&#x27;");
        return limpio.trim();
    }

    public String sanitizarEmail(String email) {
        if (email == null) {
            return null;
        }
        String limpio = email.replaceAll("<[^>]*>", "");
        limpio = limpio.replaceAll("[\\p{Cntrl}&&[^\t\n\r]]", "");
        return limpio.trim().toLowerCase();
    }
}
