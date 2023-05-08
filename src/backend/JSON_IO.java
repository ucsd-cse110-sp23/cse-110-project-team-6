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
            if (historyFile.createNewFile() || historyFile.length() == 0) {
                this.JSON_IO = new JSONObject();
            } else {
                String jsonStr = new String(Files.readAllBytes(Paths.get(filePath)));
                this.JSON_IO = new JSONObject(jsonStr);
                this.splitToQuestionAnswerArray();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void splitToQuestionAnswerArray() {
        JSONArray questionIdx = JSON_IO.names();

        for (int i = 0; i < questionIdx.length(); i++) {
            String idx = questionIdx.getString(i);
            JSONObject questionAnswer = JSON_IO.getJSONObject(idx);

            String question = questionAnswer.getString("Question");
            String answer = questionAnswer.getString("Answer");

            Question questionObj = new Question(question);
            Answer answerObj = new Answer(answer);
            this.questions.add(questionObj);
            this.answers.add(answerObj);
        }
        
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
                JSONObject questionAnswer = new JSONObject();
                questionAnswer.put("Question", insertQuestion.get(id).getQuestion());
                questionAnswer.put("Answer", answers.get(id).getAnswer());

                JSON_IO.put(Integer.toString(id), questionAnswer);

                //JSON_IO.put(insertQuestion.get(id).getQuestion(), answers.get(id).getAnswer());
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