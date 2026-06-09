package domain.interfaces;
import domain.model.Persona;


public interface IActualizarService {
    Persona actualizar(Long id,Persona persona);
}
