package Logica.reserva;

/**
 * Interfaz que establece el contrato general por el cual pasaran los estados de las Reservas
 * Corresponde a la aplicacion directa del patron de diseño State
 */
public interface StateReserva {

    /**
     * Metodo que intenta confirmar la reserva desde el estado actual en el que se encuentra
     * @param reserva reserva sobre la cual se intenta confirmar su sesion
     */
    void confirmar(Reserva reserva);

    /**
     * Metodo que intenta cancelar la reserva desde el estado actual en el que se encuentra
     * @param reserva reserva sobre la cual se intenta cancelar su sesion
     */
    void cancelar(Reserva reserva);

    /**
     * Metodo utilizado para verificar la modificacion de reservas segun su estado actual
     * @param reserva reserva sobre la cual se intenta modificar su sesion
     */
    void validarModificacion(Reserva reserva);

    /**
     * Getter del estado actual de la reserva
     * @return String que contiene el nombre descriptivo del estado actual, para mostrar en la interfaz
     */
    String getEstado();
}
