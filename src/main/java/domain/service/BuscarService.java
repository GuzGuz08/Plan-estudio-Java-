package domain.service;
import application.port.out.PersonaRepositoryPort;
import domain.exception.PersonaNoEncontradaException;
import domain.interfaces.IBuscarService;
import domain.model.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BuscarService implements IBuscarService {

    private static final Logger log = LoggerFactory.getLogger(BuscarService.class);
    private final PersonaRepositoryPort personaRepository;

    public BuscarService(PersonaRepositoryPort personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Override
    public Persona buscarPorId(Long id) {
        Persona personaEncontrar = buscarPersonaOFallar(id);
        return (personaEncontrar);
    }

    public Persona buscarPersonaOFallar(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Persona con ID: {} no encontrada", id);
                    return new PersonaNoEncontradaException(id);
                });
    }
}
