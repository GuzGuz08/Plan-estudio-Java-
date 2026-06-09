package service;

import domain.repositoryPort.PersonaRepositoryPort;
import domain.exception.EmailDuplicadoException;
import domain.exception.PersonaNoEncontradaException;
import domain.model.Persona;
import domain.service.ActualizarService;
import domain.service.BuscarService;
import domain.service.SanitizacionService;
import domain.service.ValidarEmailService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActualizarServiceTest {

    @Mock
    PersonaRepositoryPort personaRepository;

    @Mock
    BuscarService buscarService;

    @Mock
    ValidarEmailService validarEmailService;

    @Mock
    SanitizacionService sanitizacionService;

    private ActualizarService actualizarService;

    private static final LocalDate FECHA_NACIMIENTO = LocalDate.of(1990, 3, 20);

    @BeforeEach
    void setUp() {
        actualizarService = new ActualizarService(personaRepository, buscarService, validarEmailService, sanitizacionService);
    }

    @Test
    void should_UpdatePersona_When_ValidData() {
        Long id = 1L;
        Persona personaExistente = new Persona("Juan", "Pérez", "juan@test.com", FECHA_NACIMIENTO);
        personaExistente.setId(id);

        Persona personaActualizada = new Persona("Carlos", "García", "carlos@test.com", LocalDate.of(1991, 3, 20));
        personaActualizada.setId(id);

        when(buscarService.buscarPersonaOFallar(id)).thenReturn(personaExistente);
        when(personaRepository.save(personaExistente)).thenReturn(personaActualizada);

        Persona dtoActualizar = new Persona("Carlos", "García", "carlos@test.com", LocalDate.of(1991, 3, 20));
        dtoActualizar.id = id;

        Persona resultado = actualizarService.actualizar(id,dtoActualizar);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Carlos", resultado.getNombre());
        assertEquals("García", resultado.getApellido());
        assertEquals("carlos@test.com", resultado.getEmail());
        verify(validarEmailService).validarEmailUnico("carlos@test.com", id);
        verify(buscarService).buscarPersonaOFallar(id);
    }

    @Test
    void should_ThrowException_When_PersonaNotFound() {
        Long id = 999L;
        Persona persona = new Persona("Carlos", "García", "carlos@test.com", FECHA_NACIMIENTO);
        persona.id = id;

        when(buscarService.buscarPersonaOFallar(id)).thenThrow(
                new PersonaNoEncontradaException(id));

        assertThrows(PersonaNoEncontradaException.class,
                () -> actualizarService.actualizar(id,persona));

        verify(validarEmailService, never()).validarEmailUnico(anyString(), anyLong());
    }

    @Test
    void should_ThrowException_When_EmailAlreadyInUse() {
        Long id = 1L;
        Persona personaExistente = new Persona("Juan", "Pérez", "juan@test.com", FECHA_NACIMIENTO);
        personaExistente.setId(id);

        Persona personaActualizar = new Persona("Carlos", "García", "duplicado@test.com", FECHA_NACIMIENTO);
        personaActualizar.id = id;

        when(buscarService.buscarPersonaOFallar(id)).thenReturn(personaExistente);
        doThrow(new EmailDuplicadoException("duplicado@test.com"))
                .when(validarEmailService).validarEmailUnico("duplicado@test.com", id);

        assertThrows(EmailDuplicadoException.class,
                () -> actualizarService.actualizar(id,personaActualizar));
    }
}
