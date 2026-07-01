package Logica.excepciones;

/**
 * Excepción lanzada cuando se detecta un conflicto de horario al crear o modificar una reserva,
 * ya sea por falta de disponibilidad del tutor o por superposición con otra reserva existente.
 */

public class ConflictoHorarioException extends RuntimeException {

    /**
     * Constructor utilizado para lanzar mensaje descriptivo sobre la excepcion
     * @param message mensaje descriptivo sobre la excepcion
     */
    public ConflictoHorarioException(String message) {
        super(message);
    }
}