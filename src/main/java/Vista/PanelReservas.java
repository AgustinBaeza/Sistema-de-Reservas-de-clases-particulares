package Vista;

import Controlador.SistemaReservasControlador;
import Logica.estudiante.*;
import Logica.excepciones.*;
import Logica.reserva.Reserva;
import Logica.tutor.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Panel encargado de la gestion de reservas de clases particulares.
 * Permite buscar tutores disponibles segun una materia, fecha y horario,
 * crear nuevas reservas, visualizar todas las reservas registradas y
 * confirmar o cancelar reservas existentes mediante una interfaz grafica.
 */
public class PanelReservas extends JPanel {
    private SistemaReservasControlador controladorSistema;

    private ArrayList<Reserva> reservasAMostrar;

    private JTextField txtMateria;
    private JTextField txtFecha;
    private JTextField txtHoraInicio;
    private JTextField txtHoraFin;
    private JButton botonBuscarTutores;
    private JComboBox<Tutor> comboBoxTutores;
    private JComboBox<Estudiante> comboBoxEstudiantes;
    private JButton botonCrearReserva;
    private JButton botonLimpiar;

    private DefaultTableModel modeloTabla;
    private JTable tablaReservas;
    private JButton botonConfirmar;
    private JButton botonCancelar;

    /**
     * Constructor de panelReservas.
     * Inicializa el controlador del sistema, la lista auxiliar de reservas
     * mostradas en la tabla y construye todos los componentes graficos del
     * panel, incluyendo el titulo, el formulario de creacion de reservas y
     * la tabla de reservas registradas. Ademas registra los eventos de los
     * botones y carga la informacion inicial en la tabla.
     *
     * @param controladorSistema controlador principal encargado de gestionar
     *                           la logica del sistema de reservas.
     */
    public PanelReservas(SistemaReservasControlador controladorSistema) {
        this.controladorSistema = controladorSistema;
        this.reservasAMostrar = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(panelTitulo(), BorderLayout.NORTH);
        add(panelFormulario(), BorderLayout.WEST);
        add(panelTabla(), BorderLayout.CENTER);
        agregarEventos();
        actualizarTabla();
    }

    /**
     * Crea el panel superior del modulo de reservas.
     * Contiene el titulo principal que identifica la seccion de reservas
     * dentro de la interfaz grafica.
     *
     * @return panel con el titulo del modulo.
     */
    private JPanel panelTitulo(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titulo = new JLabel("Reservas:");

        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(titulo);

        return panel;
    }

