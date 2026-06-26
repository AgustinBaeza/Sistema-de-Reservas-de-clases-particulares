package Logica.reserva;

import Logica.estudiante.Estudiante;
import Logica.excepciones.ConflictoHorarioException;
import Logica.tutor.MateriaTutor;
import Logica.tutor.Tutor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class GestorReservas {
    private ArrayList<Reserva> reservas;
    private BuscadorDisponibilidad buscador;

    public GestorReservas( BuscadorDisponibilidad buscador ) {
        this.reservas = new ArrayList<>();
        this.buscador = buscador;
    }

    public Reserva crearReserva(Tutor tutor, Estudiante estudiante, MateriaTutor materiaTutor,
                                LocalDate fecha, LocalTime horaInicio, LocalTime horaFin)
                                throws ConflictoHorarioException {

        if (!buscador.tutorDisponible(tutor, fecha, horaInicio, horaFin)) {
            throw new ConflictoHorarioException("El tutor no tiene disponibilidad en ese horario.");
        }

        if (buscador.hayConflictoHorario(tutor, reservas, fecha, horaInicio, horaFin)) {
            throw new ConflictoHorarioException("El tutor ya tiene una reserva en ese horario.");
        }

        Reserva reserva = new Reserva(tutor, estudiante, materiaTutor, fecha, horaInicio, horaFin);
        reservas.add(reserva);
        return reserva;
    }

    /**
     * Metodo que modifica una reserva existente, validando su estado actual y la disponibilidad del nuevo tutor.
     * @param reserva reserva a modificar
     * @param nuevoTutor nuevo tutor a asignar
     * @param nuevaMateria nueva materia a asignar
     * @param nuevoEstudiante nuevo estudiante a asignar
     * @param nuevaFecha nueva fecha de la clase
     * @param nuevoHoraInicio nuevo horario de inicio
     * @param nuevoHoraFin nuevo horario de termino
     * @throws AccionEstadoInvalida si la reserva esta cancelada
     * @throws ConflictoHorarioException si el tutor no tiene disponibilidad o ya tiene una reserva en ese horario
     */
    public void modificarReserva(Reserva reserva, Tutor nuevoTutor, MateriaTutor nuevaMateria,
                                 Estudiante nuevoEstudiante, LocalDate nuevaFecha,
                                 LocalTime nuevoHoraInicio, LocalTime nuevoHoraFin)
            throws ConflictoHorarioException {

        reserva.validarModificacion();

        if (!buscador.tutorDisponible(nuevoTutor, nuevaFecha, nuevoHoraInicio, nuevoHoraFin)) {
            throw new ConflictoHorarioException("El tutor no tiene disponibilidad en ese horario.");
        }

        if (buscador.hayConflictoHorario(nuevoTutor, reservas, nuevaFecha, nuevoHoraInicio, nuevoHoraFin)) {
            throw new ConflictoHorarioException("El tutor ya tiene una reserva en ese horario.");
        }

        reserva.actualizarDetalles(nuevoTutor, nuevaMateria, nuevoEstudiante, nuevaFecha, nuevoHoraInicio, nuevoHoraFin);
    }

    public void cancelarReserva(Reserva reserva){
        reserva.cancelarReserva();
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }
}
