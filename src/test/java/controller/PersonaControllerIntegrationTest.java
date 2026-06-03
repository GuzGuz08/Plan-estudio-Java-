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
class PersonaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonaJpaRepository personaRepository;

    @BeforeEach
    void setUp() {
        personaRepository.deleteAll();
    }

    private PersonaEntity crearPersonaEnBD(String nombre, String apellido, String email) {
        PersonaEntity persona = new PersonaEntity(nombre, apellido, email, LocalDate.of(1990, 5, 15));
        return personaRepository.save(persona);
    }

    private String jsonPersona(String nombre, String apellido, String email, String fecha) {
        return """
                {
                    "nombre": "%s",
                    "apellido": "%s",
                    "email": "%s",
                    "fechaNacimiento": "%s"
                }
                """.formatted(nombre, apellido, email, fecha);
    }

    @Nested
    @DisplayName("GET /api/personas")
    class ListarTodasTests {

        @Test
        @Order(1)
        @DisplayName("Debe retornar 200 y lista vacía cuando no hay personas")
        void should_Return200AndEmptyList_When_NoPersonasExist() throws Exception {
            mockMvc.perform(get("/api/personas"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(0)));
        }

        @Test
        @Order(2)
        @DisplayName("Debe retornar 200 y lista de personas existentes")
        void should_Return200AndPersonasList_When_PersonasExist() throws Exception {
            crearPersonaEnBD("Juan", "Pérez", "juan@correo.com");
            crearPersonaEnBD("Ana", "García", "ana@correo.com");

            mockMvc.perform(get("/api/personas"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].nombre").exists())
                    .andExpect(jsonPath("$[0].apellido").exists())
                    .andExpect(jsonPath("$[0].email").exists());
        }
    }

    @Nested
    @DisplayName("GET /api/personas/{id}")
    class BuscarPorIdTests {

        @Test
        @Order(3)
        @DisplayName("Debe retornar 200 y la persona cuando el ID existe")
        void should_Return200AndPersona_When_IdExists() throws Exception {
            PersonaEntity persona = crearPersonaEnBD("Juan", "Pérez", "juan@correo.com");

            mockMvc.perform(get("/api/personas/{id}", persona.getId()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(persona.getId()))
                    .andExpect(jsonPath("$.nombre").value("Juan"))
                    .andExpect(jsonPath("$.apellido").value("Pérez"))
                    .andExpect(jsonPath("$.email").value("juan@correo.com"))
                    .andExpect(jsonPath("$.fechaNacimiento").value("1990-05-15"));
        }

        @Test
        @Order(4)
        @DisplayName("Debe retornar 404 cuando el ID no existe")
        void should_Return404_When_IdNotExists() throws Exception {
            mockMvc.perform(get("/api/personas/{id}", 999L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.mensaje").value("Persona no encontrada"));
        }
    }

    @Nested
    @DisplayName("POST /api/personas")
    class RegistrarTests {

        @Test
        @Order(5)
        @DisplayName("Debe retornar 201 y la persona creada con datos válidos")
        void should_Return201AndCreatedPersona_When_ValidData() throws Exception {
            String json = jsonPersona("Carlos", "López", "carlos@correo.com", "1995-08-20");

            mockMvc.perform(post("/api/personas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.nombre").value("Carlos"))
                    .andExpect(jsonPath("$.apellido").value("López"))
                    .andExpect(jsonPath("$.email").value("carlos@correo.com"))
                    .andExpect(jsonPath("$.fechaNacimiento").value("1995-08-20"));
        }

        @Test
        @Order(6)
        @DisplayName("Debe retornar 400 cuando el nombre está vacío")
        void should_Return400_When_NombreIsBlank() throws Exception {
            String json = jsonPersona("", "López", "carlos@correo.com", "1995-08-20");

            mockMvc.perform(post("/api/personas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.errores").isArray())
                    .andExpect(jsonPath("$.errores[0].campo").value("nombre"));
        }

        @Test
        @Order(7)
        @DisplayName("Debe retornar 400 cuando el apellido está vacío")
        void should_Return400_When_ApellidoIsBlank() throws Exception {
            String json = jsonPersona("Carlos", "", "carlos@correo.com", "1995-08-20");

            mockMvc.perform(post("/api/personas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.errores").isArray())
                    .andExpect(jsonPath("$.errores[0].campo").value("apellido"));
        }

        @Test
        @Order(8)
        @DisplayName("Debe retornar 400 cuando el email tiene formato inválido")
        void should_Return400_When_EmailIsInvalid() throws Exception {
            String json = jsonPersona("Carlos", "López", "no-es-email", "1995-08-20");

            mockMvc.perform(post("/api/personas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.errores").isArray())
                    .andExpect(jsonPath("$.errores[0].campo").value("email"));
        }

        @Test
        @Order(9)
        @DisplayName("Debe retornar 400 cuando la fecha de nacimiento es futura")
        void should_Return400_When_FechaNacimientoIsFuture() throws Exception {
            String json = jsonPersona("Carlos", "López", "carlos@correo.com", "2030-01-01");

            mockMvc.perform(post("/api/personas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.errores").isArray())
                    .andExpect(jsonPath("$.errores[0].campo").value("fechaNacimiento"));
        }

        @Test
        @Order(10)
        @DisplayName("Debe retornar 400 cuando faltan todos los campos obligatorios")
        void should_Return400_When_AllFieldsMissing() throws Exception {
            String json = "{}";

            mockMvc.perform(post("/api/personas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.totalErrores").value(greaterThanOrEqualTo(3)))
                    .andExpect(jsonPath("$.errores").isArray());
        }

        @Test
        @Order(11)
        @DisplayName("Debe retornar error cuando el email ya existe")
        void should_ReturnError_When_EmailAlreadyExists() throws Exception {
            crearPersonaEnBD("Juan", "Pérez", "duplicado@correo.com");

            String json = jsonPersona("Carlos", "López", "duplicado@correo.com", "1995-08-20");

            mockMvc.perform(post("/api/personas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.mensaje").exists());
        }
    }

    @Nested
    @DisplayName("PUT /api/personas/{id}")
    class ActualizarTests {

        @Test
        @Order(12)
        @DisplayName("Debe retornar 200 y la persona actualizada con datos válidos")
        void should_Return200AndUpdatedPersona_When_ValidData() throws Exception {
            PersonaEntity persona = crearPersonaEnBD("Juan", "Pérez", "juan@correo.com");
            String json = jsonPersona("Juan Actualizado", "Pérez Mod", "juan.nuevo@correo.com", "1991-03-10");

            mockMvc.perform(put("/api/personas/{id}", persona.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(persona.getId()))
                    .andExpect(jsonPath("$.email").value("juan.nuevo@correo.com"));
        }

        @Test
        @Order(13)
        @DisplayName("Debe retornar 404 cuando se intenta actualizar una persona inexistente")
        void should_Return404_When_UpdatingNonExistentPersona() throws Exception {
            String json = jsonPersona("Carlos", "López", "carlos@correo.com", "1995-08-20");

            mockMvc.perform(put("/api/personas/{id}", 999L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.mensaje").value("Persona no encontrada"));
        }

        @Test
        @Order(14)
        @DisplayName("Debe retornar 400 cuando los datos de actualización son inválidos")
        void should_Return400_When_UpdateDataIsInvalid() throws Exception {
            PersonaEntity persona = crearPersonaEnBD("Juan", "Pérez", "juan@correo.com");
            String json = jsonPersona("", "", "no-email", "2030-01-01");

            mockMvc.perform(put("/api/personas/{id}", persona.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.errores").isArray())
                    .andExpect(jsonPath("$.totalErrores").value(greaterThanOrEqualTo(3)));
        }
    }

    @Nested
    @DisplayName("DELETE /api/personas/{id}")
    class EliminarTests {

        @Test
        @Order(15)
        @DisplayName("Debe retornar 204 cuando se elimina una persona existente")
        void should_Return204_When_DeletingExistingPersona() throws Exception {
            PersonaEntity persona = crearPersonaEnBD("Juan", "Pérez", "juan@correo.com");

            mockMvc.perform(delete("/api/personas/{id}", persona.getId()))
                    .andExpect(status().isNoContent());

            // Verificar que ya no existe en la BD
            mockMvc.perform(get("/api/personas/{id}", persona.getId()))
                    .andExpect(status().isNotFound());
        }

        @Test
        @Order(16)
        @DisplayName("Debe retornar 404 cuando se intenta eliminar una persona inexistente")
        void should_Return404_When_DeletingNonExistentPersona() throws Exception {
            mockMvc.perform(delete("/api/personas/{id}", 999L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.mensaje").value("Persona no encontrada"));
        }
    }
}
