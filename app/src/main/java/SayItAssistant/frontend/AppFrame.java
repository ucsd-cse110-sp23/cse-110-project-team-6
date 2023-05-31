package SayItAssistant.frontend;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/*
 * The AppFrame is the overall skeleton of the app, providing the structure and delegating function.
 */
public class AppFrame extends JFrame {

    // formatting
    private final String ICON_FILE         = "images/icon.png";
    private final String APPFRAME_TITLE    = "SayIt Assistant";
    private final int    APPFRAME_WIDTH    = 1280;
    private final int    APPFRAME_HEIGHT   = 720;
    
    // paneling
    private DisplayPanel displayPanel;
    private HistoryPanel historyPanel;
    private LoginWindow  loginWindow;
    /**
     * Constructor for AppFrame class which coordinates the GUI
     */
    public AppFrame() {
        setInformation();   // sets the overall app frame's behavior and style
        setUpLogin();       // sets up the login window
    }

    /**
     * Sets the information for the appframe
     */
    private void setInformation() {
        this.setTitle(APPFRAME_TITLE);
        this.setSize(APPFRAME_WIDTH, APPFRAME_HEIGHT);
        this.setLayout(new BorderLayout());
        ClassLoader classLoader = getClass().getClassLoader();
        URL input = classLoader.getResource(ICON_FILE);

        // sets the icon for the appframe
        this.setIconImage(new ImageIcon(input).getImage());

        //this.setIconImage(new ImageIcon(input).getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Sets up login
     */
    private void setUpLogin() {
        this.loginWindow = new LoginWindow();
        this.add(loginWindow);
    }

    /**
     * Sets up the panels for the appframe
     */
    public void setUpPanels() {
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

    /**
     * Retrieves a reference to the login window.
     */
    public LoginWindow getLoginWindow() {
        return this.loginWindow;
    }

    /**
     * Closes the login window.
     */
    public void closeLoginWindow() {
        this.loginWindow.setVisible(false);
        this.remove(loginWindow);
    }
}