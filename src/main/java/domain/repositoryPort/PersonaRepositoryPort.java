package domain.repositoryPort;

import domain.model.Persona;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonaRepositoryPort {

    Persona save(Persona persona);

    Optional<Persona> findById(Long id);

    List<Persona> findAllByOrderByApellidoAsc();

    boolean existsByEmailAndIdNot(String email, Long id);

    void deleteById(Long id);

    Page<Persona> search(String nombre, String apellido, LocalDate fechaMin, LocalDate fechaMax, Pageable pageable);
}
