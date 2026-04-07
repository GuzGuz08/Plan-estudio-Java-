package service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import repository.PersonaRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ValidarEmailService {
    private final PersonaRepository personaRepository;
    static final Logger log = LoggerFactory.getLogger(ValidarEmailService.class);

    public ValidarEmailService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

     void validarEmailUnico(String email, Long idExcluido) {
        if (personaRepository.existsByEmailAndIdNot(email, idExcluido)) {
            log.warn("El email {} ya está en uso por otra persona", email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya está en uso");
        }
    }

}
