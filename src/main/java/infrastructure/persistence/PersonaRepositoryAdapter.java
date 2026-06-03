package infrastructure.persistence;

import application.port.out.PersonaRepositoryPort;
import domain.model.Persona;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PersonaRepositoryAdapter implements PersonaRepositoryPort {

    private final PersonaJpaRepository jpaRepository;

    public PersonaRepositoryAdapter(PersonaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Persona save(Persona persona) {
        PersonaEntity entity = PersonaEntity.fromDomain(persona);
        PersonaEntity saved = jpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Persona> findById(Long id) {
        return jpaRepository.findById(id).map(PersonaEntity::toDomain);
    }

    @Override
    public List<Persona> findAllByOrderByApellidoAsc() {
        return jpaRepository.findAllByOrderByApellidoAsc()
                .stream()
                .map(PersonaEntity::toDomain)
                .toList();
    }

    @Override
    public boolean existsByEmailAndIdNot(String email, Long id) {
        return jpaRepository.existsByEmailAndIdNot(email, id);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Page<Persona> search(String nombre, String apellido, LocalDate fechaMin, LocalDate fechaMax, Pageable pageable) {
        return jpaRepository.search(nombre, apellido, fechaMin, fechaMax, pageable)
                .map(PersonaEntity::toDomain);
    }
}
