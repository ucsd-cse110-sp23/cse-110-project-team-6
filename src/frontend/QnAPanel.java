package frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import middleware.Answer;
import middleware.Question;

/**
 * Class which contains the Buttons for asking and deleting questions
 */
public class QnAPanel extends AppPanels {

    QuestionPanel questionPanel;
    AnswerPanel answerPanel;
    public QnAPanel() {
      
        this.setLayout(new GridLayout(2,1));
        this.setBackground(GREY);
        this.setPreferredSize(new Dimension(30,30));
        this.questionPanel = new QuestionPanel();
        this.answerPanel = new AnswerPanel();

        addQuestionPanel(questionPanel);
        addAnswerPanel(answerPanel);

    }

    public void addQuestionPanel(QuestionPanel questionPanel) {
        add(questionPanel.getQuestionScrollPane(), BorderLayout.NORTH);
    }

    public void addAnswerPanel(AnswerPanel answerPanel) {
        add(answerPanel.getAnswerScrollPane(), BorderLayout.SOUTH);
    }

    public void setQuestion(Question question) {
        questionPanel.setQuestion(question);
    }

    public void setAnswer(Answer answer) {
        answerPanel.setAnswer(answer);
    }
}