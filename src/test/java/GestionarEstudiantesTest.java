import Logica.estudiante.Estudiante;
import Logica.estudiante.GestionarEstudiantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase dedicada a tests unitarios respecto a la gestion de estudiantes.
 * Se prueba:
 * - Creacion de perfiles de estudiante.
 * - Edicion de datos principales del estudiante.
 * - Busqueda de estudiantes por ID.
 * - Asignacion de identificadores distintos.
 * - Casos invalidos, como estudiantes inexistentes.
 */

class GestionarEstudiantesTest {

    private GestionarEstudiantes gestionarEstudiantes;

    @BeforeEach
    void setUp() {
        gestionarEstudiantes = new GestionarEstudiantes();
    }

    @Test
    void crearPerfilEstudianteAgregaEstudianteCorrectamente() {
        Estudiante estudiante = gestionarEstudiantes.crearPerfilEstudiante(
                "Alan",
                "alan@gmail.com",
                "987677734"
        );

        assertEquals(1, gestionarEstudiantes.getEstudiantes().size());
        assertEquals(500, estudiante.getId());
        assertEquals("Alan", estudiante.getNombre());
        assertEquals("alan@gmail.com", estudiante.getCorreo());
    }

    @Test
    void crearDosEstudiantesAsignaIdsDistintos() {
        Estudiante estudiante1 = gestionarEstudiantes.crearPerfilEstudiante(
                "Alan",
                "alan@gmail.com",
                "111111111"
        );

        Estudiante estudiante2 = gestionarEstudiantes.crearPerfilEstudiante(
                "Maria",
                "maria@gmail.com",
                "222222222"
        );

        assertNotEquals(estudiante1.getId(), estudiante2.getId());
        assertEquals(500, estudiante1.getId());
        assertEquals(501, estudiante2.getId());
    }

    @Test
    void editarPerfilEstudianteExistenteRetornaTrue() {
        Estudiante estudiante = gestionarEstudiantes.crearPerfilEstudiante(
                "Alan",
                "alan@gmail.com",
                "987654321"
        );

        boolean resultado = gestionarEstudiantes.editarPerfilEstudiante(
                estudiante.getId(),
                "Alan Actualizado",
                "nuevo@gmail.com",
                "123123123"
        );

        assertTrue(resultado);
        assertEquals("Alan Actualizado", estudiante.getNombre());
        assertEquals("nuevo@gmail.com", estudiante.getCorreo());
        assertEquals("123123123", estudiante.getTelefono());
    }

    @Test
    void editarPerfilEstudianteInexistenteRetornaFalse() {
        boolean resultado = gestionarEstudiantes.editarPerfilEstudiante(
                999,
                "Nombre",
                "correo@gmail.com",
                "000000000"
        );

        assertFalse(resultado);
    }

    @Test
    void buscarEstudiantePorIdExistenteRetornaEstudiante() {
        Estudiante estudiante = gestionarEstudiantes.crearPerfilEstudiante(
                "Alan",
                "alan@gmail.com",
                "987654321"
        );

        Estudiante encontrado = gestionarEstudiantes.buscarEstudiantePorID(estudiante.getId());

        assertNotNull(encontrado);
        assertEquals("Alan", encontrado.getNombre());
    }

    @Test
    void buscarEstudiantePorIdInexistenteRetornaNull() {
        Estudiante encontrado = gestionarEstudiantes.buscarEstudiantePorID(999);

        assertNull(encontrado);
    }
}