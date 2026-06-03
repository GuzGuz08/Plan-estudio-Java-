package infrastructure.persistence;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaJpaRepository extends JpaRepository<PersonaEntity, Long> {
    List<PersonaEntity> findAllByOrderByApellidoAsc();
    boolean existsByEmailAndIdNot(String email, Long id);

    @Query("SELECT p FROM PersonaEntity p WHERE " +
           "(:nombre IS NULL OR p.nombre LIKE CONCAT('%', :nombre, '%')) AND " +
           "(:apellido IS NULL OR p.apellido LIKE CONCAT('%', :apellido, '%')) AND " +
           "(:fechaMin IS NULL OR p.fechaNacimiento >= :fechaMin) AND " +
           "(:fechaMax IS NULL OR p.fechaNacimiento <= :fechaMax)")
    Page<PersonaEntity> search(@Param("nombre") String nombre,
                               @Param("apellido") String apellido,
                               @Param("fechaMin") LocalDate fechaMin,
                               @Param("fechaMax") LocalDate fechaMax,
                               Pageable pageable);
}
