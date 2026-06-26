package Logica.reserva;

import Logica.estudiante.Estudiante;
import Logica.tutor.DisponibilidadTutor;
import Logica.tutor.MateriaTutor;
import Logica.tutor.Tutor;
import Logica.tutor.TutorBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase dedicada a tests unitarios respecto al buscador de disponibilidad
 * Se prueba para tutorDisponible:
 * - Horario completamente dentro del bloque retorna true
 * - Horario igual a los limites exactos del bloque retorna true
 * - Tutor sin bloques de disponibilidad retorna false
 * - Fecha distinta al bloque retorna false
 * - Hora de inicio antes del bloque retorna false
 * - Hora de termino despues del bloque retorna false
 * - Multiples bloques donde uno coincide retorna true
 *
 * Se prueba para hayConflictoHorario:
 * - Sin reservas no hay conflicto
 * - Reserva de otro tutor no genera conflicto
 * - Reserva de otro dia no genera conflicto
 * - Reserva cancelada del mismo tutor en el mismo horario no genera conflicto
 * - Nueva reserva que empieza cuando termina la existente no genera conflicto
 * - Nueva reserva que termina cuando empieza la existente no genera conflicto
 * - Superposicion parcial al inicio genera conflicto
 * - Superposicion parcial al final genera conflicto
 * - Nueva reserva completamente dentro de la existente genera conflicto
 */
class BuscadorDisponibilidadTest {

    private BuscadorDisponibilidad buscador;
    private Tutor tutor;
    private Estudiante estudiante;
    private MateriaTutor materia;
    private LocalDate fecha;

    @BeforeEach
    void setUp() {
        buscador = new BuscadorDisponibilidad();

        tutor = new TutorBuilder()
                .conDatosBasicos(1, "Jonathan", "jbriones@udec.cl", "123456789")
                .agregarMateria("Calculo III", 15000, 3)
                .agregarDisponibilidad(
                        LocalDate.of(2026, 6, 19),
                        LocalTime.of(14, 0),
                        LocalTime.of(18, 0)
                )
                .build();

        materia = tutor.getMaterias().get(0);
        estudiante = new Estudiante(201, "Javier", "jvidal@udec.cl", "99999999");
        fecha = LocalDate.of(2026, 6, 19);
    }


    // tests dedicados a tutorDisponible

    @Test
    void solicitarReservaEnHorarioDisponible() {
        assertTrue(buscador.tutorDisponible(tutor, fecha,
                LocalTime.of(15, 0), LocalTime.of(16, 0)));
    }

    @Test
    void solicitarReservaEnHorarioDisponibleLimite() {
        assertTrue(buscador.tutorDisponible(tutor, fecha,
                LocalTime.of(14, 0), LocalTime.of(18, 0)));
    }

    @Test
    void solicitarReservaTutorSinDisponibilidades() {
        Tutor tutorSinDisponibilidad = new TutorBuilder()
                .conDatosBasicos(2, "Pepe", "pfuentes@udec.cl", "99999999")
                .build();

        assertFalse(buscador.tutorDisponible(tutorSinDisponibilidad, fecha,
                LocalTime.of(14, 0), LocalTime.of(15, 0)));
    }

    @Test
    void solicitarReservaTutorMismoHorarioDisponibleDistintaFecha() {
        LocalDate otraFecha = LocalDate.of(2026, 6, 20);

        assertFalse(buscador.tutorDisponible(tutor, otraFecha,
                LocalTime.of(14, 0), LocalTime.of(15, 0)));
    }

    @Test
    void solicitarReservaHoraAntesTutorDisponible() {
        assertFalse(buscador.tutorDisponible(tutor, fecha,
                LocalTime.of(13, 30), LocalTime.of(15, 0)));
    }

    @Test
    void solicitarReservaHoraDespuesTutorDisponible() {
        assertFalse(buscador.tutorDisponible(tutor, fecha,
                LocalTime.of(15, 0), LocalTime.of(18, 30)));
    }

    @Test
    void solicitarReservaCorrectaTutorMultiplesBloquesDisponibles() {
        tutor.agregarDisponibilidad(new DisponibilidadTutor(
                LocalDate.of(2026, 6, 20),
                LocalTime.of(9, 0),
                LocalTime.of(12, 0)));

        assertTrue(buscador.tutorDisponible(tutor,
                LocalDate.of(2026, 6, 20),
                LocalTime.of(10, 0), LocalTime.of(11, 0)));
    }


    // tests dedicados a hayConflictoHorario

