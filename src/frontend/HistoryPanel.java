package frontend;


import javax.swing.*;

import middleware.HistoryGrabber;

import java.awt.*;
import java.util.ArrayList;

public class HistoryPanel extends JPanel {
    private JScrollPane scrollPane = new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private MyFont myFont;
    private final Color WHITE = new Color(255, 255, 255);

    public HistoryPanel(MyFont myFont) {
        setLayout(new GridLayout(0, 1));
        setBackground(new Color(24, 25, 26));
        this.myFont = myFont;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void addHistoryButton(HistoryButton historyButton) {
        this.add(historyButton);

    }

    public void populateHistoryPanel (DisplayPanel displayPanel) {

        // TODO: Change this for loop to use HistoryGrabber
        //HistoryGrabber historyGrabber = new HistoryGrabber();

        for (int i = 0; i < 10; i++) {
            HistoryButton h = new HistoryButton(i, "History Button " + i);
            h.setFont(this.myFont.getFont());
            h.addActionListener(e -> {
                displayPanel.display.setText("This is history for question number " + h.id);
                displayPanel.display.setFont(this.myFont.getFont());
                displayPanel.display.setForeground(WHITE);

            });
            this.addHistoryButton(h);
        }
    }
}


class HistoryButton extends JButton {

    int id;
    private final Color BLACK = new Color(24, 25, 26);
    private final Color WHITE = new Color(255, 255, 255);
    private final int historyButtonWidth = 200;
    private final int historyButtonHeight = 50;

    public HistoryButton(int id, String displayText) {
        super(displayText);
        this.id = id;
        this.setBackground(BLACK);
        this.setForeground(WHITE);
        setHorizontalAlignment(SwingConstants.LEFT);

        setAlignmentX(Component.LEFT_ALIGNMENT);
        setPreferredSize(new Dimension(historyButtonWidth, historyButtonHeight));
    }
}
