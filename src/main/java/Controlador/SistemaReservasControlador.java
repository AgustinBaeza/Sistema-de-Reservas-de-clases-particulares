package Controlador;

import Logica.estudiante.Estudiante;
import Logica.estudiante.GestionarEstudiantes;
import Logica.excepciones.CampoVacioException;
import Logica.excepciones.FechaHoraInvalidaException;
import Logica.excepciones.MateriaNoEncontradaException;
import Logica.excepciones.ObjetoNoSeleccionadoException;
import Logica.excepciones.ValorNumericoInvalidoException;
import Logica.reserva.BuscadorDisponibilidad;
import Logica.reserva.GestorReservas;
import Logica.reserva.Reserva;
import Logica.tutor.DisponibilidadTutor;
import Logica.tutor.GestionarTutores;
import Logica.tutor.MateriaTutor;
import Logica.tutor.Tutor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Clase controladora principal del sistema de reservas de clases particulares.
 * Se encarga de recibir las solicitudes realizadas desde la interfaz grafica (Vista) y delegarlas
 * hacia las clases correspondientes de la capa logica (Modelo), estableciendo una separacion entre aquellas dos
 * manteniendose fiel a la arquitectura MVC
 */
public class SistemaReservasControlador{

    private GestionarTutores gestorTutores;
    private GestionarEstudiantes gestorEstudiantes;
    private GestorReservas gestorReservas;

    /**
     * Constructor del controlador principal, inicializa los gestores necesarios
     * para administrar tutores, estudiantes y reservas dentro del sistema.
     * Ademas crea una instancia de BuscadorDisponibilidad para poder ser utilizada por GestorReservas para la correspondiente
     * validacion de disponibilidad y conflictos horarios
     */
    public SistemaReservasControlador(){
        BuscadorDisponibilidad buscadorDisponibilidad = new BuscadorDisponibilidad();

        this.gestorTutores = new GestionarTutores();
        this.gestorEstudiantes = new GestionarEstudiantes();
        this.gestorReservas = new GestorReservas(buscadorDisponibilidad);

        //BORRAR dsps
        datosPrueba();
    }

    // datos para probar :) borrar dsps
    private void datosPrueba() {

        Tutor pedro = gestorTutores.crearPerfilTutor("Pedro Soto", "pedro@gmail.com", "999999999");
        gestorTutores.definirMateriaTutor(pedro.getId(), "Calculo III", 15000, 3);
        gestorTutores.definirMateriaTutor(pedro.getId(), "Algebra",     12000, 2);
        gestorTutores.definirDisponibilidadTutor(pedro.getId(),
                LocalDate.of(2026, 7, 7), LocalTime.of(14, 0), LocalTime.of(18, 0));
        gestorTutores.definirDisponibilidadTutor(pedro.getId(),
                LocalDate.of(2026, 7, 8), LocalTime.of(9, 0),  LocalTime.of(12, 0));

        Tutor ana = gestorTutores.crearPerfilTutor("Ana Muñoz", "ana@gmail.com", "988888888");
        gestorTutores.definirMateriaTutor(ana.getId(), "Calculo III", 12000, 4);
        gestorTutores.definirMateriaTutor(ana.getId(), "Fisica I",    10000, 3);
        gestorTutores.definirDisponibilidadTutor(ana.getId(),
                LocalDate.of(2026, 7, 7), LocalTime.of(14, 0), LocalTime.of(18, 0));
        gestorTutores.definirDisponibilidadTutor(ana.getId(),
                LocalDate.of(2026   , 7, 9), LocalTime.of(10, 0), LocalTime.of(14, 0));

        Tutor luis = gestorTutores.crearPerfilTutor("Luis Vera", "luis@gmail.com", "977777777");
        gestorTutores.definirMateriaTutor(luis.getId(), "Fisica I", 11000, 2);
        gestorTutores.definirDisponibilidadTutor(luis.getId(),
                LocalDate.of(2026, 7, 8), LocalTime.of(14, 0), LocalTime.of(17, 0));

        gestorEstudiantes.crearPerfilEstudiante("Juan Garcia",    "juan@gmail.com",    "911111111");
        gestorEstudiantes.crearPerfilEstudiante("Mario Perez",  "maria@gmail.com",   "922222222");
        gestorEstudiantes.crearPerfilEstudiante("Carlos Lopes",  "carlos@gmail.com",  "933333333");
    }


