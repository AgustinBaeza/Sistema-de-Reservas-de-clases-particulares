package Logica.estudiante;

import java.util.ArrayList;

/**
 * Clase encargada de administrar los estudiantes registrados en el sistema
 * Permite crear perfiles de estudiantes, editarlos, buscarlos por identificador
 * y obtener la lista completa de estudiantes registrados.
 */
public class GestionarEstudiantes {

    private ArrayList<Estudiante> estudiantes;
    private int siguienteId = 500;

    /**
     * Constructor de GestionarEstudiantes
     * Inicializa la lista de estudiantes vacia
     */
    public GestionarEstudiantes() {
        this.estudiantes = new ArrayList<>();
    }

    /**
     * Metodo que crea un nuevo perfil de estudiante y lo registra en el sistema
     * El identificador del estudiante es asignado automaticamente por el gestor
     * @param nombre nombre del estudiante
     * @param correo correo de contacto del estudiante
     * @param telefono telefono de contacto del estudiante
     * @return estudiante creado y registrado en el sistema
     */
    public Estudiante crearPerfilEstudiante(String nombre, String correo, String telefono) {
        Estudiante estudiante = new Estudiante(siguienteId, nombre, correo, telefono);
        siguienteId++;
        estudiantes.add(estudiante);
        return estudiante;
    }

    /**
     * Metodo que edita el perfil de un estudiante existente
     * El estudiante se identifica mediante su id
     * @param id identificador del estudiante a editar
     * @param nuevoNombre nuevo nombre del estudiante
     * @param nuevoCorreo nuevo correo de contacto
     * @param nuevoTelefono nuevo telefono de contacto
     * @return true si el estudiante fue encontrado y editado, false si no existe
     */
    public boolean editarPerfilEstudiante(int id, String nuevoNombre, String nuevoCorreo, String nuevoTelefono) {
        Estudiante estudiante = buscarEstudiantePorID(id);

        if (estudiante == null) {
            return false;
        }

        estudiante.editarDatos(nuevoNombre, nuevoCorreo, nuevoTelefono);
        return true;
    }

    /**
     * Metodo que busca un estudiante registrado segun su identificador
     * @param id identificador del estudiante a buscar
     * @return estudiante encontrado, o null si no existe un estudiante con ese id
     */
    public Estudiante buscarEstudiantePorID(int id) {
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getId() == id) {
                return estudiante;
            }
        }
        return null;
    }

    /**
     * Getter de la lista de estudiantes registrados en el sistema
     * @return ArrayList de Estudiante con todos los estudiantes registrados
     */
    public ArrayList<Estudiante> getEstudiantes() {
        return estudiantes;
    }
}
