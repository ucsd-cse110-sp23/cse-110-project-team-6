package middleware;

import java.lang.reflect.MalformedParametersException;
import java.util.ArrayList;

import backend.*;

/**
 * This class is responsible for grabbing the history of questions and answers
 */
public class HistoryGrabber {

    private History myHistory;
    private String historyFilePath;
    private JSON_IO historyJsonIO;
    private HistoryTextIO historyTextIO;
    private ArrayList<Question> questions;
    private ArrayList<Answer> answers;
   
    public HistoryGrabber(String historyFilePath) {
        myHistory = new History();
        this.historyFilePath = historyFilePath;
        //this.historyTextIO = new HistoryTextIO("src/backend/history.txt");
        this.historyJsonIO = new JSON_IO("src/backend/history.json");
        this.populateHistory();
    }

    public Answer getAnswer (Question question) {
        return myHistory.getAnswer(question);
    }

    public ArrayList<Question> getQuestions() {
        return myHistory.getQuestions();
    }

    public int size() {
        return myHistory.numberOfQuestions();
    }

    public void populateHistory() {
        /* 
        this.questions = historyTextIO.getQuestions();
        this.answers = historyTextIO.getAnswers();
        */

        this.questions = historyJsonIO.getQuestions();
        this.answers = historyJsonIO.getAnswers(); 
        myHistory.setHistory(questions, answers);
    }

    public void addQuestionAndAnswer(Question question, Answer answer) {
        if (question == null || answer == null) {
            throw new MalformedParametersException("Attempted use of null ArrayList.");
        }
        else {
            myHistory.addQuestionAndAnswer(question, answer);
        }
        answers.add(answer);
        //historyTextIO.write(questions, answers, "src/backend/history.txt");
        historyJsonIO.write(questions, answers);
    }

    public void deleteQuestionAndAnswer(Question question) {
        myHistory.deleteQuestionAndAnswer(question);
    }

    public void clearAllQuestionsAndAnswers() {
        myHistory.clearAllQuestionsAndAnswers();
    }
}
