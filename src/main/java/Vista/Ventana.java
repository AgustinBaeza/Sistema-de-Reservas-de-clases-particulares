package Vista;

import javax.swing.*;

/**
 * Ventana principal de la aplicacion grafica.
 * Configura el tamaño, cierre de la ventana, posicion inicial y visibilidad.
 */
public class Ventana extends JFrame {

    /**
     * Constructor de la ventana principal.
     * Crea la ventana, agrega el PanelPrincipal y deja visible la interfaz grafica del programa.
     */
    public Ventana() {
        setTitle("Sistema de Reservas de Clases Particulares");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new PanelPrincipal());

        setLocationRelativeTo(null);
        setVisible(true);
    }
}