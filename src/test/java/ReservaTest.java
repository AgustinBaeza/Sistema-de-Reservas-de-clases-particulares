package Logica.reserva;

import Logica.estudiante.Estudiante;
import Logica.excepciones.AccionEstadoInvalidaException;
import Logica.tutor.MateriaTutor;
import Logica.tutor.Tutor;
import Logica.tutor.TutorBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase dedicada a tests unitarios respecto a las reservas
 * Se prueba:
 * - Inicializacion correcta de reserva en estado pendiente
 * - Confirmar reserva de estado pendiente cambia a estado confirmada
 * - Cancelar reserva de estado pendiente cambia a estado cancelada
 * - validarModificacion no lanza excepcion desde una reserva en estado pendiente
 * - Confirmar reserva de estado confirmada lanza excepcion
 * - Cancelar reserva de estado confirmada cambia a estado cancelada
 * - validarModificacion no lanza excepcion desde una reserva en estado confirmada
 * - Confirmar reserva de estado cancelada lanza excepcion
 * - Cancelar reserva de estado cancelada lanza excepcion
 * - validarModificacion lanza excepcion desde una reserva en estado confirmada
 */
class ReservaTest {

    private Reserva reserva;

    @BeforeEach
    void setUp() {
        Tutor tutor = new TutorBuilder()
                .conDatosBasicos(101, "Jonathan", "jbriones@udec.cl", "123456789")
                .build();
        Estudiante estudiante = new Estudiante(201, "Javier", "jvidal@udec.cl", "987654321");
        MateriaTutor materia = new MateriaTutor("Calculo III", 15000, 3);

        reserva = new Reserva(tutor, estudiante, materia, LocalDate.of(2026, 6, 15),
                LocalTime.of(14, 0), LocalTime.of(15, 0));
    }

    @Test
    void reservaInicialEstadoPendiente() {
        assertEquals("PENDIENTE", reserva.getEstadoReserva());
    }

    @Test
    void confirmarPendienteCambiaConfirmada() {
        reserva.confirmarReserva();
        assertEquals("CONFIRMADA", reserva.getEstadoReserva());
    }

    @Test
    void cancelarPendienteCambiaCancelada() {
        reserva.cancelarReserva();
        assertEquals("CANCELADA", reserva.getEstadoReserva());
    }

    @Test
    void validarModificacionPendienteLanzaExcepcion() {
        assertDoesNotThrow(() -> reserva.validarModificacion());
    }

    @Test
    void confirmarConfirmadaLanzaExcepcion() {
        reserva.confirmarReserva();
        assertThrows(AccionEstadoInvalidaException.class, () -> reserva.confirmarReserva());
    }

    @Test
    void cancelarConfirmadaCambiaCancelada() {
        reserva.confirmarReserva();
        reserva.cancelarReserva();
        assertEquals("CANCELADA", reserva.getEstadoReserva());
    }

    @Test
    void validarModificacionConfirmadaNoLanzaExcepcion() {
        reserva.confirmarReserva();
        assertDoesNotThrow(() -> reserva.validarModificacion());
    }

    @Test
    void confirmarCanceladaLanzaExcepcion() {
        reserva.cancelarReserva();
        assertThrows(AccionEstadoInvalidaException.class, () -> reserva.confirmarReserva());
    }

    @Test
    void cancelarCanceladaLanzaExcepcion() {
        reserva.cancelarReserva();
        assertThrows(AccionEstadoInvalidaException.class, () -> reserva.cancelarReserva());
    }

    @Test
    void validarModificacionCanceladaLanzaExcepcion() {
        reserva.cancelarReserva();
        assertThrows(AccionEstadoInvalidaException.class, () -> reserva.validarModificacion());
    }
}