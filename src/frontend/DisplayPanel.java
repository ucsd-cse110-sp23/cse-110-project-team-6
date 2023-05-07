package frontend;


import javax.swing.*;
import java.awt.*;

public class DisplayPanel extends JPanel {
    JTextArea display = new JTextArea();

    JButton newQuestionButton;

    public DisplayPanel() {
        Color background = new Color(52, 53, 62);
        add(display);
        setBackground(background);
        display.setBackground(background);
        display.setText("Text");


        //Creating newQuestionButton
        newQuestionButton = new JButton("New Question");
        newQuestionButton.setPreferredSize(new Dimension(1200, 20));
        this.add(newQuestionButton,BorderLayout.SOUTH);
    }
}
