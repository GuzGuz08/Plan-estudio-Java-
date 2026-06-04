package application.service;

import application.dto.PersonaPageDTO;
import application.interfaces.IBuscadorAvanzadoServiceApp;
import domain.interfaces.IBuscarAvanzadoService;
import org.springframework.stereotype.Service;

@Service
public class BuscadorAvanzadoServiceApp implements IBuscadorAvanzadoServiceApp {

    private final IBuscarAvanzadoService buscarAvanzadoService;

    public BuscadorAvanzadoServiceApp(IBuscarAvanzadoService buscarAvanzadoService) {
        this.buscarAvanzadoService = buscarAvanzadoService;
    }

    @Override
    public PersonaPageDTO buscar(String nombre, String apellido,
                                 Integer edadMin, Integer edadMax,
                                 int page, int size, String sort) {
        return buscarAvanzadoService.buscar(nombre, apellido, edadMin, edadMax, page, size, sort);
    }
}