    // metodos auxiliares

    /**
     * Metodo auxiliar que valida que un texto ingresado no sea nulo ni este vacio.
     * @param texto texto que se desea validar
     * @param mensaje mensaje de error en caso de que el texto no sea valido
     * @throws CampoVacioException se lanza excepcion si el texto es nulo o esta vacio
     */
    private void validarTexto(String texto, String mensaje){

        if (texto == null || texto.isBlank()){
            throw new CampoVacioException(mensaje);
        }
    }

    /**
     * Metodo auxiliar que valida que un objeto ingresado no sea nulo.
     * @param objeto objeto que se desea validar
     * @param mensaje mensaje de error en caso de que el objeto sea nulo
     * @throws ObjetoNoSeleccionadoException se lanza excepcion si el objeto recibido es nulo
     */
    private void validarObjeto(Object objeto, String mensaje){

        if (objeto == null){
            throw new ObjetoNoSeleccionadoException(mensaje);
        }
    }

    /**
     * Metodo auxiliar que valida los datos de fecha y hora de una reserva no sean nulos o no sean un rango invalido (inicia despues que termina)
     * @param fecha fecha que se desea validar
     * @param horaInicio hora de inicio
     * @param horaFin hora de termino
     * @throws FechaHoraInvalidaException se lanza excepcion si la fecha u horario no cumplen con las reglas esperadas
     */
    private void validarFechaHora(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin){

        if (fecha == null){
            throw new FechaHoraInvalidaException("La fecha no puede estar vacia");
        }

        if (horaInicio == null || horaFin == null){
            throw new FechaHoraInvalidaException("Debe ingresar hora de inicio y hora de termino");
        }

        if (!horaInicio.isBefore(horaFin)){
            throw new FechaHoraInvalidaException("La hora de inicio debe ser anterior a la hora de termino");
        }
    }

    /**
     * Metodo auxiliar que valida que una tarifa ingresada no sea negativa.
     * @param tarifa tarifa que se desea validar
     * @throws ValorNumericoInvalidoException lanza excepcion si la tarifa es negativa
     */
    private void validarTarifa(int tarifa){

        if (tarifa < 0) {
            throw new ValorNumericoInvalidoException("La tarifa no puede ser negativa");
        }
    }

    /**
     * Metodo auxiliar que valida que el cupo maximo sea mayor que cero.
     * @param cupoMaximo cupo maximo que se desea validar
     * @throws ValorNumericoInvalidoException lanza excepcion si el cupo maximo es menor o igual que cero
     */
    private void validarCupoMaximo(int cupoMaximo){

        if (cupoMaximo <= 0){
            throw new ValorNumericoInvalidoException("El cupo maximo debe ser mayor que cero");
        }
    }


    // metodos de creacion/edicion

    /**
     * Metodo que crea un nuevo perfil de tutor dentro del sistema.
     * Antes de gestionar la creacion de tutores, valida que los datos ingresados al tutor sean validos (no nulos)
     * @param nombre nombre del tutor
     * @param correo correo de contacto del tutor
     * @param telefono telefono de contacto del tutor
     * @return tutor creado y registrado en el sistema
     */
    public Tutor crearTutor(String nombre, String correo, String telefono){

        validarTexto(nombre, "El nombre del tutor no puede estar vacio");
        validarTexto(correo, "El correo del tutor no puede estar vacio");
        validarTexto(telefono, "El telefono del tutor no puede estar vacio");

        return gestorTutores.crearPerfilTutor(nombre, correo, telefono);
    }

