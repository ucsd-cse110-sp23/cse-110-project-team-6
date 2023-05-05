package frontend;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class HistoryPanel extends JPanel {
    private JScrollPane scrollPane = new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    public HistoryPanel() {
        setLayout(new GridLayout(0, 1));
        setBackground(new Color(24, 25, 26));
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void addHistoryButton(HistoryButton historyButton) {
        this.add(historyButton);

    }
}


class HistoryButton extends JButton {
    int id;

    public HistoryButton(int id, String displayText) {
        super(displayText);
        this.id = id;
        this.setBackground(new Color(24, 25, 26));
        this.setForeground(new Color(255, 255, 255));
        setHorizontalAlignment(SwingConstants.LEFT);

        setAlignmentX(Component.LEFT_ALIGNMENT);
        setPreferredSize(new Dimension(200, 50));
    }
}
