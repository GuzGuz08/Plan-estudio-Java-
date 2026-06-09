package domain.service;
import org.springframework.stereotype.Service;

import domain.interfaces.ISanitizarService;
import domain.model.Persona;

@Service
public class SanitizacionService implements ISanitizarService {

    public Persona sanitizar(Persona persona) {
        persona.nombre = sanitizarTexto(persona.nombre);
        persona.apellido = sanitizarTexto(persona.apellido);
        persona.email = sanitizarEmail(persona.email);
        return persona;
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
