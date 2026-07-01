package Logica.excepciones;

/**
 * Excepcion lanzada cuando se intenta agregar materias, disponibilidades o finalizar
 * la construccion de un tutor sin haber definido previamente sus datos basicos
 * mediante el metodo conDatosBasicos()
 */
public class ConstruccionTutorInvalidaException extends RuntimeException {

    /**
     * Constructor de la excepcion
     * @param message detalle del motivo por el cual la construccion es invalida
     */
    public ConstruccionTutorInvalidaException(String message) {
        super(message);
    }
}