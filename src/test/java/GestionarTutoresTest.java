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
 * - Creacion, edicion y busqueda de tutores
 * - Definicion y edicion de materias, tarifas y cupos
 * - Definicion y edicion de disponibilidad horaria
 * - Casos invalidos principales
 */
class GestionarTutoresTest {

    private GestionarTutores gestionarTutores;

    @BeforeEach
    void setUp() {
        gestionarTutores = new GestionarTutores();
    }

    private Tutor crearTutorAlan() {
        return gestionarTutores.crearPerfilTutor("Alan", "alan@gmail.com", "111111111");
    }

    private void agregarMateriaCalculo3(int idTutor) {
        gestionarTutores.definirMateriaTutor( idTutor, "Calculo 3", 15000, 3);
    }

    private void agregarDisponibilidadBase(int idTutor) {
        gestionarTutores.definirDisponibilidadTutor(idTutor, LocalDate.of(2026, 6, 25), LocalTime.of(10, 0), LocalTime.of(12, 0));
    }

    @Test
    void crearPerfilTutorAgregaTutorCorrectamente() {
        Tutor tutor = crearTutorAlan();

        assertEquals(1, gestionarTutores.getTutores().size());
        assertEquals(1, tutor.getId());
        assertEquals("Alan", tutor.getNombre());
        assertEquals("alan@gmail.com", tutor.getCorreo());
        assertEquals("111111111", tutor.getTelefono());
    }

    @Test
    void crearDosTutoresAsignaIdsDistintos() {
        Tutor tutor1 = crearTutorAlan();

        Tutor tutor2 = gestionarTutores.crearPerfilTutor("Alex", "alex@gmail.com", "222222222");

        assertEquals(2, gestionarTutores.getTutores().size());
        assertNotEquals(tutor1.getId(), tutor2.getId());
        assertEquals(1, tutor1.getId());
        assertEquals(2, tutor2.getId());
    }

    @Test
    void editarPerfilTutorExistenteActualizaDatos() {
        Tutor tutor = crearTutorAlan();

        boolean resultado = gestionarTutores.editarPerfilTutor(tutor.getId(), "Valentina", "valentina@gmail.com", "333333333");

        assertTrue(resultado);
        assertEquals("Valentina", tutor.getNombre());
        assertEquals("valentina@gmail.com", tutor.getCorreo());
        assertEquals("333333333", tutor.getTelefono());
    }

    @Test
    void buscarTutorPorIdRetornaTutorONull() {
        Tutor tutor = crearTutorAlan();

        assertNotNull(gestionarTutores.buscarTutorPorId(tutor.getId()));
        assertEquals("Alan", gestionarTutores.buscarTutorPorId(tutor.getId()).getNombre());
        assertNull(gestionarTutores.buscarTutorPorId(999));
    }

    @Test
    void definirMateriaTutorAgregaMateriaCorrectamente() {
        Tutor tutor = crearTutorAlan();

        boolean resultado = gestionarTutores.definirMateriaTutor(tutor.getId(), "Calculo 3", 15000, 3);

        assertTrue(resultado);
        assertEquals(1, tutor.getMaterias().size());
        assertEquals("Calculo 3", tutor.getMaterias().get(0).getNombreMateria());
        assertEquals(15000, tutor.getMaterias().get(0).getTarifa());
        assertEquals(3, tutor.getMaterias().get(0).getCupoMaximo());
    }

    @Test
    void definirMateriaTutorConDatosInvalidosRetornaFalse() {
        Tutor tutor = crearTutorAlan();
        agregarMateriaCalculo3(tutor.getId());

        assertFalse(gestionarTutores.definirMateriaTutor(tutor.getId(), "Calculo 3", 18000, 5));
        assertFalse(gestionarTutores.definirMateriaTutor(tutor.getId(), "", 15000, 3));
        assertFalse(gestionarTutores.definirMateriaTutor(tutor.getId(), "Logica", -1000, 3));
        assertFalse(gestionarTutores.definirMateriaTutor(999, "Logica", 15000, 3));
    }

    @Test
    void definirDisponibilidadTutorAgregaBloqueCorrectamente() {
        Tutor tutor = crearTutorAlan();
        LocalDate dia = LocalDate.of(2026, 6, 25);
        LocalTime inicio = LocalTime.of(10, 0);
        LocalTime fin = LocalTime.of(12, 0);

        boolean resultado = gestionarTutores.definirDisponibilidadTutor(tutor.getId(), dia, inicio, fin);

        assertTrue(resultado);
        assertEquals(1, tutor.getDisponibilidades().size());
        assertEquals(dia, tutor.getDisponibilidades().get(0).getDia());
        assertEquals(inicio, tutor.getDisponibilidades().get(0).getHoraInicio());
        assertEquals(fin, tutor.getDisponibilidades().get(0).getHoraFin());
    }

