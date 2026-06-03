package controller;

import infrastructure.persistence.PersonaEntity;
import infrastructure.persistence.PersonaJpaRepository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = TestApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BuscarAvanzadoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonaJpaRepository personaRepository;

    @BeforeEach
    void setUp() {
        personaRepository.deleteAll();
    }

    private PersonaEntity crearPersonaEnBD(String nombre, String apellido, String email, LocalDate fechaNacimiento) {
        PersonaEntity persona = new PersonaEntity(nombre, apellido, email, fechaNacimiento);
        return personaRepository.save(persona);
    }

    @Nested
    @DisplayName("GET /api/personas/search - Búsqueda por nombre")
    class BuscarPorNombreTests {

        @Test
        @Order(1)
        @DisplayName("Debe retornar personas cuyo nombre contenga el texto buscado (case insensitive)")
        void should_ReturnMatchingPersonas_When_SearchByNombreParcial() throws Exception {
            crearPersonaEnBD("Juan Carlos", "Pérez", "juan@correo.com", LocalDate.of(1990, 5, 15));
            crearPersonaEnBD("Juana", "García", "juana@correo.com", LocalDate.of(1985, 3, 20));
            crearPersonaEnBD("Ana", "López", "ana@correo.com", LocalDate.of(1995, 8, 10));

            mockMvc.perform(get("/api/personas/search").param("nombre", "juan"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.totalElements", is(2)));
        }

        @Test
        @Order(2)
        @DisplayName("Debe retornar lista vacía cuando no hay coincidencias por nombre")
        void should_ReturnEmptyContent_When_NombreNotFound() throws Exception {
            crearPersonaEnBD("Ana", "López", "ana@correo.com", LocalDate.of(1995, 8, 10));

            mockMvc.perform(get("/api/personas/search").param("nombre", "Carlos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(0)))
                    .andExpect(jsonPath("$.totalElements", is(0)));
        }
    }

    @Nested
    @DisplayName("GET /api/personas/search - Búsqueda por apellido")
    class BuscarPorApellidoTests {

        @Test
        @Order(3)
        @DisplayName("Debe retornar personas cuyo apellido contenga el texto buscado (case insensitive)")
        void should_ReturnMatchingPersonas_When_SearchByApellidoParcial() throws Exception {
            crearPersonaEnBD("Juan", "Pérez López", "juan@correo.com", LocalDate.of(1990, 5, 15));
            crearPersonaEnBD("Ana", "García", "ana@correo.com", LocalDate.of(1985, 3, 20));
            crearPersonaEnBD("Luis", "Pérez", "luis@correo.com", LocalDate.of(1992, 7, 25));

            mockMvc.perform(get("/api/personas/search").param("apellido", "pérez"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.totalElements", is(2)));
        }
    }

    @Nested
    @DisplayName("GET /api/personas/search - Búsqueda por rango de edad")
    class BuscarPorEdadTests {

        @Test
        @Order(4)
        @DisplayName("Debe retornar personas dentro del rango de edad especificado")
        void should_ReturnPersonas_When_WithinAgeRange() throws Exception {
            int year = LocalDate.now().getYear();
            crearPersonaEnBD("Joven", "Uno", "joven@correo.com", LocalDate.of(year - 25, 1, 1));
            crearPersonaEnBD("Medio", "Dos", "medio@correo.com", LocalDate.of(year - 35, 6, 15));
            crearPersonaEnBD("Mayor", "Tres", "mayor@correo.com", LocalDate.of(year - 55, 12, 31));

            mockMvc.perform(get("/api/personas/search")
                            .param("edadMin", "20")
                            .param("edadMax", "40"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(2)))
                    .andExpect(jsonPath("$.totalElements", is(2)));
        }

        @Test
        @Order(5)
        @DisplayName("Debe retornar vacío cuando nadie está en el rango de edad")
        void should_ReturnEmpty_When_NoPersonasInAgeRange() throws Exception {
            crearPersonaEnBD("Mayor", "Uno", "mayor@correo.com", LocalDate.of(1950, 1, 1));

            mockMvc.perform(get("/api/personas/search")
                            .param("edadMin", "18")
                            .param("edadMax", "25"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(0)))
                    .andExpect(jsonPath("$.totalElements", is(0)));
        }
    }

    @Nested
    @DisplayName("GET /api/personas/search - Paginación y metadatos")
    class PaginacionTests {

        @Test
        @Order(6)
        @DisplayName("Debe retornar metadatos de paginación correctos")
        void should_ReturnCorrectPaginationMetadata() throws Exception {
            for (int i = 1; i <= 15; i++) {
                crearPersonaEnBD("Persona" + i, "Apellido" + i, "persona" + i + "@correo.com",
                        LocalDate.of(1990, 1, i));
            }

            mockMvc.perform(get("/api/personas/search")
                            .param("page", "0")
                            .param("size", "5"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(5)))
                    .andExpect(jsonPath("$.totalElements", is(15)))
                    .andExpect(jsonPath("$.totalPages", is(3)))
                    .andExpect(jsonPath("$.currentPage", is(0)))
                    .andExpect(jsonPath("$.size", is(5)));
        }

        @Test
        @Order(7)
        @DisplayName("Debe retornar segunda página correctamente")
        void should_ReturnSecondPage_When_PageIs1() throws Exception {
            for (int i = 1; i <= 8; i++) {
                crearPersonaEnBD("Persona" + i, "Apellido" + i, "persona" + i + "@correo.com",
                        LocalDate.of(1990, 1, i));
            }

            mockMvc.perform(get("/api/personas/search")
                            .param("page", "1")
                            .param("size", "5"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(3)))
                    .andExpect(jsonPath("$.totalElements", is(8)))
                    .andExpect(jsonPath("$.totalPages", is(2)))
                    .andExpect(jsonPath("$.currentPage", is(1)));
        }

        @Test
        @Order(8)
        @DisplayName("Debe retornar todos los metadatos cuando no hay resultados")
        void should_ReturnEmptyWithMetadata_When_NoResults() throws Exception {
            mockMvc.perform(get("/api/personas/search"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(0)))
                    .andExpect(jsonPath("$.totalElements", is(0)))
                    .andExpect(jsonPath("$.totalPages", is(0)))
                    .andExpect(jsonPath("$.currentPage", is(0)));
        }
    }

    @Nested
    @DisplayName("GET /api/personas/search - Búsqueda combinada")
    class BusquedaCombinadaTests {

        @Test
        @Order(9)
        @DisplayName("Debe filtrar por nombre y apellido simultáneamente")
        void should_FilterByNombreAndApellido() throws Exception {
            crearPersonaEnBD("Juan", "Pérez", "juan@correo.com", LocalDate.of(1990, 5, 15));
            crearPersonaEnBD("Juan", "García", "juang@correo.com", LocalDate.of(1985, 3, 20));
            crearPersonaEnBD("Ana", "Pérez", "anap@correo.com", LocalDate.of(1995, 8, 10));

            mockMvc.perform(get("/api/personas/search")
                            .param("nombre", "Juan")
                            .param("apellido", "Pérez"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(1)))
                    .andExpect(jsonPath("$.content[0].nombre", is("Juan")))
                    .andExpect(jsonPath("$.content[0].apellido", is("Pérez")));
        }

        @Test
        @Order(10)
        @DisplayName("Debe filtrar por todos los criterios combinados")
        void should_FilterByAllCriteria() throws Exception {
            int year = LocalDate.now().getYear();
            crearPersonaEnBD("Juan", "Pérez", "juan@correo.com", LocalDate.of(year - 30, 5, 15));
            crearPersonaEnBD("Juan", "Pérez", "juan2@correo.com", LocalDate.of(year - 60, 3, 20));
            crearPersonaEnBD("Ana", "López", "ana@correo.com", LocalDate.of(year - 25, 8, 10));

            mockMvc.perform(get("/api/personas/search")
                            .param("nombre", "Juan")
                            .param("apellido", "Pérez")
                            .param("edadMin", "20")
                            .param("edadMax", "40"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(1)))
                    .andExpect(jsonPath("$.totalElements", is(1)));
        }
    }
}
