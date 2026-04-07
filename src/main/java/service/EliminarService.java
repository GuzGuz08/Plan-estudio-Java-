package service;
import domain.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EliminarService {
    private final BuscarService buscarService;
    static final Logger log = LoggerFactory.getLogger(EliminarService.class);
    private final repository.PersonaRepository personaRepository;

    public EliminarService(repository.PersonaRepository personaRepository, BuscarService buscarService) {

        this.personaRepository = personaRepository;
        this.buscarService = buscarService;
    }

    public  void eliminar( Long id) {
        log.info("Eliminando persona con ID: {}", id);
        Persona persona = buscarService.buscarId(id); 
        personaRepository.delete(persona);
        log.info("Persona con ID: {} eliminada exitosamente", id);
    }

}
