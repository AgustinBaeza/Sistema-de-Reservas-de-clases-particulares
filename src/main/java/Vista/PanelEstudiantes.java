package Vista;

import Controlador.SistemaReservasControlador;
import Logica.estudiante.Estudiante;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Panel visual encargado de gestionar estudiantes
 * Permite crear, editar, buscar y ver estudiantes registrados
 */
public class PanelEstudiantes extends JPanel {

    private SistemaReservasControlador controlador;
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JTextField txtTelefono;
    private JTable tablaEstudiantes;
    private DefaultTableModel modeloTabla;

    /**
     * Constructor del panel de estudiantes
     * @param controlador controlador principal del sistema
     */
    public PanelEstudiantes(SistemaReservasControlador controlador) {
        this.controlador = controlador;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(crearFormulario(), BorderLayout.NORTH);
        add(crearTabla(), BorderLayout.CENTER);

        cargarTabla();
    }

    /**
     * Crea el formulario superior para ingresar datos de estudiantes
     * @return panel con campos y botones de accion
     */
    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Gestionar estudiantes"));
        JPanel campos = new JPanel(new GridLayout(4, 2, 10, 10));

        txtId = new JTextField();
        txtId.setEditable(false);
        txtNombre = new JTextField();
        txtCorreo = new JTextField();
        txtTelefono = new JTextField();

        campos.add(new JLabel("ID"));
        campos.add(txtId);
        campos.add(new JLabel("Nombre"));
        campos.add(txtNombre);
        campos.add(new JLabel("Correo"));
        campos.add(txtCorreo);
        campos.add(new JLabel("Telefono"));
        campos.add(txtTelefono);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnBuscar = new JButton("Buscar por ID");
        JButton btnLimpiar = new JButton("Limpiar");

        btnAgregar.addActionListener(new EventoAgregarEstudiante());
        btnEditar.addActionListener(new EventoEditarEstudiante());
        btnBuscar.addActionListener(new EventoBuscarEstudiante());
        btnLimpiar.addActionListener(new EventoLimpiarFormulario());

        botones.add(btnAgregar);
        botones.add(btnEditar);
        botones.add(btnBuscar);
        botones.add(btnLimpiar);

