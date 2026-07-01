package Vista;

import javax.swing.*;
import java.awt.*;

public class PanelEstudiantes extends JPanel {

    public PanelEstudiantes() {

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