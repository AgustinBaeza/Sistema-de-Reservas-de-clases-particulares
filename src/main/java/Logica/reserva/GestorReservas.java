package Logica.reserva;

import Logica.estudiante.Estudiante;
import Logica.excepciones.ConflictoHorarioException;
import Logica.tutor.MateriaTutor;
import Logica.tutor.Tutor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Clase que crea reservas, las modifica, y obtiene datos de ellas,
 * como su numero de reservas activas, y las listas de tutores y estudiantes
 * inscritos de forma cronologica
 */
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

    /**
     * retorna las reservas no canceladas
     * @return lista de reservas con estado CONFIRMADA o PENDIENTE
     */
    public ArrayList<Reserva> getReservasActivas(){
        ArrayList<Reserva> activa = new ArrayList<>();
        for(Reserva r : reservas){
            if(r.getEstadoReserva() != "CANCELADA"){
                activa.add(r);
            }
        }
        return activa;
    }

    /**
     * retorna reservas de un tutor, ordenadas por fecha y hora
     * @param tutor cuyas reservas se deben consultar
     * @return lista de reservas de un tutor ordenadas
     */
    public ArrayList<Reserva> getReservasTutor(Tutor tutor){
        ArrayList<Reserva> resultado = new ArrayList<>();
        for ( Reserva r : reservas ){
            if ( r.getTutor().getId() == tutor.getId() ){
                resultado.add(r);
            }
        }
        resultado.sort(Comparator.comparing(Reserva::getFecha).thenComparing(Reserva::getHoraInicio));
        return resultado;
    }

    /**
     * retorna reservas de un estudiante, ordenadas por fecha y hora
     * @param estudiante cuyas reservas se deben consultar
     * @return lista de reservas de un estudiante    ordenadas
     */
    public ArrayList<Reserva> getReservasEstudiante(Estudiante estudiante){
        ArrayList<Reserva> resultado = new ArrayList<>();
        for ( Reserva r : reservas ){
            if ( r.getEstudiante().getId() == estudiante.getId() ){
                resultado.add(r);
            }
        }
        resultado.sort(Comparator.comparing(Reserva::getFecha).thenComparing(Reserva::getHoraInicio));
        return resultado;
    }

    /**
     * cuenta cuantas reservas no canceladas tiene un tutor para una materia
     * @param tutor cuyas reservas se consultan
     * @param materiaTutor materia cual se cuenta el cupo ocupado
     * @return cantidad de reservas no canceladas activas para ese tutor y materia
     */
    public int contadorReservasActivas(Tutor tutor, MateriaTutor materiaTutor){
        int contador = 0;
        for ( Reserva r : reservas ){
            if ( r.getTutor().getId() == tutor.getId()
                 && r.getMateriaTutor().getNombreMateria().equalsIgnoreCase(materiaTutor.getNombreMateria())
                 && r.getEstadoReserva() != "CANCELADA"){
                contador++;
            }
        }
        return contador;
    }

}