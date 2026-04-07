package repository;
import domain.Persona;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    List<Persona> findAllByOrderByApellidoAsc();
    boolean existsByEmailAndIdNot(String email, Long id);
}


