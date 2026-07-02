package Vista;

import Controlador.SistemaReservasControlador;

import javax.swing.*;
import java.awt.event.*;

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

        PanelPrincipal panelPrincipal = new PanelPrincipal();
        add(panelPrincipal);

        setTitle("Sistema de Reservas de Clases Particulares");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                panelPrincipal.getControlador().guardarDatos();
                dispose();
                System.exit(0);
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);

    }
}