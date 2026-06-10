package infrastructure.persistence;
import domain.model.Persona;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import domain.repositoryPort.PersonaRepositoryPort;

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
    public Page<Persona> search(String nombre, String apellido, Integer edadMin, Integer edadMax, Pageable pageable) {
        LocalDate fechaMin = (edadMax == null) ? null : LocalDate.now().minusYears(edadMax);
        LocalDate fechaMax = (edadMin == null) ? null : LocalDate.now().minusYears(edadMin);

        Specification<PersonaEntity> spec = buildSearchSpecification(nombre, apellido, fechaMin, fechaMax);
        return jpaRepository.findAll(spec, pageable).map(PersonaEntity::toDomain);
    }

    private Specification<PersonaEntity> buildSearchSpecification(String nombre, String apellido, LocalDate fechaMin, LocalDate fechaMax) {
        return (root, query, cb) -> {
            List<Predicate> predicates = Stream.of(
                    Objects.nonNull(nombre) && !nombre.isEmpty() 
                        ? cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%") : null,
                    Objects.nonNull(apellido) && !apellido.isEmpty() 
                        ? cb.like(cb.lower(root.get("apellido")), "%" + apellido.toLowerCase() + "%") : null,
                    Objects.nonNull(fechaMin) 
                        ? cb.greaterThanOrEqualTo(root.get("fechaNacimiento"), fechaMin) : null,
                    Objects.nonNull(fechaMax) 
                        ? cb.lessThanOrEqualTo(root.get("fechaNacimiento"), fechaMax) : null
            )
            .filter(Objects::nonNull)
            .toList();
            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
