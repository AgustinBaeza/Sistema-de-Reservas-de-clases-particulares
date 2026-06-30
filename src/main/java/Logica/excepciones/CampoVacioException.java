package Logica.excepciones;

/**
 * Excepción lanzada cuando un campo de texto requerido por el sistema se encuentra vacío o nulo
 */

public class CampoVacioException extends RuntimeException {

    /**
     * Constructor utilizado para lanzar mensaje descriptivo sobre la excepcion
     * @param mensaje mensaje descriptivo sobre la excepcion
     */
    public CampoVacioException(String mensaje) {
        super(mensaje);
    }
}