package Vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel lateral de navegacion de la aplicacion, contiene los botones principales que permiten cambiar entre las vistas
 * del sistema mediante el CardLayout administrado por PanelPrincipal.
 */
public class PanelMenu extends JPanel {

    private PanelPrincipal panelPrincipal;

    private JButton btnInicio;
    private JButton btnTutores;
    private JButton btnEstudiantes;
    private JButton btnReservas;
    private JButton btnCalendario;

    /**
     * Constructor del panel de menu, establece las dimensiones del panel,
     * con el diseño de GridLayout para distribuir los botones con un margen entre ellos y un margen exterior para el menu.
     * @param panelPrincipal panel principal sobre el cual se realizaran los cambios de vista
     */
    public PanelMenu(PanelPrincipal panelPrincipal) {
        this.panelPrincipal = panelPrincipal;

        setPreferredSize(new Dimension(220, 700));
        setLayout(new GridLayout(8, 1, 8, 8));
        setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        agregarBotones();
        agregarEventos();
    }

    /**
     * Metodo que crea los botones principales del menu lateral que permiten cambiar entre paneles
     * utilizado en el constructor del PanelMenu.
     */
    private void agregarBotones() {
        JLabel lblTitulo = new JLabel("Menu principal");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        btnInicio = new JButton("Inicio");
        btnTutores = new JButton("Tutores");
        btnEstudiantes = new JButton("Estudiantes");
        btnReservas = new JButton("Reservas");
        btnCalendario = new JButton("Calendario");

        add(lblTitulo);
        add(btnInicio);
        add(btnTutores);
        add(btnEstudiantes);
        add(btnReservas);
        add(btnCalendario);
    }

    /**
     * Metodo que asigna listeners a los botones para alternar entre los paneles del sistema
     * Reune todas las Inner Classes declaradas posteriormente asignandole la respectiva funcionalidad al boton correspondiente.
     * Metodo utilizado en constructor de PanelMenu.
     */
    private void agregarEventos() {
        btnInicio.addActionListener(new EventoInicio());
        btnTutores.addActionListener(new EventoTutores());
        btnEstudiantes.addActionListener(new EventoEstudiantes());
        btnReservas.addActionListener(new EventoReservas());
        btnCalendario.addActionListener(new EventoCalendario());
    }

    /**
     * Inner Class encargada de atender el evento asociado al boton Inicio
     * Al activarse solicita al PanelPrincipal mostrar el panel de inicio.
     */
    private class EventoInicio implements ActionListener {

        /**
         * Metodo llamado al presionar el boton Inicio
         * Solicita al PanelPrincipal mostrar el panel inicial del sistema.
         * @param e evento generado por el boton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            panelPrincipal.mostrarPanel(PanelSistema.INICIO);
        }
    }

    /**
     * Inner Class encargada de responder al evento dado al clickear el boton Tutores
     * Al activarse solicita al PanelPrincipal mostrar el panel de tutores.
     */
    private class EventoTutores implements ActionListener {

        /**
         * Metodo llamado al presionar el boton Tutores
         * Solicita al PanelPrincipal mostrar el panel correspondiente.
         * @param e evento generado por el boton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            panelPrincipal.mostrarPanel(PanelSistema.TUTORES);
        }
    }

    /**
     * Inner Class encargada de responder al evento dado al clickear el boton Estudiantes
     * Al activarse solicita al PanelPrincipal mostrar el panel de estudiantes.
     */
    private class EventoEstudiantes implements ActionListener {

        /**
         * Metodo llamado al presionar el boton Estudiantes
         * Solicita al PanelPrincipal mostrar el panel correspondiente.
         * @param e evento generado por el boton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            panelPrincipal.mostrarPanel(PanelSistema.ESTUDIANTES);
        }
    }

    /**
     * Inner Class encargada de responder al evento dado al clickear el boton Reservas
     * Al activarse solicita al PanelPrincipal mostrar el panel de reservas.
     */
    private class EventoReservas implements ActionListener {

        /**
         * Metodo llamado al presionar el boton Reservas
         * Solicita al PanelPrincipal mostrar el panel correspondiente.
         * @param e evento generado por el boton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            panelPrincipal.mostrarPanel(PanelSistema.RESERVAS);
        }
    }

    /**
     * Inner Class encargada de responder al evento dado al clickear el boton Calendario
     * Al activarse solicita al PanelPrincipal mostrar el panel de calendario.
     */
    private class EventoCalendario implements ActionListener {

        /**
         * Metodo llamado al presionar el boton Calendario
         * Solicita al PanelPrincipal mostrar el panel correspondiente.
         * @param e evento generado por el boton
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            panelPrincipal.mostrarPanel(PanelSistema.CALENDARIO);
        }
    }

}