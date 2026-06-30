package Logica.excepciones;

/**
 * Excepción lanzada cuando los datos de fecha u horario son nulos o fuera del rango
 */

public class FechaHoraInvalidaException extends RuntimeException {

    /**
     * Constructor utilizado para lanzar mensaje descriptivo sobre la excepcion
     * @param mensaje mensaje descriptivo sobre la excepcion
     */
    public FechaHoraInvalidaException(String mensaje) {
        super(mensaje);
    }
}