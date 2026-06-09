package application.service;
import org.springframework.stereotype.Service;
import domain.interfaces.IActualizarService;
import domain.model.Persona;
import application.dto.PersonaDTO;
import application.interfaces.IActualizarServiceApp;

@Service
public class ActualizarServiceApp implements IActualizarServiceApp {
    private final IActualizarService actualizarService; 
    
    public ActualizarServiceApp(IActualizarService actualizarService){
        this.actualizarService = actualizarService;
    }

    @Override
    public PersonaDTO actualizar(Long id, PersonaDTO personaDTO){
        Persona personaModel = personaDTO.aModelo();
        Persona personaActualizada = actualizarService.actualizar(id,personaModel);
        return PersonaDTO.desdeModelo(personaActualizada);
    }
}
