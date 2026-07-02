package Logica.CargadoGuardado;

import Logica.tutor.*;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Clase que guarda y carga los datos de los tutores en un archivo txt
 * Cada tutor se escribe con una linea TUTOR seguida de lineas MATERIA y DISPONIBILIDAD
 * que le pertenecen, hasta encontrar el siguiente TUTOR o fin de archivo.
 */
public class CargadoGuardadoTutor {

    private static final String ARCHIVO = "datosTutores.txt";

    /**
     * Guarda todos los tutores con sus materias y disponibilidades en el archivo.
     * @param tutores lista de tutores a guardar
     * @throws IOException si ocurre un error al escribir el archivo
     */
    public static void guardarTutores(ArrayList<Tutor> tutores) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO));

        for (Tutor tutor : tutores) {
            bw.write("TUTOR;" + tutor.getId() + ";" + tutor.getNombre() + ";"
                    + tutor.getCorreo() + ";" + tutor.getTelefono());
            bw.newLine();

            for (MateriaTutor m : tutor.getMaterias()) {
                bw.write("MATERIA;" + m.getNombreMateria() + ";"
                        + m.getTarifa() + ";" + m.getCupoMaximo());
                bw.newLine();
            }

            for (DisponibilidadTutor d : tutor.getDisponibilidades()) {
                bw.write("DISPONIBILIDAD;" + d.getDia() + ";"
                        + d.getHoraInicio() + ";" + d.getHoraFin());
                bw.newLine();
            }
        }

        bw.close();
    }

    /**
     * Carga los tutores desde el archivo y los registra en el gestor.
     * Si el archivo no existe, no hace nada.
     * @param gestor gestor de tutores donde se registraran los tutores cargados
     * @throws IOException si ocurre un error al leer el archivo
     */
    public static void cargarTutores(GestionarTutores gestor) throws IOException {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return;

        BufferedReader br = new BufferedReader(new FileReader(archivo));
        String linea;
        Tutor tutorActual = null;

        while ((linea = br.readLine()) != null) {
            if (linea.isBlank()) continue;
            String[] datos = linea.split(";");

            try {
                switch (datos[0]) {
                    case "TUTOR":
                        tutorActual = new TutorBuilder()
                                .conDatosBasicos(
                                        Integer.parseInt(datos[1]),
                                        datos[2], datos[3], datos[4])
                                .build();
                        gestor.getTutores().add(tutorActual);
                        break;

                    case "MATERIA":
                        if (tutorActual != null) {
                            tutorActual.agregarMateria(new MateriaTutor(
                                    datos[1],
                                    Integer.parseInt(datos[2]),
                                    Integer.parseInt(datos[3])));
                        }
                        break;

                    case "DISPONIBILIDAD":
                        if (tutorActual != null) {
                            tutorActual.agregarDisponibilidad(new DisponibilidadTutor(
                                    LocalDate.parse(datos[1]),
                                    LocalTime.parse(datos[2]),
                                    LocalTime.parse(datos[3])));
                        }
                        break;
                }
            } catch (Exception e){
                System.out.println("Error Cargado");
            }
        }

        br.close();
        gestor.actualizarSiguienteId();
    }
}