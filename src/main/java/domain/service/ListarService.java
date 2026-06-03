package domain.service;

import java.util.List;
import org.springframework.stereotype.Service;

import application.dto.PersonaDTO;
import application.port.in.IListarService;
import application.port.out.PersonaRepositoryPort;
import application.service.ConvertirService;

@Service
public class ListarService implements IListarService {

    private final PersonaRepositoryPort personaRepository;
    private final ConvertirService convertirService;

    public ListarService(PersonaRepositoryPort personaRepository, ConvertirService convertirService) {
        this.personaRepository = personaRepository;
        this.convertirService = convertirService;
    }

    @Override
    public List<PersonaDTO> listarPersonas() {
        return personaRepository.findAllByOrderByApellidoAsc()
                .stream()
                .map(convertirService::convertirADTO)
                .toList();
    }
}
