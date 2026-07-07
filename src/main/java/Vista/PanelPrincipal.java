package Vista;

import Controlador.SistemaReservasControlador;

import javax.swing.*;
import java.awt.*;

/**
 * Panel principal de la interfaz grafica, organiza la estructura base del sistema,
 * uniendo el menu lateral junto a sus botones con el panel de contenido principal.
 * Utiliza CardLayout para permitir el cambio entre los distintos paneles del sistema sin necesidad de abrir nuevas ventanas
 */
public class PanelPrincipal extends JPanel {

    private SistemaReservasControlador controlador;
    private CardLayout cardLayout;
    private JPanel panelContenido;
    private PanelCalendario panelCalendario;

    /**
     * Constructor del panel principal, crea el controlador principal del sistema, inicializa el menu lateral
     * y configura el panel central donde se mostraran las distintas vistas
     */
    public PanelPrincipal() {
        this.controlador = new SistemaReservasControlador();

        setLayout(new BorderLayout());

        PanelMenu panelMenu = new PanelMenu(this);

        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);

        panelContenido.add(new PanelInicio(), PanelSistema.INICIO.getIdPanel());
        panelContenido.add(new PanelTutores(controlador), PanelSistema.TUTORES.getIdPanel());
        panelContenido.add(new PanelEstudiantes(controlador), PanelSistema.ESTUDIANTES.getIdPanel());
        panelContenido.add(new PanelReservas(controlador), PanelSistema.RESERVAS.getIdPanel());
        panelCalendario = new PanelCalendario(controlador);
        panelContenido.add(panelCalendario, PanelSistema.CALENDARIO.getIdPanel());

        add(panelMenu, BorderLayout.WEST);
        add(panelContenido, BorderLayout.CENTER);

        mostrarPanel(PanelSistema.INICIO);
    }

    /**
     * Metodo que cambia el panel visible dentro del CardLayout
     * Recibe el nombre identificador del panel directo de la enumeracion PanelSistema
     * y con eso solicita a CardLayout que muestre el panel correspondiente
     * @param panelSistema panel del sistema que se desea mostrar en pantalla
     */
    public void mostrarPanel(PanelSistema panelSistema) {
        if (panelSistema == PanelSistema.CALENDARIO) {
            panelCalendario.actualizarCalendario();
        }
        cardLayout.show(panelContenido, panelSistema.getIdPanel());
    }

    /**
     * Getter del controlador principal del programa
     * @return controlador principal del sistema
     */
    public SistemaReservasControlador getControlador() {
        return controlador;
    }
}