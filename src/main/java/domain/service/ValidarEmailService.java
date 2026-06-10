package domain.service;
import domain.exception.EmailDuplicadoException;
import org.springframework.stereotype.Service;
import domain.repositoryPort.PersonaRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ValidarEmailService {

    private final PersonaRepositoryPort personaRepository;
    private static final Logger log = LoggerFactory.getLogger(ValidarEmailService.class);

    public ValidarEmailService(PersonaRepositoryPort personaRepository) {
        this.personaRepository = personaRepository;
    }

    public void validarEmailUnico(String email, Long idExcluido) {
        if (personaRepository.existsByEmailAndIdNot(email, idExcluido)) {
            log.warn("El email {} ya está en uso por otra persona", email);
            throw new EmailDuplicadoException(email);
        }
    }
}
