package domain.interfaces;
import domain.model.Persona;

public interface IBuscarService {
    Persona buscarPorId(Long id);
}