    /**
     * Metodo que edita los datos principales de un tutor ya registrado.
     * Antes de gestionar la edicion de tutores, valida que los datos ingresados a modificar del tutor sean validos
     * @param idTutor identificador del tutor que se desea editar
     * @param nuevoNombre nuevo nombre del tutor
     * @param nuevoCorreo nuevo correo de contacto del tutor
     * @param nuevoTelefono nuevo telefono de contacto del tutor
     * @return true si el tutor fue encontrado y editado, false en caso contrario
     */
    public boolean editarTutor(int idTutor, String nuevoNombre, String nuevoCorreo, String nuevoTelefono){

        validarTexto(nuevoNombre, "El nombre del tutor no puede estar vacio");
        validarTexto(nuevoCorreo, "El correo del tutor no puede estar vacio");
        validarTexto(nuevoTelefono, "El telefono del tutor no puede estar vacio");

        return gestorTutores.editarPerfilTutor(idTutor, nuevoNombre, nuevoCorreo, nuevoTelefono);
    }

    /**
     * Metodo que agrega una materia a un tutor existente.
     * Previo a gestionar la adicion de materias a un tutor, valida que los datos de la materia sean validos:
     * - Nombre de la materia no nulo
     * - Tarifa mayor o igual que cero, no hay "dinero negativo"
     * - CupoMaximo mayor que cero, no se permite agregar materias sin cupos o con cupos negativos
     * @param idTutor identificador del tutor al que se le agregara la materia
     * @param nombreMateria nombre de la materia
     * @param tarifa tarifa asociada a la materia
     * @param cupoMaximo cantidad maxima de estudiantes permitidos
     * @return true si la materia fue agregada correctamente, false en caso contrario
     */
    public boolean agregarMateriaTutor(int idTutor, String nombreMateria, int tarifa, int cupoMaximo){

        validarTexto(nombreMateria, "El nombre de la materia no puede estar vacio");
        validarTarifa(tarifa);
        validarCupoMaximo(cupoMaximo);

        return gestorTutores.definirMateriaTutor(idTutor, nombreMateria, tarifa, cupoMaximo);
    }

    /**
     * Metodo que edita una materia ya asociada a un tutor realizando validacion de los datos ingresados respecto a los criterios previos
     * @param idTutor identificador del tutor
     * @param nombreMateriaActual nombre actual de la materia
     * @param nuevoNombreMateria nuevo nombre de la materia
     * @param nuevaTarifa nueva tarifa de la materia
     * @param nuevoCupoMaximo nuevo cupo maximo de la materia
     * @return true si la materia fue editada correctamente, false en caso contrario
     */
    public boolean editarMateriaTutor(int idTutor, String nombreMateriaActual, String nuevoNombreMateria, int nuevaTarifa,
                                      int nuevoCupoMaximo){

        validarTexto(nombreMateriaActual, "Debe indicar la materia actual");
        validarTexto(nuevoNombreMateria, "El nuevo nombre de la materia no puede estar vacio");
        validarTarifa(nuevaTarifa);
        validarCupoMaximo(nuevoCupoMaximo);

        return gestorTutores.editarMateriaTutor(idTutor, nombreMateriaActual, nuevoNombreMateria, nuevaTarifa, nuevoCupoMaximo);
    }

    /**
     * Metodo que agrega un bloque de disponibilidad horaria a un tutor, validando los datos de fecha y hora ingresados
     * @param idTutor identificador del tutor
     * @param fecha fecha disponible del tutor
     * @param horaInicio hora de inicio del bloque
     * @param horaFin hora de termino del bloque
     * @return true si la disponibilidad fue agregada correctamente, false en caso contrario
     */
    public boolean agregarDisponibilidadTutor(int idTutor, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin){

        validarFechaHora(fecha, horaInicio, horaFin);

        return gestorTutores.definirDisponibilidadTutor(idTutor, fecha, horaInicio, horaFin);
    }

