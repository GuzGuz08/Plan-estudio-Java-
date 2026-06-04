package application.service;
import org.springframework.stereotype.Service;
import domain.interfaces.IEliminarService;
import application.dto.PersonaDTO;
import application.interfaces.IEliminarServiceApp;

@Service
public class EliminarServiceApp implements IEliminarServiceApp {
    private final IEliminarService eliminarService;

    public EliminarServiceApp(IEliminarService eliminarService){
        this.eliminarService = eliminarService;
    }
    @Override
    public PersonaDTO eliminar(Long id){
        eliminarService.eliminar(id);
        return null;
         
    }
}
