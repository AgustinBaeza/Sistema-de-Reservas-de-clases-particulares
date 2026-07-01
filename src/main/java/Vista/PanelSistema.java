package Vista;

/**
 * Enumeracion que representa los paneles principales del sistema.
 * Cada valor contiene el nombre identificador que utiliza CardLayout para reconocer los distintos paneles del GUI.
 */
public enum PanelSistema {

    INICIO("PANEL_INICIO"),
    TUTORES("PANEL_TUTORES"),
    ESTUDIANTES("PANEL_ESTUDIANTES"),
    RESERVAS("PANEL_RESERVAS"),
    CALENDARIO("PANEL_CALENDARIO");

    private String idPanel;

    /**
     * Constructor de la enumeracion, asocia a cada panel del sistema un nombre identificador que es utilizado por CardLayout.
     * @param idPanel identificador del panel dentro del CardLayout
     */
    PanelSistema(String idPanel) {
        this.idPanel = idPanel;
    }

    /**
     * Getter del nombre identificador del panel.
     * @return nombre identificador utilizado por CardLayout
     */
    public String getIdPanel() {
        return idPanel;
    }
}