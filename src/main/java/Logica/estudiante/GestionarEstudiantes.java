package Logica.estudiante;

import java.util.ArrayList;

public class GestionarEstudiantes {

    private ArrayList<Estudiante> estudiantes;

    public GestionarEstudiantes() {
        this.estudiantes = new ArrayList<>();
    }
    private int siguienteId = 500;

    public void crearPerfilEstudiante(String nombre, String correo, String telefono) {
        Estudiante estudiante = new Estudiante(siguienteId, nombre, correo, telefono);
        siguienteId++;
        estudiantes.add(estudiante);
    }

    public boolean editarPerfilEstudiante(int id, String nuevoNombre, String nuevoCorreo, String nuevoTelefono) {
        Estudiante estudiante = buscarEstudiantePorID(id);

        if (estudiante == null) {
            return false;
        }

        estudiante.editarDatos(nuevoNombre, nuevoCorreo, nuevoTelefono);
        return true;
    }

    public Estudiante buscarEstudiantePorID(int id) {
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getId() == id) {
                return estudiante;
            }
        }
        return null;
    }

    public ArrayList<Estudiante> getEstudiantes() {
        return estudiantes;
    }
}
