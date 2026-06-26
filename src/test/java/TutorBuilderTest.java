package Logica.tutor;

import Logica.excepciones.ConstruccionTutorInvalida;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase dedicada a tests unitarios respecto al builder de tutores
 * Se prueba:
 * - Construccion de un tutor con solo sus datos basicos
 * - Construccion de un tutor con una materia agregada
 * - Construccion de un tutor con varias materias y disponibilidades
 * - Reutilizacion del builder para construir dos tutores distintos
 * - Agregar una materia sin haber llamado antes a conDatosBasicos() lanza excepcion
 * - Agregar una disponibilidad sin haber llamado antes a conDatosBasicos() lanza excepcion
 * - Finalizar la construccion sin haber llamado antes a conDatosBasicos() lanza excepcion
 */
class TutorBuilderTest {

    @Test
    void construyeTutorConDatosBasicos() {
        Tutor tutor = new TutorBuilder()
                .conDatosBasicos(101, "Jonathan", "jbriones@udec.cl", "123456789")
                .build();

        assertEquals(101, tutor.getId());
        assertEquals("Jonathan", tutor.getNombre());
        assertEquals("jbriones@udec.cl", tutor.getCorreo());
        assertTrue(tutor.getMaterias().isEmpty());
        assertTrue(tutor.getDisponibilidades().isEmpty());
    }

    @Test
    void construyeTutorUnaMateria() {
        Tutor tutor = new TutorBuilder()
                .conDatosBasicos(101, "Jonathan", "jbriones@udec.cl", "123456789")
                .agregarMateria("Calculo III", 15000, 3)
                .build();

        assertEquals(1, tutor.getMaterias().size());
        assertEquals("Calculo III", tutor.getMaterias().get(0).getNombreMateria());
        assertEquals(15000, tutor.getMaterias().get(0).getTarifa());
    }

    @Test
    void construyeTutorMultiplesMateriasYDisponibilidades() {
        Tutor tutor = new TutorBuilder()
                .conDatosBasicos(101, "Jonathan", "jbriones@udec.cl", "123456789")
                .agregarMateria("Calculo III", 15000, 3)
                .agregarMateria("Algebra I", 12000, 5)
                .agregarDisponibilidad(LocalDate.of(2026, 6, 19), LocalTime.of(14, 0), LocalTime.of(18, 0))
                .agregarDisponibilidad(LocalDate.of(2026, 6, 20), LocalTime.of(9, 0), LocalTime.of(12, 0))
                .build();

        assertEquals(2, tutor.getMaterias().size());
        assertEquals(2, tutor.getDisponibilidades().size());
    }

    @Test
    void builderReutilizadoConstruyeTutoresDistintos() {
        TutorBuilder builder = new TutorBuilder();

        Tutor primerTutor = builder
                .conDatosBasicos(101, "Jonathan", "jbriones@udec.cl", "123456789")
                .build();

        Tutor segundoTutor = builder
                .conDatosBasicos(102, "Jose", "jfuentes@udec.cl", "987654321")
                .build();

        assertNotSame(primerTutor, segundoTutor);
        assertEquals(101, primerTutor.getId());
        assertEquals(102, segundoTutor.getId());
    }

    @Test
    void agregarMateriaSinDatosBasicosLanzaExcepcion() {
        TutorBuilder builder = new TutorBuilder();

        assertThrows(ConstruccionTutorInvalida.class, () ->
                builder.agregarMateria("Calculo III", 15000, 3));
    }

    @Test
    void agregarDisponibilidadSinDatosBasicosLanzaExcepcion() {
        TutorBuilder builder = new TutorBuilder();

        assertThrows(ConstruccionTutorInvalida.class, () ->
                builder.agregarDisponibilidad(LocalDate.of(2026, 6, 24), LocalTime.of(14, 0), LocalTime.of(18, 0)));
    }

    @Test
    void buildSinDatosBasicosLanzaExcepcion() {
        TutorBuilder builder = new TutorBuilder();

        assertThrows(ConstruccionTutorInvalida.class, builder::build);
    }
}