package domain.service;

import domain.interfaces.IBuscarAvanzadoService;
import domain.model.Persona;
import domain.model.PersonaPage;
import infrastructure.persistence.PersonaEntity;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class BuscarAvanzadoService implements IBuscarAvanzadoService {

    private final EntityManager entityManager;


    public BuscarAvanzadoService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public PersonaPage buscar(String nombre, String apellido, Integer edadMin, Integer edadMax, int page, int size, String sort) {

        LocalDate fechaMin = (edadMax == null) ? null : LocalDate.now().minusYears(edadMax);
        LocalDate fechaMax = (edadMin == null) ? null : LocalDate.now().minusYears(edadMin);

        Pageable pageable = crearPageable(page, size, sort);

        List<PersonaEntity> resultados = buscarConCriteria(nombre, apellido, fechaMin, fechaMax, pageable);
        long total = contarConCriteria(nombre, apellido, fechaMin, fechaMax);

        List<Persona> personas = resultados.stream()
                .map(PersonaEntity::toDomain)
                .toList();

        int totalPages = (size > 0) ? (int) Math.ceil((double) total / size) : 0;

        return new PersonaPage(personas, total, totalPages, page, size);
    }

    private List<PersonaEntity> buscarConCriteria(String nombre, String apellido,
                                                  LocalDate fechaMin, LocalDate fechaMax,
                                                  Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PersonaEntity> cq = cb.createQuery(PersonaEntity.class);
        Root<PersonaEntity> root = cq.from(PersonaEntity.class);

        Predicate[] predicates = Stream.of(
                Optional.ofNullable(nombre).filter(n -> !n.isBlank()).map(n -> cb.like(cb.lower(root.get("nombre")), "%" + n.toLowerCase() + "%")),
                Optional.ofNullable(apellido).filter(a -> !a.isBlank()).map(a -> cb.like(cb.lower(root.get("apellido")), "%" + a.toLowerCase() + "%")),
                Optional.ofNullable(fechaMin).map(f -> cb.greaterThanOrEqualTo(root.get("fechaNacimiento"), f)),
                Optional.ofNullable(fechaMax).map(f -> cb.lessThanOrEqualTo(root.get("fechaNacimiento"), f))
        ).flatMap(Optional::stream).toArray(Predicate[]::new);

        cq.where(predicates);

        pageable.getSort().stream().forEach(order -> 
            cq.orderBy(order.isAscending() ? cb.asc(root.get(order.getProperty())) : cb.desc(root.get(order.getProperty())))
        );
        Optional.of(pageable.getSort()).filter(Sort::isUnsorted).ifPresent(s -> cq.orderBy(cb.asc(root.get("apellido"))));

        return entityManager.createQuery(cq)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    private long contarConCriteria(String nombre, String apellido,
                                   LocalDate fechaMin, LocalDate fechaMax) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<PersonaEntity> root = cq.from(PersonaEntity.class);

        Predicate[] predicates = Stream.of(
                Optional.ofNullable(nombre).filter(n -> !n.isBlank()).map(n -> cb.like(cb.lower(root.get("nombre")), "%" + n.toLowerCase() + "%")),
                Optional.ofNullable(apellido).filter(a -> !a.isBlank()).map(a -> cb.like(cb.lower(root.get("apellido")), "%" + a.toLowerCase() + "%")),
                Optional.ofNullable(fechaMin).map(f -> cb.greaterThanOrEqualTo(root.get("fechaNacimiento"), f)),
                Optional.ofNullable(fechaMax).map(f -> cb.lessThanOrEqualTo(root.get("fechaNacimiento"), f))
        ).flatMap(Optional::stream).toArray(Predicate[]::new);

        cq.select(cb.count(root)).where(predicates);

        return entityManager.createQuery(cq).getSingleResult();
    }

    private Pageable crearPageable(int page, int size, String sort) {
        return Optional.ofNullable(sort)
                .filter(s -> !s.isBlank())
                .map(s -> {
                    String[] parts = s.split(",");
                    String property = parts[0].trim();
                    Sort.Direction direction = (parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim()))
                            ? Sort.Direction.DESC : Sort.Direction.ASC;
                    return PageRequest.of(page, size, Sort.by(direction, property));
                })
                .orElseGet(() -> PageRequest.of(page, size, Sort.by("apellido").ascending()));
    }
}