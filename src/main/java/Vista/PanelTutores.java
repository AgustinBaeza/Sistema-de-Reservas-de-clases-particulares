package Vista;

import Controlador.SistemaReservasControlador;
import Logica.tutor.DisponibilidadTutor;
import Logica.tutor.MateriaTutor;
import Logica.tutor.Tutor;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Panel visual encargado de gestionar tutores
 * Permite crear y editar tutores, administrar sus materias y registrar
 * bloques de disponibilidad horaria
 */
public class PanelTutores extends JPanel {

    private SistemaReservasControlador controlador;
    private JTextField txtIdTutor;
    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JTextField txtTelefono;
    private JTextField txtMateria;
    private JTextField txtTarifa;
    private JTextField txtCupoMaximo;
    private JTextField txtFecha;
    private JTextField txtHoraInicio;
    private JTextField txtHoraFin;
    private JTable tablaTutores;
    private JTable tablaMaterias;
    private JTable tablaDisponibilidades;
    private DefaultTableModel modeloTutores;
    private DefaultTableModel modeloMaterias;
    private DefaultTableModel modeloDisponibilidades;

    /**
     * Constructor del panel de tutores
     * @param controlador controlador principal del sistema
     */
    public PanelTutores(SistemaReservasControlador controlador) {
        this.controlador = controlador;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(crearFormularioTutor(), BorderLayout.NORTH);
        add(crearTablaTutores(), BorderLayout.CENTER);
        add(crearPanelDetalleTutor(), BorderLayout.SOUTH);
        cargarTablaTutores();
    }

    /**
     * Crea el formulario superior para crear o editar tutores
     * @return panel con campos y botones del tutor
     */
    private JPanel crearFormularioTutor() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Gestionar tutores"));
        JPanel campos = new JPanel(new GridLayout(4, 2, 10, 10));

        txtIdTutor = new JTextField();
        txtIdTutor.setEditable(false);
        txtNombre = new JTextField();
        txtCorreo = new JTextField();
        txtTelefono = new JTextField();

        campos.add(new JLabel("ID"));
        campos.add(txtIdTutor);
        campos.add(new JLabel("Nombre"));
        campos.add(txtNombre);
        campos.add(new JLabel("Correo"));
        campos.add(txtCorreo);
        campos.add(new JLabel("Telefono"));
        campos.add(txtTelefono);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAgregar = new JButton("Agregar tutor");
        JButton btnEditar = new JButton("Editar tutor");
        JButton btnLimpiar = new JButton("Limpiar");

        btnAgregar.addActionListener(e -> agregarTutor());
        btnEditar.addActionListener(e -> editarTutor());
        btnLimpiar.addActionListener(e -> limpiarCamposTutor());

        botones.add(btnAgregar);
        botones.add(btnEditar);
        botones.add(btnLimpiar);

