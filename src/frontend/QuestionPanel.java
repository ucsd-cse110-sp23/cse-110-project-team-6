package frontend;

import middleware.Question;

import javax.swing.*;
import java.awt.*;

/*
 * Panel that contains the area for questions to be displayed.
 */
public class QuestionPanel extends AppPanels{
    
    private JTextPane question = new JTextPane();   // area for the question to be displayed with text
    private JScrollPane questionScrollPane = new JScrollPane
        (this, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);   // wraps the question area and allows it to be scrollable

    private int currentQuestionNumber; //position of the current question displayed in the database

    /*
     * Initializes and formats the panel for the question.
     */
    public QuestionPanel() {
        this.setLayout(new GridLayout(1, 1));
        question.setBackground(GREY);
        question.setForeground(WHITE);
        question.setAlignmentX(BOTTOM_ALIGNMENT);
        question.setAlignmentY(BOTTOM_ALIGNMENT);
        question.setText("Welcome to SayIt Assistant");
        add(question);
    }

    /*
     * Allows the question area to be explicitly set.
     * 
     * @param question: the question to be displayed
     */
    public void setQuestion(Question question) {
        this.question.setText(question.toString());
        currentQuestionNumber = question.getQuestionNumber(); 
    }

    /*
     * Returns the question area with the scrollbar.
     * 
     * @return JScrollPane: the question pane with scrollbar
     */
    public JScrollPane getQuestionScrollPane() {
        return this.questionScrollPane;
    }

    /*
     * getting the position of the current question in the database. 
     */
    public int getCurrentQuestionNumber(){
        return currentQuestionNumber; 
    }

}