package Logica.tutor;

import java.util.ArrayList;

/**
 * Clase que representa a un tutor encargado de impartir clases particulares
 * Mantiene los datos de contacto del tutor junto con las materias que imparte
 * y los bloques de disponibilidad horaria que ha declarado.
 */
public class Tutor {

    private int id;
    private String nombre;
    private String correo;
    private String telefono;
    private ArrayList<MateriaTutor> materias;
    private ArrayList<DisponibilidadTutor> disponibilidades;

    /**
     * Constructor de un tutor nuevo en el sistema
     * @param id identificador unico del tutor
     * @param nombre nombre del tutor
     * @param correo correo electronico de contacto del tutor
     * @param telefono telefono de contacto del tutor
     */
    Tutor(int id, String nombre, String correo, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.materias = new ArrayList<>();
        this.disponibilidades = new ArrayList<>();
    }

    /**
     * Metodo que agrega una materia a la lista de materias que imparte el tutor
     * @param materia materia a agregar
     */
    public void agregarMateria(MateriaTutor materia) {
        materias.add(materia);
    }

    /**
     * Metodo que agrega un bloque de disponibilidad horaria a la lista del tutor
     * @param disponibilidad bloque de disponibilidad a agregar
     */
    public void agregarDisponibilidad(DisponibilidadTutor disponibilidad) {
        disponibilidades.add(disponibilidad);
    }

    /**
     * Metodo que busca una materia del tutor por su nombre, ignorando mayusculas y minusculas
     * @param nombre nombre de la materia a buscar
     * @return la MateriaTutor encontrada, o null si el tutor no imparte esa materia
     */
    public MateriaTutor buscarMateria(String nombre) {
        for (MateriaTutor materia : materias) {
            if (materia.getNombreMateria().equalsIgnoreCase(nombre)) {
                return materia;
            }
        }
        return null;
    }

    /**
     * Metodo para editar los datos de contacto del tutor
     * El identificador del tutor no se modifica, ya que corresponde a su identidad dentro del sistema
     * @param nombre nuevo nombre del tutor
     * @param correo nuevo correo de contacto
     * @param telefono nuevo telefono de contacto
     */
    public void editarDatos(String nombre, String correo, String telefono) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
    }

    /**
     * Getter del identificador unico del tutor
     * @return int que representa el id del tutor
     */
    public int getId() {
        return id;
    }

    /**
     * Getter del nombre del tutor
     * @return String con el nombre del tutor
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Getter del correo de contacto del tutor
     * @return String con el correo del tutor
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Getter del telefono de contacto del tutor
     * @return String con el telefono del tutor
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Getter de la lista de materias que imparte el tutor
     * @return ArrayList de MateriaTutor con las materias asociadas al tutor
     */
    public ArrayList<MateriaTutor> getMaterias() {
        return materias;
    }

    /**
     * Getter de la lista de bloques de disponibilidad horaria del tutor
     * @return ArrayList de DisponibilidadTutor con los bloques declarados por el tutor
     */
    public ArrayList<DisponibilidadTutor> getDisponibilidades() {
        return disponibilidades;
    }

    /**
     * Metodo toString descriptivo del tutor
     * @return String que contiene el nombre y correo del tutor
     */
    @Override
    public String toString() {
        return nombre + " / " + correo;
    }
}