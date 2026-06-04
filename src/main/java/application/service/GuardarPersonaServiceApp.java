package application.service;
import org.springframework.stereotype.Service;
import domain.interfaces.IGuardarService;
import domain.model.Persona;
import application.dto.PersonaDTO;
import application.interfaces.IGuardarServiceApp;

@Service
public class GuardarPersonaServiceApp implements IGuardarServiceApp {
    private final IGuardarService guardarService; 
    
    public GuardarPersonaServiceApp(IGuardarService guardarService){
        this.guardarService = guardarService;
    }
    @Override
    public PersonaDTO guardarPersonaApp(PersonaDTO personaDTO){
        Persona personaModel = personaDTO.aModelo();
        Persona personaGuardada = guardarService.registrar(personaModel);
        return PersonaDTO.desdeModelo(personaGuardada);

    }
}