    /**
     * Metodo que edita una disponibilidad existente de un tutor, validando los datos de fecha y hora ingresados
     * @param idTutor identificador del tutor
     * @param indiceDisponibilidad posicion de la disponibilidad dentro de la lista
     * @param nuevaFecha nueva fecha disponible
     * @param nuevaHoraInicio nueva hora de inicio
     * @param nuevaHoraFin nueva hora de termino
     * @return true si la disponibilidad fue editada correctamente, false en caso contrario
     */
    public boolean editarDisponibilidadTutor(int idTutor, int indiceDisponibilidad, LocalDate nuevaFecha,
                                             LocalTime nuevaHoraInicio, LocalTime nuevaHoraFin){

        validarFechaHora(nuevaFecha, nuevaHoraInicio, nuevaHoraFin);

        return gestorTutores.editarDisponibilidadTutor(idTutor, indiceDisponibilidad, nuevaFecha, nuevaHoraInicio, nuevaHoraFin);
    }

    /**
     * Metodo que crea un nuevo perfil de estudiante dentro del sistema, realizando las validaciones sobre campos de texto no vacios
     * @param nombre nombre del estudiante
     * @param correo correo de contacto del estudiante
     * @param telefono telefono de contacto del estudiante
     * @return estudiante creado y registrado en el sistema
     */
    public Estudiante crearEstudiante(String nombre, String correo, String telefono){

        validarTexto(nombre, "El nombre del estudiante no puede estar vacio");
        validarTexto(correo, "El correo del estudiante no puede estar vacio");
        validarTexto(telefono, "El telefono del estudiante no puede estar vacio");

        return gestorEstudiantes.crearPerfilEstudiante(nombre, correo, telefono);
    }

    /**
     * Metodo que edita los datos principales de un estudiante existente, realizando las validaciones sobre campos de texto no vacios
     * @param idEstudiante IDdel estudiante que se desea editar
     * @param nuevoNombre nuevo nombre del estudiante
     * @param nuevoCorreo nuevo correo de contacto
     * @param nuevoTelefono nuevo telefono de contacto
     * @return true si el estudiante fue encontrado y editado, false en caso contrario
     */
    public boolean editarEstudiante(int idEstudiante, String nuevoNombre, String nuevoCorreo, String nuevoTelefono){

        validarTexto(nuevoNombre, "El nombre del estudiante no puede estar vacio");
        validarTexto(nuevoCorreo, "El correo del estudiante no puede estar vacio");
        validarTexto(nuevoTelefono, "El telefono del estudiante no puede estar vacio");

        return gestorEstudiantes.editarPerfilEstudiante(idEstudiante, nuevoNombre, nuevoCorreo, nuevoTelefono);
    }

