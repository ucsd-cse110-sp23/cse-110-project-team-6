package backend;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.MalformedParametersException;
import java.util.*;

public class HistoryTextIO {
    private String historyFilePath;
    FileReader reader;
    FileWriter writer;
    ArrayList<Question> questions;
    ArrayList<Answer> answers;

    public HistoryTextIO(String filePath) {

        this.historyFilePath = filePath;
        questions = new ArrayList<Question>();
        answers = new ArrayList<Answer>();

        try {
            
            File historyFile = new File(filePath);
            if (!historyFile.isFile()) {
                historyFile.createNewFile();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            this.reader = new FileReader(historyFilePath);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line;
            int tick = 0;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                if ((tick++) % 2 == 0) {
                    questions.add(new Question(line));
                }
                else {
                    answers.add(new Answer(line));
                }

            }
            
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Question> getQuestions() {
        return this.questions;
    }

    public ArrayList<Answer> getAnswers() {
        return this.answers;
    }

    public void write(ArrayList<Question> questions, ArrayList<Answer> answers, String filePath) {
        if ((questions != null) && (answers != null)) {
            if (questions.size() != answers.size()) {
                throw new MalformedParametersException("Mismatched ArrayList sizes");
            }
            
            if (!questions.isEmpty() && !answers.isEmpty()) {
                try {
                    writer = new FileWriter(filePath, false);
                    
                    for(int i = 0; i < questions.size(); i++) {
                        writer.write(questions.get(i).getQuestion());
                        writer.write("\n");
                        writer.write(answers.get(i).getAnswer());
                        if (i != questions.size() - 1) writer.write("\n");
                    }
                    writer.close();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }

            else {
                try {
                    writer = new FileWriter(filePath, false);
                    writer.close();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            throw new MalformedParametersException("Attempted use of null ArrayList.");
        }
    }
}
