package Logica.estudiante;

/**
 * Clase que representa a un estudiante registrado en el sistema
 * Mantiene los datos principales del estudiante, como identificador,
 * nombre, correo y telefono de contacto
 */
public class Estudiante {

    private int id;
    private String nombre;
    private String correo;
    private String telefono;

    /**
     * Constructor de Estudiante
     * @param id identificador unico del estudiante
     * @param nombre nombre del estudiante
     * @param correo correo de contacto del estudiante
     * @param telefono telefono de contacto del estudiante
     */
    public Estudiante(int id, String nombre, String correo, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
    }

    /**
     * Metodo que edita los datos principales del estudiante
     * El identificador no se modifica, ya que corresponde a su identidad dentro del sistema
     * @param nombre nuevo nombre del estudiante
     * @param correo nuevo correo de contacto
     * @param telefono nuevo telefono de contacto
     */
    public void editarDatos(String nombre, String correo, String telefono) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
    }

    /**
     * Getter del identificador del estudiante
     * @return id del estudiante
     */
    public int getId() {
        return id;
    }

    /**
     * Getter del nombre del estudiante
     * @return nombre del estudiante
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Getter del correo del estudiante
     * @return correo de contacto del estudiante
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Getter del telefono del estudiante
     * @return telefono de contacto del estudiante
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Metodo toString descriptivo del estudiante
     * @return texto con el nombre y correo del estudiante
     */
    @Override
    public String toString() {
        return nombre + " - " + correo;
    }
}