package frontend;

import middleware.Answer;
import middleware.Question;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

/**
 * This panel contains space for a question and answer to be displayed.
 */
public class QnAPanel extends AppPanels {

    QuestionPanel questionPanel;    // panel for the question
    AnswerPanel answerPanel;        // panel for the answer
    Boolean isSet = false; //boolean to confirm if there is a question set or not

    /*
     * Creates and formats the panel for questions and answers.
     */
    public QnAPanel() {
      
        // formats the panel
        this.setLayout(new GridLayout(2,1));
        this.setBackground(GREY);
        this.setPreferredSize(new Dimension(30,30));
        this.questionPanel = new QuestionPanel();
        this.answerPanel = new AnswerPanel();

        // adds the question and answer panels to the display
        addQuestionPanel(questionPanel);
        addAnswerPanel(answerPanel);

    }

    /*
     * Adds the question panel to the QNA panel.
     * 
     * @param questionPanel: the question panel to be added
     */
    public void addQuestionPanel(QuestionPanel questionPanel) {
        add(questionPanel.getQuestionScrollPane(), BorderLayout.NORTH);
    }

    /*
     * Adds the answer panel to the QNA panel.
     * 
     * @param answerPanel: the answer panel to be added
     */
    public void addAnswerPanel(AnswerPanel answerPanel) {
        add(answerPanel.getAnswerScrollPane(), BorderLayout.SOUTH);
    }

    /*
     * Allows for the question text to be directly set.
     * 
     * @param question: the question to be displayed
     */
    public void setQuestion(Question question) {
        questionPanel.setQuestion(question);
        isSet = true; //confirm the display is set
    }

    /*
     * Allows for the answer text to be directly set.
     * 
     * @param answer: the answer to be displayed
     */
    public void setAnswer(Answer answer) {
        answerPanel.setAnswer(answer);
    }
    
    public boolean getStatus(){
        return this.isSet;
    }
}