    @Test
    void definirDisponibilidadTutorConDatosInvalidosRetornaFalse() {
        Tutor tutor = crearTutorAlan();
        assertFalse(gestionarTutores.definirDisponibilidadTutor(999, LocalDate.of(2026, 6, 25), LocalTime.of(10, 0), LocalTime.of(12, 0)));
        assertFalse(gestionarTutores.definirDisponibilidadTutor(tutor.getId(), LocalDate.of(2026, 6, 25), LocalTime.of(12, 0), LocalTime.of(10, 0)));
    }

    @Test
    void definirTarifaYCupoActualizaMateriaCorrectamente() {
        Tutor tutor = crearTutorAlan();
        agregarMateriaCalculo3(tutor.getId());

        boolean tarifaActualizada = gestionarTutores.definirTarifaTutor(tutor.getId(), "Calculo 3", 18000);
        boolean cupoActualizado = gestionarTutores.definirCupoMaximoPorMateria(tutor.getId(), "Calculo 3", 6);

        assertTrue(tarifaActualizada);
        assertTrue(cupoActualizado);
        assertEquals(18000, tutor.buscarMateria("Calculo 3").getTarifa());
        assertEquals(6, tutor.buscarMateria("Calculo 3").getCupoMaximo());
    }

    @Test
    void editarMateriaTutorActualizaDatosCorrectamente() {
        Tutor tutor = crearTutorAlan();
        agregarMateriaCalculo3(tutor.getId());

        boolean resultado = gestionarTutores.editarMateriaTutor(tutor.getId(), "Calculo 3", "Desarrollo Orientado a Objetos", 20000, 4);

        assertTrue(resultado);
        assertNull(tutor.buscarMateria("Calculo 3"));
        assertNotNull(tutor.buscarMateria("Desarrollo Orientado a Objetos"));
        assertEquals(20000, tutor.buscarMateria("Desarrollo Orientado a Objetos").getTarifa());
        assertEquals(4, tutor.buscarMateria("Desarrollo Orientado a Objetos").getCupoMaximo());
    }

    @Test
    void editarMateriaTutorConNombreDuplicadoRetornaFalse() {
        Tutor tutor = crearTutorAlan();

        gestionarTutores.definirMateriaTutor(tutor.getId(), "Calculo 3", 15000, 3);
        gestionarTutores.definirMateriaTutor(tutor.getId(), "Logica", 12000, 4);

        boolean resultado = gestionarTutores.editarMateriaTutor(tutor.getId(), "Calculo 3", "Logica", 18000, 5);

        assertFalse(resultado);
        assertNotNull(tutor.buscarMateria("Calculo 3"));
        assertNotNull(tutor.buscarMateria("Logica"));
    }

    @Test
    void editarDisponibilidadTutorActualizaBloqueCorrectamente() {
        Tutor tutor = crearTutorAlan();
        agregarDisponibilidadBase(tutor.getId());

        boolean resultado = gestionarTutores.editarDisponibilidadTutor(tutor.getId(), 0, LocalDate.of(2026, 6, 26), LocalTime.of(14, 0), LocalTime.of(16, 0));

        assertTrue(resultado);
        assertEquals(LocalDate.of(2026, 6, 26), tutor.getDisponibilidades().get(0).getDia());
        assertEquals(LocalTime.of(14, 0), tutor.getDisponibilidades().get(0).getHoraInicio());
        assertEquals(LocalTime.of(16, 0), tutor.getDisponibilidades().get(0).getHoraFin());
    }

    @Test
    void editarDisponibilidadTutorConIndiceInvalidoRetornaFalse() {
        Tutor tutor = crearTutorAlan();
        agregarDisponibilidadBase(tutor.getId());

        boolean resultado = gestionarTutores.editarDisponibilidadTutor(tutor.getId(), 5, LocalDate.of(2026, 6, 26), LocalTime.of(14, 0), LocalTime.of(16, 0));

        assertFalse(resultado);
    }

    @Test
    void operacionesConTutorInexistenteRetornanFalse() {
        assertFalse(gestionarTutores.editarPerfilTutor(999, "Guts", "guts@gmail.com", "444444444"));
        assertFalse(gestionarTutores.definirTarifaTutor(999, "Calculo 3", 18000));
        assertFalse(gestionarTutores.definirCupoMaximoPorMateria(999, "Calculo 3", 5));
        assertFalse(gestionarTutores.editarMateriaTutor(999, "Calculo 3", "Logica", 15000, 3));
        assertFalse(gestionarTutores.editarDisponibilidadTutor(999, 0, LocalDate.of(2026, 6, 26), LocalTime.of(14, 0), LocalTime.of(16, 0)));
    }
}
