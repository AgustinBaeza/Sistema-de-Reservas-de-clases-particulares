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
import java.util.Locale;

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

    private void ajustarColumnas() {
        setColumna(0, 90, 90);
        setColumna(1, 47, 47);
        setColumna(2, 50, 50);
        setColumna(3, 50, 50);
        setColumna(4, 170, 230);
        setColumna(5, 170, 230);
        setColumna(6, 170, 230);
        setColumna(7, 100, 120);

        DefaultTableCellRenderer letrasEnElCentro = new DefaultTableCellRenderer();
        letrasEnElCentro.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(letrasEnElCentro);
        }
    }

    private void setColumna(int indice, int anchoMin, int anchoMax) {
        TableColumn column = tabla.getColumnModel().getColumn(indice);
        column.setMinWidth(anchoMin);
        column.setMaxWidth(anchoMax);
        column.setPreferredWidth(anchoMin);
    }

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

    private void cargarAcciones() {
        botonSemanaAnterior.addActionListener(e -> {
            lunesActual = lunesActual.minusWeeks(1);

            if (calendarioGeneral) {
                buscarTodasReservas();
            } else if (filtrandoPorTutor) {
                buscarReservasTutores();
            } else {
                buscarReservasEstudiantes();
            }

            cargarSemana();
        });

        botonSemanaSiguiente.addActionListener(e -> {
            lunesActual = lunesActual.plusWeeks(1);

            if (calendarioGeneral) {
                buscarTodasReservas();
            } else if (filtrandoPorTutor) {
                buscarReservasTutores();
            } else {
                buscarReservasEstudiantes();
            }

            cargarSemana();
        });

        botonVerTodas.addActionListener(e -> {
            calendarioGeneral = true;
            buscarTodasReservas();
            cargarSemana();
        });

        botonConfirmarTutor.addActionListener(e -> {
            calendarioGeneral = false;
            filtrandoPorTutor = true;
            buscarReservasTutores();
            cargarSemana();
        });

        botonConfirmarEstudiante.addActionListener(e -> {
            calendarioGeneral = false;
            filtrandoPorTutor = false;
            buscarReservasEstudiantes();
            cargarSemana();
        });
    }

    private void buscarTodasReservas() {

        reservasFiltradas.clear();

        labelCalendarioDe.setText("Calendario general");

        LocalDate inicio = lunesActual;
        LocalDate fin = lunesActual.plusDays(6);

        for (Reserva r : controladorSistema.getReservas()) {

            LocalDate fecha = r.getFecha();

            if ((fecha.isEqual(inicio) || fecha.isAfter(inicio)) &&
                    (fecha.isEqual(fin) || fecha.isBefore(fin))) {

                reservasFiltradas.add(r);
            }
        }
    }

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
                    (fecha.isEqual(fin) || fecha.isBefore(fin))) {

                reservasFiltradas.add(r);
            }
        }
    }

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
                    (fecha.isEqual(fin) || fecha.isBefore(fin))) {

                reservasFiltradas.add(r);
            }
        }
    }

    private void cargarSemana() {

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

    private String conMayuscula(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }
}