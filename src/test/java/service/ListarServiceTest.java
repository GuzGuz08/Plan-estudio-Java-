package service;

import application.port.out.PersonaRepositoryPort;
import domain.model.Persona;
import domain.service.ListarService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarServiceTest {

    @Mock
    PersonaRepositoryPort personaRepository;

    @InjectMocks
    ListarService listarService;

    private static final LocalDate FECHA_NACIMIENTO = LocalDate.of(1995, 6, 15);

    @Test
    void should_ReturnAllPersonas_When_ListarPersonas() {
        List<Persona> personas = List.of(
                crearPersona(1L, "Ana", "García", "ana@test.com"),
                crearPersona(2L, "Carlos", "López", "carlos@test.com")
        );

        when(personaRepository.findAllByOrderByApellidoAsc()).thenReturn(personas);

        List<Persona> resultado = listarService.listarPersonas();

        assertEquals(2, resultado.size());
        assertEquals("Ana", resultado.get(0).getNombre());
        assertEquals("Carlos", resultado.get(1).getNombre());
        verify(personaRepository).findAllByOrderByApellidoAsc();
    }

    @Test
    void should_ReturnEmptyList_When_NoPersonasExist() {
        when(personaRepository.findAllByOrderByApellidoAsc()).thenReturn(List.of());

        List<Persona> resultado = listarService.listarPersonas();

        assertTrue(resultado.isEmpty());
        verify(personaRepository).findAllByOrderByApellidoAsc();
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
