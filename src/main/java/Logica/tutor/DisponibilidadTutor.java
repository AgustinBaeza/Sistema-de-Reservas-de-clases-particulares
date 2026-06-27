package Logica.tutor;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Clase que representa un bloque de disponibilidad horaria de un tutor
 * Permite almacenar el dia, la hora de inicio y la hora de termino
 * en que el tutor se encuentra disponible para realizar clases
 */
public class DisponibilidadTutor {

    private LocalDate dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    /**
     * Constructor de DisponibilidadTutor
     * @param dia dia en que el tutor se encuentra disponible
     * @param horaInicio hora de inicio del bloque disponible
     * @param horaFin hora de termino del bloque disponible
     */
    public DisponibilidadTutor(LocalDate dia, LocalTime horaInicio, LocalTime horaFin) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    /**
     * Getter del dia de disponibilidad
     * @return dia disponible
     */
    public LocalDate getDia() {
        return dia;
    }

    /**
     * Getter de la hora de inicio
     * @return hora de inicio del bloque
     */
    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    /**
     * Getter de la hora de termino
     * @return hora de termino del bloque
     */
    public LocalTime getHoraFin() {
        return horaFin;
    }

    /**
     * Metodo toString descriptivo de la disponibilidad del tutor
     * @return texto con el dia, hora de inicio y hora de termino
     */
    @Override
    public String toString() {
        return dia + " / " + horaInicio + " / " + horaFin;
    }
}
