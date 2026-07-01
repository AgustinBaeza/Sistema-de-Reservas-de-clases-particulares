package Logica.reserva;

import Logica.excepciones.AccionEstadoInvalidaException;

/**
 * Estado en el que una reserva se encuentra cancelada
 * Es un estado final que no permite ninguna accion adicional sobre su estado, osea ni confirmarse, ni modificarse, ni volver a cancelarse.
 */
public class StateCancelada implements StateReserva{

    /**
     * Metodo para confirmar una reserva, como su estado actual es cancelada, se impide volver a confirmarla con una excepcion
     * @param reserva reserva sobre la cual se intenta confirmar su sesion
     * @throws AccionEstadoInvalidaException se lanza excepcion pues no se puede confirmar una reserva ya cancelada
     */
    @Override
    public void confirmar(Reserva reserva) {
        throw new AccionEstadoInvalidaException("No se puede confirmar una reserva cancelada.");
    }

    /**
     * Metodo para cancelar una reserva, como su estado actual es cancelada, se impide volver a cancelarla con una excepcion
     * @param reserva reserva sobre la cual se intenta cancelar su sesion
     * @throws AccionEstadoInvalidaException se lanza excepcion pues no se puede cancelar una reserva ya cancelada
     */
    @Override
    public void cancelar(Reserva reserva) {
        throw new AccionEstadoInvalidaException("La reserva ya se encuentra cancelada.");
    }

    /**
     * Metodo para validar la modificacion de una reserva en estado cancelada
     * Como es una reserva en estado cancelada, la modificacion de su detalle no tiene sentido, por lo cual se impide
     * @param reserva reserva sobre la cual se intenta modificar su sesion
     */
    @Override
    public void validarModificacion(Reserva reserva) {
        throw new AccionEstadoInvalidaException("No se puede modificar una reserva cancelada.");
    }

    /**
     * Getter del estado actual de la reserva
     * @return String que contiene el nombre descriptivo del estado actual, para mostrar en la interfaz
     */
    @Override
    public String getEstado() {
        return "CANCELADA";
    }
}
