package Logica.reserva;

/**
 * Estado inicial de una nueva reserva creada, antes de ser confirmada o cancelada
 * Permite modificar libremente la reserva, ademas de transicionar su estado a confirmada o cancelada
 */
public class StatePendiente implements StateReserva {

    /**
     * Metodo para confirmar una reserva, traslada su estado a una reserva confirmada
     * @param reserva reserva sobre la cual se intenta confirmar su sesion
     */
    @Override
    public void confirmar(Reserva reserva) {
        reserva.setEstado(new StateConfirmada());
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
     * Metodo para validar la modificacion de una reserva en estado pendiente
     * Como es una reserva en estado pendiente, su modificacion es libre, por lo cual el metodo no prohibe nada
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
        return "PENDIENTE";
    }
}
