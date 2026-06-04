package application.service;
import org.springframework.stereotype.Service;
import domain.interfaces.IListarService;
import domain.model.Persona;
import application.dto.PersonaDTO;
import application.interfaces.IListarServiceApp;
import java.util.List;

@Service
public class ListarServiceApp implements IListarServiceApp {
    private final IListarService listarService;

    public ListarServiceApp(IListarService listarService){
        this.listarService = listarService;
    }
    @Override
    public List<PersonaDTO> listar(){
       List<Persona> entidades = listarService.listarPersonas();

        return entidades.stream()
                .map(PersonaDTO::desdeModelo)
                .toList();

    }

}
