package frontend;

import middleware.HistoryManager;
import middleware.SayItAssistant;
import middleware.WhisperRequest;

import javax.swing.*;
import java.awt.*;

import java.io.File;

/*
 * The AppFrame is the overall skeleton of the app, providing the structure and delegating function.
 */
public class AppFrame extends JFrame {

    // formatting
    private final String HISTORY_FONT_FILE = "src/fonts/OpenSans-Regular.ttf";
    private final String ICON_FILE         = "src/icon.png";
    private final String APPFRAME_TITLE    = "SayIt Assistant";
    private final float  FONT_SIZE         = 16f;
    private final int    APPFRAME_WIDTH    = 1280;
    private final int    APPFRAME_HEIGHT   = 720;
    
    // paneling
    private DisplayPanel displayPanel;
    private HistoryPanel historyPanel;
    private NewQuestionPanel newQuestionPanel;
    private SayItAssistant sayItAssistant;
    private HistoryManager historyManager;
    private boolean loggedIn = false;
    /**
     * Constructor for AppFrame class which coordinates the GUI
     */
    public AppFrame() {
        MyFont myFont = new MyFont(new File(HISTORY_FONT_FILE), FONT_SIZE);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(myFont.getFont());
        setInformation();
        LoginWindow loginWindow = new LoginWindow();
        this.add(loginWindow);
        AbstractButton loginWindowButton = loginWindow.getButton();
        loginWindowButton.addActionListener(e -> {
            loggedIn = true; //TODO: Add login functionality
            this.remove(loginWindow);
            setupQuestion();
            revalidate();
        });
        revalidate();
    }

    private void setupQuestion(){
        // Initalizes the history manager and SayIt Assistant for Panels
        sayItAssistant = new SayItAssistant(new WhisperRequest());
        historyManager = new HistoryManager(sayItAssistant);

        // Sets the font, overall display panel, and the history sidepanel
        setUpPanels();

        // adds the history panel and display panel to the appframe
        this.add(historyPanel.getScrollPane(), BorderLayout.WEST);
        this.add(displayPanel, BorderLayout.CENTER);
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
        historyPanel = new HistoryPanel(historyManager);
        displayPanel = new DisplayPanel(sayItAssistant, historyPanel, historyManager);
        historyPanel.revalidateHistory(displayPanel.getQnAPanel());
    }
}