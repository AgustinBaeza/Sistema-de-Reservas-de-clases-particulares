package Logica.tutor;

//Clase de las materias de los tutores

public class MateriaTutor {

    private String nombreMateria;
    private int tarifa;
    private int cupoMaximo;

    public MateriaTutor(String nombreMateria, int tarifa, int cupoMaximo) {
        this.nombreMateria = nombreMateria;
        this.tarifa = tarifa;
        this.cupoMaximo = cupoMaximo;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public int getTarifa() {
        return tarifa;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

    public void setTarifa(int tarifa) {
        this.tarifa = tarifa;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }
}