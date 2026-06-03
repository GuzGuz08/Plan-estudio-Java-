package domain.service;

import application.dto.PersonaDTO;
import application.port.in.IRegistrarService;
import application.port.out.PersonaRepositoryPort;
import application.service.ConvertirService;
import application.service.SanitizacionService;
import application.service.ValidarEmailService;
import domain.model.Persona;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CrearService implements IRegistrarService {

    private static final Logger log = LoggerFactory.getLogger(CrearService.class);
    private final PersonaRepositoryPort personaRepository;
    private final ValidarEmailService validarEmailService;
    private final ConvertirService convertirService;
    private final SanitizacionService sanitizacionService;

    public CrearService(PersonaRepositoryPort personaRepository, ValidarEmailService validarEmailService, ConvertirService convertirService, SanitizacionService sanitizacionService) {
        this.personaRepository = personaRepository;
        this.validarEmailService = validarEmailService;
        this.convertirService = convertirService;
        this.sanitizacionService = sanitizacionService;
    }

    @Override
    public PersonaDTO registrar(PersonaDTO personaDTO) {
        sanitizacionService.sanitizar(personaDTO);
        validarEmailService.validarEmailUnico(personaDTO.email, 0L);
        log.info("Registrando nueva persona con email: {}", personaDTO.email);
        Persona persona = new Persona(
                personaDTO.nombre,
                personaDTO.apellido,
                personaDTO.email,
                personaDTO.fechaNacimiento
        );
        Persona personaGuardada = personaRepository.save(persona);
        log.info("Persona registrada exitosamente con ID: {}", personaGuardada.getId());
        return convertirService.convertirADTO(personaGuardada);
    }
}
