package application.port.in;

import application.dto.PersonaDTO;
import java.util.List;

public interface IListarService {
    List<PersonaDTO> listarPersonas();
}
