package frontend;

//import javax.print.attribute.standard.JobHoldUntil;
import javax.swing.*;

import middleware.SayItAssistant;

import java.awt.*;

public class DisplayPanel extends AppPanels {
    private SayItAssistant sayItAssistant;

    JButton newQuestionButton;
    QnAPanel qnaPanel;
    ButtonPanel buttonPanel;
    HistoryPanel historyPanel;

    public DisplayPanel(SayItAssistant sayItAssistant, HistoryPanel historyPanel) {
        this.sayItAssistant = sayItAssistant;
        this.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        this.qnaPanel = new QnAPanel();
        this.historyPanel = historyPanel;
        this.buttonPanel = new ButtonPanel(sayItAssistant, qnaPanel, historyPanel);

        addQnAPanel(qnaPanel, c);
        addButtonPanel(buttonPanel, c);
    }

    public void addQnAPanel(QnAPanel qnaPanel, GridBagConstraints c) {
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 0.75;
        c.gridy = 0;
        add(qnaPanel, c);
    }

    public void addButtonPanel(ButtonPanel buttonPanel, GridBagConstraints c) {
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 0.25;
        c.gridy = 1;
        buttonPanel.setMaximumSize(new Dimension(20,20));
        add(buttonPanel, c);
    }

    public QnAPanel getQnAPanel() {
        return this.qnaPanel;
    }
    
}

