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


        // por ahora se añaden paneles temporales con el texto descriptivo "> panel --- que iria aca <"
        panelContenido.add(new PanelInicio(), PanelSistema.INICIO.getIdPanel());
        panelContenido.add(crearPanelTemporal("> panel de tutores que iria aca <"), PanelSistema.TUTORES.getIdPanel());
        panelContenido.add(crearPanelTemporal("> panel de estudiantes que iria aca <"), PanelSistema.ESTUDIANTES.getIdPanel());
        panelContenido.add(crearPanelTemporal("> panel de reservas que iria aca <"), PanelSistema.RESERVAS.getIdPanel());
        panelContenido.add(crearPanelTemporal("> panel de calendario que iria aca <"), PanelSistema.CALENDARIO.getIdPanel());

        add(panelMenu, BorderLayout.WEST);
        add(panelContenido, BorderLayout.CENTER);

        mostrarPanel(PanelSistema.INICIO);
    }

    // este metodo es SOLO TEMPORAL, una vez que se terminen los paneles hay que BORRARLO!!1111!11 pq es solo para poder ir testeando
    private JPanel crearPanelTemporal(String mensaje) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel lblMensaje = new JLabel(mensaje);
        lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 22));

        panel.add(lblMensaje, BorderLayout.CENTER);

        return panel;
    }
    //----------q no se olvide borrar :,v---------------

    /**
     * Metodo que cambia el panel visible dentro del CardLayout
     * Recibe el nombre identificador del panel directo de la enumeracion PanelSistema
     * y con ello solicita a CardLayout que muestre el panel correspondiente.
     * @param panelSistema panel del sistema que se desea mostrar en pantalla
     */
    public void mostrarPanel(PanelSistema panelSistema) {
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