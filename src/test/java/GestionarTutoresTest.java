import Logica.tutor.GestionarTutores;
import Logica.tutor.Tutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase dedicada a tests unitarios respecto a la gestion de tutores
 * Se prueba:
 * - Creacion de perfiles de tutor
 * - Edicion de datos principales del tutor
 * - Busqueda de tutores por ID
 * - Definicion de materias del tutor
 * - Definicion de disponibilidad horaria del tutor
 * - Casos invalidos, como tutores inexistentes o materias duplicadas
 */
class GestionarTutoresTest {

    private GestionarTutores gestionarTutores;

    @BeforeEach
    void setUp() {
        gestionarTutores = new GestionarTutores();
    }

    @Test
    void crearPerfilTutorAgregaTutorCorrectamente() {
        gestionarTutores.crearPerfilTutor(
                "Pedro",
                "pedro@gmail.com",
                "123456789"
        );

        Tutor tutor = gestionarTutores.buscarTutorPorId(1);

        assertEquals(1, gestionarTutores.getTutores().size());
        assertNotNull(tutor);
        assertEquals(1, tutor.getId());
        assertEquals("Pedro", tutor.getNombre());
        assertEquals("pedro@gmail.com", tutor.getCorreo());
        assertEquals("123456789", tutor.getTelefono());
    }

    @Test
    void crearDosPerfilesTutorAsignaIdsDistintos() {
        gestionarTutores.crearPerfilTutor(
                "Pedro",
                "pedro@gmail.com",
                "123456789"
        );

        gestionarTutores.crearPerfilTutor(
                "Maria",
                "maria@gmail.com",
                "987654321"
        );

        Tutor tutor1 = gestionarTutores.buscarTutorPorId(1);
        Tutor tutor2 = gestionarTutores.buscarTutorPorId(2);

        assertEquals(2, gestionarTutores.getTutores().size());
        assertNotNull(tutor1);
        assertNotNull(tutor2);
        assertNotEquals(tutor1.getId(), tutor2.getId());
        assertEquals("Pedro", tutor1.getNombre());
        assertEquals("Maria", tutor2.getNombre());
    }

    @Test
    void editarPerfilTutorExistenteRetornaTrueYActualizaDatos() {
        gestionarTutores.crearPerfilTutor(
                "Pedro",
                "pedro@gmail.com",
                "123456789"
        );

        boolean resultado = gestionarTutores.editarPerfilTutor(
                1,
                "Pedro Actualizado",
                "pedroactualizado@gmail.com",
                "111111111"
        );

        Tutor tutor = gestionarTutores.buscarTutorPorId(1);

        assertTrue(resultado);
        assertNotNull(tutor);
        assertEquals("Pedro Actualizado", tutor.getNombre());
        assertEquals("pedroactualizado@gmail.com", tutor.getCorreo());
        assertEquals("111111111", tutor.getTelefono());
    }

    @Test
    void editarPerfilTutorInexistenteRetornaFalse() {
        boolean resultado = gestionarTutores.editarPerfilTutor(
                999,
                "Tutor Inexistente",
                "correo@gmail.com",
                "000000000"
        );

        assertFalse(resultado);
    }

    @Test
    void buscarTutorPorIdExistenteRetornaTutor() {
        gestionarTutores.crearPerfilTutor(
                "Pedro",
                "pedro@gmail.com",
                "123456789"
        );

        Tutor tutor = gestionarTutores.buscarTutorPorId(1);

        assertNotNull(tutor);
        assertEquals("Pedro", tutor.getNombre());
        assertEquals("pedro@gmail.com", tutor.getCorreo());
    }

    @Test
    void buscarTutorPorIdInexistenteRetornaNull() {
        Tutor tutor = gestionarTutores.buscarTutorPorId(999);

        assertNull(tutor);
    }

    @Test
    void definirMateriaTutorAgregaMateriaCorrectamente() {
        gestionarTutores.crearPerfilTutor(
                "Pedro",
                "pedro@gmail.com",
                "123456789"
        );

        boolean resultado = gestionarTutores.definirMateriaTutor(
                1,
                "Calculo II",
                15000,
                3
        );

        Tutor tutor = gestionarTutores.buscarTutorPorId(1);

        assertTrue(resultado);
        assertNotNull(tutor);
        assertEquals(1, tutor.getMaterias().size());
        assertEquals("Calculo II", tutor.getMaterias().get(0).getNombreMateria());
        assertEquals(15000, tutor.getMaterias().get(0).getTarifa());
        assertEquals(3, tutor.getMaterias().get(0).getCupoMaximo());
    }

    @Test
    void definirMateriaTutorDuplicadaRetornaFalse() {
        gestionarTutores.crearPerfilTutor(
                "Pedro",
                "pedro@gmail.com",
                "123456789"
        );

        boolean primeraMateria = gestionarTutores.definirMateriaTutor(
                1,
                "Calculo II",
                15000,
                3
        );

        boolean materiaDuplicada = gestionarTutores.definirMateriaTutor(
                1,
                "Calculo II",
                18000,
                5
        );

        Tutor tutor = gestionarTutores.buscarTutorPorId(1);

        assertTrue(primeraMateria);
        assertFalse(materiaDuplicada);
        assertNotNull(tutor);
        assertEquals(1, tutor.getMaterias().size());
        assertEquals(15000, tutor.getMaterias().get(0).getTarifa());
        assertEquals(3, tutor.getMaterias().get(0).getCupoMaximo());
    }

    @Test
    void definirMateriaTutorInexistenteRetornaFalse() {
        boolean resultado = gestionarTutores.definirMateriaTutor(
                999,
                "Calculo II",
                15000,
                3
        );

        assertFalse(resultado);
    }

    @Test
    void definirDisponibilidadTutorAgregaBloqueCorrectamente() {
        gestionarTutores.crearPerfilTutor(
                "Pedro",
                "pedro@gmail.com",
                "123456789"
        );

        LocalDate dia = LocalDate.of(2026, 6, 25);
        LocalTime horaInicio = LocalTime.of(10, 0);
        LocalTime horaFin = LocalTime.of(12, 0);

        boolean resultado = gestionarTutores.definirDisponibilidadTutor(
                1,
                dia,
                horaInicio,
                horaFin
        );

        Tutor tutor = gestionarTutores.buscarTutorPorId(1);

        assertTrue(resultado);
        assertNotNull(tutor);
        assertEquals(1, tutor.getDisponibilidades().size());
        assertEquals(dia, tutor.getDisponibilidades().get(0).getDia());
        assertEquals(horaInicio, tutor.getDisponibilidades().get(0).getHoraInicio());
        assertEquals(horaFin, tutor.getDisponibilidades().get(0).getHoraFin());
    }

    @Test
    void definirDisponibilidadTutorInexistenteRetornaFalse() {
        boolean resultado = gestionarTutores.definirDisponibilidadTutor(
                999,
                LocalDate.of(2026, 6, 25),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0)
        );

        assertFalse(resultado);
    }
}