    /**
     * Crea el panel del formulario utilizado para ingresar los datos
     * necesarios para registrar una nueva reserva.
     * Contiene los campos de materia, fecha, horario, seleccion de tutor
     * y estudiante, ademas de los botones para buscar tutores, crear la
     * reserva y limpiar el formulario.
     *
     * @return panel con el formulario de creacion de reservas.
     */
    private JPanel panelFormulario(){
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));

        panel.setPreferredSize(new Dimension(270, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Nueva reserva"));

        txtMateria    = new JTextField();
        txtFecha      = new JTextField();
        txtHoraInicio = new JTextField();
        txtHoraFin    = new JTextField();

        botonBuscarTutores = new JButton("Buscar tutores disponibles");
        comboBoxEstudiantes = new JComboBox<>();
        comboBoxTutores = new JComboBox<>();
        botonCrearReserva = new JButton("Crear Reserva");
        botonLimpiar = new JButton("Limpiar formulario");


        panel.add(new JLabel("Materia:"));
        panel.add(txtMateria);
        panel.add(new JLabel("Fecha (AAAA-MM-DD):"));
        panel.add(txtFecha);
        panel.add(new JLabel("Hora inicio (HH:MM):"));
        panel.add(txtHoraInicio);
        panel.add(new JLabel("Hora fin (HH:MM):"));
        panel.add(txtHoraFin);
        panel.add(botonBuscarTutores);
        panel.add(new JLabel("Tutor disponible: "));
        panel.add(comboBoxTutores);
        panel.add(new JLabel("Estudiante:"));
        panel.add(comboBoxEstudiantes);
        panel.add(botonCrearReserva);
        panel.add(botonLimpiar);

        return panel;
    }

    /**
     * Registra todos los eventos de los botones del panel de reservas,
     * asociando cada accion de la interfaz con su metodo correspondiente.
     */
    private void agregarEventos() {
        botonBuscarTutores.addActionListener(e -> buscarTutoresDisponibles());
        botonLimpiar.addActionListener(e -> limpiarFormulario());
        botonCrearReserva.addActionListener(e -> crearReserva());
        botonConfirmar.addActionListener(e -> confirmarReserva());
        botonCancelar.addActionListener(e -> cancelarReserva());
    }

    /**
     * Busca los tutores disponibles para la materia, fecha y horario ingresados.
     * Valida que la materia no este vacia y que la fecha y horas tengan un formato valido.
     * Si existen tutores compatibles, los carga en el combobox de seleccion junto con
     * los estudiantes registrados en el sistema.
     */
    private void buscarTutoresDisponibles(){
        String nombreMateria = txtMateria.getText().trim();
        if (nombreMateria.isBlank()) {
            mostrarAviso("Ingrese el nombre de la materia.",1);
            return;
        }

        try {
            LocalDate fecha      = LocalDate.parse(txtFecha.getText().trim());
            LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText().trim());
            LocalTime horaFin    = LocalTime.parse(txtHoraFin.getText().trim());

            ArrayList<Tutor> disponibles = controladorSistema.buscarTutoresDisponibles(
                    nombreMateria, fecha, horaInicio, horaFin);

            comboBoxTutores.removeAllItems();

            if (disponibles.isEmpty()) {
                mostrarAviso("No hay tutores disponibles para ese horario y materia.",-1);
                return;
            }

            for (Tutor t : disponibles) {
                comboBoxTutores.addItem(t);
            }

            cargarEstudiantes();

        } catch (DateTimeParseException ex){
            mostrarAviso("Formato incorrecto.\nFecha: AAAA-MM-DD  Hora: HH:MM",2);
        }

    }

    /**
     * Construye el panel que muestra la tabla de reservas registradas junto
     * con los botones para confirmar o cancelar una reserva.
     *
     * @return panel que contiene la tabla de reservas y sus acciones.
     */
    private JPanel panelTabla(){
        JPanel panel = new JPanel(new BorderLayout(0,9));
        panel.setBorder(BorderFactory.createTitledBorder("Reservas registradas"));

        String[] columnas = {"#", "Tutor", "Tarifa","Estudiante", "Materia", "Fecha", "Inicio", "Fin", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int col) { return false; }
        };

        tablaReservas = new JTable(modeloTabla);
        tablaReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaReservas.getTableHeader().setReorderingAllowed(false);
        tablaReservas.getTableHeader().setResizingAllowed(false);

        tablaReservas.setColumnSelectionAllowed(false);
        tablaReservas.setCellSelectionEnabled(false);
        tablaReservas.getColumnModel().getColumn(0).setMaxWidth(30);
        //tablaReservas.getColumnModel().getColumn(6).setMaxWidth(40);
        //tablaReservas.getColumnModel().getColumn(7).setMaxWidth(40);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botonConfirmar = new JButton("Confirmar");
        botonCancelar = new JButton("Cancelar");

        panelBotones.add(botonConfirmar);
        panelBotones.add(botonCancelar);

        panel.add(new JScrollPane(tablaReservas), BorderLayout.CENTER);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Actualiza el contenido de la tabla de reservas.
     * Elimina todas las filas existentes y vuelve a cargar las reservas
     * registradas actualmente en el sistema.
     */
    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        reservasAMostrar.clear();

        int numero = 1;
        for (Reserva r : controladorSistema.getReservas()) {
            modeloTabla.addRow(new Object[]{
                    numero++,
                    r.getTutor().getNombre(),
                    r.getMateriaTutor().getTarifa(),
                    r.getEstudiante().getNombre(),
                    r.getMateriaTutor().getNombreMateria(),
                    r.getFecha(),
                    r.getHoraInicio(),
                    r.getHoraFin(),
                    r.getEstadoReserva()
            });
            reservasAMostrar.add(r);
        }
    }

    /**
     * Confirma la reserva seleccionada en la tabla.
     * Solicita confirmacion al usuario antes de realizar la operacion y
     * actualiza la tabla una vez confirmada.
     */
    private void confirmarReserva(){
        Reserva reserva = getReservaSeleccionada();
        if (reserva == null){
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea confirmar esta reserva?",
                "Confirmar reserva",
                JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            try {

                controladorSistema.confirmarReserva(reserva);
                actualizarTabla();
                mostrarAviso("Reserva confirmada!", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                mostrarAviso("Esta reserva se encuentra cancelada!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Cancela la reserva seleccionada en la tabla.
     * Solicita confirmacion al usuario antes de realizar la operacion y
     * actualiza la tabla una vez cancelada.
     */
    private void cancelarReserva(){
        Reserva reserva = getReservaSeleccionada();
        if (reserva == null){
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea cancelar esta reserva?",
                "Cancelar reserva",
                JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            try {

                controladorSistema.cancelarReserva(reserva);
                actualizarTabla();
                mostrarAviso("Reserva cancelada!", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                mostrarAviso("Esta reserva se encuentra cancelada!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    /**
     * Crea una nueva reserva utilizando los datos ingresados en el formulario.
     * Valida la existencia del tutor, estudiante, materia y el formato de la
     * fecha y horario antes de solicitar la creacion al controlador.
     */
    private void crearReserva(){
        Tutor tutor = (Tutor) comboBoxTutores.getSelectedItem();
        Estudiante estudiante = (Estudiante) comboBoxEstudiantes.getSelectedItem();

        if ( tutor == null || estudiante == null){
            mostrarAviso("Busque primero un tutor y seleccione un estudiante", 2);
            return;
        }

        try {
            LocalDate fecha      = LocalDate.parse(txtFecha.getText().trim());
            LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText().trim());
            LocalTime horaFin    = LocalTime.parse(txtHoraFin.getText().trim());
            String nombreMateria = txtMateria.getText().trim();

            MateriaTutor materia = tutor.buscarMateria(nombreMateria);
            if (materia == null) {
                mostrarAviso("El tutor seleccionado no imparte esa materia.",JOptionPane.ERROR_MESSAGE);
                return;
            }

            controladorSistema.crearReserva(tutor, estudiante, materia.getNombreMateria(), fecha, horaInicio, horaFin);
            mostrarAviso("Reserva creada correctamente.", JOptionPane.INFORMATION_MESSAGE);
            actualizarTabla();
            limpiarFormulario();



        } catch (DateTimeParseException ex) {
            mostrarAviso("Formato incorrecto.\nFecha: AAAA-MM-DD   Hora: HH:MM",JOptionPane.ERROR_MESSAGE);
        } catch (ConflictoHorarioException ex) {
            mostrarAviso("Existe un conflicto en el horario seleccionado",JOptionPane.ERROR_MESSAGE);
        } catch (CupoMaximoExcedidoException ex) {
            mostrarAviso("No quedan mas cupos para la materia seleccionada",JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Carga en el combobox de seleccion todos los estudiantes registrados
     * actualmente en el sistema.
     */
    private void cargarEstudiantes() {
        comboBoxEstudiantes.removeAllItems();
        for (Estudiante e : controladorSistema.getEstudiantes()) {
            comboBoxEstudiantes.addItem(e);
        }
    }

    /**
     * Obtiene la reserva actualmente seleccionada en la tabla.
     * Si no existe una fila seleccionada, informa al usuario.
     *
     * @return la reserva seleccionada o null si no hay ninguna seleccionada.
     */
    private Reserva getReservaSeleccionada() {
        int fila = tablaReservas.getSelectedRow();
        if (fila == -1) {
            mostrarAviso("Seleccione una reserva de la tabla.", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        return reservasAMostrar.get(fila);
    }

    /**
     * Se encarga de borrar todo lo que este escrito en el formulario
     */
    private void limpiarFormulario() {
        txtMateria.setText("");
        txtFecha.setText("");
        txtHoraInicio.setText("");
        txtHoraFin.setText("");
        comboBoxTutores.removeAllItems();
        comboBoxEstudiantes.removeAllItems();
    }


    /**
     * Muestra un cuadro de dialogo con un mensaje para informar al usuario.
     *
     * @param mensaje texto que se mostrara en la ventana de dialogo.
     * @param tipoAviso tipo de mensaje segun JOptionPane (Ej: INFORMATION_MESSAGE)
     */
    private void mostrarAviso(String mensaje, int tipoAviso) {

        JOptionPane.showMessageDialog(this, mensaje, "Aviso", tipoAviso);
    }
}
