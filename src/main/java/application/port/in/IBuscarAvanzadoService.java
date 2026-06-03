package application.port.in;

import application.dto.PersonaPageDTO;

public interface IBuscarAvanzadoService {
    PersonaPageDTO buscar(String nombre, String apellido, Integer edadMin, Integer edadMax,
                          int page, int size, String sort);
}
