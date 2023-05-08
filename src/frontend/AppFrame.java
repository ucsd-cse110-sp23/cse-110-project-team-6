package frontend;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class AppFrame extends JFrame {

    private final String historyFontFilePath = "../fonts/OpenSans-Regular.ttf";
    private final String imageIconFilePath = "src/icon.png";

    private final String appFrameTitle = "SayIt Assistant";
    private final int appFrameWidth = 1280;
    private final int appFrameHeight = 720;

    private final float fontSize = 16f;

    public AppFrame() {

        // sets the font, overall display panel, and the history sidepanel
        MyFont myFont = new MyFont (new File(historyFontFilePath), fontSize);
        DisplayPanel displayPanel = new DisplayPanel();
        HistoryPanel historyPanel = new HistoryPanel(myFont);
        NewQuestionPanel newQuestionPanel = new NewQuestionPanel(myFont);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        
        // sets the graphics environment's font
        ge.registerFont(myFont.getFont());

        displayPanel.addNewQuestionPanel(newQuestionPanel);

        historyPanel.revalidateHistory(displayPanel);

        // populates the history panel with the history
        historyPanel.populateHistoryPanel(displayPanel);
        newQuestionPanel.populateNewQuestionPanel(displayPanel, historyPanel);

        // sets the information for the appframe
        this.setTitle(appFrameTitle);
        this.setSize(appFrameWidth, appFrameHeight);
        this.setLayout(new BorderLayout());
        this.setIconImage(new ImageIcon(imageIconFilePath).getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
        // adds the history panel and display panel to the appframe
        this.add(historyPanel.getScrollPane(), BorderLayout.WEST);
        this.add(displayPanel, BorderLayout.CENTER);   

        revalidate();

        //TODO: add mouse listener for new Question
    }
}
