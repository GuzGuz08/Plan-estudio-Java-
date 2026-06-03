package service;

import application.dto.PersonaDTO;
import application.port.out.PersonaRepositoryPort;
import application.service.ConvertirService;
import application.service.SanitizacionService;
import application.service.ValidarEmailService;
import domain.exception.PersonaNoEncontradaException;
import domain.model.Persona;
import domain.service.ActualizarService;
import domain.service.BuscarService;

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
    ConvertirService convertirService;

    @Mock
    SanitizacionService sanitizacionService;

    private ActualizarService actualizarService;

    private static final LocalDate FECHA_NACIMIENTO = LocalDate.of(1990, 3, 20);

    @BeforeEach
    void setUp() {
        actualizarService = new ActualizarService(personaRepository, buscarService, validarEmailService, convertirService, sanitizacionService);
    }

    @Test
    void should_UpdatePersona_When_ValidData() {
        Long id = 1L;
        PersonaDTO dtoActualizado = new PersonaDTO(null, "Carlos", "García", "carlos@test.com", FECHA_NACIMIENTO);

        Persona personaExistente = new Persona("Juan", "Pérez", "juan@test.com", FECHA_NACIMIENTO);
        personaExistente.setId(id);

        Persona personaActualizada = new Persona("Carlos", "García", "carlos@test.com", FECHA_NACIMIENTO);
        personaActualizada.setId(id);

        when(buscarService.buscarPersonaOFallar(id)).thenReturn(personaExistente);
        when(personaRepository.save(personaExistente)).thenReturn(personaActualizada);
        when(convertirService.convertirADTO(personaActualizada)).thenReturn(new PersonaDTO(id, "Carlos", "García", "carlos@test.com", FECHA_NACIMIENTO));

        PersonaDTO resultado = actualizarService.actualizar(id, dtoActualizado);

        assertNotNull(resultado);
        assertEquals(id, resultado.id);
        assertEquals("Carlos", resultado.nombre);
        verify(validarEmailService).validarEmailUnico("carlos@test.com", id);
        verify(buscarService).buscarPersonaOFallar(id);
    }

    @Test
    void should_ThrowException_When_PersonaNotFound() {
        Long id = 999L;
        PersonaDTO dto = new PersonaDTO(null, "Carlos", "García", "carlos@test.com", FECHA_NACIMIENTO);

        when(buscarService.buscarPersonaOFallar(id)).thenThrow(
                new PersonaNoEncontradaException(id));

        assertThrows(PersonaNoEncontradaException.class,
                () -> actualizarService.actualizar(id, dto));

        verify(validarEmailService, never()).validarEmailUnico(anyString(), anyLong());
    }

    @Test
    void should_ThrowException_When_EmailAlreadyInUse() {
        Long id = 1L;
        PersonaDTO dto = new PersonaDTO(null, "Carlos", "García", "duplicado@test.com", FECHA_NACIMIENTO);

        Persona personaExistente = new Persona("Juan", "Pérez", "juan@test.com", FECHA_NACIMIENTO);
        personaExistente.setId(id);

        when(buscarService.buscarPersonaOFallar(id)).thenReturn(personaExistente);
        doThrow(new domain.exception.EmailDuplicadoException("duplicado@test.com"))
                .when(validarEmailService).validarEmailUnico("duplicado@test.com", id);

        assertThrows(domain.exception.EmailDuplicadoException.class,
                () -> actualizarService.actualizar(id, dto));
    }
}
