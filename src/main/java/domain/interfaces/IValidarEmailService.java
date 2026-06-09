package domain.interfaces;

import domain.model.Persona;

public interface IValidarEmailService {
Persona validarEmailUnico(String email, Long idExcluido);
}
