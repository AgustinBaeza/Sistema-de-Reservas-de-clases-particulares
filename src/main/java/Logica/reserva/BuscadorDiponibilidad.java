package Logica.reserva;

import Logica.estudiante.Estudiante;
import Logica.tutor.MateriaTutor;
import Logica.tutor.Tutor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class BuscadorDiponibilidad {



    public boolean tutorDisponible(Tutor tutor, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin){
        return false;
    }

    public boolean hayConflictoHorario(Tutor tutor, ArrayList reservas, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin){
        return false;
    }

}
