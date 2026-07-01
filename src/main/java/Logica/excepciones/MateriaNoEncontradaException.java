package Logica.excepciones;

/**
 * Excepción lanzada cuando se busca una materia dentro de las materias impartidas por un tutor que no es impartida por este ultimo
 */

public class MateriaNoEncontradaException extends RuntimeException {

    /**
     * Constructor utilizado para lanzar mensaje descriptivo sobre la excepcion
     * @param mensaje mensaje descriptivo sobre la excepcion
     */
    public MateriaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}