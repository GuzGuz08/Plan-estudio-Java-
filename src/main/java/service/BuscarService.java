package service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import domain.Persona;
import org.springframework.stereotype.Service;

@Service

public class BuscarService {

    private final repository.PersonaRepository personaRepository;

    public BuscarService(repository.PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public Persona buscarId(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> {
                    PersonaService.log.warn("Persona con ID: {} no encontrada", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Persona no encontrada");
                });
    }

}
