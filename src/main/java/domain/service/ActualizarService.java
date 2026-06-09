package domain.service;
import domain.repositoryPort.PersonaRepositoryPort;
import domain.interfaces.IActualizarService;
import domain.model.Persona;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ActualizarService implements IActualizarService {

    private final PersonaRepositoryPort personaRepository;
    private final BuscarService buscarService;
    private final ValidarEmailService validarEmailService;
    private final SanitizacionService sanitizacionService;
    private static final Logger log = LoggerFactory.getLogger(ActualizarService.class);

    public ActualizarService(PersonaRepositoryPort personaRepository,
                             BuscarService buscarService,
                             ValidarEmailService validarEmailService,
                             SanitizacionService sanitizacionService) {
        this.personaRepository = personaRepository;
        this.buscarService = buscarService;
        this.validarEmailService = validarEmailService;
        this.sanitizacionService = sanitizacionService;
    }

    public Persona actualizar(Long id,Persona persona) {
        log.info("Actualizando persona con ID: {}", persona.id);
        sanitizacionService.sanitizar(persona);
        Persona personaExistente = buscarService.buscarPersonaOFallar(persona.id);
        validarEmailService.validarEmailUnico(persona.email,persona.id);
        personaExistente.actualizar(persona.nombre, persona.apellido, persona.email, persona.fechaNacimiento);
        Persona personaActualizada = personaRepository.save(personaExistente);
        log.info("Persona con ID: {} actualizada exitosamente", persona.id);
        return (personaActualizada);
    }
}
