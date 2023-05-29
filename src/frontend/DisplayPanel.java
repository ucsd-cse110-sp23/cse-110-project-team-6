package frontend;

import middleware.HistoryManager;
import middleware.SayItAssistant;

import java.awt.*;

/*
 * This panel holds the question, answer, and button panels.  It's primary function is to keep the overall 
 * format of these panels standardized.
 */
public class DisplayPanel extends AppPanels {
    
    private SayItAssistant sayItAssistant;

    // panels that are contained within the display panel
    QnAPanel qnaPanel;
    HistoryPanel historyPanel;
    StartPanel startPanel;

    /*
     * Creates and formats the display panel.
     * 
     * @param sayItAssistant:   assists with API calls
     * @param historyPanel:     panel for the history
     * @param historyManager:   manages the history
     */
    public DisplayPanel(SayItAssistant sayItAssistant, HistoryPanel historyPanel, HistoryManager historyManager) {

        // formats the panel
        this.sayItAssistant = sayItAssistant;
        this.setLayout(new GridBagLayout());
        GridBagConstraints displayFormat = new GridBagConstraints();    // this allows for finer control over formatting than GridLayout previously did
        this.qnaPanel = new QnAPanel();
        this.historyPanel = historyPanel;
        this.startPanel = new StartPanel(sayItAssistant, qnaPanel, historyPanel, historyManager);

        // adds the subpanels to the display
        addQnAPanel(qnaPanel, displayFormat);
        addStartPanel(startPanel, displayFormat);
    }

    /*
     * Formats the panel for questions and answers and adds it to the display.
     * 
     * @param qnaPanel:         panel to be added
     * @param displayFormat:    defines the formatting of the panel
     */
    public void addQnAPanel(QnAPanel qnaPanel, GridBagConstraints displayFormat) {

        // qna panel will fill up the display panel all the way horizontally and only 75% of the screen vertically
        displayFormat.fill = GridBagConstraints.BOTH;
        displayFormat.weightx = 1.0;
        displayFormat.weighty = 0.75;
        displayFormat.gridy = 0;
        add(qnaPanel, displayFormat);
    }

    /*
     * Formats the panel for the main buttons (new question, clear all, delete).
     * 
     * @param buttonPanel:      panel to be added
     * @param displayFormat:    defines the formatting of the panel
     */
    public void addStartPanel(StartPanel startPanel, GridBagConstraints displayFormat) {

        // button panel will fill up the display panel all the way horizontally and only 25% of the screen vertically
        displayFormat.fill = GridBagConstraints.BOTH;
        displayFormat.weightx = 1.0;
        displayFormat.weighty = 0.25;
        displayFormat.gridy = 1;    // button panel should be on top
        startPanel.setMaximumSize(new Dimension(20,20));   // keeping the maximum size small ensures that it is exceeded, making the button and qna panels size ratio remain constant
        add(startPanel, displayFormat);
    }

    /*
     * Returns the qna panel on the display.
     * 
     * @return QnAPanel: the qna panel that is on the display
     */
    public QnAPanel getQnAPanel() {
        return this.qnaPanel;
    }
    
}