package Vista;

import Controlador.SistemaReservasControlador;
import Logica.reserva.*;
import Logica.tutor.*;
import Logica.estudiante.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase que representa el panel visual del calendario de reservas.
 * Permite visualizar, filtrar y navegar por las reservas de tutorias
 * organizadas de forma semanal.
 */
public class PanelCalendario extends JPanel {

    private SistemaReservasControlador controladorSistema;
    private JTable tabla;
    private DefaultTableModel tablaModelo;
    private LocalDate lunesActual;
    private ArrayList<Reserva> reservasFiltradas = new ArrayList<>();

    private boolean calendarioGeneral = true;
    private boolean filtrandoPorTutor = false;

    private JLabel labelMes;
    private JLabel labelCalendarioDe;

    private JButton botonSemanaAnterior;
    private JButton botonSemanaSiguiente;
    private JButton botonVerTodas;
    private JButton botonConfirmarTutor;
    private JButton botonConfirmarEstudiante;

    private JComboBox<String> comboBoxTutores;
    private JComboBox<String> comboBoxEstudiantes;

    /**
     * Constructor del panel. Inicializa los componentes, ajusta el formato de la tabla
     * y carga las reservas correspondientes a la semana actual.
     *
     * @param controladorSistema el controlador principal del sistema para obtener los datos.
     */
    public PanelCalendario(SistemaReservasControlador controladorSistema) {
        this.controladorSistema = controladorSistema;

        setLayout(new BorderLayout());

        lunesActual = LocalDate.now().with(DayOfWeek.MONDAY);

        String[] columnas = {"Día", "Fecha", "Inicio", "Fin", "Tutor", "Estudiante", "Materia", "Estado"};
        tablaModelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(tablaModelo);
        tabla.setRowHeight(45);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.getTableHeader().setReorderingAllowed(false);

        ajustarColumnas();

        add(crearHeader(), BorderLayout.NORTH);

        JPanel panelCentrado = new JPanel(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setPreferredSize(new Dimension(850, 500));
        panelCentrado.add(scrollPane);
        add(panelCentrado, BorderLayout.CENTER);

        cargarAcciones();
        buscarTodasReservas();
        cargarSemana();
    }

    /**
     * Configura el ancho y los renderizadores de las columnas de la tabla.
     * Establece el alineado centrado para todo el contenido de la tabla.
     */
    private void ajustarColumnas() {
        setColumna(0, 80, 80);      //dia
        setColumna(1, 47, 47);      //fecha
        setColumna(2, 45, 45);      //horaInicio
        setColumna(3, 45, 45);      //horaFin
        setColumna(4, 195, 230);    //tutor
        setColumna(5, 185, 230);    //estudiante
        setColumna(6, 150, 230);    //materia
        setColumna(7, 100, 120);    //estado

        DefaultTableCellRenderer letrasEnElCentro = new DefaultTableCellRenderer();
        letrasEnElCentro.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(letrasEnElCentro);
        }
    }

    /**
     * Define las propiedades de ancho de una columna especifica.
     *
     * @param indice indice de la columna.
     * @param anchoMin ancho minimo.
     * @param anchoMax ancho maximo.
     */
    private void setColumna(int indice, int anchoMin, int anchoMax) {
        TableColumn column = tabla.getColumnModel().getColumn(indice);
        column.setMinWidth(anchoMin);
        column.setMaxWidth(anchoMax);
        column.setPreferredWidth(anchoMin);
    }

