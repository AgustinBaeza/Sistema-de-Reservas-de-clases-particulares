package GUI;

import javax.swing.*;
import java.awt.*;

public class PanelEstudiante extends JPanel {

    public PanelEstudiante() {

        setBorder(BorderFactory.createTitledBorder("Estudiantes"));
        setLayout(new GridLayout(0,1,10,10));

        add(new JLabel("Nombre"));
        add(new JTextField());

        add(new JLabel("Carrera"));
        add(new JTextField());

        add(new JLabel("Rut"));
        add(new JTextField());

        add(new JButton("Agregar"));
        add(new JButton("Buscar"));
        add(new JButton("Eliminar"));
    }

}
