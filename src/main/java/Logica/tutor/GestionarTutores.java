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
     * Metodo que crea un nuevo perfil de tutor y lo
     * registra en el sistema
     * El identificador del tutor es asignado automaticamente por el gestor
     * @param nombre nombre del tutor
     * @param correo correo de contacto
     * @param telefono telefono de contacto
     * @return tutor
     */
    public Tutor crearPerfilTutor(String nombre, String correo, String telefono) {
        Tutor tutor = new TutorBuilder()
                .conDatosBasicos(siguienteId, nombre, correo, telefono)
                .build();
        siguienteId++;
        tutores.add(tutor);

        return tutor;
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
     * Metodo que asocia una materia, con su tarifa y cupo maximo, a un tutor existente
     * Si el tutor no existe, la materia ya estaba registrada o los datos son invalidos,
     * no se agrega y retorna false
     * @param idTutor identificador del tutor al que se le asigna la materia
     * @param nombreMateria nombre de la materia impartida
     * @param tarifa tarifa por clase de la materia
     * @param cupoMaximo cantidad maxima de estudiantes permitidos en la materia
     * @return true si la materia fue agregada correctamente o false en caso contrario
     */
    public boolean definirMateriaTutor(int idTutor, String nombreMateria, int tarifa, int cupoMaximo) {
        Tutor tutor = buscarTutorPorId(idTutor);

        if (tutor == null) {
            return false;
        }
        if (nombreMateria == null || nombreMateria.isBlank()) {
            return false;
        }
        if (tarifa < 0 || cupoMaximo <= 0) {
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
     * Valida que el tutor exista, que los datos no sean nulos y que la hora de inicio
     * sea anterior a la hora de termino
     * @param idTutor identificador del tutor
     * @param dia dia de la disponibilidad
     * @param horaInicio hora de inicio del bloque
     * @param horaFin hora de termino del bloque
     * @return true si la disponibilidad fue agregada correctamente o false en caso contrario
     */
    public boolean definirDisponibilidadTutor(int idTutor, LocalDate dia, LocalTime horaInicio, LocalTime horaFin) {
        Tutor tutor = buscarTutorPorId(idTutor);

        if (tutor == null) {
            return false;
        }
        if (dia == null || horaInicio == null || horaFin == null) {
            return false;
        }
        if (!horaInicio.isBefore(horaFin)) {
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

    /**
     * Metodo que actualiza la tarifa de una materia asociada a un tutor
     * @param idTutor identificador del tutor
     * @param nombreMateria nombre de la materia a modificar
     * @param nuevaTarifa nueva tarifa de la materia
     * @return true si se actualizo correctamente o false en caso contrario
     */
    public boolean definirTarifaTutor(int idTutor, String nombreMateria, int nuevaTarifa) {
        Tutor tutor = buscarTutorPorId(idTutor);
        if (tutor == null) {
            return false;
        }
        if (nombreMateria == null || nombreMateria.isBlank()) {
            return false;
        }
        if (nuevaTarifa < 0) {
            return false;
        }
        MateriaTutor materia = tutor.buscarMateria(nombreMateria);
        if (materia == null) {
            return false;
        }
        materia.setTarifa(nuevaTarifa);
        return true;
    }

    /**
     * Metodo que actualiza el cupo maximo de una materia asociada a un tutor
     * @param idTutor identificador del tutor
     * @param nombreMateria nombre de la materia a modificar
     * @param nuevoCupoMaximo nuevo cupo maximo
     * @return true si se actualizo correctamente o false en caso contrario
     */
    public boolean definirCupoMaximoPorMateria(int idTutor, String nombreMateria, int nuevoCupoMaximo) {
        Tutor tutor = buscarTutorPorId(idTutor);
        if (tutor == null) {
            return false;
        }
        if (nombreMateria == null || nombreMateria.isBlank()) {
            return false;
        }
        if (nuevoCupoMaximo <= 0) {
            return false;
        }
        MateriaTutor materia = tutor.buscarMateria(nombreMateria);
        if (materia == null) {
            return false;
        }
        materia.setCupoMaximo(nuevoCupoMaximo);
        return true;
    }

    /**
     * Metodo que edita una materia completa asociada a un tutor
     * Permite modificar el nombre de la materia, la tarifa y el cupo maximo
     * Retorna false si el tutor no existe, la materia actual no existe,
     * los datos son invalidos o el nuevo nombre ya esta registrado en otra materia
     * @param idTutor identificador del tutor
     * @param nombreMateriaActual nombre actual de la materia
     * @param nuevoNombreMateria nuevo nombre de la materia
     * @param nuevaTarifa nueva tarifa
     * @param nuevoCupoMaximo nuevo cupo maximo
     * @return true si la materia fue editada o false en caso contrario
     */
    public boolean editarMateriaTutor(int idTutor, String nombreMateriaActual, String nuevoNombreMateria, int nuevaTarifa, int nuevoCupoMaximo) {
        Tutor tutor = buscarTutorPorId(idTutor);

        if (tutor == null) {
            return false;
        }
        if (nombreMateriaActual == null || nombreMateriaActual.isBlank()) {
            return false;
        }
        if (nuevoNombreMateria == null || nuevoNombreMateria.isBlank()) {
            return false;
        }
        if (nuevaTarifa < 0 || nuevoCupoMaximo <= 0) {
            return false;
        }

        MateriaTutor materiaRepetida = tutor.buscarMateria(nuevoNombreMateria);

        if (materiaRepetida != null && !nombreMateriaActual.equalsIgnoreCase(nuevoNombreMateria)) {
            return false;
        }

        return tutor.editarMateria(nombreMateriaActual, nuevoNombreMateria, nuevaTarifa, nuevoCupoMaximo);
    }

    /**
     * Metodo que edita un bloque de disponibilidad horaria de un tutor.
     * @param idTutor identificador del tutor
     * @param indiceDisponibilidad posicion de la disponibilidad dentro de la lista
     * @param nuevoDia nuevo dia disponible
     * @param nuevaHoraInicio nueva hora de inicio
     * @param nuevaHoraFin nueva hora de termino
     * @return true si se edito correctamente o false en caso contrario
     */
    public boolean editarDisponibilidadTutor(int idTutor, int indiceDisponibilidad, LocalDate nuevoDia, LocalTime nuevaHoraInicio, LocalTime nuevaHoraFin) {
        Tutor tutor = buscarTutorPorId(idTutor);
        if (tutor == null) {
            return false;
        }
        return tutor.editarDisponibilidad(indiceDisponibilidad, nuevoDia, nuevaHoraInicio, nuevaHoraFin);
    }

    /**
     * Metodo que busca todos los tutores que imparten una materia especifica
     * Recorre la lista de tutores registrados y agrega al resultado aquellos
     * que tengan una materia con el nombre indicado
     * @param nombreMateria nombre de la materia a buscar
     * @return ArrayList de Tutor con los tutores que imparten la materia indicada
     */
    public ArrayList<Tutor> buscarTutoresPorMateria(String nombreMateria) {
        ArrayList<Tutor> tutoresEncontrados = new ArrayList<>();

        if (nombreMateria == null || nombreMateria.isBlank()) {
            return tutoresEncontrados;
        }

        for (Tutor tutor : tutores) {
            if (tutor.buscarMateria(nombreMateria) != null) {
                tutoresEncontrados.add(tutor);
            }
        }

        return tutoresEncontrados;
    }

    /**
     * Metodo para que las id sigan sumando despues de cargar tutores
     * al sistema mediante CargadoGuardadoTutor
     */
    public void actualizarSiguienteId() {

        int mayor = 0;

        for (Tutor tutor : tutores) {
            if (tutor.getId() > mayor) {
                mayor = tutor.getId();
            }
        }

        siguienteId = mayor + 1;
    }

}