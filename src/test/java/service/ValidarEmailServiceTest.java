package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import application.port.out.PersonaRepositoryPort;
import application.service.ValidarEmailService;
import domain.exception.EmailDuplicadoException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidarEmailServiceTest {

    @Mock
    PersonaRepositoryPort personaRepository;

    @InjectMocks
    ValidarEmailService validarEmailService;

    @Test
    void should_NotThrowException_When_EmailIsUnique() {
        when(personaRepository.existsByEmailAndIdNot("nuevo@test.com", 0L)).thenReturn(false);

        assertDoesNotThrow(() -> validarEmailService.validarEmailUnico("nuevo@test.com", 0L));

        verify(personaRepository).existsByEmailAndIdNot("nuevo@test.com", 0L);
    }

    @Test
    void should_ThrowBadRequest_When_EmailAlreadyExists() {
        when(personaRepository.existsByEmailAndIdNot("duplicado@test.com", 0L)).thenReturn(true);

        EmailDuplicadoException exception = assertThrows(EmailDuplicadoException.class,
                () -> validarEmailService.validarEmailUnico("duplicado@test.com", 0L));

        assertTrue(exception.getMessage().contains("duplicado@test.com"));
    }

    @Test
    void should_NotThrowException_When_EmailBelongsToSamePersona() {
        Long personaId = 5L;
        when(personaRepository.existsByEmailAndIdNot("mismo@test.com", personaId)).thenReturn(false);

        assertDoesNotThrow(() -> validarEmailService.validarEmailUnico("mismo@test.com", personaId));

        verify(personaRepository).existsByEmailAndIdNot("mismo@test.com", personaId);
    }

    @Test
    void should_ThrowBadRequest_When_EmailUsedByAnotherPersona() {
        Long personaId = 5L;
        when(personaRepository.existsByEmailAndIdNot("otro@test.com", personaId)).thenReturn(true);

        EmailDuplicadoException exception = assertThrows(EmailDuplicadoException.class,
                () -> validarEmailService.validarEmailUnico("otro@test.com", personaId));

        assertTrue(exception.getMessage().contains("otro@test.com"));
    }
}
