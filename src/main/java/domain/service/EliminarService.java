package domain.service;

import application.port.in.IEliminarService;
import application.port.out.PersonaRepositoryPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EliminarService implements IEliminarService {

    private final BuscarService buscarService;
    private final PersonaRepositoryPort personaRepository;
    private static final Logger log = LoggerFactory.getLogger(EliminarService.class);

    public EliminarService(PersonaRepositoryPort personaRepository, BuscarService buscarService) {
        this.personaRepository = personaRepository;
        this.buscarService = buscarService;
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando persona con ID: {}", id);
        buscarService.buscarPersonaOFallar(id);
        personaRepository.deleteById(id);
        log.info("Persona con ID: {} eliminada exitosamente", id);
    }
}
