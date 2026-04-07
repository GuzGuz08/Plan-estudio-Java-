package controller;
import dto.PersonaDTO;
import service.ActualizarService;
import service.EliminarService;
import service.PersonaService;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    private final PersonaService personaService;
    private final ActualizarService actualizarService;
    private final EliminarService eliminarService;

    public PersonaController(PersonaService personaService, ActualizarService actualizarService, EliminarService eliminarService) {
        this.personaService = personaService;
        this.actualizarService = actualizarService;
        this.eliminarService = eliminarService;
    }

    @PostMapping
    public ResponseEntity<PersonaDTO> registrarPersona(@Valid @RequestBody PersonaDTO personaDTO) {
        PersonaDTO nuevaPersona = personaService.registrar(personaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPersona);
    }

    @GetMapping
    public ResponseEntity<List<PersonaDTO>> listarPersonas() {
        List<PersonaDTO> personas = personaService.listarPersonas();
        return ResponseEntity.ok(personas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaDTO> buscarPorId(@PathVariable Long id) {
        PersonaDTO persona = personaService.buscarPorId(id);
        return ResponseEntity.ok(persona);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonaDTO> actualizarPersona(@PathVariable Long id, @Valid @RequestBody PersonaDTO personaDTO) {
        PersonaDTO personaActualizada = actualizarService.actualizar(id, personaDTO); 
        return ResponseEntity.ok(personaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPersona(@PathVariable Long id) {
        eliminarService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}