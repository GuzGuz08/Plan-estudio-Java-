package application.port.in;

import application.dto.PersonaDTO;

public interface IRegistrarService {
    PersonaDTO registrar(PersonaDTO personaDTO);
}
