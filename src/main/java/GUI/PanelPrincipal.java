package GUI;

import javax.swing.*;
import java.awt.*;

public class PanelPrincipal extends JPanel {

    public PanelPrincipal() {

        setLayout(new BorderLayout());

        JPanel izquierda = new JPanel(new GridLayout(2,1));
        izquierda.setPreferredSize(new Dimension(300, 0));

        izquierda.add(new PanelEstudiante());
        izquierda.add(new PanelTutor());

        add(izquierda, BorderLayout.WEST);
        add(new PanelReservas(), BorderLayout.CENTER);
    }

}
