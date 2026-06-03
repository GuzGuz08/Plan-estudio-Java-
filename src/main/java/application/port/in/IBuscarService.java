package application.port.in;

import application.dto.PersonaDTO;

public interface IBuscarService {
    PersonaDTO buscarPorId(Long id);
}
