package frontend;

import java.awt.*;
import javax.swing.*;

import middleware.HistoryManager;

public class ClearAllPanel extends JPanel {

    public ClearAllPanel(HistoryManager historyManager, QnAPanel qna, HistoryPanel history) {
        this.setLayout(new GridLayout(1,1));

        ClearAllButton clearAllButton = new ClearAllButton(historyManager, qna, history);
        this.addClearAllButton(clearAllButton);
    }

    public void addClearAllButton(ClearAllButton clearAllButton) {
        this.add(clearAllButton);
    }


}

class ClearAllButton extends AppButtons {

    private final int ClearAllButtonWidth = 600;
    private final int ClearAllButtonHeight = 50;
    private final static String clearAllLabel = "Clear All";

    public ClearAllButton(HistoryManager historyManager, QnAPanel qna, HistoryPanel history) {
        super(clearAllLabel);
        this.setBackground(TEAL);
        this.setForeground(WHITE);
        setHorizontalAlignment(SwingConstants.CENTER);
        setPreferredSize(new Dimension(ClearAllButtonWidth, ClearAllButtonHeight));

        addActionListener(e -> {
            historyManager.clearAll();
            history.revalidateHistory(qna);
        });
    }

}