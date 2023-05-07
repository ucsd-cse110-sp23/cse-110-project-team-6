package frontend;


import javax.swing.*;
import java.awt.*;

import middleware.HistoryGrabber;

import java.util.ArrayList;

public class HistoryPanel extends AppPanels {
    
    private JScrollPane scrollPane = new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    public HistoryPanel(MyFont myFont) {
        this.setLayout(new GridLayout(0, 1));
        this.setBackground(BLACK);
        this.myFont = myFont;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void addHistoryButton(HistoryButton historyButton) {
        this.add(historyButton);

    }

    public void populateHistoryPanel(DisplayPanel display) {

        // TODO: Change this for loop to use HistoryGrabber
        //HistoryGrabber historyGrabber = new HistoryGrabber();

        for (int i = 0; i < 20; i++) {
            HistoryButton historyButton = new HistoryButton(i, "History Button " + i);
            historyButton.setFont(this.myFont.getFont());
            historyButton.addActionListener(e -> {
                display.question.setText("This is question " + historyButton.id);
                display.answer.setText("This is answer " + historyButton.id);
                display.setFont(this.myFont.getFont());
                display.setForeground(WHITE);

            });
            this.addHistoryButton(historyButton);
        }
    }
}


class HistoryButton extends AppButtons {
    
    public HistoryButton(int id, String displayText) {
        super(displayText);
        this.buttonWidth = 200;
        this.buttonHeight = 50;
        this.id = id;
        this.setBackground(BLACK);
        this.setForeground(WHITE);
        setHorizontalAlignment(SwingConstants.LEFT);

        setAlignmentX(Component.LEFT_ALIGNMENT);
        setPreferredSize(new Dimension(buttonWidth, buttonHeight));
    }
}
