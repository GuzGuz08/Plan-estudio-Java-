package domain.service;
import domain.interfaces.IBuscarAvanzadoService;
import domain.model.Persona;
import domain.model.PersonaPage;
import domain.repositoryPort.PersonaRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BuscarAvanzadoService implements IBuscarAvanzadoService {

    private final PersonaRepositoryPort personaRepository;

    public BuscarAvanzadoService(PersonaRepositoryPort personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Override
    public PersonaPage buscar(String nombre, String apellido, Integer edadMin, Integer edadMax, int page, int size, String sort) {
        Pageable pageable = crearPageable(page, size, sort);

        Page<Persona> resultado = personaRepository.search(nombre, apellido, edadMin, edadMax, pageable);

        return new PersonaPage(
                resultado.getContent(),
                resultado.getTotalElements(),
                resultado.getTotalPages(),
                page,
                size
        );
    }

    private Pageable crearPageable(int page, int size, String sort) {
        if (sort == null || sort.isBlank()) {
            return PageRequest.of(page, size, Sort.by("apellido").ascending());
        }

        String[] parts = sort.split(",");
        String property = parts[0].trim();
        Sort.Direction direction = (parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim()))
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        return PageRequest.of(page, size, Sort.by(direction, property));
    }
}