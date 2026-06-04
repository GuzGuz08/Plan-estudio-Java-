package domain.interfaces;

import application.dto.PersonaDTO;

public interface IRegistrarService {
    PersonaDTO registrar(PersonaDTO personaDTO);
}
