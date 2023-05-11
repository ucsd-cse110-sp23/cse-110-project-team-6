package backend;

import java.lang.reflect.MalformedParametersException;
import java.util.ArrayList;
import java.util.HashMap;

import middleware.*;

@Deprecated
public class History {

    private HashMap<Question, Answer> history;  // maps the questions asked to the answer provided
    private ArrayList<Question> questionList;   // holds the questions asked in the order asked
    private int size;                           // number of questions that have been asked

    /*
     * Constructs a history of questions / answers.
     */
    public History() {
        this.history = new HashMap<Question, Answer>();
        this.questionList = new ArrayList<Question>();
        this.size = 0;
    }

    /*
     * Reveals the answer to a given question.
     * 
     * @param question: the question that an answer is being requested for
     * 
     * @return Answer: the answer to the provided question
     */
    public Answer getAnswer(Question question) {
        return history.get(question);
    }

    /*
     * Returns a list of the asked questions.
     * 
     * @return ArrayList<Question>: list of the questions asked
     */
    public ArrayList<Question> getQuestions() {
        return questionList;
    }

    /*
     * Reveals the number of questions that have been asked
     * 
     * @return int: the number of questions that have been asked
     */
    public int numberOfQuestions() {
        return this.size;
    }

    /*
     * Allows the history to be set.  Takes in ArrayLists to preserve ordering.
     * 
     * @param questions: list of questions that have been asked
     * @param answers: list of answers that have been provided
     */
    public void setHistory(ArrayList<Question> questions, ArrayList<Answer> answers) {

        // ArrayLists must be the same size; questions and answers should be 1:1
        if (questions.size() != answers.size()) {
            throw new MalformedParametersException("Mismatched ArrayList sizes");
        }

        // creates new history by matching each question to each answer
        else {
            this.history = new HashMap<Question, Answer>();

            for(int i = 0; i < questions.size(); i++) {
                this.history.put(questions.get(i), answers.get(i));
            }
    
            questionList = questions;
            this.size = questions.size();
        }
        
    }

    /*
     * Adds a question and answer to the history.
     * 
     * @param question: question to be added
     * @param answer: answer to be added
     */
    public void addQuestionAndAnswer(Question question, Answer answer) {
        this.history.put (question, answer);
        this.questionList.add(question);
        ++this.size;
    }

    /*
     * Deletes a question-answer pair.
     * 
     * @param question: the question to be deleted
     */
    public void deleteQuestionAndAnswer(Question question) {

        // deletes the question and its associated answer from the history and the list of questions
        if ((this.numberOfQuestions() > 0) && (history.containsKey(question))) {
            this.history.remove(question);
            this.questionList.remove(question);
            --this.size;
        }
    }

    /*
     * Completely clears the history.
     */
    public void clearAllQuestionsAndAnswers() {
        this.history.clear();
        this.questionList = null;
        this.size = 0;
    }
}
