package domain.service;

import application.dto.PersonaDTO;
import application.port.in.IBuscarService;
import application.port.out.PersonaRepositoryPort;
import application.service.ConvertirService;
import domain.exception.PersonaNoEncontradaException;
import domain.model.Persona;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BuscarService implements IBuscarService {

    private static final Logger log = LoggerFactory.getLogger(BuscarService.class);
    private final PersonaRepositoryPort personaRepository;
    private final ConvertirService convertirService;

    public BuscarService(PersonaRepositoryPort personaRepository, ConvertirService convertirService) {
        this.personaRepository = personaRepository;
        this.convertirService = convertirService;
    }

    @Override
    public PersonaDTO buscarPorId(Long id) {
        Persona persona = buscarPersonaOFallar(id);
        return convertirService.convertirADTO(persona);
    }

    public Persona buscarPersonaOFallar(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Persona con ID: {} no encontrada", id);
                    return new PersonaNoEncontradaException(id);
                });
    }
}