        panel.add(campos, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Crea la tabla principal de tutores
     * @return scroll con tabla de tutores
     */
    private JScrollPane crearTablaTutores() {
        String[] columnas = {"ID", "Nombre", "Correo", "Telefono"};

        modeloTutores = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaTutores = new JTable(modeloTutores);
        tablaTutores.setRowHeight(25);
        tablaTutores.getTableHeader().setReorderingAllowed(false);
        tablaTutores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarTutorSeleccionado();
            }
        });

        return new JScrollPane(tablaTutores);
    }

    /**
     * Crea el panel inferior con materias y disponibilidades del tutor seleccionado
     * @return panel con pestañas de materias y disponibilidades
     */
    private JTabbedPane crearPanelDetalleTutor() {
        JTabbedPane pestañas = new JTabbedPane();

        pestañas.setPreferredSize(new Dimension(900, 380));
        pestañas.addTab("Materias", crearPanelMaterias());
        pestañas.addTab("Disponibilidad", crearPanelDisponibilidades());

        return pestañas;
    }

    /**
     * Crea el panel para gestionar materias del tutor seleccionado
     * @return panel de materias
     */
    private JPanel crearPanelMaterias() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JPanel campos = new JPanel(new GridLayout(3, 2, 10, 10));

        txtMateria = new JTextField();
        txtTarifa = new JTextField();
        txtCupoMaximo = new JTextField();

        campos.add(new JLabel("Materia"));
        campos.add(txtMateria);
        campos.add(new JLabel("Tarifa"));
        campos.add(txtTarifa);
        campos.add(new JLabel("Cupo maximo"));
        campos.add(txtCupoMaximo);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAgregarMateria = new JButton("Agregar materia");
        JButton btnEditarMateria = new JButton("Editar materia");
        JButton btnLimpiarMateria = new JButton("Limpiar materia");

        btnAgregarMateria.addActionListener(e -> agregarMateria());
        btnEditarMateria.addActionListener(e -> editarMateria());
        btnLimpiarMateria.addActionListener(e -> limpiarCamposMateria());

        botones.add(btnAgregarMateria);
        botones.add(btnEditarMateria);
        botones.add(btnLimpiarMateria);

        String[] columnas = {"Materia", "Tarifa", "Cupo maximo"};

        modeloMaterias = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaMaterias = new JTable(modeloMaterias);
        tablaMaterias.setRowHeight(25);

        tablaMaterias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarMateriaSeleccionada();
            }
        });

        JPanel formulario = new JPanel(new BorderLayout(10, 10));
        formulario.add(campos, BorderLayout.CENTER);
        formulario.add(botones, BorderLayout.SOUTH);

        panel.add(formulario, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaMaterias), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Crea el panel para gestionar disponibilidades del tutor seleccionado
     * @return panel de disponibilidades
     */
    private JPanel crearPanelDisponibilidades() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JPanel campos = new JPanel(new GridLayout(3, 2, 10, 10));

        txtFecha = new JTextField();
        txtHoraInicio = new JTextField();
        txtHoraFin = new JTextField();

        campos.add(new JLabel("Fecha (AAAA-MM-DD)"));
        campos.add(txtFecha);
        campos.add(new JLabel("Hora inicio (HH:MM)"));
        campos.add(txtHoraInicio);
        campos.add(new JLabel("Hora fin (HH:MM)"));
        campos.add(txtHoraFin);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAgregarDisponibilidad = new JButton("Agregar disponibilidad");
        JButton btnEditarDisponibilidad = new JButton("Editar disponibilidad");
        JButton btnLimpiarDisponibilidad = new JButton("Limpiar disponibilidad");

        btnAgregarDisponibilidad.addActionListener(e -> agregarDisponibilidad());
        btnEditarDisponibilidad.addActionListener(e -> editarDisponibilidad());
        btnLimpiarDisponibilidad.addActionListener(e -> limpiarCamposDisponibilidad());

        botones.add(btnAgregarDisponibilidad);
        botones.add(btnEditarDisponibilidad);
        botones.add(btnLimpiarDisponibilidad);

        String[] columnas = {"Fecha", "Inicio", "Fin"};

        modeloDisponibilidades = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaDisponibilidades = new JTable(modeloDisponibilidades);
        tablaDisponibilidades.setRowHeight(25);
        tablaDisponibilidades.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDisponibilidadSeleccionada();
            }
        });

        JPanel formulario = new JPanel(new BorderLayout(10, 10));
        formulario.add(campos, BorderLayout.CENTER);
        formulario.add(botones, BorderLayout.SOUTH);
        panel.add(formulario, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaDisponibilidades), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Agrega un tutor nuevo al sistema
     */
    private void agregarTutor() {
        try {
            controlador.crearTutor(
                    txtNombre.getText(),
                    txtCorreo.getText(),
                    txtTelefono.getText()
            );

            controlador.guardarDatos();
            cargarTablaTutores();
            limpiarCamposTutor();

            JOptionPane.showMessageDialog(this, "Tutor agregado correctamente");

        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Edita los datos principales del tutor seleccionado
     */
    private void editarTutor() {
        try {
            if (txtIdTutor.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un tutor");
                return;
            }

            int idTutor = Integer.parseInt(txtIdTutor.getText());

            boolean editado = controlador.editarTutor(idTutor, txtNombre.getText(), txtCorreo.getText(), txtTelefono.getText());

            if (editado) {
                controlador.guardarDatos();
                cargarTablaTutores();
                JOptionPane.showMessageDialog(this, "Tutor editado correctamente");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontro el tutor");
            }

        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Agrega una materia al tutor seleccionado
     */
    private void agregarMateria() {
        try {
            Tutor tutor = obtenerTutorSeleccionado();

            if (tutor == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un tutor");
                return;
            }

            int tarifa = Integer.parseInt(txtTarifa.getText());
            int cupoMaximo = Integer.parseInt(txtCupoMaximo.getText());

            boolean agregada = controlador.agregarMateriaTutor(tutor.getId(), txtMateria.getText(), tarifa, cupoMaximo);

            if (agregada) {
                controlador.guardarDatos();
                cargarTablaMaterias(tutor);
                limpiarCamposMateria();
                JOptionPane.showMessageDialog(this, "Materia agregada correctamente");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo agregar la materia");
            }

        } catch (NumberFormatException e) {
            mostrarError("La tarifa y el cupo deben ser numeros");
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Edita una materia existente del tutor seleccionado
     */
    private void editarMateria() {
        try {
            Tutor tutor = obtenerTutorSeleccionado();
            int fila = tablaMaterias.getSelectedRow();

            if (tutor == null || fila == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un tutor y una materia");
                return;
            }

            String materiaActual = modeloMaterias.getValueAt(fila, 0).toString();
            int tarifa = Integer.parseInt(txtTarifa.getText());
            int cupoMaximo = Integer.parseInt(txtCupoMaximo.getText());

            boolean editada = controlador.editarMateriaTutor(tutor.getId(), materiaActual, txtMateria.getText(), tarifa, cupoMaximo);

            if (editada) {
                controlador.guardarDatos();
                cargarTablaMaterias(tutor);
                limpiarCamposMateria();
                JOptionPane.showMessageDialog(this, "Materia editada correctamente");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo editar la materia");
            }

        } catch (NumberFormatException e) {
            mostrarError("La tarifa y el cupo deben ser numeros");
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Agrega una disponibilidad al tutor seleccionado
     */
    private void agregarDisponibilidad() {
        try {
            Tutor tutor = obtenerTutorSeleccionado();

            if (tutor == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un tutor");
                return;
            }

            LocalDate fecha = LocalDate.parse(txtFecha.getText());
            LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText());
            LocalTime horaFin = LocalTime.parse(txtHoraFin.getText());

            boolean agregada = controlador.agregarDisponibilidadTutor(tutor.getId(), fecha, horaInicio, horaFin);

            if (agregada) {
                controlador.guardarDatos();
                cargarTablaDisponibilidades(tutor);
                limpiarCamposDisponibilidad();
                JOptionPane.showMessageDialog(this, "Disponibilidad agregada correctamente");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo agregar la disponibilidad");
            }

        } catch (Exception e) {
            mostrarError("Revise la fecha y las horas ingresadas");
        }
    }

    /**
     * Edita una disponibilidad existente del tutor seleccionado
     */
    private void editarDisponibilidad() {
        try {
            Tutor tutor = obtenerTutorSeleccionado();
            int fila = tablaDisponibilidades.getSelectedRow();

            if (tutor == null || fila == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar un tutor y una disponibilidad");
                return;
            }

            LocalDate fecha = LocalDate.parse(txtFecha.getText());
            LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText());
            LocalTime horaFin = LocalTime.parse(txtHoraFin.getText());

            boolean editada = controlador.editarDisponibilidadTutor(tutor.getId(), fila, fecha, horaInicio, horaFin);

            if (editada) {
                controlador.guardarDatos();
                cargarTablaDisponibilidades(tutor);
                limpiarCamposDisponibilidad();
                JOptionPane.showMessageDialog(this, "Disponibilidad editada correctamente");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo editar la disponibilidad");
            }

        } catch (Exception e) {
            mostrarError("Revise la fecha y las horas ingresadas");
        }
    }

    /**
     * Carga los datos del tutor seleccionado en la tabla principal
     */
    private void cargarTutorSeleccionado() {
        int fila = tablaTutores.getSelectedRow();

        if (fila == -1) {
            return;
        }

        txtIdTutor.setText(modeloTutores.getValueAt(fila, 0).toString());
        txtNombre.setText(modeloTutores.getValueAt(fila, 1).toString());
        txtCorreo.setText(modeloTutores.getValueAt(fila, 2).toString());
        txtTelefono.setText(modeloTutores.getValueAt(fila, 3).toString());

        Tutor tutor = obtenerTutorSeleccionado();
        cargarTablaMaterias(tutor);
        cargarTablaDisponibilidades(tutor);
    }

    /**
     * Carga la materia seleccionada en los campos del formulario
     */
    private void cargarMateriaSeleccionada() {
        int fila = tablaMaterias.getSelectedRow();

        if (fila == -1) {
            return;
        }

        txtMateria.setText(modeloMaterias.getValueAt(fila, 0).toString());
        txtTarifa.setText(modeloMaterias.getValueAt(fila, 1).toString());
        txtCupoMaximo.setText(modeloMaterias.getValueAt(fila, 2).toString());
    }

    /**
     * Carga la disponibilidad seleccionada en los campos del formulario
     */
    private void cargarDisponibilidadSeleccionada() {
        int fila = tablaDisponibilidades.getSelectedRow();

        if (fila == -1) {
            return;
        }

        txtFecha.setText(modeloDisponibilidades.getValueAt(fila, 0).toString());
        txtHoraInicio.setText(modeloDisponibilidades.getValueAt(fila, 1).toString());
        txtHoraFin.setText(modeloDisponibilidades.getValueAt(fila, 2).toString());
    }

    /**
     * Actualiza la tabla principal de tutores
     */
    private void cargarTablaTutores() {
        modeloTutores.setRowCount(0);

        for (Tutor tutor : controlador.getTutores()) {
            modeloTutores.addRow(new Object[]{tutor.getId(), tutor.getNombre(), tutor.getCorreo(), tutor.getTelefono()});
        }
    }

    /**
     * Actualiza la tabla de materias del tutor seleccionado
     * @param tutor tutor cuyas materias se mostraran
     */
    private void cargarTablaMaterias(Tutor tutor) {
        modeloMaterias.setRowCount(0);

        if (tutor == null) {
            return;
        }

        for (MateriaTutor materia : controlador.getMateriasTutor(tutor)) {
            modeloMaterias.addRow(new Object[]{materia.getNombreMateria(), materia.getTarifa(), materia.getCupoMaximo()});
        }
    }

    /**
     * Actualiza la tabla de disponibilidades del tutor seleccionado
     * @param tutor tutor cuyas disponibilidades se mostraran
     */
    private void cargarTablaDisponibilidades(Tutor tutor) {
        modeloDisponibilidades.setRowCount(0);

        if (tutor == null) {
            return;
        }

        for (DisponibilidadTutor disponibilidad : controlador.getDisponibilidadesTutor(tutor)) {
            modeloDisponibilidades.addRow(new Object[]{disponibilidad.getDia(), disponibilidad.getHoraInicio(), disponibilidad.getHoraFin()});
        }
    }

    /**
     * Obtiene el tutor actualmente seleccionado en el formulario
     * @return tutor seleccionado o null si no hay tutor seleccionado
     */
    private Tutor obtenerTutorSeleccionado() {
        if (txtIdTutor.getText().isBlank()) {
            return null;
        }

        int idTutor = Integer.parseInt(txtIdTutor.getText());
        return controlador.buscarTutorPorId(idTutor);
    }

    /**
     * Limpia los campos principales del tutor
     */
    private void limpiarCamposTutor() {
        txtIdTutor.setText("");
        txtNombre.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");

        limpiarCamposMateria();
        limpiarCamposDisponibilidad();

        modeloMaterias.setRowCount(0);
        modeloDisponibilidades.setRowCount(0);
        tablaTutores.clearSelection();
    }

    /**
     * Limpia los campos de materia
     */
    private void limpiarCamposMateria() {
        txtMateria.setText("");
        txtTarifa.setText("");
        txtCupoMaximo.setText("");
        tablaMaterias.clearSelection();
    }

    /**
     * Limpia los campos de disponibilidad
     */
    private void limpiarCamposDisponibilidad() {
        txtFecha.setText("");
        txtHoraInicio.setText("");
        txtHoraFin.setText("");
        tablaDisponibilidades.clearSelection();
    }

    /**
     * Muestra un mensaje de error
     * @param mensaje mensaje que se mostrara
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}