        panel.add(campos, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Inner Class encargada de responder al evento dado al clickear el boton Agregar.
     * Al activarse solicita agregar un estudiante con los datos ingresados.
     */
    private class EventoAgregarEstudiante implements ActionListener {

        /**
         * Metodo llamado al presionar el boton Agregar.
         * Ejecuta el proceso de creacion de estudiante.
         * @param e evento generado por el boton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            agregarEstudiante();
        }
    }

    /**
     * Inner Class encargada de responder al evento dado al clickear el boton Editar.
     * Al activarse solicita editar el estudiante seleccionado o buscado.
     */
    private class EventoEditarEstudiante implements ActionListener {

        /**
         * Metodo llamado al presionar el boton Editar.
         * Ejecuta el proceso de edicion de estudiante.
         * @param e evento generado por el boton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            editarEstudiante();
        }
    }

    /**
     * Inner Class encargada de responder al evento dado al clickear el boton Buscar por ID.
     * Al activarse solicita buscar un estudiante registrado mediante su ID.
     */
    private class EventoBuscarEstudiante implements ActionListener {

        /**
         * Metodo llamado al presionar el boton Buscar por ID.
         * Ejecuta la busqueda de estudiante por identificador.
         * @param e evento generado por el boton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            buscarEstudiante();
        }
    }

    /**
     * Inner Class encargada de responder al evento dado al clickear el boton Limpiar.
     * Al activarse limpia los campos del formulario de estudiantes.
     */
    private class EventoLimpiarFormulario implements ActionListener {

        /**
         * Metodo llamado al presionar el boton Limpiar.
         * Limpia los campos del formulario.
         * @param e evento generado por el boton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            limpiarCampos();
        }
    }

    /**
     * Inner Class encargada de responder al evento dado al seleccionar una fila de la tabla de estudiantes.
     * Al activarse carga los datos del estudiante seleccionado en el formulario.
     */
    private class EventoSeleccionEstudiante implements ListSelectionListener {

        /**
         * Metodo llamado al cambiar la seleccion de la tabla de estudiantes.
         * Carga los datos de la fila seleccionada en el formulario.
         * @param e evento generado por la seleccion de la tabla
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                cargarDatosSeleccionados();
            }
        }
    }

    /**
     * Crea la tabla donde se muestran los estudiantes registrados
     * @return scroll con tabla de estudiantes
     */
    private JScrollPane crearTabla() {
        String[] columnas = {"ID", "Nombre", "Correo", "Telefono"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaEstudiantes = new JTable(modeloTabla);
        tablaEstudiantes.setRowHeight(25);
        tablaEstudiantes.getTableHeader().setReorderingAllowed(false);
        tablaEstudiantes.getSelectionModel().addListSelectionListener(new EventoSeleccionEstudiante());
        return new JScrollPane(tablaEstudiantes);
    }

    /**
     * Agrega un nuevo estudiante usando los datos ingresados en el formulario
     */
    private void agregarEstudiante() {
        try {
            controlador.crearEstudiante(txtNombre.getText(), txtCorreo.getText(), txtTelefono.getText());
            controlador.guardarDatos();
            cargarTabla();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Estudiante agregado correctamente");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Edita los datos de un estudiante existente.
     */
    private void editarEstudiante() {
        try {
            if (txtId.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar o buscar un estudiante");
                return;
            }

            int id = Integer.parseInt(txtId.getText());

            boolean editado = controlador.editarEstudiante(id, txtNombre.getText(), txtCorreo.getText(), txtTelefono.getText());

            if (editado) {
                controlador.guardarDatos();
                cargarTabla();
                limpiarCampos();

                JOptionPane.showMessageDialog(this, "Estudiante editado correctamente");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontro el estudiante");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Busca un estudiante por ID
     */
    private void buscarEstudiante() {
        try {
            String idTexto = JOptionPane.showInputDialog(this, "Ingrese ID del estudiante:");

            if (idTexto == null || idTexto.isBlank()) {
                return;
            }

            int id = Integer.parseInt(idTexto);
            Estudiante estudiante = controlador.buscarEstudiantePorId(id);

            if (estudiante == null) {
                JOptionPane.showMessageDialog(this, "No se encontro el estudiante");
                return;
            }

            mostrarEstudianteEnFormulario(estudiante);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser numerico", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga en el formulario el estudiante seleccionado en la tabla
     */
    private void cargarDatosSeleccionados() {
        int fila = tablaEstudiantes.getSelectedRow();

        if (fila == -1) {
            return;
        }

        txtId.setText(modeloTabla.getValueAt(fila, 0).toString());
        txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtCorreo.setText(modeloTabla.getValueAt(fila, 2).toString());
        txtTelefono.setText(modeloTabla.getValueAt(fila, 3).toString());
    }

    /**
     * Muestra un estudiante en los campos del formulario
     * @param estudiante estudiante que se desea mostrar
     */
    private void mostrarEstudianteEnFormulario(Estudiante estudiante) {
        txtId.setText(String.valueOf(estudiante.getId()));
        txtNombre.setText(estudiante.getNombre());
        txtCorreo.setText(estudiante.getCorreo());
        txtTelefono.setText(estudiante.getTelefono());
    }

    /**
     * Actualiza la tabla con los estudiantes registrados en el sistema
     */
    private void cargarTabla() {
        modeloTabla.setRowCount(0);

        for (Estudiante estudiante : controlador.getEstudiantes()) {
            modeloTabla.addRow(new Object[]{estudiante.getId(), estudiante.getNombre(), estudiante.getCorreo(), estudiante.getTelefono()});
        }
    }

    /**
     * Limpia los campos del formulario
     */
    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        tablaEstudiantes.clearSelection();
    }
}