package infrastructure.persistence;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaJpaRepository extends JpaRepository<PersonaEntity, Long>, JpaSpecificationExecutor<PersonaEntity> {

    List<PersonaEntity> findAllByOrderByApellidoAsc();

    boolean existsByEmailAndIdNot(String email, Long id);
}
