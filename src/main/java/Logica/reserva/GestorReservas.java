package Logica.reserva;

import Logica.estudiante.Estudiante;
import Logica.excepciones.ConflictoHorarioException;
import Logica.excepciones.CupoMaximoExcedidoException;
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

    /**
     * Constructor de la clase
     * @param buscador instancia del BuscadorDisponibilidad para validar horarios
     */
    public GestorReservas(BuscadorDisponibilidad buscador) {
        this.reservas = new ArrayList<>();
        this.buscador = buscador;
    }

    /**
     * Crea una reserva entre tutor y estudiante para una materia,
     * verifica si el tutor tiene disponibilidad o si un mismo estudiante no tiene dos clases al mismo tiempo.
     * de horario y que exista cupo para la materia.
     *
     * @param tutor tutor que hace la tutoria
     * @param estudiante estudiante que solicita la tutoria
     * @param materiaTutor materia de la tutoria
     * @param fecha fecha de la tutoria
     * @param horaInicio hora de inicio de la tutoria
     * @param horaFin hora de finalizacion de la tutoria
     * @return reserva creada y agendada
     * @throws ConflictoHorarioException si el tutor ya tiene reserva o no tiene disponibilidad
     * @throws CupoMaximoExcedidoException si se alcanza el cupo maximo del tutor y/o materia
     */
    public Reserva crearReserva(Tutor tutor, Estudiante estudiante, MateriaTutor materiaTutor,
                                LocalDate fecha, LocalTime horaInicio, LocalTime horaFin)
            throws ConflictoHorarioException, CupoMaximoExcedidoException {

        if (!buscador.tutorDisponible(tutor, fecha, horaInicio, horaFin)) {
            throw new ConflictoHorarioException("El tutor no tiene disponibilidad en ese horario.");
        }

        if (buscador.hayConflictoHorarioTutor(tutor, reservas, fecha, horaInicio, horaFin)) {
            throw new ConflictoHorarioException("El tutor ya tiene una reserva en ese horario.");
        }

        if (buscador.hayConflictoHorarioEstudiante(estudiante, reservas, fecha, horaInicio, horaFin)) {
            throw new ConflictoHorarioException("El estudiante ya tiene una clase en ese horario.");
        }

        if ( contadorReservasActivas(tutor, materiaTutor) >= materiaTutor.getCupoMaximo() ){
            throw new CupoMaximoExcedidoException("El cupo de la materia esta completo.");
        }

        Reserva reserva = new Reserva(tutor, estudiante, materiaTutor, fecha, horaInicio, horaFin);
        reservas.add(reserva);
        return reserva;
    }

    /**
     * Metodo que modifica una reserva existente, validando su estado actual y la disponibilidad del nuevo tutor.
     * Para evitar un falso conflicto contra si misma, genera una lista temporal con todas las reservas del sistema
     * menos la reserva actual que se desea modificar antes de realizar la verificacion horaria.
     * Paralelamente se necesita validar el cupo de la materia nueva en caso de que se requiera modificar, para ese analisis
     * se excluye la materia actual del conteo de cupos pues de lo contrario generaria un falso conflicto con la misma materia.
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

        if (buscador.hayConflictoHorarioTutor(nuevoTutor, otrasReservas, nuevaFecha, nuevoHoraInicio, nuevoHoraFin)) {
            throw new ConflictoHorarioException("El tutor ya tiene una reserva en ese horario.");
        }

        if (buscador.hayConflictoHorarioEstudiante(nuevoEstudiante, otrasReservas, nuevaFecha, nuevoHoraInicio, nuevoHoraFin)) {
            throw new ConflictoHorarioException("El estudiante ya tiene una clase en ese horario.");
        }

        int activas = 0;
        for (Reserva r : reservas) {
            if (r != reserva
                    && r.getTutor().getId() == nuevoTutor.getId()
                    && r.getMateriaTutor().getNombreMateria().equalsIgnoreCase(nuevaMateria.getNombreMateria())
                    && !r.getEstadoReserva().equals("CANCELADA")) {
                activas++;
            }
        }
        if (activas >= nuevaMateria.getCupoMaximo()) {
            throw new CupoMaximoExcedidoException("El cupo máximo de la materia está completo.");
        }

        reserva.actualizarDetalles(nuevoTutor, nuevaMateria, nuevoEstudiante, nuevaFecha, nuevoHoraInicio, nuevoHoraFin);
    }

    /**
     * retorna los tutores compatibles para una solicitud de reserva
     * @param tutores lista de todos los tutores
     * @param nombreMateria nombre de la materia solcicitada
     * @param fecha fecha de la clase
     * @param horaInicio hora de inicio de la clase
     * @param horaFin hora de fin de la clase
     * @return lista de tutures compatibles
     */
    public ArrayList<Tutor> buscarTutoresDisponibles( ArrayList<Tutor> tutores, String nombreMateria,
                                                      LocalDate fecha, LocalTime horaInicio, LocalTime horaFin ){
        return buscador.buscarTutoresDisponibles(tutores, nombreMateria, fecha, horaInicio, horaFin, reservas);
    }

    /**
     * cancela la reserva
     * @param reserva reserva a cancelar
     */
    public void cancelarReserva(Reserva reserva){
        reserva.cancelarReserva();
    }

    /**
     * obtiene la lista con las reservas
     * @return lista con las reservas
     */
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
            if(!r.getEstadoReserva().equals("CANCELADA")){
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
                 && !r.getEstadoReserva().equals("CANCELADA")){
                contador++;
            }
        }
        return contador;
    }

}