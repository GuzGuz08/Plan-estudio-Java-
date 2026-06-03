package application.port.in;

import application.dto.PersonaDTO;

public interface IActualizarService {
    PersonaDTO actualizar(Long id, PersonaDTO personaDTO);
}
