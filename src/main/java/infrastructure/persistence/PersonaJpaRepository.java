package infrastructure.persistence;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import domain.model.Persona;

@Repository
public interface PersonaJpaRepository extends JpaRepository<PersonaEntity, Long>, JpaSpecificationExecutor<PersonaEntity> {

    List<PersonaEntity> findAllByOrderByApellidoAsc();

    boolean existsByEmailAndIdNot(String email, Long id);

    Optional<Persona> buscar(String nombre, String apellido, LocalDate fechaMin, LocalDate fechaMax, Pageable pageable);
}
