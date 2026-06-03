package infrastructure.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import application.dto.PersonaDTO;
import application.dto.PersonaPageDTO;
import application.port.in.IActualizarService;
import application.port.in.IBuscarAvanzadoService;
import application.port.in.IBuscarService;
import application.port.in.IEliminarService;
import application.port.in.IListarService;
import application.port.in.IRegistrarService;

@RestController
@RequestMapping("/api/personas")
public class PersonaController {

    private final IRegistrarService registrarService;
    private final IActualizarService actualizarService;
    private final IEliminarService eliminarService;
    private final IListarService listarService;
    private final IBuscarService buscarService;
    private final IBuscarAvanzadoService buscarAvanzadoService;

    public PersonaController(IRegistrarService registrarService, IActualizarService actualizarService,
                             IEliminarService eliminarService, IListarService listarService,
                             IBuscarService buscarService, IBuscarAvanzadoService buscarAvanzadoService) {
        this.registrarService = registrarService;
        this.actualizarService = actualizarService;
        this.eliminarService = eliminarService;
        this.listarService = listarService;
        this.buscarService = buscarService;
        this.buscarAvanzadoService = buscarAvanzadoService;
    }

    @PostMapping
    public ResponseEntity<PersonaDTO> registrarPersona(@Valid @RequestBody PersonaDTO personaDTO) {
        PersonaDTO nuevaPersona = registrarService.registrar(personaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPersona);
    }

    @GetMapping
    public ResponseEntity<List<PersonaDTO>> listarPersonas() {
        List<PersonaDTO> personas = listarService.listarPersonas();
        return ResponseEntity.ok(personas);
    }

    @GetMapping("/search")
    public ResponseEntity<PersonaPageDTO> buscarPersonas(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) Integer edadMin,
            @RequestParam(required = false) Integer edadMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "apellido,asc") String sort) {
        PersonaPageDTO resultado = buscarAvanzadoService.buscar(nombre, apellido, edadMin, edadMax, page, size, sort);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaDTO> buscarPorId(@PathVariable Long id) {
        PersonaDTO persona = buscarService.buscarPorId(id);
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