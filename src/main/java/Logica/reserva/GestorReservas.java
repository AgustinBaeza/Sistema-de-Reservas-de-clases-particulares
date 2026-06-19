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
    private BuscadorDiponibilidad buscador;

    public GestorReservas( BuscadorDiponibilidad buscador ) {
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

    public void cancelarReserva(Reserva reserva){
        reserva.cancelarReserva();
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }
}
