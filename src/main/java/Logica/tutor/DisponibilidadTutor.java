package Logica.tutor;

public class DisponibilidadTutor {

    private String dia;
    private String horaInicio;
    private String horaFin;

    public DisponibilidadTutor(String dia, String horaInicio, String horaFin) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public String getDia() {
        return dia;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    @Override
    public String toString() {
        return dia + " / " + horaInicio + " / " + horaFin;
    }
}
