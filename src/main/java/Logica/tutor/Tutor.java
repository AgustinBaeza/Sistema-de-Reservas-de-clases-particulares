package Logica.tutor;

import java.util.ArrayList;

public class Tutor {

    private int id;
    private String nombre;
    private String correo;
    private String telefono;
    private ArrayList<MateriaTutor> materias;
    private ArrayList<DisponibilidadTutor> disponibilidades;

    public Tutor(int id, String nombre, String correo, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.materias = new ArrayList<>();
        this.disponibilidades = new ArrayList<>();
    }

    public void agregarMateria(MateriaTutor materia) {
        materias.add(materia);
    }

    public void agregarDisponibilidad(DisponibilidadTutor disponibilidad) {
        disponibilidades.add(disponibilidad);
    }

    public void editarDatos(String nombre, String correo, String telefono) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public ArrayList<MateriaTutor> getMaterias() {
        return materias;
    }

    public ArrayList<DisponibilidadTutor> getDisponibilidades() {
        return disponibilidades;
    }

    @Override
    public String toString() {
        return nombre + " / " + correo;
    }
}