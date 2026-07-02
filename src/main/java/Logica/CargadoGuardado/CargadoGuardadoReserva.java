package Logica.CargadoGuardado;

import Logica.estudiante.Estudiante;
import Logica.estudiante.GestionarEstudiantes;
import Logica.reserva.Reserva;
import Logica.tutor.GestionarTutores;
import Logica.tutor.MateriaTutor;
import Logica.tutor.Tutor;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Clase que guarda y carga las reservas en un archivo txt.
 * Formato de cada linea: idTutor;idEstudiante;nombreMateria;fecha;horaInicio;horaFin;estado
 * Las reservas canceladas tambien se guardan para mantener el historial completo.
 */
public class CargadoGuardadoReserva {

    private static final String ARCHIVO = "datosReservas.txt";

    /**
     * Guarda todas las reservas en el archivo.
     * @param reservas lista de reservas a guardar
     * @throws IOException si ocurre un error al escribir el archivo
     */
    public static void guardarReservas(ArrayList<Reserva> reservas) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO));

        for (Reserva r : reservas) {
            bw.write(
                    r.getTutor().getId() + ";" +
                            r.getEstudiante().getId() + ";" +
                            r.getMateriaTutor().getNombreMateria() + ";" +
                            r.getFecha() + ";" +
                            r.getHoraInicio() + ";" +
                            r.getHoraFin() + ";" +
                            r.getEstadoReserva()
            );
            bw.newLine();
        }

        bw.close();
    }

    /**
     * Carga las reservas desde el archivo y las agrega a la lista de reservas del sistema.
     * Si el archivo no existe, no hace nada.
     * Si un tutor o estudiante referenciado no existe, esa reserva se salta.
     * @param listaReservas lista donde se agregaran las reservas cargadas
     * @param gestionarTutores gestor de tutores para buscar por id
     * @param gestionarEstudiantes gestor de estudiantes para buscar por id
     * @throws IOException si ocurre un error al leer el archivo
     */
    public static void cargarReservas(ArrayList<Reserva> listaReservas,
                                      GestionarTutores gestionarTutores,
                                      GestionarEstudiantes gestionarEstudiantes) throws IOException {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return;

        BufferedReader br = new BufferedReader(new FileReader(archivo));
        String linea;

        while ((linea = br.readLine()) != null) {
            if (linea.isBlank()) continue;
            String[] datos = linea.split(";");

            Tutor tutor = gestionarTutores.buscarTutorPorId(Integer.parseInt(datos[0]));
            Estudiante estudiante = gestionarEstudiantes.buscarEstudiantePorID(Integer.parseInt(datos[1]));

            if (tutor == null || estudiante == null) {
                System.out.println("Advertencia: tutor o estudiante no encontrado en: " + linea);
                continue;
            }

            MateriaTutor materia = tutor.buscarMateria(datos[2]);
            if (materia == null) {
                System.out.println("Advertencia: materia no encontrada para tutor " + tutor.getNombre() + " en: " + datos[2]);
                continue;
            }

            Reserva reserva = new Reserva(
                    tutor, estudiante, materia,
                    LocalDate.parse(datos[3]),
                    LocalTime.parse(datos[4]),
                    LocalTime.parse(datos[5]));

            String estado = datos[6];
            switch (estado) {
                case "CONFIRMADA" -> reserva.confirmarReserva();
                case "CANCELADA"  -> reserva.cancelarReserva();
            }

            listaReservas.add(reserva);
        }

        br.close();
    }
}