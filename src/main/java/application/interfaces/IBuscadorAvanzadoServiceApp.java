package application.interfaces;

import application.dto.PersonaPageDTO;

public interface IBuscadorAvanzadoServiceApp {

    PersonaPageDTO buscar(String nombre, String apellido,
                          Integer edadMin, Integer edadMax,
                          int page, int size, String sort);
}
