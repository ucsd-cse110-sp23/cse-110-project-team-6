package SayItAssistant.frontend;

import javax.swing.*;
import java.awt.*;

/*
 * The AppFrame is the overall skeleton of the app, providing the structure and delegating function.
 */
public class AppFrame extends JFrame {

    // formatting
    private final String ICON_FILE         = "src/icon.png";
    private final String APPFRAME_TITLE    = "SayIt Assistant";
    private final int    APPFRAME_WIDTH    = 1280;
    private final int    APPFRAME_HEIGHT   = 720;
    
    // paneling
    private DisplayPanel displayPanel;
    private HistoryPanel historyPanel;

    /**
     * Constructor for AppFrame class which coordinates the GUI
     */
    public AppFrame() {
        setInformation();   // sets the overall app frame's behavior and style
        setUpPanels();      // adds the other parts of the UI to the frame
    }

    /**
     * Sets the information for the appframe
     */
    private void setInformation() {
        this.setTitle(APPFRAME_TITLE);
        this.setSize(APPFRAME_WIDTH, APPFRAME_HEIGHT);
        this.setLayout(new BorderLayout());
        this.setIconImage(new ImageIcon(ICON_FILE).getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Sets up the panels for the appframe
     */
    private void setUpPanels() {
        historyPanel = new HistoryPanel();  // side panel for history
        displayPanel = new DisplayPanel();  // panel to hold prompts, responses, and start button

        // adds the history panel and display panel to the appframe
        this.add(historyPanel.getScrollPane(), BorderLayout.WEST);
        this.add(displayPanel, BorderLayout.CENTER);
    }

    /*
     * Retrieves a reference to the history panel.
     */
    public HistoryPanel getHistoryPanel() {
        return this.historyPanel;
    }

    /*
     * Retrieves a reference to the display panel.
     */
    public DisplayPanel getDisplayPanel() {
        return this.displayPanel;
    }
}