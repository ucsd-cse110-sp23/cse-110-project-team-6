/*package backend;

import java.util.ArrayList;
import java.io.*;
import java.lang.reflect.MalformedParametersException;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSON_IO {

    private JSONObject JSON_IO;
    
    public JSON_IO() {
        this.JSON_IO = new JSONObject();
    }

    public ArrayList<Question> splitToQuestionArray() {
        return null;
    }

    public ArrayList<Answer> splitToAnswerArray() {
        return null;
    }

    public void write(ArrayList<Question> questions, ArrayList<Answer> answers, String filePath) {
        
        int id = 0;
        
        if (questions.size() != answers.size()) {
            throw new MalformedParametersException("Mismatched ArrayList sizes");
        }

        else {

            for (; id < questions.size(); id++) {
                JSON_IO.put("ID", id);
                JSON_IO.put("Question", questions.get(id).getQuestion());
                JSON_IO.put("Answer", answers.get(id).getAnswer());
            }
            
           // FileWriter file = null;

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
}*/