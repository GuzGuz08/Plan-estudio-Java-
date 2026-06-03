package service;

import application.dto.PersonaDTO;
import application.service.SanitizacionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SanitizacionService — Prevención XSS")
class SanitizacionServiceTest {

    private SanitizacionService sanitizacionService;

    @BeforeEach
    void setUp() {
        sanitizacionService = new SanitizacionService();
    }

    @Test
    @DisplayName("Debe eliminar etiquetas HTML del nombre")
    void should_StripHtmlTags_When_NombreContainsHtml() {
        String resultado = sanitizacionService.sanitizarTexto("<script>alert('xss')</script>Juan");
        assertFalse(resultado.contains("<script>"));
        assertTrue(resultado.contains("Juan"));
    }

    @Test
    @DisplayName("Debe escapar caracteres peligrosos")
    void should_EscapeDangerousChars_When_InputContainsThem() {
        String resultado = sanitizacionService.sanitizarTexto("Juan & María");
        assertTrue(resultado.contains("&amp;"));
    }

    @Test
    @DisplayName("Debe normalizar espacios en blanco")
    void should_TrimWhitespace_When_InputHasExtraSpaces() {
        String resultado = sanitizacionService.sanitizarTexto("  Juan  ");
        assertEquals("Juan", resultado);
    }

    @Test
    @DisplayName("Debe retornar null si el valor es null")
    void should_ReturnNull_When_InputIsNull() {
        assertNull(sanitizacionService.sanitizarTexto(null));
    }

    @Test
    @DisplayName("Debe convertir email a minúsculas")
    void should_LowerCaseEmail_When_EmailHasUpperCase() {
        String resultado = sanitizacionService.sanitizarEmail("Juan@Correo.COM");
        assertEquals("juan@correo.com", resultado);
    }

    @Test
    @DisplayName("Debe sanitizar todos los campos del DTO")
    void should_SanitizeAllFields_When_DtoHasDangerousContent() {
        PersonaDTO dto = new PersonaDTO(
                null,
                "<b>Juan</b>",
                "<i>Pérez</i>",
                "JUAN@TEST.COM",
                LocalDate.of(1990, 1, 1)
        );

        sanitizacionService.sanitizar(dto);

        assertFalse(dto.nombre.contains("<b>"));
        assertFalse(dto.apellido.contains("<i>"));
        assertEquals("juan@test.com", dto.email);
    }

    @Test
    @DisplayName("Debe eliminar caracteres de control")
    void should_RemoveControlChars_When_InputContainsThem() {
        String resultado = sanitizacionService.sanitizarTexto("Juan\u0000Carlos");
        assertFalse(resultado.contains("\u0000"));
    }
}