    /**
     * Crea y organiza los componentes del header.
     * @return un JPanel configurado como header.
     */
    private JPanel crearHeader() {

        JPanel header = new JPanel(new BorderLayout());

        // Centro
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        labelMes = new JLabel("", SwingConstants.CENTER);
        labelMes.setFont(new Font("Arial", Font.BOLD, 18));
        labelMes.setAlignmentX(Component.CENTER_ALIGNMENT);

        botonSemanaAnterior = new JButton("Semana anterior");
        botonSemanaSiguiente = new JButton("Semana siguiente");
        botonVerTodas = new JButton("Ver todas");

        JPanel panelBotonesSemana = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotonesSemana.add(botonSemanaAnterior);
        panelBotonesSemana.add(botonSemanaSiguiente);
        panelBotonesSemana.add(botonVerTodas);

        labelCalendarioDe = new JLabel("Calendario general", SwingConstants.CENTER);
        labelCalendarioDe.setFont(new Font("Arial", Font.BOLD, 14));
        labelCalendarioDe.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelCalendarioDe.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));

        centro.add(labelMes);
        centro.add(panelBotonesSemana);
        centro.add(labelCalendarioDe);

        // Izquierda
        JPanel izquierda = new JPanel(new BorderLayout());

        JLabel labelTutores = new JLabel("Tutores", SwingConstants.CENTER);

        comboBoxTutores = new JComboBox<>();
        for (Tutor t : controladorSistema.getTutores()) {
            comboBoxTutores.addItem(t.toString());
        }

        botonConfirmarTutor = new JButton("Confirmar tutor");

        JPanel panelTutor = new JPanel(new BorderLayout());
        panelTutor.add(labelTutores, BorderLayout.NORTH);
        panelTutor.add(comboBoxTutores, BorderLayout.CENTER);
        panelTutor.add(botonConfirmarTutor, BorderLayout.SOUTH);

        izquierda.add(panelTutor);

        // Derecha
        JPanel derecha = new JPanel(new BorderLayout());

        JLabel labelEstudiantes = new JLabel("Estudiantes", SwingConstants.CENTER);

        comboBoxEstudiantes = new JComboBox<>();
        for (Estudiante e : controladorSistema.getEstudiantes()) {
            comboBoxEstudiantes.addItem(e.toString());
        }

        botonConfirmarEstudiante = new JButton("Confirmar estudiante");

        JPanel panelEstudiante = new JPanel(new BorderLayout());
        panelEstudiante.add(labelEstudiantes, BorderLayout.NORTH);
        panelEstudiante.add(comboBoxEstudiantes, BorderLayout.CENTER);
        panelEstudiante.add(botonConfirmarEstudiante, BorderLayout.SOUTH);

        derecha.add(panelEstudiante);

        header.add(izquierda, BorderLayout.WEST);
        header.add(centro, BorderLayout.CENTER);
        header.add(derecha, BorderLayout.EAST);

        return header;
    }

    /**
     * Inner Class encargada de responder al evento dado al clickear el boton Semana anterior.
     * Al activarse retrocede una semana y recarga el calendario actual.
     */
    private class EventoSemanaAnterior implements ActionListener {

        /**
         * Metodo llamado al presionar el boton Semana anterior.
         * Retrocede una semana y actualiza la vista segun el filtro activo.
         * @param e evento generado por el boton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            lunesActual = lunesActual.minusWeeks(1);

            if (calendarioGeneral) {
                buscarTodasReservas();
            } else if (filtrandoPorTutor) {
                buscarReservasTutores();
            } else {
                buscarReservasEstudiantes();
            }

            cargarSemana();
        }
    }

    /**
     * Inner Class encargada de responder al evento dado al clickear el boton Semana siguiente.
     * Al activarse avanza una semana y recarga el calendario actual.
     */
    private class EventoSemanaSiguiente implements ActionListener {

        /**
         * Metodo llamado al presionar el boton Semana siguiente.
         * Avanza una semana y actualiza la vista segun el filtro activo.
         * @param e evento generado por el boton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            lunesActual = lunesActual.plusWeeks(1);

            if (calendarioGeneral) {
                buscarTodasReservas();
            } else if (filtrandoPorTutor) {
                buscarReservasTutores();
            } else {
                buscarReservasEstudiantes();
            }

            cargarSemana();
        }
    }

    /**
     * Inner Class encargada de responder al evento dado al clickear el boton Ver todas.
     * Al activarse muestra el calendario general de reservas.
     */
    private class EventoVerTodas implements ActionListener {

        /**
         * Metodo llamado al presionar el boton Ver todas.
         * Desactiva los filtros y carga el calendario general.
         * @param e evento generado por el boton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            calendarioGeneral = true;
            buscarTodasReservas();
            cargarSemana();
        }
    }

    /**
     * Inner Class encargada de responder al evento dado al clickear el boton Confirmar tutor.
     * Al activarse filtra el calendario por el tutor seleccionado.
     */
    private class EventoConfirmarTutor implements ActionListener {

        /**
         * Metodo llamado al presionar el boton Confirmar tutor.
         * Activa el filtro por tutor y actualiza el calendario.
         * @param e evento generado por el boton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            calendarioGeneral = false;
            filtrandoPorTutor = true;
            buscarReservasTutores();
            cargarSemana();
        }
    }

    /**
     * Inner Class encargada de responder al evento dado al clickear el boton Confirmar estudiante.
     * Al activarse filtra el calendario por el estudiante seleccionado.
     */
    private class EventoConfirmarEstudiante implements ActionListener {

        /**
         * Metodo llamado al presionar el boton Confirmar estudiante.
         * Activa el filtro por estudiante y actualiza el calendario.
         * @param e evento generado por el boton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            calendarioGeneral = false;
            filtrandoPorTutor = false;
            buscarReservasEstudiantes();
            cargarSemana();
        }
    }

    /**
     * Define la logica de los botones de navegacion y filtros.
     */
    private void cargarAcciones() {
        botonSemanaAnterior.addActionListener(new EventoSemanaAnterior());
        botonSemanaSiguiente.addActionListener(new EventoSemanaSiguiente());
        botonVerTodas.addActionListener(new EventoVerTodas());
        botonConfirmarTutor.addActionListener(new EventoConfirmarTutor());
        botonConfirmarEstudiante.addActionListener(new EventoConfirmarEstudiante());
    }

    /**
     * Filtra las reservas generales dentro de la semana actual.
     */
    private void buscarTodasReservas() {

        reservasFiltradas.clear();

        labelCalendarioDe.setText("Calendario general");

        LocalDate inicio = lunesActual;
        LocalDate fin = lunesActual.plusDays(6);

        for (Reserva r : controladorSistema.getReservas()) {

            LocalDate fecha = r.getFecha();

            if ((fecha.isEqual(inicio) || fecha.isAfter(inicio)) &&
                    (fecha.isEqual(fin) || fecha.isBefore(fin)) &&
                    !r.getEstadoReserva().equals("CANCELADA")) {

                reservasFiltradas.add(r);
            }
        }
    }

    /**
     * Filtra las reservas por estudiante.
     */
    private void buscarReservasEstudiantes() {

        reservasFiltradas.clear();

        String nombreEstudiante = (String) comboBoxEstudiantes.getSelectedItem();
        if (nombreEstudiante == null) return;

        Estudiante estudiante = null;

        for (Estudiante e : controladorSistema.getEstudiantes()) {
            if (e.toString().equals(nombreEstudiante)) {
                estudiante = e;
                break;
            }
        }

        if (estudiante == null) return;

        labelCalendarioDe.setText("Calendario de estudiante: " + estudiante.toString());

        LocalDate inicio = lunesActual;
        LocalDate fin = lunesActual.plusDays(6);

        for (Reserva r : controladorSistema.getReservas()) {

            if (!r.getEstudiante().equals(estudiante)) continue;

            LocalDate fecha = r.getFecha();

            if ((fecha.isEqual(inicio) || fecha.isAfter(inicio)) &&
                    (fecha.isEqual(fin) || fecha.isBefore(fin)) &&
                    !r.getEstadoReserva().equals("CANCELADA")) {

                reservasFiltradas.add(r);
            }
        }
    }

    /**
     * Filtra las reservas por tutor.
     */
    private void buscarReservasTutores() {

        reservasFiltradas.clear();

        String nombreTutor = (String) comboBoxTutores.getSelectedItem();
        if (nombreTutor == null) return;

        Tutor tutor = null;

        for (Tutor t : controladorSistema.getTutores()) {
            if (t.toString().equals(nombreTutor)) {
                tutor = t;
                break;
            }
        }

        if (tutor == null) return;

        labelCalendarioDe.setText("Calendario de tutor: " + tutor.toString());

        LocalDate inicio = lunesActual;
        LocalDate fin = lunesActual.plusDays(6);

        for (Reserva r : controladorSistema.getReservas()) {

            if (!r.getTutor().equals(tutor)) continue;

            LocalDate fecha = r.getFecha();

            if ((fecha.isEqual(inicio) || fecha.isAfter(inicio)) &&
                    (fecha.isEqual(fin) || fecha.isBefore(fin))  &&
                    !r.getEstadoReserva().equals("CANCELADA")) {

                reservasFiltradas.add(r);
            }
        }
    }

    /**
     * Ordena las reservas por fecha y hora, y actualiza la tabla.
     */
    private void cargarSemana() {
        reservasFiltradas.sort(Comparator.comparing(Reserva::getFecha)
                .thenComparing(Reserva::getHoraInicio));
        tablaModelo.setRowCount(0);

        LocalDate dia = lunesActual;

        for (int i = 0; i < 7; i++) {

            String nombreDia = dia.getDayOfWeek()
                    .getDisplayName(TextStyle.FULL, new Locale("es", "ES"));

            boolean tieneReservas = false;

            for (Reserva r : reservasFiltradas) {

                if (r.getFecha().equals(dia)) {

                    tablaModelo.addRow(new Object[]{
                            conMayuscula(nombreDia),
                            String.valueOf(dia.getDayOfMonth()),
                            r.getHoraInicio(),
                            r.getHoraFin(),
                            r.getTutor(),
                            r.getEstudiante(),
                            r.getMateriaTutor().getNombreMateria(),
                            r.getEstadoReserva()
                    });

                    tieneReservas = true;
                }
            }

            if (!tieneReservas) {
                tablaModelo.addRow(new Object[]{
                        conMayuscula(nombreDia),
                        String.valueOf(dia.getDayOfMonth()),
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""
                });
            }

            dia = dia.plusDays(1);
        }

        actualizarMes();
    }

    /**
     * Actualiza el label del mes.
     */
    private void actualizarMes() {
        LocalDate domingo = lunesActual.plusDays(6);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("es", "ES"));

        String inicio = lunesActual.format(fmt);
        String fin = domingo.format(fmt);

        if (inicio.equals(fin)) {
            labelMes.setText(conMayuscula(inicio));
        } else {
            labelMes.setText(conMayuscula(inicio + " - " + fin));
        }
    }

    /**
     * Formatea el texto para que la primera letra sea mayuscula.
     */
    private String conMayuscula(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }

    /**
     * Actualiza los datos visibles del calendario
     * Refresca los combobox de tutores y estudiantes, vuelve a filtrar las reservas
     * y recarga la tabla de la semana actual
     */
    public void actualizarCalendario() {
        actualizarCombos();

        if (calendarioGeneral) {
            buscarTodasReservas();
        } else if (filtrandoPorTutor) {
            buscarReservasTutores();
        } else {
            buscarReservasEstudiantes();
        }

        cargarSemana();
    }

    /**
     * Actualiza los combobox de tutores y estudiantes con los datos actuales
     * registrados en el controlador
     */
    private void actualizarCombos() {
        Object tutorSeleccionado = comboBoxTutores.getSelectedItem();
        Object estudianteSeleccionado = comboBoxEstudiantes.getSelectedItem();

        comboBoxTutores.removeAllItems();
        for (Tutor tutor : controladorSistema.getTutores()) {
            comboBoxTutores.addItem(tutor.toString());
        }

        comboBoxEstudiantes.removeAllItems();
        for (Estudiante estudiante : controladorSistema.getEstudiantes()) {
            comboBoxEstudiantes.addItem(estudiante.toString());
        }

        if (tutorSeleccionado != null) {
            comboBoxTutores.setSelectedItem(tutorSeleccionado);
        }

        if (estudianteSeleccionado != null) {
            comboBoxEstudiantes.setSelectedItem(estudianteSeleccionado);
        }
    }
}