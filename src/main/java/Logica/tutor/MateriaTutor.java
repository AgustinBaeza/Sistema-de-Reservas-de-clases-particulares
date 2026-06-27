package Logica.tutor;

/**
 * Clase que representa una materia asociada a un tutor
 * Almacena el nombre de la materia, la tarifa por clase y el cupo maximo
 * de estudiantes permitido para dicha materia
 */
public class MateriaTutor {

    private String nombreMateria;
    private int tarifa;
    private int cupoMaximo;

    /**
     * Constructor de MateriaTutor
     * @param nombreMateria nombre de la materia que imparte el tutor
     * @param tarifa tarifa asociada a la materia
     * @param cupoMaximo cantidad maxima de estudiantes permitidos
     */
    public MateriaTutor(String nombreMateria, int tarifa, int cupoMaximo) {
        this.nombreMateria = nombreMateria;
        this.tarifa = tarifa;
        this.cupoMaximo = cupoMaximo;
    }

    /**
     * Getter del nombre de la materia
     * @return nombre de la materia
     */
    public String getNombreMateria() {
        return nombreMateria;
    }

    /**
     * Getter de la tarifa de la materia
     * @return tarifa asociada a la materia
     */
    public int getTarifa() {
        return tarifa;
    }

    /**
     * Getter del cupo maximo de estudiantes
     * @return cupo maximo permitido
     */
    public int getCupoMaximo() {
        return cupoMaximo;
    }

    /**
     * Setter del nombre de la materia
     * @param nombreMateria nuevo nombre de la materia
     */
    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    /**
     * Setter de la tarifa de la materia
     * @param tarifa nueva tarifa de la materia
     */
    public void setTarifa(int tarifa) {
        this.tarifa = tarifa;
    }

    /**
     * Setter del cupo maximo de estudiantes
     * @param cupoMaximo nuevo cupo maximo permitido
     */
    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }
}