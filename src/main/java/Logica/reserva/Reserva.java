package Logica.reserva;

import Logica.estudiante.*;
import Logica.tutor.*;

import java.time.*;

public class Reserva {
    private Tutor tutor;
    private Estudiante estudiante;
    private MateriaTutor materiaTutor;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private EstadoReserva estadoReserva;

    public Reserva(Tutor tutor, Estudiante estudiante, MateriaTutor materiaTutor,
                   LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        this.tutor = tutor;
        this.estudiante = estudiante;
        this.materiaTutor = materiaTutor;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estadoReserva = EstadoReserva.PENDIENTE;
    }

    public void confirmarReserva(){
        estadoReserva = EstadoReserva.CONFIRMADA;
    }

    public void cancelarReserva(){
        estadoReserva = EstadoReserva.CANCELADA;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public EstadoReserva getEstadoReserva() {
        return estadoReserva;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public MateriaTutor getMateriaTutor() {
        return materiaTutor;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }
}
