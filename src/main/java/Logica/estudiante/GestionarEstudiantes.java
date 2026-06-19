package Logica.estudiante;

import java.util.ArrayList;

public class GestionarEstudiantes {

    private ArrayList<Estudiante> estudiantes;

    public GestionarEstudiantes() {
        this.estudiantes = new ArrayList<>();
    }

    public void crearPerfilEstudiante(String nombre, String correo, String telefono) {
        Estudiante estudiante = new Estudiante(nombre, correo, telefono);
        estudiantes.add(estudiante);
    }

    public boolean editarPerfilEstudiante(String nombreActual, String nuevoNombre, String nuevoCorreo, String nuevoTelefono) {
        Estudiante estudiante = buscarEstudiantePorNombre(nombreActual);

        if (estudiante == null) {
            return false;
        }

        estudiante.editarDatos(nuevoNombre, nuevoCorreo, nuevoTelefono);
        return true;
    }

    public Estudiante buscarEstudiantePorNombre(String nombre) {
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getNombre().equalsIgnoreCase(nombre)) {
                return estudiante;
            }
        }
        return null;
    }

    public ArrayList<Estudiante> getEstudiantes() {
        return estudiantes;
    }
}
