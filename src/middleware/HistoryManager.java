package middleware;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.MalformedParametersException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Class responsible for grabbing history of questions and answers
 * from the JSON and storing it within the History class.
 */
public class HistoryManager implements Subject, Observer {

    private static final String HISTORY_DIR = System.getProperty("user.dir") + "/bin/backend";
    private static final String HISTORY_PATH = HISTORY_DIR + "/history.json";
    private JSON_IO jsonIO;
    private SayItAssistant assistantSubject;
    private LinkedHashMap<Integer, QuestionAnswerPair> history;
    private ArrayList<Question> questions;
    private ArrayList<Observer> observers;
    /**
     * Constructor for HistoryManager class
     *
     * @param HISTORY_PATH path to the JSON file containing the history of
     */
    public HistoryManager(SayItAssistant assistantSubject) {
        observers = new ArrayList<Observer>();
        jsonIO = new JSON_IO();
        history = jsonIO.readHistory();
        this.assistantSubject = assistantSubject;
        this.assistantSubject.registerObserver(this);
    }

    /**
     * Getter method for the answer to a given question indicated by that question's number
     *
     * @param questionNum index of the question in the history of all questions asked
     * @return Answer object containing the answer to the given question
     * @require questionNum >= 0 && questionNum < history.size() && knowledge of questionNum
     */
    public Answer getAnswer(int questionNum) {
        return history.get(questionNum).getAnswer();
    }

    /**
     * Getter method for the list of all questions asked
     *
     * @return ArrayList<Question> list of all questions asked
     */
    public ArrayList<Question> getQuestions() {
        return questions;
    }

    /**
     * Getter method for the number of questions asked
     *
     * @return int number of questions asked
     */
    public int getHistorySize() {
        return history.size();
    }

    /**
     * Registers the observer to the list of observers
     */
    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    /**
     * Removes the observer from the list of observers
     */
    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    /**
     * Notifies all observers of the change in the history of questions and answers
     */
    @Override
    public void notifyObservers() {
        Question recentQuestion = null;
        Answer recentAnswer = null;
        if (history.size() != 0) {
            recentQuestion = history.get(history.size() - 1).getQuestion();
            recentAnswer = history.get(history.size() - 1).getAnswer();
        }

        for (Observer o : observers) {
            o.update(recentQuestion, recentAnswer);
        }
    }

    /**
     * Updates the history of questions and answers with the most recent question and answer
     *
     * @param question Question object containing the question asked
     * @param answer   Answer object containing the answer to the question asked
     * @require newQuestion != null && newAnswer != null &&
     * newQuestion instanceof Question && newAnswer instanceof Answer
     */
    public void add(Question newQuestion, Answer newAnswer) {
        if (newQuestion == null || newAnswer == null) {
            throw new MalformedParametersException("Attempted use of null ArrayList.");
        } else {
            int newIdx = history.size();
            QuestionAnswerPair newQA = new QuestionAnswerPair(newQuestion, newAnswer);
            history.put(newIdx, newQA);
            notifyObservers();
            questions.add(newQuestion);
        }
    }

    /**
     * Updates the history of questions and answers with the most recent question and answer
     *
     * @param questionNum
     */
    @Override
    public void update(Question newQuestion, Answer newAnswer) {
        add(newQuestion, newAnswer);
    }

    /**
     * Deletes the question and answer indicated by the question number
     *
     * @param question
     * @require questionNum >= 0 && questionNum < history.size() && knowledge of questionNum
     */
    public void delete(int questionNum) {
        // Store answers to rebuild history
        ArrayList<Answer> answers = new ArrayList<Answer>();
        for (Integer i : history.keySet()) {
            answers.add(history.get(i).getAnswer());
        }

        history.remove(questionNum);
        questions.remove(questionNum);
        answers.remove(questionNum);

        // Rebuild the history to update the indices
        history = new LinkedHashMap<Integer, QuestionAnswerPair>();
        for (int i = 0; i < questions.size(); i++) {
            history.put(i, new QuestionAnswerPair(questions.get(i), answers.get(i)));
        }
        notifyObservers();
    }

