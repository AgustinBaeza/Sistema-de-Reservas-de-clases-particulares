package Logica.tutor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class GestionarTutores {

    private ArrayList<Tutor> tutores;

    public GestionarTutores() {
        this.tutores = new ArrayList<>();
    }

    public void crearPerfilTutor(int id, String nombre, String correo, String telefono) {
        Tutor tutor = new Tutor(id, nombre, correo, telefono);
        tutores.add(tutor);
    }

    public boolean editarPerfilTutor(String nombreActual, String nuevoNombre, String nuevoCorreo, String nuevoTelefono) {
        Tutor tutor = buscarTutorPorNombre(nombreActual);

        if (tutor == null) {
            return false;
        }

        tutor.editarDatos(nuevoNombre, nuevoCorreo, nuevoTelefono);
        return true;
    }

    public boolean definirMateriaTutor(String nombreTutor, String nombreMateria, int tarifa, int cupoMaximo) {
        Tutor tutor = buscarTutorPorNombre(nombreTutor);

        if (tutor == null) {
            return false;
        }

        MateriaTutor materia = new MateriaTutor(nombreMateria, tarifa, cupoMaximo);
        tutor.agregarMateria(materia);
        return true;
    }

    public boolean definirDisponibilidadTutor(String nombreTutor, LocalDate dia, LocalTime horaInicio, LocalTime horaFin) {
        Tutor tutor = buscarTutorPorNombre(nombreTutor);

        if (tutor == null) {
            return false;
        }

        DisponibilidadTutor disponibilidad = new DisponibilidadTutor(dia, horaInicio, horaFin);
        tutor.agregarDisponibilidad(disponibilidad);
        return true;
    }

    public Tutor buscarTutorPorNombre(String nombre) {
        for (Tutor tutor : tutores) {
            if (tutor.getNombre().equalsIgnoreCase(nombre)) {
                return tutor;
            }
        }
        return null;
    }

    public ArrayList<Tutor> getTutores() {
        return tutores;
    }
}
