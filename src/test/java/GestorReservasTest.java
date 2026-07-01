
import Logica.estudiante.*;
import Logica.excepciones.*;
import Logica.reserva.*;
import Logica.tutor.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GestorReservasTest {

    private GestorReservas gestor;
    private Tutor tutor1;
    private Tutor tutor2;
    private Estudiante estudiante1;
    private Estudiante estudiante2;
    private Estudiante estudiante3;
    private MateriaTutor materia;
    private LocalDate fecha = LocalDate.of(2026, 6, 19);

    @BeforeEach
    void setUp() {

        tutor1 = new TutorBuilder()
                .conDatosBasicos(102, "Pedro", "pedro@gmail.com", "123456789")
                .agregarMateria("CalculoIII", 15000, 3)
                .agregarDisponibilidad(
                        fecha,
                        LocalTime.of(14, 0),
                        LocalTime.of(18, 0)
                )
                .build();

        tutor2 = new TutorBuilder()
                .conDatosBasicos(2, "Ana", "ana@gmail.cl", "987654321")
                .agregarMateria("Calculo III", 12000, 3)
                .agregarDisponibilidad( fecha,
                                        LocalTime.of(14, 0),
                                        LocalTime.of(18, 0))
                .build();
        materia = new MateriaTutor("Calculo III", 15000, 3);
        tutor1.agregarMateria(materia);
        tutor1.agregarDisponibilidad(
                new DisponibilidadTutor(fecha,
                    LocalTime.of(14, 0), LocalTime.of(18, 0)) );
        estudiante1 = new Estudiante(201,"Juan", "juan@gmail.com", "111111111");
        estudiante2 = new Estudiante(202,"Daniel", "daniel@gmail.com", "222222222");
        estudiante3 = new Estudiante(203,"Ernesto", "ernesto@gmail.com", "3333333333");


        gestor = new GestorReservas(new BuscadorDisponibilidad());
    }
    @Test
    void crearReservaValidaQuedaEnPendienteYSeAgregaALaLista()
            throws ConflictoHorarioException, CupoMaximoExcedidoException {
        Reserva r = gestor.crearReserva(tutor1, estudiante1, materia,
                fecha,
                LocalTime.of(15, 0),
                LocalTime.of(16, 0));

        assertNotNull(r);
        assertEquals("PENDIENTE", r.getEstadoReserva());
        assertEquals(1, gestor.getReservas().size());
    }

    @Test
    void crearReservaTutorSinDisponibilidadLanzaConflictoHorario() {
        assertThrows(ConflictoHorarioException.class, () ->
                gestor.crearReserva(tutor1, estudiante1, materia,
                        fecha,
                        LocalTime.of(20, 0),
                        LocalTime.of(21, 0)));
    }

    @Test
    void crearReservaEnHorarioDeCanceladaNoGeneraConflicto()
            throws ConflictoHorarioException, CupoMaximoExcedidoException {
        Reserva r = gestor.crearReserva(tutor1, estudiante1, materia,
                fecha, LocalTime.of(15, 0), LocalTime.of(16, 0));
        gestor.cancelarReserva(r);

        Reserva nueva = gestor.crearReserva(tutor1, estudiante1, materia,
                fecha, LocalTime.of(15, 0), LocalTime.of(16, 0));
        assertNotNull(nueva);
    }

    @Test
    void modificarReservaNoGeneraConflictoConsigoMisma()
            throws ConflictoHorarioException, CupoMaximoExcedidoException {
        Reserva r = gestor.crearReserva(tutor1, estudiante1, materia,
                fecha, LocalTime.of(15, 0), LocalTime.of(16, 0));

        // modificar al mismo tutor y mismo horario no debe chocar consigo misma
        assertDoesNotThrow(() ->
                gestor.modificarReserva(r, tutor1, materia, estudiante1,
                        fecha, LocalTime.of(15, 0), LocalTime.of(16, 0)));
    }

    @Test
    void modificarReservaReservaCanceladaLanzaAccionEstadoInvalida()
            throws ConflictoHorarioException, CupoMaximoExcedidoException {
        Reserva r = gestor.crearReserva(tutor1, estudiante1, materia,
                fecha, LocalTime.of(15, 0), LocalTime.of(16, 0));
        gestor.cancelarReserva(r);

        assertThrows(AccionEstadoInvalidaException.class, () ->
                gestor.modificarReserva(r, tutor1, materia, estudiante1,
                        fecha, LocalTime.of(16, 0), LocalTime.of(17, 0)));
    }

    @Test
    void buscarTutoresDisponiblesRetornaTutoresCompatibles() {
        ArrayList<Tutor> todos = new ArrayList<>();
        todos.add(tutor1);
        todos.add(tutor2);

        ArrayList<Tutor> resultado = gestor.buscarTutoresDisponibles(
                todos, "Calculo III", fecha, LocalTime.of(15, 0), LocalTime.of(16, 0));

        assertEquals(2, resultado.size());
    }

    @Test
    void buscarTutoresDisponibleExcluyeTutorSinDisponibilidad() {
        Tutor sinDisp = new TutorBuilder()
                .conDatosBasicos(3, "Luis", "luis@gmail.cl", "999")
                .agregarMateria("Calculo III", 10000, 3)
                .build();

        ArrayList<Tutor> todos = new ArrayList<>();
        todos.add(tutor1);
        todos.add(sinDisp);

        ArrayList<Tutor> resultado = gestor.buscarTutoresDisponibles(
                todos, "Calculo III", fecha, LocalTime.of(15, 0), LocalTime.of(16, 0));

        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(tutor1));
    }

    @Test
    void buscarTutoresDisponiblesExcluyeTutorConConflictoHorario()
            throws ConflictoHorarioException, CupoMaximoExcedidoException {
        gestor.crearReserva(tutor1, estudiante1, materia,
                fecha, LocalTime.of(15, 0), LocalTime.of(16, 0));

        ArrayList<Tutor> todos = new ArrayList<>();
        todos.add(tutor1);
        todos.add(tutor2);

        ArrayList<Tutor> resultado = gestor.buscarTutoresDisponibles(
                todos, "Calculo III", fecha, LocalTime.of(15, 0), LocalTime.of(16, 0));

        assertFalse(resultado.contains(tutor1));
        assertTrue(resultado.contains(tutor2));
    }


}