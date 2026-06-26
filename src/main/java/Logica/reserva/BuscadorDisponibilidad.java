package Logica.reserva;

import Logica.tutor.DisponibilidadTutor;
import Logica.tutor.Tutor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Clase encargada de verificar la disponibilidad horaria de los tutores y detectar
 * conflictos entre reservas existentes en el sistema.
 */
public class BuscadorDisponibilidad {

    private boolean flagTutorDisponible;
    private boolean flagConflictoHorario;

    /**
     * Metodo que verifica si un tutor tiene disponibilidad declarada para un horario especifico utilizando una flag
     * que en un principio asume que el tutor no esta disponible para el horario.
     * Va analizando cada bloque horario registrado como disponible para el tutor y por cada uno aplica descartes secuenciales:
     * - Si es que la fecha del bloque horario no coincide con la fecha que se reserva la clase, se pasa al siguiente bloque horario.
     * - Si es que la hora de inicio de la reserva empieza antes de la hora registrada como disponible para el tutor, se pasa al siguiente bloque horario.
     * - Si es que la hora de termino de la reserva finaliza despues de la hora registrada como disponible para el tutor, se pasa al siguiente bloque horario.
     *
     * En caso de que un bloque supere todos los descartes, se activa la flag indicando que el tutor esta disponible, sino la flag permanece en false.
     * @param tutor tutor cuya disponibilidad se desea verificar
     * @param fecha fecha en la que se desea la clase
     * @param horaInicio hora de inicio de la reserva solicitada
     * @param horaFin hora de termino de la reserva solicitada
     * @return true si la flag de disponibilidad se activo al encontrar un bloque disponible para el tutor, false en caso contrario
     */
    public boolean tutorDisponible(Tutor tutor, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {

        this.flagTutorDisponible = false;

        for (DisponibilidadTutor bloqueHorario : tutor.getDisponibilidades()) {

            if (!bloqueHorario.getDia().equals(fecha)) {
                continue;
            }

            if (horaInicio.isBefore(bloqueHorario.getHoraInicio())) {
                continue;
            }

            if (horaFin.isAfter(bloqueHorario.getHoraFin())) {
                continue;
            }

            this.flagTutorDisponible = true;
            break;
        }

        return flagTutorDisponible;
    }

    /**
     * Metodo que verifica si un tutor tiene conflicto horario con alguna reserva existente utilizando una flag
     * que en un principio asume que no hay conflicto entre los horarios de las reservas.
     * Va analizando cada reserva registrada en el sistema y por cada una de ellas aplica descartes secuenciales:
     * - Si es que la reserva existente no sea del mismo tutor que se esta consultando, se descarta para evitar un conflicto falso ante el caso de que otro tutor tenga reservada una clase a la misma hora.
     * - Si es que no es del mismo dia el posible conflicto, se descarta para no abarcar reservas de otros dias.
     * - Si es que la reserva con la que esta chocando se encuentra cancelada, se descarta pues dado este caso ya no estaria siendo ocupado ese horario.
     * - Si es que la nueva reserva no empieza antes de que la otra reserva termine, se descarta al no coincidir en el mismo bloque horario.
     * - Si es que la nueva reserva no termina despues de que la otra reserva empiece, se descarta al no haber un choque en el rango de horas.
     *
     * Si una reserva supera todos los descartes, se modifica la flag confirmando que hay conflicto con otra reserva del mismo tutor, sino es un "falso conflicto" y la flag permanece en false.
     * @param tutor tutor cuya agenda se desea verificar
     * @param reservas lista de reservas existentes en el sistema
     * @param fecha fecha en la que se desea la clase
     * @param horaInicio hora de inicio de la reserva solicitada
     * @param horaFin hora de termino de la reserva solicitada
     * @return true si la flag de conflicto se activo al detectar una superposicion de horarios, false en caso contrario
     */
    public boolean hayConflictoHorario(Tutor tutor, ArrayList<Reserva> reservas,
                                       LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {

        this.flagConflictoHorario = false;

        for (Reserva reserva : reservas) {

            if (reserva.getTutor().getId() != tutor.getId()) {
                continue;
            }

            if (!reserva.getFecha().equals(fecha)) {
                continue;
            }

            if (reserva.getEstadoReserva().equals("CANCELADA")) {
                continue;
            }

            if (!horaInicio.isBefore(reserva.getHoraFin())) {
                continue;
            }

            if (!horaFin.isAfter(reserva.getHoraInicio())) {
                continue;
            }

            this.flagConflictoHorario = true;
            break;
        }

        return flagConflictoHorario;
    }
}