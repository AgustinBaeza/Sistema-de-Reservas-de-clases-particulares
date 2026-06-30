package Logica.excepciones;

/**
 * Excepción lanzada cuando se intenta crear o modificar una reserva que supera el cupo máximo de estudiantes
 * permitido para una materia de un tutor.
 */

public class CupoMaximoExcedidoException extends RuntimeException {

    /**
     * Constructor utilizado para lanzar mensaje descriptivo sobre la excepcion
     * @param message mensaje descriptivo sobre la excepcion
     */
    public CupoMaximoExcedidoException(String message) {
        super(message);
    }
}