    /**
     * Metodo que crea una reserva entre un tutor y un estudiante desde la GUI,
     * validando que se hayan seleccionado tutores/estudiantes correctamente,
     * que se haya ingresado texto no vacio para la materia, una fecha/hora valida
     * y que la materia ingresada sea impartida por el tutor seleccionado
     * @param tutor tutor que realizara la clase
     * @param estudiante estudiante que recibira la clase
     * @param nombreMateria nombre de la materia solicitada
     * @param fecha fecha de la reserva
     * @param horaInicio hora de inicio de la reserva
     * @param horaFin hora de termino de la reserva
     * @return reserva creada y registrada en el sistema
     */
    public Reserva crearReserva(Tutor tutor, Estudiante estudiante, String nombreMateria,
                                LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {

        validarObjeto(tutor, "Debe seleccionar un tutor");
        validarObjeto(estudiante, "Debe seleccionar un estudiante");
        validarTexto(nombreMateria, "Debe ingresar una materia");
        validarFechaHora(fecha, horaInicio, horaFin);

        MateriaTutor materiaTutor = tutor.buscarMateria(nombreMateria);

        if (materiaTutor == null) {
            throw new MateriaNoEncontradaException("El tutor seleccionado no imparte esa materia");
        }

        return gestorReservas.crearReserva(tutor, estudiante, materiaTutor, fecha, horaInicio, horaFin);
    }

    /**
     * Metodo que modifica una reserva existente desde la GUI
     * validando que se hayan seleccionado tutores/estudiantes correctamente,
     * que se haya ingresado texto no vacio para la materia, una fecha/hora valida
     * y que la materia ingresada sea impartida por el tutor seleccionado
     * @param reserva reserva que se desea modificar
     * @param nuevoTutor nuevo tutor asignado
     * @param nuevoEstudiante nuevo estudiante asignado
     * @param nombreMateria nueva materia solicitada
     * @param nuevaFecha nueva fecha de la reserva
     * @param nuevaHoraInicio nueva hora de inicio
     * @param nuevaHoraFin nueva hora de termino
     */
    public void modificarReserva(Reserva reserva, Tutor nuevoTutor, Estudiante nuevoEstudiante, String nombreMateria,
                                 LocalDate nuevaFecha, LocalTime nuevaHoraInicio, LocalTime nuevaHoraFin) {

        validarObjeto(reserva, "Debe seleccionar una reserva");
        validarObjeto(nuevoTutor, "Debe seleccionar un tutor");
        validarObjeto(nuevoEstudiante, "Debe seleccionar un estudiante");
        validarTexto(nombreMateria, "Debe ingresar una materia");
        validarFechaHora(nuevaFecha, nuevaHoraInicio, nuevaHoraFin);

        MateriaTutor nuevaMateria = nuevoTutor.buscarMateria(nombreMateria);

        if (nuevaMateria == null){
            throw new MateriaNoEncontradaException("El tutor seleccionado no imparte esa materia");
        }

        gestorReservas.modificarReserva(reserva, nuevoTutor, nuevaMateria, nuevoEstudiante, nuevaFecha, nuevaHoraInicio, nuevaHoraFin);
    }

    /**
     * Metodo que cancela una reserva existente, validando que se haya seleccionado una reserva a cancelar
     * @param reserva reserva que se desea cancelar
     */
    public void cancelarReserva(Reserva reserva){
        validarObjeto(reserva, "Debe seleccionar una reserva");

        gestorReservas.cancelarReserva(reserva);
    }

    /**
     * Metodo que confirma una reserva existente, validando que se haya seleccionado una reserva a confirmar
     * @param reserva reserva que se desea confirmar
     */
    public void confirmarReserva(Reserva reserva){
        validarObjeto(reserva, "Debe seleccionar una reserva");

        reserva.confirmarReserva();
    }

    // metodos getters de la logica

    /**
     * Busca un tutor registrado segun su ID en el sistema.
     * @param idTutor ID del tutor
     * @return tutor encontrado, o null si no existe
     */
    public Tutor buscarTutorPorId(int idTutor) {
        return gestorTutores.buscarTutorPorId(idTutor);
    }

    /**
     * Obtiene la lista de tutores registrados en el sistema, devuelve una shallow copy de la lista de tutores
     * para agregar una capa de encapsulamiento y con ello evitar que la vista pueda modifique directamente los datos del sistema,
     * forzando a que la vista tenga que pasar si o si por el controlador
     * @return shallow copy de lista de tutores registrados
     */
    public ArrayList<Tutor> getTutores() {
        return new ArrayList<>(gestorTutores.getTutores());
    }

    /**
     * Obtiene la lista de materias asociadas a un tutor, devuelve una shallow copy de la lista de materias
     * para agregar una capa de encapsulamiento y con ello evitar que la vista pueda modifique directamente los datos del sistema,
     * forzando a que la vista tenga que pasar si o si por el controlador
     * Adicionalmente valida si se ha seleccionado un tutor
     * @param tutor tutor cuyas materias se desean consultar
     * @return shallow copy de lista de materias del tutor
     */
    public ArrayList<MateriaTutor> getMateriasTutor(Tutor tutor){
        validarObjeto(tutor, "Debe seleccionar un tutor");

        return new ArrayList<>(tutor.getMaterias());
    }

    /**
     * Obtiene la lista de disponibilidades asociadas a un tutor, devolviendo una shallow copy de la lista de disponibilidades del tutor
     * para agregar una capa de encapsulamiento y con ello evitar que la vista pueda modifique directamente los datos del sistema,
     * forzando a que la vista tenga que pasar si o si por el controlador
     * Adicionalmente valida si se ha seleccionado un tutor
     * @param tutor tutor cuyas disponibilidades se desean consultar
     * @return lista de disponibilidades del tutor
     */
    public ArrayList<DisponibilidadTutor> getDisponibilidadesTutor(Tutor tutor){

        validarObjeto(tutor, "Debe seleccionar un tutor");

        return new ArrayList<>(tutor.getDisponibilidades());
    }

    /**
     * Busca todos los tutores que imparten una materia especifica
     * Adicionalmente verifica si se ha ingresado un texto no vacio
     * @param nombreMateria nombre de la materia buscada
     * @return lista de tutores que imparten la materia indicada
     */
    public ArrayList<Tutor> buscarTutoresPorMateria(String nombreMateria){

        validarTexto(nombreMateria, "Debe ingresar una materia");

        return gestorTutores.buscarTutoresPorMateria(nombreMateria);
    }

    /**
     * Busca un estudiante registrado segun su ID en el sistema
     * @param idEstudiante ID del estudiante
     * @return estudiante encontrado, o null si no existe
     */
    public Estudiante buscarEstudiantePorId(int idEstudiante) {
        return gestorEstudiantes.buscarEstudiantePorID(idEstudiante);
    }

    /**
     * Obtiene la lista de estudiantes registrados en el sistema.
     * @return lista de estudiantes registrados
     */
    public ArrayList<Estudiante> getEstudiantes() {
        return new ArrayList<>(gestorEstudiantes.getEstudiantes());
    }

    /**
     * Busca tutores compatibles para una solicitud de reserva, considerando la materia solicitada, fecha y horario
     * Ademas de verificar la disponibilidad del tutor, los posibles conflictos horarios y el cupo maximo respectivo.
     * Adicionalmente realiza validacion sobre texto de materia ingresada no vacia y fecha/hora validas.
     * @param nombreMateria nombre de la materia solicitada
     * @param fecha fecha en que se desea realizar la clase
     * @param horaInicio hora de inicio de la clase
     * @param horaFin hora de termino de la clase
     * @return lista de tutores compatibles con la solicitud
     */
    public ArrayList<Tutor> buscarTutoresDisponibles(String nombreMateria, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {

        validarTexto(nombreMateria, "Debe ingresar una materia");
        validarFechaHora(fecha, horaInicio, horaFin);

        return gestorReservas.buscarTutoresDisponibles(gestorTutores.getTutores(), nombreMateria, fecha, horaInicio, horaFin);
    }

    /**
     * Getter que entrega todas las reservas registradas en el sistema como una shallow copy de estas para agregar una capa de encapsulamiento
     * @return lista de reservas registradas
     */
    public ArrayList<Reserva> getReservas(){
        return new ArrayList<>(gestorReservas.getReservas());
    }

    /**
     * Getter que entrega todas las reservas activas (que no estan en estado cancelada) como una shallow copy
     * @return lista de reservas activas
     */
    public ArrayList<Reserva> getReservasActivas(){
        return new ArrayList<>(gestorReservas.getReservasActivas());
    }

    /**
     * Getter que entrega las reservas asociadas a un tutor especifico como una shallow copy de la lista en el sistema,
     * validando que se haya seleccionado un tutor.
     * @param tutor tutor cuyas reservas se desean consultar
     * @return lista de reservas del tutor
     */
    public ArrayList<Reserva> getReservasTutor(Tutor tutor){
        validarObjeto(tutor, "Debe seleccionar un tutor");

        return new ArrayList<>(gestorReservas.getReservasTutor(tutor));
    }

    /**
     * Getter que entrega las reservas asociadas a un estudiante especifico como una shallow copy de la lista en el sistema,
     * validando que se haya seleccionado un estudiante.
     * @param estudiante estudiante cuyas reservas se desean consultar
     * @return lista de reservas del estudiante
     */
    public ArrayList<Reserva> getReservasEstudiante(Estudiante estudiante){
        validarObjeto(estudiante, "Debe seleccionar un estudiante");

        return new ArrayList<>(gestorReservas.getReservasEstudiante(estudiante));
    }
}