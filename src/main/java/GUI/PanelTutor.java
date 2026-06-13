package GUI;

import javax.swing.*;
import java.awt.*;

public class PanelTutor extends JPanel {

    public PanelTutor() {

        setBorder(BorderFactory.createTitledBorder("Tutores"));
        setLayout(new GridLayout(0,1,10,10));

        add(new JLabel("Nombre"));
        add(new JTextField());

        add(new JLabel("Ramo"));
        add(new JTextField());

        add(new JButton("Agregar"));
        add(new JButton("Buscar"));
        add(new JButton("Eliminar"));
    }

}
