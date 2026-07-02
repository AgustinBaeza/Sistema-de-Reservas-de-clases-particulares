package Logica.CargadoGuardado;

import Logica.estudiante.Estudiante;
import Logica.estudiante.GestionarEstudiantes;

import java.io.*;
import java.util.ArrayList;

/**
 * Clase que guarda y carga los datos de los estudiantes en un archivo txt.
 * Formato de cada linea: id;nombre;correo;telefono
 */
public class CargadoGuardadoEstudiante {

    private static final String ARCHIVO = "datosEstudiantes.txt";

    /**
     * Guarda todos los estudiantes en el archivo.
     * @param estudiantes lista de estudiantes a guardar
     * @throws IOException si ocurre un error al escribir el archivo
     */
    public static void guardarEstudiantes(ArrayList<Estudiante> estudiantes) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO));

        for (Estudiante e : estudiantes) {
            bw.write(e.getId() + ";" + e.getNombre() + ";"
                    + e.getCorreo() + ";" + e.getTelefono());
            bw.newLine();
        }

        bw.close();
    }

    /**
     * Carga los estudiantes desde el archivo y los registra en el gestor.
     * Si el archivo no existe, no hace nada.
     * @param gestor gestor de estudiantes donde se registraran los datos cargados
     * @throws IOException si ocurre un error al leer el archivo
     */
    public static void cargarEstudiantes(GestionarEstudiantes gestor) throws IOException {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return;

        BufferedReader br = new BufferedReader(new FileReader(archivo));
        String linea;

        while ((linea = br.readLine()) != null) {
            if (linea.isBlank()) continue;
            String[] datos = linea.split(";");

            try{
            Estudiante estudiante = new Estudiante(
                    Integer.parseInt(datos[0]),
                    datos[1], datos[2], datos[3]);
            gestor.getEstudiantes().add(estudiante);}
            catch (Exception e){
                System.out.println("Error de cargado ");
            }
        }

        br.close();
        gestor.actualizarSiguienteId();
    }
}
