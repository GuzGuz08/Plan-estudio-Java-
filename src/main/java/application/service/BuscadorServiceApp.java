package application.service;
import org.springframework.stereotype.Service;
import domain.interfaces.IBuscarService;
import domain.model.Persona;
import application.dto.PersonaDTO;
import application.interfaces.IBuscarServiceApp;

@Service
public class BuscadorServiceApp implements IBuscarServiceApp {
    private final IBuscarService buscarService;

    public BuscadorServiceApp(IBuscarService buscarService){
        this.buscarService = buscarService;
    }
    @Override
    public PersonaDTO buscar(Long id){
        Persona personaencontrada = buscarService.buscarPorId(id);
        return PersonaDTO.desdeModelo(personaencontrada);
    }
}
