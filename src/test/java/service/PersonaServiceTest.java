package service;

import application.dto.PersonaDTO;
import application.port.out.PersonaRepositoryPort;
import application.service.ConvertirService;
import application.service.SanitizacionService;
import application.service.ValidarEmailService;
import domain.exception.EmailDuplicadoException;
import domain.model.Persona;
import domain.service.CrearService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

    @Mock
    ConvertirService convertirService;

    @Mock
    SanitizacionService sanitizacionService;

    @InjectMocks
    CrearService personaService;

    private static final LocalDate FECHA_NACIMIENTO = LocalDate.of(1995, 6, 15);

    // =============================================
    // Tests para registrar persona
    // =============================================

    @Test
    void should_RegisterPersona_When_ValidData() {
        PersonaDTO dto = new PersonaDTO(null, "Juan", "Pérez", "juan@test.com", FECHA_NACIMIENTO);
        Persona personaGuardada = crearPersona(1L, "Juan", "Pérez", "juan@test.com");
        PersonaDTO dtoEsperado = new PersonaDTO(1L, "Juan", "Pérez", "juan@test.com", FECHA_NACIMIENTO);

        when(personaRepository.save(any(Persona.class))).thenReturn(personaGuardada);
        when(convertirService.convertirADTO(personaGuardada)).thenReturn(dtoEsperado);

        PersonaDTO resultado = personaService.registrar(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id);
        assertEquals("Juan", resultado.nombre);
        assertEquals("Pérez", resultado.apellido);
        assertEquals("juan@test.com", resultado.email);
        assertEquals(FECHA_NACIMIENTO, resultado.fechaNacimiento);

        verify(validarEmailService).validarEmailUnico("juan@test.com", 0L);
        verify(personaRepository).save(any(Persona.class));
        verify(convertirService).convertirADTO(personaGuardada);
    }

    @Test
    void should_CallValidarEmailWithZeroId_When_Registering() {
        PersonaDTO dto = new PersonaDTO(null, "Ana", "López", "ana@test.com", FECHA_NACIMIENTO);
        Persona personaGuardada = crearPersona(2L, "Ana", "López", "ana@test.com");
        PersonaDTO dtoEsperado = new PersonaDTO(2L, "Ana", "López", "ana@test.com", FECHA_NACIMIENTO);

        when(personaRepository.save(any(Persona.class))).thenReturn(personaGuardada);
        when(convertirService.convertirADTO(personaGuardada)).thenReturn(dtoEsperado);

        personaService.registrar(dto);

        verify(validarEmailService).validarEmailUnico("ana@test.com", 0L);
    }

    @Test
    void should_ThrowException_When_EmailAlreadyExists() {
        PersonaDTO dto = new PersonaDTO(null, "Juan", "Pérez", "duplicado@test.com", FECHA_NACIMIENTO);

        doThrow(new EmailDuplicadoException("duplicado@test.com"))
                .when(validarEmailService).validarEmailUnico("duplicado@test.com", 0L);

        assertThrows(EmailDuplicadoException.class,
                () -> personaService.registrar(dto));

        verify(personaRepository, never()).save(any(Persona.class));
    }

    // =============================================
    // Tests para convertirADTO (ahora en ConvertirService)
    // =============================================

    @Test
    void should_ConvertPersonaToDTO_When_ValidPersona() {
        Persona persona = crearPersona(5L, "María", "Rodríguez", "maria@test.com");
        ConvertirService convertirServiceReal = new ConvertirService();

        PersonaDTO dto = convertirServiceReal.convertirADTO(persona);

        assertEquals(5L, dto.id);
        assertEquals("María", dto.nombre);
        assertEquals("Rodríguez", dto.apellido);
        assertEquals("maria@test.com", dto.email);
        assertEquals(FECHA_NACIMIENTO, dto.fechaNacimiento);
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