    /**
     * Clears all questions and answers from the history
     *
     * @param question
     */
    public void clearAll() {
        if (history.size() == 0) {
            return;
        }

        // Delete backwards to avoid index out of bounds
        for (int i = history.size() - 1; i >= 0; i--) {
            delete(i);
        }
    }

    /**
     * Nested class of HistoryManager acting as tuple for a question and answer
     */
    private class QuestionAnswerPair {
        private Question question;
        private Answer answer;

        public QuestionAnswerPair(Question newQuestion, Answer newAnswer) {
            question = newQuestion;
            answer = newAnswer;
        }

        public Question getQuestion() {
            return question;
        }

        public Answer getAnswer() {
            return answer;
        }
    }

    /**
     * Nested class of HistoryManager responsible for reading and writing
     * the history of questions and answers to a JSON file
     */
    private class JSON_IO implements Observer {
        private Subject historySubject;
        private JSONObject storedJSON;

        /**
         * Constructor for JSON_IO class which initializes the relevant fields for the class and
         * History Manager
         *
         * @param HISTORY_PATH path to the JSON file containing the history of questions and answers
         */
        private JSON_IO() {
            try {
                HistoryManager.this.questions = new ArrayList<Question>();
                historySubject = HistoryManager.this;
                historySubject.registerObserver(this);
                storedJSON = openJSON();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Open JSON file and read in the history of questions and answers as JSONObject
         * Creates the directory and file for history if they do not exist
         *
         * @param HISTORY_PATH path to the JSON file containing the history of questions and answers
         */
        private JSONObject openJSON() throws IOException, InterruptedException {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:1337/question"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            JSONObject json = new JSONObject(body);
            try (FileWriter f = new FileWriter("history.json")) {
                f.write(json.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return json;
        }

        /**
         * Updates the JSON file with the most recent question and answer
         *
         * @param args Object... args containing the history of questions and answers
         * @require args[0] instanceof QuestionAnswerPair
         */
        @Override
        public void update(Question newQuestion, Answer newAnswer) {
            storedJSON = new JSONObject();
            for (int i = 0; i < history.size(); i++) {
                QuestionAnswerPair qaPair = history.get(i);
                add(qaPair.getQuestion(), qaPair.getAnswer());
            }
            write();
        }

        /**
         * Reads in the history of questions and answers from the JSON file
         *
         * @return LinkedHashMap<Integer, QuestionAnswerPair> history of questions and answers
         */
        public LinkedHashMap<Integer, QuestionAnswerPair> readHistory() {
            LinkedHashMap<Integer, QuestionAnswerPair> history
                    = new LinkedHashMap<Integer, QuestionAnswerPair>();

            JSONArray storedQA = storedJSON.names();

            if (storedQA == null) {
                return history;
            }

            // Iterate through the JSON and split it into questions and answers
            for (int idx = 0; idx < storedQA.length(); idx++) {
                JSONObject questionAnswer = storedJSON.getJSONObject(Integer.toString(idx));

                Question questionObj = new Question(questionAnswer.getString("Question"));
                // Tracks questions in order for HistoryManager
                HistoryManager.this.questions.add(questionObj);
                Answer answerObj = new Answer(questionAnswer.getString("Answer"));

                QuestionAnswerPair qaPair = new QuestionAnswerPair(questionObj, answerObj);

                history.put(idx, qaPair);
            }

            return history;
        }

        /**
         * Updates the stored JSON with the most recent question and answer
         *
         * @param newQuestion Question object containing the question asked
         * @param newAnswer   Answer object containing the answer to the question asked
         * @require newQuestion != null && newAnswer != null &&
         * newQuestion instanceof Question && newAnswer instanceof Answer
         */
        public void add(Question newQuestion, Answer newAnswer) {
            int idx = storedJSON.length();

            JSONObject questionAnswer = new JSONObject();

            questionAnswer.put("Question", newQuestion.toString());
            questionAnswer.put("Answer", newAnswer.toString());

            storedJSON.put(Integer.toString(idx), questionAnswer);
        }

        /**
         * Writes the history of questions and answers to a JSON file
         */
        public void write() {
            try {
                Files.write(Paths.get("history.json"),
                        storedJSON.toString().getBytes());
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:1337/question"))
                        .PUT(HttpRequest.BodyPublishers.ofString(storedJSON.toString()))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}