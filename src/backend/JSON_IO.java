package backend;

import java.util.ArrayList;
import java.io.*;
import java.lang.reflect.MalformedParametersException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class is responsible for reading and writing the history of questions and answers
 * to a JSON file
 */
public class JSON_IO {

    private String filePath;
    private JSONObject JSON_IO;
    private ArrayList<Question> questions;
    private ArrayList<Answer> answers;

    /**
     * Constructor for the JSON_IO class
     */
    public JSON_IO(String filePath) {

        this.filePath = filePath;
        questions = new ArrayList<Question>();
        answers = new ArrayList<Answer>();

        try {
            File historyFile = new File(filePath);
            if (historyFile.createNewFile()) {
                this.JSON_IO = new JSONObject();
            } else {
                String jsonStr = new String(Files.readAllBytes(Paths.get(filePath)));
                this.JSON_IO = new JSONObject(jsonStr);
                this.splitToAnswerArray();
                this.splitToQuestionArray();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private ArrayList<Question> splitToQuestionArray() {
        JSONArray questionKeys = JSON_IO.names();

        for (int i = 0; i < questionKeys.length(); i++) {
            String question = questionKeys.getString(i);
            Question questionObj = new Question(question);
            this.questions.add(questionObj);
        }
        
        return this.questions;
    }

    private ArrayList<Answer> splitToAnswerArray() {
        JSONArray answerValues = JSON_IO.toJSONArray(JSON_IO.names());

        for (int i = 0; i < answerValues.length(); i++) {
            String answer = answerValues.getString(i);
            Answer answerObj = new Answer(answer);
            this.answers.add(answerObj);
        }

        return this.answers;
    }

    public ArrayList<Question> getQuestions() {
        return this.questions;
    }

    public ArrayList<Answer> getAnswers() {
        return this.answers;
    }

    public void write(ArrayList<Question> insertQuestion, ArrayList<Answer> answers) {
        
        int id = 0;
        
        if (insertQuestion.size() != answers.size()) {
            throw new MalformedParametersException("Mismatched ArrayList sizes");
        }

        else {

            for (; id < insertQuestion.size(); id++) {
                JSON_IO.put(insertQuestion.get(id).getQuestion(), answers.get(id).getAnswer());
            }

            try {
                FileWriter file = new FileWriter(filePath);
                file.write(JSON_IO.toString());
                file.close();
                System.out.println("Sjidf");
            }
            catch (IOException e) {
                System.out.println("Sdlijf");
                e.printStackTrace();
            }
        }


    }
}