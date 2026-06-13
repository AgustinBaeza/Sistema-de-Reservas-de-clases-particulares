package GUI;

import javax.swing.*;

public class Ventana extends JFrame {

    public Ventana () {
        setTitle("Sistema de Gestion de tutorias ");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new PanelPrincipal());

        setLocationRelativeTo(null);
        setVisible(true);


    }

}
