package Logica.excepciones;

/**
 * Excepción lanzada cuando se requiere un objeto seleccionado por el usuario desde la interfaz grafica y este no fue proporcionado
 */

public class ObjetoNoSeleccionadoException extends RuntimeException {

    /**
     * Constructor utilizado para lanzar mensaje descriptivo sobre la excepcion
     * @param mensaje mensaje descriptivo sobre la excepcion
     */
    public ObjetoNoSeleccionadoException(String mensaje) {
        super(mensaje);
    }
}