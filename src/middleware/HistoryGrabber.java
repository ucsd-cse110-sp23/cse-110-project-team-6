package middleware;

import java.util.ArrayList;

import backend.*;
 /*
     * TODO: implement.
     * 
     * This will function as a way for HistoryPanel.java in frontend to grab the history
     * from History in backend
     */
public class HistoryGrabber {

    private History myHistory;
   
    public HistoryGrabber() {
        //myHistory = new History();

        // TODO: get the question / answer arraylists from the JSON file
        //this.populateHistory();

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
        // TODO: Get the question / answer arraylists from JSON file
        // this.setHistory (question arraylist, answer arraylist);
    }

    public void addQuestionAndAnswer(Question question, Answer answer) {
        myHistory.addQuestionAndAnswer(question, answer);
    }

    public void deleteQuestionAndAnswer(Question question) {
        myHistory.deleteQuestionAndAnswer(question);
    }

    public void clearAllQuestionsAndAnswers() {
        myHistory.clearAllQuestionsAndAnswers();
    }
}
