package application.interfaces;
import application.dto.PersonaDTO;

public interface IActualizarServiceApp {
    public PersonaDTO actualizar(Long id, PersonaDTO personaDTO);
}
