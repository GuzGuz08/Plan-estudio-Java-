package domain.service;

import application.dto.PersonaDTO;
import application.dto.PersonaPageDTO;
import application.port.in.IBuscarAvanzadoService;
import application.service.ConvertirService;
import infrastructure.persistence.PersonaEntity;
import infrastructure.persistence.PersonaJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BuscarAvanzadoService implements IBuscarAvanzadoService {

    private final ConvertirService convertirService;
    private final EntityManager entityManager;

    public BuscarAvanzadoService(PersonaJpaRepository personaJpaRepository,
                                 ConvertirService convertirService,
                                 EntityManager entityManager) {
        this.convertirService = convertirService;
        this.entityManager = entityManager;
    }

    @Override
    public PersonaPageDTO buscar(String nombre, String apellido, Integer edadMin, Integer edadMax,
                                 int page, int size, String sort) {

        LocalDate fechaMax = (edadMin == null) ? null : LocalDate.now().minusYears(edadMin);
        LocalDate fechaMin = (edadMax == null) ? null : LocalDate.now().minusYears(edadMax).minusYears(1).plusDays(1);

        Pageable pageable = crearPageable(page, size, sort);

        List<PersonaEntity> resultados = buscarConCriteria(nombre, apellido, fechaMin, fechaMax, pageable);
        long total = contarConCriteria(nombre, apellido, fechaMin, fechaMax);

        List<PersonaDTO> dtos = resultados.stream()
                .map(PersonaEntity::toDomain)
                .map(convertirService::convertirADTO)
                .toList();

        int totalPages = (int) Math.ceil((double) total / size);

        return new PersonaPageDTO(dtos, total, totalPages, page, size);
    }

    private List<PersonaEntity> buscarConCriteria(String nombre, String apellido,
                                                   LocalDate fechaMin, LocalDate fechaMax,
                                                   Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PersonaEntity> cq = cb.createQuery(PersonaEntity.class);
        Root<PersonaEntity> root = cq.from(PersonaEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (nombre != null && !nombre.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));
        }
        if (apellido != null && !apellido.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("apellido")), "%" + apellido.toLowerCase() + "%"));
        }
        if (fechaMin != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("fechaNacimiento"), fechaMin));
        }

        if (fechaMax != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("fechaNacimiento"), fechaMax));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                jakarta.persistence.criteria.Expression<?> path = root.get(order.getProperty());
                cq.orderBy(order.isAscending() ? cb.asc(path) : cb.desc(path));
            }
        } else {
            cq.orderBy(cb.asc(root.get("apellido")));
        }

        // Aplicar paginación
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

        List<Predicate> predicates = new ArrayList<>();

        if (nombre != null && !nombre.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));
        }

        if (apellido != null && !apellido.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("apellido")), "%" + apellido.toLowerCase() + "%"));
        }

        if (fechaMin != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("fechaNacimiento"), fechaMin));
        }

        if (fechaMax != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("fechaNacimiento"), fechaMax));
        }

        cq.select(cb.count(root)).where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getSingleResult();
    }

    private Pageable crearPageable(int page, int size, String sort) {
        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",");
            String property = parts[0].trim();
            Sort.Direction direction = (parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim()))
                    ? Sort.Direction.DESC : Sort.Direction.ASC;
            return PageRequest.of(page, size, Sort.by(direction, property));
        }
        return PageRequest.of(page, size, Sort.by("apellido").ascending());
    }
}
