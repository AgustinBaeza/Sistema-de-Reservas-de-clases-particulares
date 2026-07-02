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


    public PanelReservas(SistemaReservasControlador controladorSistema) {
        this.controladorSistema = controladorSistema;
        this.reservasAMostrar = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(panelTitulo(), BorderLayout.NORTH);
        add(panelFormulario(), BorderLayout.WEST);
        add(panelTabla(), BorderLayout.CENTER);
        agregarEventos();
    }

    private JPanel panelTitulo(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titulo = new JLabel("Reservas:");

        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        panel.add(titulo);

        return panel;
    }

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

    private void agregarEventos() {
        botonBuscarTutores.addActionListener(e -> buscarTutoresDisponibles());
        botonLimpiar.addActionListener(e -> limpiarFormulario());
        botonCrearReserva.addActionListener(e -> crearReserva());
        botonConfirmar.addActionListener(e -> confirmarReserva());
        botonCancelar.addActionListener(e -> cancelarReserva());
    }

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
                mostrarAviso("Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

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
                mostrarAviso("Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


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

    private void cargarEstudiantes() {
        comboBoxEstudiantes.removeAllItems();
        for (Estudiante e : controladorSistema.getEstudiantes()) {
            comboBoxEstudiantes.addItem(e);
        }
    }

    private Reserva getReservaSeleccionada() {
        int fila = tablaReservas.getSelectedRow();
        if (fila == -1) {
            mostrarAviso("Seleccione una reserva de la tabla.", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        return reservasAMostrar.get(fila);
    }

    private void limpiarFormulario() {
        txtMateria.setText("");
        txtFecha.setText("");
        txtHoraInicio.setText("");
        txtHoraFin.setText("");
        comboBoxTutores.removeAllItems();
        comboBoxEstudiantes.removeAllItems();
    }



    private void mostrarAviso(String mensaje, int tipoAviso) {

        JOptionPane.showMessageDialog(this, mensaje, "Aviso", tipoAviso);
    }
}
