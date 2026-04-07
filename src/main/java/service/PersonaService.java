package service;
import domain.Persona;
import dto.PersonaDTO;
import repository.PersonaRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PersonaService {

    static final Logger log = LoggerFactory.getLogger(PersonaService.class);
    private final BuscarService buscarService;
    final PersonaRepository personaRepository;
    private final ValidarEmailService validarEmailService;

    public PersonaService(PersonaRepository personaRepository, BuscarService buscarService, ValidarEmailService validarEmailService) {
        this.personaRepository = personaRepository;
        this.buscarService = buscarService;
        this.validarEmailService = validarEmailService;
    }

    public PersonaDTO registrar(PersonaDTO personaDTO) {
        // Validar que el email no esté en uso por otra persona
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
        return convertirADTO(personaGuardada);
    }

    public List<PersonaDTO> listarPersonas() {
        return personaRepository.findAllByOrderByApellidoAsc()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    public PersonaDTO buscarPorId(Long id) {
        Persona persona = buscarService.buscarId(id);
        return convertirADTO(persona);
    }

    PersonaDTO convertirADTO(Persona persona) {
        return new PersonaDTO( 
                persona.getId(),
                persona.getNombre(),
                persona.getApellido(),
                persona.getEmail(),
                persona.getFechaNacimiento()
        );
    }
}
