package application.service;

import application.dto.PersonaDTO;
import application.dto.PersonaPageDTO;
import application.interfaces.IBuscadorAvanzadoServiceApp;
import domain.interfaces.IBuscarAvanzadoService;
import domain.model.PersonaPage;

import java.util.List;
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
        PersonaPage personaPage = buscarAvanzadoService.buscar(nombre, apellido, edadMin, edadMax, page, size, sort);
        return convertirADto(personaPage);
    }

    private PersonaPageDTO convertirADto(PersonaPage personaPage) {
        List<PersonaDTO> dtos = personaPage.content.stream()
                .map(PersonaDTO::desdeModelo)
                .toList();
        return new PersonaPageDTO(
                dtos,
                personaPage.totalElements,
                personaPage.totalPages,
                personaPage.currentPage,
                personaPage.size
        );
    }
}
