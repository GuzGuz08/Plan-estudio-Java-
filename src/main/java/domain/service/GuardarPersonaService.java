package domain.service;
import  domain.model.Persona;
import  domain.interfaces.IGuardarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import domain.repositoryPort.PersonaRepositoryPort;



@Service
public class GuardarPersonaService implements IGuardarService {

    private static final Logger log = LoggerFactory.getLogger(GuardarPersonaService.class);
    private final PersonaRepositoryPort personaRepository;
    private final ValidarEmailService validarEmailService;
    private final SanitizacionService sanitizacionService;

    public GuardarPersonaService(PersonaRepositoryPort personaRepository,
                                 ValidarEmailService validarEmailService,
                                 SanitizacionService sanitizacionService) {
        this.personaRepository = personaRepository;
        this.validarEmailService = validarEmailService;
        this.sanitizacionService = sanitizacionService;
    }

    @Override
    public Persona registrar(Persona persona) {
        validarEmailService.validarEmailUnico(persona.email, 0L);
        sanitizacionService.sanitizar(persona);
        log.info("Registrando nueva persona con email: {}", persona.email);
        Persona personaGuardada = personaRepository.save(persona);
        log.info("Persona registrada exitosamente con ID: {}", personaGuardada.getId());
        return (personaGuardada);
    }
}
