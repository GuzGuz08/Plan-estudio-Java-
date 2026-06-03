package domain.service;

import application.dto.PersonaDTO;
import application.port.in.IActualizarService;
import application.port.out.PersonaRepositoryPort;
import application.service.ConvertirService;
import application.service.SanitizacionService;
import application.service.ValidarEmailService;
import domain.model.Persona;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ActualizarService implements IActualizarService {

    private final PersonaRepositoryPort personaRepository;
    private final BuscarService buscarService;
    private final ValidarEmailService validarEmailService;
    private final ConvertirService convertirService;
    private final SanitizacionService sanitizacionService;
    private static final Logger log = LoggerFactory.getLogger(ActualizarService.class);

    public ActualizarService(PersonaRepositoryPort personaRepository, BuscarService buscarService, ValidarEmailService validarEmailService, ConvertirService convertirService, SanitizacionService sanitizacionService) {
        this.personaRepository = personaRepository;
        this.buscarService = buscarService;
        this.validarEmailService = validarEmailService;
        this.convertirService = convertirService;
        this.sanitizacionService = sanitizacionService;
    }

    @Override
    public PersonaDTO actualizar(Long id, PersonaDTO personaDTO) {
        log.info("Actualizando persona con ID: {}", id);
        sanitizacionService.sanitizar(personaDTO);
        Persona personaExistente = buscarService.buscarPersonaOFallar(id);
        validarEmailService.validarEmailUnico(personaDTO.email, id);
        personaExistente.actualizar(personaDTO.nombre, personaDTO.apellido, personaDTO.email, personaDTO.fechaNacimiento);
        Persona personaActualizada = personaRepository.save(personaExistente);
        log.info("Persona con ID: {} actualizada exitosamente", id);
        return convertirService.convertirADTO(personaActualizada);
    }
}
