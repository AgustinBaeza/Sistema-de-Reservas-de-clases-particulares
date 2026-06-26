package Logica.reserva;

import Logica.estudiante.Estudiante;
import Logica.tutor.MateriaTutor;
import Logica.tutor.Tutor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Clase que representa una reserva de clases particulaes
 * Mantiene toda la informacion de la reserva (fecha, horarios, materia) y gestiona sus cambios de estado
 * delegando el comportamiento en la implementacion del patron State mediante el objeto estadoReserva.
 */
public class Reserva {
    private Tutor tutor;
    private Estudiante estudiante;
    private MateriaTutor materiaTutor;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private StateReserva estadoReserva;

    /**
     * Constructor de una reserva nueva en el sistema
     * Al momento de crearse, la reserva se inicializa de manera automatica en un estado pendiente.
     * @param tutor tutor de la clase particular
     * @param estudiante estudiante que solicita la clase particular
     * @param materiaTutor materia que imparte el tutor
     * @param fecha fecha en la que se llevara a cabo la clase
     * @param horaInicio horario en el que inicia la clase
     * @param horaFin horario en el que finaliza la clase
     */
    public Reserva(Tutor tutor, Estudiante estudiante, MateriaTutor materiaTutor,
                   LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        this.tutor = tutor;
        this.estudiante = estudiante;
        this.materiaTutor = materiaTutor;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estadoReserva = new StatePendiente();
    }

    /**
     * Metodo para confirmar la reserva actual
     * Dependiendo del estado en el que se encuentre, permitira la confirmacion o lanzara una excepcion si la accion es invalida
     */
    public void confirmarReserva(){
        estadoReserva.confirmar(this);
    }

    /**
     * Metodo para cancelar la reserva actual
     * Si la reserva ya se encuentra en el estado cancelada, el State impedira la accion lanzando una excepcion
     */
    public void cancelarReserva(){
        estadoReserva.cancelar(this);
    }

    /**
     * Metodo para validar si la reserva segun su estado es compatible con una modificacion
     * El unico estado en el que esta opcion se impide es si se encuentra cancelada
     */
    public void validarModificacion(){
        estadoReserva.validarModificacion(this);
    }

    /**
     * Setter del estado actual de la reserva, permite cambiar su estado entre Pendiente - Confirmada - Cancelada
     * @param nuevoEstado estado al que cambiara la reserva
     */
    void setEstado(StateReserva nuevoEstado){
        this.estadoReserva = nuevoEstado;
    }

    /**
     * Metodo que permite actualizar el estado de una reserva tras una modificacion ya validada segun su estado.
     * @param nuevoTutor tutor nuevo a asignar a la reserva
     * @param nuevaMateria materia nueva a asignar a la reserva
     * @param nuevoEstudiante estudiante nuevo a asignar a la reserva
     * @param nuevaFecha fecha nueva a asignar a la reserva
     * @param nuevoHoraInicio horario de inicio a asignar a la reserva
     * @param nuevoHoraFin horario de finalizacion a asignar a la reserva
     */
    public void actualizarDetalles(Tutor nuevoTutor, MateriaTutor nuevaMateria, Estudiante nuevoEstudiante,
                                       LocalDate nuevaFecha, LocalTime nuevoHoraInicio, LocalTime nuevoHoraFin) {
        this.tutor = nuevoTutor;
        this.materiaTutor = nuevaMateria;
        this.estudiante = nuevoEstudiante;
        this.fecha = nuevaFecha;
        this.horaInicio = nuevoHoraInicio;
        this.horaFin = nuevoHoraFin;
    }

    /**
     * Getter del tutor que tiene asignada la reserva
     * @return el objeto Tutor que ofrece la tutoria
     */
    public Tutor getTutor() {
        return tutor;
    }

    /**
     * Getter del estado actual de la reserva en formato de texto
     * @return String que contiene el nombre descriptivo del estado actual proveniente de la interfaz StateReserva
     */
    public String getEstadoReserva() {
        return estadoReserva.getEstado();
    }

    /**
     * Getter del horario de termino de la clase
     * @return LocalTime que representa la hora en la que finaliza la sesion
     */
    public LocalTime getHoraFin() {
        return horaFin;
    }

    /**
     * Getter del horario de inicio de la clase
     * @return LocalTime que representa la hora en la que comienza la sesion
     */
    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    /**
     * Getter de la fecha a la que se agendo la clase
     * @return LocalDate que contiene el dia, mes y año de la cita
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Getter de la materia que imparte el tutor de la clase
     * @return MateriaTutor con los datos de la asignatura vinculada al tutor
     */
    public MateriaTutor getMateriaTutor() {
        return materiaTutor;
    }

    /**
     * Getter del estudiante que solicito la reserva
     * @return el objeto Estudiante correspondiente al alumno de la clase
     */
    public Estudiante getEstudiante() {
        return estudiante;
    }


    /**
     * Metodo toString descriptivo de la reserva hecha
     * @return String que detalla el tutor, estudiante, materia, fecha, hora de inicio y fin, y el estado actual de la reserva
     */
    @Override
    public String toString() {
        return "Reserva{" +
                "tutor=" + tutor +
                ", estudiante=" + estudiante +
                ", materiaTutor=" + materiaTutor +
                ", fecha=" + fecha +
                ", horaInicio=" + horaInicio +
                ", horaFin=" + horaFin +
                ", estadoReserva=" + estadoReserva +
                '}';
    }
}