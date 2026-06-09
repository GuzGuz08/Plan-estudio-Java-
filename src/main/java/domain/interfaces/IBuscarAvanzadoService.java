package domain.interfaces;

import domain.model.PersonaPage;

public interface IBuscarAvanzadoService {
    PersonaPage buscar(String nombre, String apellido, Integer edadMin, Integer edadMax,
                          int page, int size, String sort);
}
