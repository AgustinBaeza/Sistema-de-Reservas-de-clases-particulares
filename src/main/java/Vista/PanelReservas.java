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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JButton botonModificar;

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
    private JPanel panelTitulo() {
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
    private JPanel panelFormulario() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));

        panel.setPreferredSize(new Dimension(270, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Nueva reserva"));

        txtMateria = new JTextField();
        txtFecha = new JTextField();
        txtHoraInicio = new JTextField();
        txtHoraFin = new JTextField();

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
        botonBuscarTutores.addActionListener(new EventoBuscarTutores());
        botonLimpiar.addActionListener(new EventoLimpiarFormulario());
        botonCrearReserva.addActionListener(new EventoCrearReserva());
        botonConfirmar.addActionListener(new EventoConfirmarReserva());
        botonCancelar.addActionListener(new EventoCancelarReserva());
        botonModificar.addActionListener(new EventoModificarReserva());
    }

    /**
     * Inner Class encargada de responder al evento dado al clickear el boton Buscar tutores disponibles.
     * Al activarse solicita buscar tutores compatibles con la materia, fecha y horario ingresados.
     */
    private class EventoBuscarTutores implements ActionListener {

        /**
         * Metodo llamado al presionar el boton Buscar tutores disponibles.
         * Ejecuta la busqueda de tutores compatibles.
         * @param e evento generado por el boton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            buscarTutoresDisponibles();
        }
    }

    /**
     * Inner Class encargada de responder al evento dado al clickear el boton Limpiar formulario.
     * Al activarse limpia los campos del formulario de reservas.
     */
    private class EventoLimpiarFormulario implements ActionListener {

        /**
         * Metodo llamado al presionar el boton Limpiar formulario.
         * Limpia los datos ingresados en el formulario.
         * @param e evento generado por el boton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            limpiarFormulario();
        }
    }

    /**
     * Inner Class encargada de responder al evento dado al clickear el boton Crear Reserva.
     * Al activarse solicita crear una nueva reserva con los datos ingresados.
     */
    private class EventoCrearReserva implements ActionListener {

        /**
         * Metodo llamado al presionar el boton Crear Reserva.
         * Ejecuta el proceso de creacion de una reserva.
         * @param e evento generado por el boton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            crearReserva();
        }
    }

    /**
     * Inner Class encargada de responder al evento dado al clickear el boton Confirmar.
     * Al activarse solicita confirmar la reserva seleccionada.
     */
    private class EventoConfirmarReserva implements ActionListener {

        /**
         * Metodo llamado al presionar el boton Confirmar.
         * Ejecuta la confirmacion de la reserva seleccionada.
         * @param e evento generado por el boton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            confirmarReserva();
        }
    }

    /**
     * Inner Class encargada de responder al evento dado al clickear el boton Cancelar.
     * Al activarse solicita cancelar la reserva seleccionada.
     */
    private class EventoCancelarReserva implements ActionListener {

        /**
         * Metodo llamado al presionar el boton Cancelar.
         * Ejecuta la cancelacion de la reserva seleccionada.
         * @param e evento generado por el boton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            cancelarReserva();
        }
    }

    /**
     * Inner Class encargada de responder al evento dado al clickear el boton Modificar.
     * Al activarse abre el dialogo de modificacion para la reserva seleccionada.
     */
    private class EventoModificarReserva implements ActionListener {

        /**
         * Metodo llamado al presionar el boton Modificar.
         * Abre el dialogo que permite editar los datos de la reserva seleccionada.
         * @param e evento generado por el boton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            dialogoModificar();
        }
    }

    /**
     * Busca los tutores disponibles para la materia, fecha y horario ingresados.
     * Valida que la materia no este vacia y que la fecha y horas tengan un formato valido.
     * Si existen tutores compatibles, los carga en el combobox de seleccion junto con
     * los estudiantes registrados en el sistema.
     */
    private void buscarTutoresDisponibles() {
        String nombreMateria = txtMateria.getText().trim();
        if (nombreMateria.isBlank()) {
            mostrarAviso("Ingrese el nombre de la materia.", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDate fecha = LocalDate.parse(txtFecha.getText().trim());
            LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText().trim());
            LocalTime horaFin = LocalTime.parse(txtHoraFin.getText().trim());

            ArrayList<Tutor> disponibles = controladorSistema.buscarTutoresDisponibles(
                    nombreMateria, fecha, horaInicio, horaFin);

            comboBoxTutores.removeAllItems();

            if (disponibles.isEmpty()) {
                mostrarAviso("No hay tutores disponibles para ese horario y materia.", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (Tutor t : disponibles) {
                comboBoxTutores.addItem(t);
            }

            cargarEstudiantes();

        } catch (DateTimeParseException ex) {
            mostrarAviso("Formato incorrecto.\nFecha: AAAA-MM-DD  Hora: HH:MM", JOptionPane.ERROR_MESSAGE);
        } catch (FechaHoraInvalidaException ex) {
            mostrarAviso(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Construye el panel que muestra la tabla de reservas registradas junto
     * con los botones para confirmar o cancelar una reserva.
     *
     * @return panel que contiene la tabla de reservas y sus acciones.
     */
    private JPanel panelTabla() {
        JPanel panel = new JPanel(new BorderLayout(0, 9));
        panel.setBorder(BorderFactory.createTitledBorder("Reservas registradas"));

        String[] columnas = {"#", "Tutor", "Tarifa", "Estudiante", "Materia", "Fecha", "Inicio", "Fin", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int col) {
                return false;
            }
        };

        tablaReservas = new JTable(modeloTabla);
        tablaReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaReservas.getTableHeader().setReorderingAllowed(false);
        tablaReservas.getTableHeader().setResizingAllowed(false);

        tablaReservas.setColumnSelectionAllowed(false);
        tablaReservas.setCellSelectionEnabled(false);
        tablaReservas.getColumnModel().getColumn(0).setMaxWidth(30);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botonConfirmar = new JButton("Confirmar");
        botonCancelar = new JButton("Cancelar");
        botonModificar = new JButton("Modificar");

        panelBotones.add(botonModificar);
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
    private void confirmarReserva() {
        Reserva reserva = getReservaSeleccionada();
        if (reserva == null) {
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea confirmar esta reserva?",
                "Confirmar reserva",
                JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            try {
                controladorSistema.confirmarReserva(reserva);
                controladorSistema.guardarDatos();
                actualizarTabla();
                mostrarAviso("Reserva confirmada!", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                mostrarAviso(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Cancela la reserva seleccionada en la tabla.
     * Solicita confirmacion al usuario antes de realizar la operacion y
     * actualiza la tabla una vez cancelada.
     */
    private void cancelarReserva() {
        Reserva reserva = getReservaSeleccionada();
        if (reserva == null) {
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea cancelar esta reserva?",
                "Cancelar reserva",
                JOptionPane.YES_NO_OPTION);

        if (respuesta == JOptionPane.YES_OPTION) {
            try {
                controladorSistema.cancelarReserva(reserva);
                controladorSistema.guardarDatos();
                actualizarTabla();
                mostrarAviso("Reserva cancelada!", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                mostrarAviso(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    /**
     * Crea una nueva reserva utilizando los datos ingresados en el formulario.
     * Valida la existencia del tutor, estudiante, materia y el formato de la
     * fecha y horario antes de solicitar la creacion al controlador.
     */
    private void crearReserva() {
        Tutor tutor = (Tutor) comboBoxTutores.getSelectedItem();
        Estudiante estudiante = (Estudiante) comboBoxEstudiantes.getSelectedItem();

        if (tutor == null || estudiante == null) {
            mostrarAviso("Busque primero un tutor y seleccione un estudiante", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDate fecha = LocalDate.parse(txtFecha.getText().trim());
            LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText().trim());
            LocalTime horaFin = LocalTime.parse(txtHoraFin.getText().trim());
            String nombreMateria = txtMateria.getText().trim();

            MateriaTutor materia = tutor.buscarMateria(nombreMateria);
            if (materia == null) {
                mostrarAviso("El tutor seleccionado no imparte esa materia.", JOptionPane.ERROR_MESSAGE);
                return;
            }

            controladorSistema.crearReserva(tutor, estudiante, materia.getNombreMateria(), fecha, horaInicio, horaFin);
            controladorSistema.guardarDatos();

            mostrarAviso("Reserva creada correctamente.", JOptionPane.INFORMATION_MESSAGE);
            actualizarTabla();
            limpiarFormulario();

        } catch (DateTimeParseException ex) {
            mostrarAviso("Formato incorrecto.\nFecha: AAAA-MM-DD   Hora: HH:MM", JOptionPane.ERROR_MESSAGE);
        } catch (FechaHoraInvalidaException ex) {
            mostrarAviso(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        } catch (ConflictoHorarioException ex) {
            mostrarAviso("Existe un conflicto en el horario seleccionado", JOptionPane.ERROR_MESSAGE);
        } catch (CupoMaximoExcedidoException ex) {
            mostrarAviso("No quedan mas cupos para la materia seleccionada", JOptionPane.ERROR_MESSAGE);
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
     * Abre un dialogo con el formulario de modificacion de la reserva seleccionada.
     * Los campos se prerellenan con los valores actuales.
     * Permite cambiar tutor, estudiante, materia, fecha y horario.
     */
    private void dialogoModificar() {
        Reserva reserva = getReservaSeleccionada();
        if (reserva == null) {
            return;
        }

        if (reserva.getEstadoReserva().equals("CANCELADA")) {
            mostrarAviso("No se puede modificar una reserva cancelada.", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialogo = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Modificar reserva", true);
        dialogo.setLayout(new BorderLayout(10, 10));
        dialogo.setSize(400, 500);
        dialogo.setLocationRelativeTo(this);

        JPanel formulario = new JPanel(new GridLayout(0, 1, 5, 5));
        formulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        JTextField modMateria = new JTextField(reserva.getMateriaTutor().getNombreMateria());
        JTextField modFecha = new JTextField(reserva.getFecha().toString());
        JTextField modHoraInicio = new JTextField(reserva.getHoraInicio().toString());
        JTextField modHoraFin = new JTextField(reserva.getHoraFin().toString());
        JComboBox<Tutor> modTutores = new JComboBox<>();
        JComboBox<Estudiante> modEstudiantes = new JComboBox<>();
        JButton botonBuscar = new JButton("Buscar tutores disponibles");

        for (Estudiante e : controladorSistema.getEstudiantes()) {
            modEstudiantes.addItem(e);
            if (e.getId() == reserva.getEstudiante().getId()) {
                modEstudiantes.setSelectedItem(e);
            }
        }

        formulario.add(new JLabel("Materia:"));
        formulario.add(modMateria);
        formulario.add(new JLabel("Fecha (AAAA-MM-DD):"));
        formulario.add(modFecha);
        formulario.add(new JLabel("Hora inicio (HH:MM):"));
        formulario.add(modHoraInicio);
        formulario.add(new JLabel("Hora fin (HH:MM):"));
        formulario.add(modHoraFin);
        formulario.add(botonBuscar);
        formulario.add(new JLabel("Tutor disponible:"));
        formulario.add(modTutores);
        formulario.add(new JLabel("Estudiante:"));
        formulario.add(modEstudiantes);

        precargarTutoresEnDialogo(modTutores, modMateria, modFecha,
                modHoraInicio, modHoraFin, reserva.getTutor());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        JButton botonGuardar = new JButton("Guardar cambios");
        JButton botonCancelarD = new JButton("Cancelar");

        panelBotones.add(botonGuardar);
        panelBotones.add(botonCancelarD);

        /**
         * Inner Class encargada de responder al evento dado al clickear el boton Buscar tutores disponibles
         * dentro del dialogo de modificacion.
         */
        class EventoBuscarTutoresDialogo implements ActionListener {

            /**
             * Metodo llamado al presionar el boton Buscar tutores disponibles.
             * Actualiza el combobox de tutores segun la materia, fecha y horario ingresados.
             * @param e evento generado por el boton
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                precargarTutoresEnDialogo(modTutores, modMateria, modFecha,
                        modHoraInicio, modHoraFin, null);
            }
        }

        /**
         * Inner Class encargada de responder al evento dado al clickear el boton Cancelar
         * dentro del dialogo de modificacion.
         */
        class EventoCancelarDialogo implements ActionListener {

            /**
             * Metodo llamado al presionar el boton Cancelar.
             * Cierra el dialogo sin realizar cambios sobre la reserva.
             * @param e evento generado por el boton
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogo.dispose();
            }
        }

        /**
         * Inner Class encargada de responder al evento dado al clickear el boton Guardar cambios
         * dentro del dialogo de modificacion.
         */
        class EventoGuardarCambiosDialogo implements ActionListener {

            /**
             * Metodo llamado al presionar el boton Guardar cambios.
             * Valida los datos ingresados y solicita al controlador modificar la reserva.
             * @param e evento generado por el boton
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                Tutor nuevoTutor = (Tutor) modTutores.getSelectedItem();
                Estudiante nuevoEstudiante = (Estudiante) modEstudiantes.getSelectedItem();

                if (nuevoTutor == null || nuevoEstudiante == null) {
                    mostrarAviso("Busque tutores disponibles antes de guardar.", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                try {
                    String nombreMateria = modMateria.getText().trim();

                    if (nombreMateria.isBlank()) {
                        throw new CampoVacioException("Debe ingresar una materia");
                    }

                    if (nuevoTutor.buscarMateria(nombreMateria) == null) {
                        throw new MateriaNoEncontradaException("El tutor seleccionado no imparte esa materia");
                    }

                    LocalDate fecha = LocalDate.parse(modFecha.getText().trim());
                    LocalTime horaInicio = LocalTime.parse(modHoraInicio.getText().trim());
                    LocalTime horaFin = LocalTime.parse(modHoraFin.getText().trim());

                    controladorSistema.modificarReserva(reserva, nuevoTutor, nuevoEstudiante,
                            nombreMateria, fecha, horaInicio, horaFin);

                    controladorSistema.guardarDatos();

                    mostrarAviso("Reserva modificada correctamente.", JOptionPane.INFORMATION_MESSAGE);
                    actualizarTabla();
                    dialogo.dispose();

                } catch (DateTimeParseException ex) {
                    mostrarAviso("Formato incorrecto.\nFecha: AAAA-MM-DD  Hora: HH:MM", JOptionPane.ERROR_MESSAGE);
                } catch (FechaHoraInvalidaException ex) {
                    mostrarAviso(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                } catch (CampoVacioException ex) {
                    mostrarAviso(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                } catch (MateriaNoEncontradaException ex) {
                    mostrarAviso(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                } catch (ConflictoHorarioException ex) {
                    mostrarAviso("Existe un conflicto en el horario seleccionado", JOptionPane.ERROR_MESSAGE);
                } catch (CupoMaximoExcedidoException ex) {
                    mostrarAviso("No quedan mas cupos para la materia seleccionada", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    mostrarAviso(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        botonBuscar.addActionListener(new EventoBuscarTutoresDialogo());
        botonCancelarD.addActionListener(new EventoCancelarDialogo());
        botonGuardar.addActionListener(new EventoGuardarCambiosDialogo());

        dialogo.add(formulario, BorderLayout.CENTER);
        dialogo.add(panelBotones, BorderLayout.SOUTH);
        dialogo.setVisible(true);
    }

    /**
     * Carga el dialogo de modificar con los datos de la reserva actual,
     * intentando buscar tutores con los mismos requerimientos.
     * Si el tutor actual no aparece por el conflicto con su propia reserva,
     * se mantiene como opcion seleccionable siempre que imparta la materia indicada.
     * @param comboboxTutores combobox donde se cargaran los tutores encontrados
     * @param modMateria campo de texto con la materia
     * @param modFecha campo de texto con la fecha
     * @param modHoraInicio campo de texto con la hora de inicio
     * @param modHoraFin campo de texto con la hora de fin
     * @param tutorActual tutor a preseleccionar si esta disponible, null si no aplica
     */
    private void precargarTutoresEnDialogo(JComboBox<Tutor> comboboxTutores,
                                           JTextField modMateria, JTextField modFecha,
                                           JTextField modHoraInicio, JTextField modHoraFin,
                                           Tutor tutorActual) {
        try {
            String materia = modMateria.getText().trim();

            if (materia.isBlank()) {
                throw new CampoVacioException("Debe ingresar una materia");
            }

            LocalDate fecha = LocalDate.parse(modFecha.getText().trim());
            LocalTime inicio = LocalTime.parse(modHoraInicio.getText().trim());
            LocalTime fin = LocalTime.parse(modHoraFin.getText().trim());

            ArrayList<Tutor> disponibles = controladorSistema.buscarTutoresDisponibles(
                    materia, fecha, inicio, fin);

            comboboxTutores.removeAllItems();

            boolean tutorActualEncontrado = false;

            for (Tutor t : disponibles) {
                comboboxTutores.addItem(t);

                if (tutorActual != null && t.getId() == tutorActual.getId()) {
                    comboboxTutores.setSelectedItem(t);
                    tutorActualEncontrado = true;
                }
            }

            if (tutorActual != null && !tutorActualEncontrado && tutorActual.buscarMateria(materia) != null) {
                comboboxTutores.addItem(tutorActual);
                comboboxTutores.setSelectedItem(tutorActual);
            }

            if (comboboxTutores.getItemCount() == 0) {
                mostrarAviso("No hay tutores disponibles para ese horario y materia.", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (DateTimeParseException ex) {
            mostrarAviso("Formato incorrecto.\nFecha: AAAA-MM-DD  Hora: HH:MM", JOptionPane.ERROR_MESSAGE);
        } catch (CampoVacioException ex) {
            mostrarAviso(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            mostrarAviso(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
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
