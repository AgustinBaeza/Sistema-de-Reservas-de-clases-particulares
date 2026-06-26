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

    public GestorReservas(BuscadorDisponibilidad buscador) {
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
         * Para evitar un falso conflicto contra si misma, genera una lista temporal con todas las reservas del sistema
         * menos la reserva actual que se desea modificar antes de realizar la verificacion horaria.
         */
    public void modificarReserva(Reserva reserva, Tutor nuevoTutor, MateriaTutor nuevaMateria,
                                 Estudiante nuevoEstudiante, LocalDate nuevaFecha,
                                 LocalTime nuevoHoraInicio, LocalTime nuevoHoraFin)
            throws ConflictoHorarioException {

        reserva.validarModificacion();

        if (!buscador.tutorDisponible(nuevoTutor, nuevaFecha, nuevoHoraInicio, nuevoHoraFin)) {
            throw new ConflictoHorarioException("El tutor no tiene disponibilidad en ese horario.");
        }

        ArrayList<Reserva> otrasReservas = new ArrayList<>(this.reservas);

        otrasReservas.remove(reserva);

        if (buscador.hayConflictoHorario(nuevoTutor, otrasReservas, nuevaFecha, nuevoHoraInicio, nuevoHoraFin)) {
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