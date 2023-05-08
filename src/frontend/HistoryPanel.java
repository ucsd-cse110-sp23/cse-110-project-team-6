package frontend;


import javax.swing.*;

import backend.Question;

import java.awt.*;

import middleware.HistoryGrabber;

import java.util.ArrayList;

public class HistoryPanel extends AppPanels {
    
    private JScrollPane scrollPane = new JScrollPane(this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private HistoryGrabber historyGrabber;

    public HistoryPanel(MyFont myFont) {
        this.setLayout(new GridLayout(0, 1));
        this.setBackground(BLACK);
        this.myFont = myFont;
    }


    public void revalidateHistory(DisplayPanel display) {
        this.removeAll();
        historyGrabber = new HistoryGrabber(historyFilePath);
        this.populateHistoryPanel(display);
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void addHistoryButton(HistoryButton historyButton) {
        this.add(historyButton);

    }

    public HistoryGrabber getHistoryGrabber() {
        return historyGrabber;
    }

    public void populateHistoryPanel(DisplayPanel display) {
        ArrayList<Question> questions = historyGrabber.getQuestions();
        
        for (int i = 0; i < questions.size(); i++) {
            HistoryButton historyButton = new HistoryButton(i, questions.get(i).getQuestion());
            Question question = questions.get(i);
            historyButton.setFont(this.myFont.getFont());
            historyButton.addActionListener(e -> {
                display.question.setText(question.getQuestion());
                display.answer.setText(historyGrabber.getAnswer(question).getAnswer());
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