    @Test
    void solicitarReservaSinReservasExistentes() {
        ArrayList<Reserva> reservas = new ArrayList<>();

        assertFalse(buscador.hayConflictoHorario(tutor, reservas, fecha,
                LocalTime.of(14, 0), LocalTime.of(15, 0)));
    }

    @Test
    void solicitarReservaHorarioOcupadoPorOtroTutor() {
        Tutor otroTutor = new TutorBuilder()
                .conDatosBasicos(2, "Jose", "jfuenteso@udec.cl", "99999999")
                .agregarDisponibilidad(fecha, LocalTime.of(14, 0), LocalTime.of(18, 0))
                .build();

        ArrayList<Reserva> reservas = new ArrayList<>();
        reservas.add(new Reserva(otroTutor, estudiante, materia, fecha,
                LocalTime.of(14, 0), LocalTime.of(15, 0)));

        assertFalse(buscador.hayConflictoHorario(tutor, reservas, fecha,
                LocalTime.of(14, 0), LocalTime.of(15, 0)));
    }

    @Test
    void solicitarReservaMismoHorarioOcupadoDistintaFecha() {
        LocalDate otraFecha = LocalDate.of(2026, 6, 20);
        ArrayList<Reserva> reservas = new ArrayList<>();
        reservas.add(new Reserva(tutor, estudiante, materia, otraFecha,
                LocalTime.of(14, 0), LocalTime.of(15, 0)));

        assertFalse(buscador.hayConflictoHorario(tutor, reservas, fecha,
                LocalTime.of(14, 0), LocalTime.of(15, 0)));
    }

    @Test
    void solicitarReservaHorarioDeOtraReservaCancelada() {
        ArrayList<Reserva> reservas = new ArrayList<>();
        Reserva reservaCancelada = new Reserva(tutor, estudiante, materia, fecha,
                LocalTime.of(14, 0), LocalTime.of(15, 0));
        reservaCancelada.cancelarReserva();
        reservas.add(reservaCancelada);

        assertFalse(buscador.hayConflictoHorario(tutor, reservas, fecha,
                LocalTime.of(14, 0), LocalTime.of(15, 0)));
    }

    @Test
    void solicitarReservaJustoCuandoTerminaReservaExistente() {
        ArrayList<Reserva> reservas = new ArrayList<>();
        reservas.add(new Reserva(tutor, estudiante, materia, fecha,
                LocalTime.of(14, 0), LocalTime.of(15, 0)));

        assertFalse(buscador.hayConflictoHorario(tutor, reservas, fecha,
                LocalTime.of(15, 0), LocalTime.of(16, 0)));
    }

    @Test
    void solicitarReservaJustoCuandoEmpiezaReservaExistente() {
        ArrayList<Reserva> reservas = new ArrayList<>();
        reservas.add(new Reserva(tutor, estudiante, materia, fecha,
                LocalTime.of(15, 0), LocalTime.of(16, 0)));

        assertFalse(buscador.hayConflictoHorario(tutor, reservas, fecha,
                LocalTime.of(14, 0), LocalTime.of(15, 0)));
    }

    @Test
    void solicitarReservaSuperponeHorarioInicio() {
        ArrayList<Reserva> reservas = new ArrayList<>();
        reservas.add(new Reserva(tutor, estudiante, materia, fecha,
                LocalTime.of(14, 0), LocalTime.of(15, 0)));

        assertTrue(buscador.hayConflictoHorario(tutor, reservas, fecha,
                LocalTime.of(14, 30), LocalTime.of(15, 30)));
    }

    @Test
    void solicitarReservaSuperponeHorarioTermino() {
        ArrayList<Reserva> reservas = new ArrayList<>();
        reservas.add(new Reserva(tutor, estudiante, materia, fecha,
                LocalTime.of(14, 0), LocalTime.of(15, 0)));

        assertTrue(buscador.hayConflictoHorario(tutor, reservas, fecha,
                LocalTime.of(13, 30), LocalTime.of(14, 30)));
    }

    @Test
    void solicitarReservaCompletamenteDentroDeReservaExistente() {
        ArrayList<Reserva> reservas = new ArrayList<>();
        reservas.add(new Reserva(tutor, estudiante, materia, fecha,
                LocalTime.of(14, 0), LocalTime.of(16, 0)));

        assertTrue(buscador.hayConflictoHorario(tutor, reservas, fecha,
                LocalTime.of(14, 30), LocalTime.of(15, 30)));
    }
}