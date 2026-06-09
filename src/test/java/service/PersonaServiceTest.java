package service;

import domain.repositoryPort.PersonaRepositoryPort;
import domain.model.Persona;
import domain.service.GuardarPersonaService;
import domain.service.SanitizacionService;
import domain.service.ValidarEmailService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonaServiceTest {

    @Mock
    PersonaRepositoryPort personaRepository;

    @Mock
    ValidarEmailService validarEmailService;

    private SanitizacionService sanitizacionService;

    private GuardarPersonaService guardarService;

    private static final LocalDate FECHA_NACIMIENTO = LocalDate.of(1995, 6, 15);

    @BeforeEach
    void setUp() {
        sanitizacionService = new SanitizacionService();
        guardarService = new GuardarPersonaService(personaRepository, validarEmailService, sanitizacionService);
    }
    

    @Test
    void should_RegisterPersona_When_ValidData() {
        Persona personaNuevo = new Persona("Juan", "Pérez", "juan@test.com", FECHA_NACIMIENTO);
        Persona personaGuardada = crearPersona(1L, "Juan", "Pérez", "juan@test.com");

        when(personaRepository.save(any(Persona.class))).thenReturn(personaGuardada);

        Persona resultado = guardarService.registrar(personaNuevo);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan", resultado.getNombre());
        assertEquals("Pérez", resultado.getApellido());
        assertEquals("juan@test.com", resultado.getEmail());
        assertEquals(FECHA_NACIMIENTO, resultado.getFechaNacimiento());

        verify(personaRepository).save(any(Persona.class));
    }

    @Test
    void should_SavePersona_When_CreatingNew() {
        Persona persona = new Persona("Ana", "López", "ana@test.com", FECHA_NACIMIENTO);

        when(personaRepository.save(any(Persona.class))).thenReturn(crearPersona(2L, "Ana", "López", "ana@test.com"));

        Persona resultado = guardarService.registrar(persona);

        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        verify(personaRepository).save(any(Persona.class));
    }

    private Persona crearPersona(Long id, String nombre, String apellido, String email) {
        Persona persona = new Persona(nombre, apellido, email, FECHA_NACIMIENTO);
        persona.setId(id);
        return persona;
    }
}
