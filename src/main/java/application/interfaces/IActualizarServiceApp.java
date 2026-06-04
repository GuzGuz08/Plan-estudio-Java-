package application.interfaces;
import application.dto.PersonaDTO;

public interface IActualizarServiceApp {
    public PersonaDTO actualizarApp(PersonaDTO persona);

    public PersonaDTO actualizar(Long id, PersonaDTO personaDTO);
}
