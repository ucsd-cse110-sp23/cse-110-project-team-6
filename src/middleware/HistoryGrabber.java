package middleware;

import java.lang.reflect.MalformedParametersException;
import java.util.ArrayList;

import backend.*;

public class HistoryGrabber {

    private History myHistory;
    private String historyFilePath;
    private HistoryTextIO historyTextIO;
    private ArrayList<Question> questions;
    private ArrayList<Answer> answers;
   
    public HistoryGrabber(String historyFilePath) {
        myHistory = new History();
        this.historyFilePath = historyFilePath;
        this.historyTextIO = new HistoryTextIO("src/backend/history.txt");
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
        this.questions = historyTextIO.getQuestions();
        this.answers = historyTextIO.getAnswers();

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
        historyTextIO.write(questions, answers, "src/backend/history.txt");
    }

    public void deleteQuestionAndAnswer(Question question) {
        myHistory.deleteQuestionAndAnswer(question);
    }

    public void clearAllQuestionsAndAnswers() {
        myHistory.clearAllQuestionsAndAnswers();
    }
}
