package Logica.excepciones;

/**
 * Excepción lanzada cuando se intenta realizar una acción sobre una reserva que no es válida según el estado actual en que se encuentra
 * (por ejemplo, confirmar una reserva ya cancelada).
 */

public class AccionEstadoInvalidaException extends RuntimeException {

    /**
     * Constructor utilizado para lanzar mensaje descriptivo sobre la excepcion
     * @param mensaje mensaje descriptivo sobre la excepcion
     */
    public AccionEstadoInvalidaException(String mensaje) {
        super(mensaje);
    }
}
