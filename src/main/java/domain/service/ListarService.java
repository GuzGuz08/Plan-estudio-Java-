package domain.service;
import domain.model.Persona;

import java.util.List;
import org.springframework.stereotype.Service;

import domain.repositoryPort.PersonaRepositoryPort;
import domain.interfaces.IListarService;

@Service
public class ListarService implements IListarService {

    private final PersonaRepositoryPort personaRepository;


    public ListarService(PersonaRepositoryPort personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Override
    public List<Persona> listarPersonas() {
        return personaRepository.findAllByOrderByApellidoAsc();
    }
}
