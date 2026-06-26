package Logica.tutor;

import Logica.excepciones.ConstruccionTutorInvalida;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Clase que permite construir un objeto Tutor paso a paso
 * Implementa el patron Builder, permitiendo asociar materias y bloques de disponibilidad
 * al tutor antes de finalizar su construccion mediante el metodo build().
 * Requiere llamar a conDatosBasicos() antes de cualquier otro metodo de construccion.
 */
public class TutorBuilder {

    private int id;
    private String nombre;
    private String correo;
    private String telefono;
    private Tutor tutorEnConstruccion;

    /**
     * Metodo que define los datos basicos del tutor antes de iniciar su construccion
     * @param id identificador unico del tutor
     * @param nombre nombre del tutor
     * @param correo correo electronico de contacto del tutor
     * @param telefono telefono de contacto del tutor
     * @return el propio builder, permitiendo encadenar los demas metodos de construccion
     */
    public TutorBuilder conDatosBasicos(int id, String nombre, String correo, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        tutorEnConstruccion = new Tutor(id, nombre, correo, telefono);
        return this;
    }

    /**
     * Metodo que agrega una materia que el tutor impartira
     * @param nombreMateria nombre de la materia a agregar
     * @param tarifa tarifa por clase de la materia
     * @param cupoMaximo cantidad maxima de estudiantes permitidos en la materia
     * @return el propio builder, permitiendo encadenar los demas metodos de construccion
     */
    public TutorBuilder agregarMateria(String nombreMateria, int tarifa, int cupoMaximo) {
        asegurarTutorCreado();
        tutorEnConstruccion.agregarMateria(new MateriaTutor(nombreMateria, tarifa, cupoMaximo));
        return this;
    }

    /**
     * Metodo que agrega un bloque de disponibilidad horaria del tutor
     * @param dia dia en el que el tutor estara disponible
     * @param horaInicio hora de inicio del bloque de disponibilidad
     * @param horaFin hora de termino del bloque de disponibilidad
     * @return el propio builder, permitiendo encadenar los demas metodos de construccion
     */
    public TutorBuilder agregarDisponibilidad(LocalDate dia, LocalTime horaInicio, LocalTime horaFin) {
        asegurarTutorCreado();
        tutorEnConstruccion.agregarDisponibilidad(new DisponibilidadTutor(dia, horaInicio, horaFin));
        return this;
    }

    /**
     * Metodo interno que verifica si el tutor en construccion ya fue inicializado
     * Lanza una excepcion si se intenta agregar materias, disponibilidades o finalizar
     * la construccion sin haber llamado primero a conDatosBasicos()
     */
    private void asegurarTutorCreado() {
        if (tutorEnConstruccion == null) {
            throw new ConstruccionTutorInvalida(
                    "Debe llamar a conDatosBasicos() antes de agregar materias o disponibilidades."
            );
        }
    }

    /**
     * Metodo que finaliza la construccion y entrega el tutor ya armado
     * Libera al builder para que pueda reutilizarse en la construccion de un tutor distinto
     * @return el tutor construido con los datos, materias y disponibilidades acumuladas
     */
    public Tutor build() {
        asegurarTutorCreado();
        Tutor tutorConstruido = tutorEnConstruccion;
        tutorEnConstruccion = null;
        return tutorConstruido;
    }
}