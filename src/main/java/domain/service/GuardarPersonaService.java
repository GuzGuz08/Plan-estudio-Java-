package domain.service;
import  domain.model.Persona;
import  domain.interfaces.IGuardarService;
import  application.port.out.PersonaRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class GuardarPersonaService implements IGuardarService {

    private static final Logger log = LoggerFactory.getLogger(GuardarPersonaService.class);
    private final PersonaRepositoryPort personaRepository;
    private final application.service.ValidarEmailService validarEmailService;

    public GuardarPersonaService(PersonaRepositoryPort personaRepository, application.service.ValidarEmailService validarEmailService) {
        this.personaRepository = personaRepository;
        this.validarEmailService = validarEmailService;
    }

    @Override
    public Persona registrar(Persona persona) {
        validarEmailService.validarEmailUnico(persona.email, 0L);
        log.info("Registrando nueva persona con email: {}", persona.email);
        Persona personaGuardada = personaRepository.save(persona);
        log.info("Persona registrada exitosamente con ID: {}", personaGuardada.getId());
        return (personaGuardada);
    }
}
