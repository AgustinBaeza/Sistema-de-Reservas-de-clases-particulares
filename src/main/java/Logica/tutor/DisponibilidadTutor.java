package Logica.tutor;
import java.time.LocalDate;
import java.time.LocalTime;

public class DisponibilidadTutor {

    private LocalDate dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    public DisponibilidadTutor(LocalDate dia, LocalTime horaInicio, LocalTime horaFin) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public LocalDate getDia() {
        return dia;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    @Override
    public String toString() {
        return dia + " / " + horaInicio + " / " + horaFin;
    }
}
