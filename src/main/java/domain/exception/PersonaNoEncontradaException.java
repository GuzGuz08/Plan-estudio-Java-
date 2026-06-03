package domain.exception;

public class PersonaNoEncontradaException extends RuntimeException {

    public PersonaNoEncontradaException(Long id) {
        super("Persona no encontrada con ID: " + id);
    }

    public PersonaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
