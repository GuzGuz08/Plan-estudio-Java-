package service;
import domain.Persona;
import dto.PersonaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ActualizarService {
    private final PersonaService personaService;
    private final BuscarService buscarService;
    private final ValidarEmailService validarEmailService;
    static final Logger log = LoggerFactory.getLogger(ActualizarService.class);

    public ActualizarService(PersonaService personaService, BuscarService buscarService, ValidarEmailService validarEmailService) {
        this.personaService = personaService;
        this.buscarService = buscarService;
        this.validarEmailService = validarEmailService;
    }

    public  PersonaDTO actualizar(Long id, PersonaDTO personaDTO) {
        log.info("Actualizando persona con ID: {}", id);
        Persona personaExistente = buscarService.buscarId(id);
        validarEmailService.validarEmailUnico(personaDTO.email, id);
        personaExistente.actualizarDesdeDTO(personaDTO);
        Persona personaActualizada = personaService.personaRepository.save(personaExistente);
        log.info("Persona con ID: {} actualizada exitosamente", id);
        return personaService.convertirADTO(personaActualizada);
    }

}
