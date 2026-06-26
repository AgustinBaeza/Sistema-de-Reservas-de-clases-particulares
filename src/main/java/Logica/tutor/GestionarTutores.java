package Logica.tutor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Clase que administra el conjunto de tutores registrados en el sistema
 * Permite crear perfiles de tutores mediante TutorBuilder, editarlos,
 * y asociarles materias y bloques de disponibilidad horaria.
 */
public class GestionarTutores {

    private ArrayList<Tutor> tutores;
    private int siguienteId = 1;

    /**
     * Constructor de GestionarTutores
     * Inicializa la lista de tutores vacia
     */
    public GestionarTutores() {
        this.tutores = new ArrayList<>();
    }

    /**
     * Metodo que crea un nuevo perfil de tutor y lo registra en el sistema
     * El identificador del tutor es asignado automaticamente por el gestor
     * @param nombre nombre del tutor
     * @param correo correo de contacto
     * @param telefono telefono de contacto
     */
    public void crearPerfilTutor(String nombre, String correo, String telefono) {
        Tutor tutor = new TutorBuilder()
                .conDatosBasicos(siguienteId, nombre, correo, telefono)
                .build();
        siguienteId++;
        tutores.add(tutor);
    }

    /**
     * Metodo que edita el perfil de un tutor existente, identificado por su id
     * @param id identificador del tutor a editar
     * @param nuevoNombre nombre actualizado
     * @param nuevoCorreo correo actualizado
     * @param nuevoTelefono telefono actualizado
     * @return true si el tutor fue encontrado y editado, false si no existe
     */
    public boolean editarPerfilTutor(int id, String nuevoNombre, String nuevoCorreo, String nuevoTelefono) {
        Tutor tutor = buscarTutorPorId(id);

        if (tutor == null) {
            return false;
        }

        tutor.editarDatos(nuevoNombre, nuevoCorreo, nuevoTelefono);
        return true;
    }

    /**
     * Metodo que asocia una materia, con su tarifa y cupo maximo, a un tutor existente.
     * Si el tutor ya imparte una materia con el mismo nombre, no se agrega y retorna false.
     * @param idTutor identificador del tutor al que se le asigna la materia
     * @param nombreMateria nombre de la materia impartida
     * @param tarifa tarifa por clase de la materia
     * @param cupoMaximo cantidad maxima de estudiantes permitidos en la materia
     * @return true si el tutor existe y la materia fue agregada, false si no existe el tutor o la materia ya estaba registrada
     */
    public boolean definirMateriaTutor(int idTutor, String nombreMateria, int tarifa, int cupoMaximo) {
        Tutor tutor = buscarTutorPorId(idTutor);

        if (tutor == null) {
            return false;
        }

        if (tutor.buscarMateria(nombreMateria) != null) {
            return false;
        }

        tutor.agregarMateria(new MateriaTutor(nombreMateria, tarifa, cupoMaximo));
        return true;
    }

    /**
     * Metodo que agrega un bloque de disponibilidad horaria a un tutor existente
     * @param idTutor identificador del tutor
     * @param dia dia de la disponibilidad
     * @param horaInicio hora de inicio del bloque
     * @param horaFin hora de termino del bloque
     * @return true si el tutor existe y la disponibilidad fue agregada, false en caso contrario
     */
    public boolean definirDisponibilidadTutor(int idTutor, LocalDate dia, LocalTime horaInicio, LocalTime horaFin) {
        Tutor tutor = buscarTutorPorId(idTutor);

        if (tutor == null) {
            return false;
        }

        DisponibilidadTutor disponibilidad = new DisponibilidadTutor(dia, horaInicio, horaFin);
        tutor.agregarDisponibilidad(disponibilidad);
        return true;
    }

    /**
     * Metodo que busca un tutor registrado segun su identificador
     * @param id identificador del tutor a buscar
     * @return el tutor encontrado, o null si no existe ninguno con ese id
     */
    public Tutor buscarTutorPorId(int id) {
        for (Tutor tutor : tutores) {
            if (tutor.getId() == id) {
                return tutor;
            }
        }
        return null;
    }

    /**
     * Getter de la lista de tutores registrados en el sistema
     * @return ArrayList de Tutor con todos los tutores registrados
     */
    public ArrayList<Tutor> getTutores() {
        return tutores;
    }
}