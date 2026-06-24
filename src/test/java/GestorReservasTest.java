
import Logica.estudiante.*;
import Logica.excepciones.*;
import Logica.reserva.*;
import Logica.tutor.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class GestorReservasTest {

    private GestorReservas gestor;
    private Tutor tutor;
    private Estudiante estudiante;
    private MateriaTutor materia;
    private LocalDate fecha;

    @BeforeEach
    void setUp() {
        tutor = new Tutor(102,"Pedro", "pedro@gmail.com", "123456789");
        materia = new MateriaTutor("CalculoIII", 15000, 3);
        tutor.agregarMateria(materia);
        tutor.agregarDisponibilidad(
                new DisponibilidadTutor(LocalDate.of(2026, 6, 19),
                    LocalTime.of(14, 0), LocalTime.of(18, 0)) );
        estudiante = new Estudiante(201,"Juan", "juan@gmail.com", "987654321");
        fecha = LocalDate.of(2026, 6, 19);
    }

}