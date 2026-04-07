package controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String KEY_STATUS = "status";
    private static final String KEY_ERROR = "error";
    private static final String KEY_MENSAJE = "mensaje";
    private static final String MENSAJE_VALIDACION = "Los datos enviados no cumplen con las validaciones requeridas";

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException excepcion) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put(KEY_STATUS, excepcion.getStatusCode().value());
        respuesta.put(KEY_ERROR, excepcion.getStatusCode().toString());
        respuesta.put(KEY_MENSAJE, excepcion.getReason());
        return ResponseEntity.status(excepcion.getStatusCode()).body(respuesta);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException excepcion) {
        List<Map<String, String>> listaErrores = new ArrayList<>();

        excepcion.getBindingResult().getFieldErrors().forEach(error -> {
            Map<String, String> detalle = new HashMap<>();
            detalle.put("campo", error.getField());
            detalle.put("valorRechazado", error.getRejectedValue() != null ? error.getRejectedValue().toString() : "null");
            detalle.put(KEY_MENSAJE, error.getDefaultMessage());
            detalle.put("explicacion", generarExplicacion(error.getField(), error.getCode(), error.getRejectedValue()));
            listaErrores.add(detalle);
        });

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put(KEY_STATUS, HttpStatus.BAD_REQUEST.value());
        respuesta.put(KEY_ERROR, HttpStatus.BAD_REQUEST.getReasonPhrase());
        respuesta.put(KEY_MENSAJE, MENSAJE_VALIDACION);
        respuesta.put("totalErrores", listaErrores.size());
        respuesta.put("errores", listaErrores);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    private String generarExplicacion(String campo, String codigoError, Object valorRechazado) {
        switch (campo) {
            case "nombre":
                return "El campo 'nombre' es obligatorio y no puede estar vacío. Debe enviar un texto con al menos un carácter.";
            case "apellido":
                return "El campo 'apellido' es obligatorio y no puede estar vacío. Debe enviar un texto con al menos un carácter.";
            case "email":
                if ("Email".equals(codigoError)) {
                    return "El valor '" + valorRechazado + "' no tiene formato de email válido. Ejemplo correcto: usuario@dominio.com";
                }
                return "El campo 'email' es obligatorio y debe tener formato válido. Ejemplo: usuario@dominio.com";
            case "fechaNacimiento":
                if ("Past".equals(codigoError)) {
                    return "La fecha '" + valorRechazado + "' es una fecha futura. La fecha de nacimiento debe ser anterior a la fecha actual.";
                }
                return "El campo 'fechaNacimiento' es obligatorio. Debe enviar una fecha en formato YYYY-MM-DD, por ejemplo: 1995-03-15";
            default:
                return "El campo '" + campo + "' no cumple con la validación requerida.";
        }
    }
}
