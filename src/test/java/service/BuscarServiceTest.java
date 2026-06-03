package service;

import application.dto.PersonaDTO;
import application.port.out.PersonaRepositoryPort;
import application.service.ConvertirService;
import domain.exception.PersonaNoEncontradaException;
import domain.model.Persona;
import domain.service.BuscarService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarServiceTest {

    @Mock
    PersonaRepositoryPort personaRepository;

    @Mock
    ConvertirService convertirService;

    @InjectMocks
    BuscarService buscarService;

    private static final LocalDate FECHA_NACIMIENTO = LocalDate.of(1990, 3, 20);

    @Test
    void should_ReturnPersona_When_IdExists() {
        Persona persona = crearPersona(1L, "Laura", "Martínez", "laura@test.com");

        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));

        Persona resultado = buscarService.buscarPersonaOFallar(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Laura", resultado.getNombre());
        verify(personaRepository).findById(1L);
    }

    @Test
    void should_ThrowNotFoundException_When_IdNotExists() {
        when(personaRepository.findById(999L)).thenReturn(Optional.empty());

        PersonaNoEncontradaException exception = assertThrows(PersonaNoEncontradaException.class,
                () -> buscarService.buscarPersonaOFallar(999L));

        assertTrue(exception.getMessage().contains("999"));
        verify(personaRepository).findById(999L);
    }

    @Test
    void should_ReturnPersonaDTO_When_IdExists() {
        Persona persona = crearPersona(1L, "Juan", "Pérez", "juan@test.com");

        when(personaRepository.findById(1L)).thenReturn(Optional.of(persona));
        when(convertirService.convertirADTO(persona)).thenReturn(new PersonaDTO(1L, "Juan", "Pérez", "juan@test.com", FECHA_NACIMIENTO));

        PersonaDTO resultado = buscarService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id);
        assertEquals("Juan", resultado.nombre);
        assertEquals("juan@test.com", resultado.email);
        verify(personaRepository).findById(1L);
    }

    @Test
    void should_ThrowException_When_BuscarPorIdNotExists() {
        when(personaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(PersonaNoEncontradaException.class,
                () -> buscarService.buscarPorId(999L));

        verify(personaRepository).findById(999L);
    }

    // =============================================
    // Helper
    // =============================================

    private Persona crearPersona(Long id, String nombre, String apellido, String email) {
        Persona persona = new Persona(nombre, apellido, email, FECHA_NACIMIENTO);
        persona.setId(id);
        return persona;
    }
}
