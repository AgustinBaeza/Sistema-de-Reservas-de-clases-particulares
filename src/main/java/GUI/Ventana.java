package GUI;

import javax.swing.*;

public class Ventana extends JFrame {

    public Ventana () {
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new PanelPrincipal());

        setLocationRelativeTo(null);
        setVisible(true);


    }

}
