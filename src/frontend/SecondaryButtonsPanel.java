package frontend;

import javax.swing.*;

import middleware.HistoryManager;

import java.awt.*;
import java.io.File;

public class SecondaryButtonsPanel extends AppPanels{

    public SecondaryButtonsPanel(HistoryManager historyManager, QnAPanel qna, HistoryPanel history) {
        this.setPreferredSize(new Dimension(400,20));
        this.setLayout(new GridLayout(1,2));

        ClearAllPanel clearAllPanel = new ClearAllPanel(historyManager, qna, history);

        this.addDeletePanel();
        this.addClearAllPanel(clearAllPanel);
       
    }

    public void addDeletePanel() {
        JTextArea jt = new JTextArea();
        jt.setFont(myFont.getFont());
        jt.setText("Delete Button Area");
        jt.setBackground(TEAL);
        jt.setBorder(BorderFactory.createLineBorder(BLACK));
        add(jt);
    }

    public void addClearAllPanel(ClearAllPanel clearAllPanel) {
        add(clearAllPanel);
    }

}
