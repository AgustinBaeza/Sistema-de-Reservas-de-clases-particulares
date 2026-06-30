package Logica.excepciones;

/**
 * Excepción lanzada cuando un valor numérico ingresado para la tarifa o cupo maximo es negativo o cero.
 */

public class ValorNumericoInvalidoException extends RuntimeException {

    /**
     * Constructor utilizado para lanzar mensaje descriptivo sobre la excepcion
     * @param mensaje mensaje descriptivo sobre la excepcion
     */
    public ValorNumericoInvalidoException(String mensaje) {
        super(mensaje);
    }
}