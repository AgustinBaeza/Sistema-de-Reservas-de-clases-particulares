package Logica.reserva;

import Logica.excepciones.AccionEstadoInvalida;

/**
 * Estado en el que una reserva se encuentra confirmada
 * No permite volver a confirmarse, pero si cancelarse o modificarse
 */
public class StateConfirmada implements StateReserva{

    /**
     * Metodo para confirmar una reserva, como su estado actual es confirmada, se impide volver a confirmarla con una excepcion
     * @param reserva reserva sobre la cual se intenta confirmar su sesion
     * @throws AccionEstadoInvalida se lanza excepcion pues no se puede confirmar una reserva ya confirmada
     */
    @Override
    public void confirmar(Reserva reserva) {
        throw new AccionEstadoInvalida("La reserva ya se encuentra confirmada.");
    }

    /**
     * Metodo para cancelar una reserva, traslada su estado a una reserva cancelada
     * @param reserva reserva sobre la cual se intenta cancelar su sesion
     */
    @Override
    public void cancelar(Reserva reserva) {
        reserva.setEstado(new StateCancelada());
    }

    /**
     * Metodo para validar la modificacion de una reserva en estado confirmada
     * Como es una reserva en estado confirmada, se permite su modificacion
     * @param reserva reserva sobre la cual se intenta modificar su sesion
     */
    @Override
    public void validarModificacion(Reserva reserva) {
    }

    /**
     * Getter del estado actual de la reserva
     * @return String que contiene el nombre descriptivo del estado actual, para mostrar en la interfaz
     */
    @Override
    public String getEstado() {
        return "CONFIRMADA";
    }

}
