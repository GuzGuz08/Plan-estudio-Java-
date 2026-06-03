package application.service;

import application.dto.PersonaDTO;
import domain.model.Persona;

import org.springframework.stereotype.Service;

@Service
public class ConvertirService {

    public PersonaDTO convertirADTO(Persona persona) {
        return new PersonaDTO(
                persona.getId(),
                persona.getNombre(),
                persona.getApellido(),
                persona.getEmail(),
                persona.getFechaNacimiento()
        );
    }
}
