package Vista;

import javax.swing.*;
import java.awt.*;

/**
 * Panel inicial de la aplicacion, se muestra al abrir el sistema y sirve como pantalla de bienvenida y punto de referencia
 * antes de que el administrador seleccione una funcionalidad especifica del sistema de reservas.
 */
public class PanelInicio extends JPanel {

    /**
     * Constructor del panel de inicio, construye una vista simple con texto "guia" de que hacer en el panel.
     */
    public PanelInicio() {
        setLayout(new BorderLayout());

        JLabel lblTextoCentral = new JLabel("Seleccione una opcion del menu lateral");
        lblTextoCentral.setHorizontalAlignment(SwingConstants.CENTER);
        lblTextoCentral.setFont(new Font("Arial", Font.BOLD, 24));

        add(lblTextoCentral, BorderLayout.CENTER);
    }
}