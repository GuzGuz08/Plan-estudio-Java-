package service;

import domain.exception.PersonaNoEncontradaException;
import domain.model.Persona;
import domain.service.BuscarService;
import domain.service.EliminarService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import domain.repositoryPort.PersonaRepositoryPort;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EliminarServiceTest {

    @Mock
    PersonaRepositoryPort personaRepository;

    @Mock
    BuscarService buscarService;

    @InjectMocks
    EliminarService eliminarService;

    private static final LocalDate FECHA_NACIMIENTO = LocalDate.of(1990, 3, 20);

    @Test
    void should_DeletePersona_When_IdExists() {
        Long id = 1L;
        Persona persona = new Persona("Juan", "Pérez", "juan@test.com", FECHA_NACIMIENTO);
        persona.setId(id);

        when(buscarService.buscarPersonaOFallar(id)).thenReturn(persona);

        eliminarService.eliminar(id);

        verify(buscarService).buscarPersonaOFallar(id);
        verify(personaRepository).deleteById(id);
    }

    @Test
    void should_ThrowException_When_DeletingNonExistentPersona() {
        Long id = 999L;

        when(buscarService.buscarPersonaOFallar(id)).thenThrow(
                new PersonaNoEncontradaException(id));

        assertThrows(PersonaNoEncontradaException.class,
                () -> eliminarService.eliminar(id));

        verify(personaRepository, never()).deleteById(anyLong());
    }
}
