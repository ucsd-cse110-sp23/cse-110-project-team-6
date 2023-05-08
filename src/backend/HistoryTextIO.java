package backend;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.MalformedParametersException;
import java.util.*;

/**
 * This class is responsible for reading and writing the history of questions and answers
 * 
 * @field historyFilePath String containing the path to the history file
 * @field reader FileReader object which reads the history file
 * @field writer FileWriter object which writes to the history file
 * @field questions ArrayList containing the questions in the history
 * @field answers ArrayList containing the answers in the history
 */
public class HistoryTextIO {
    private String historyFilePath;
    FileReader reader;
    FileWriter writer;
    ArrayList<Question> questions;
    ArrayList<Answer> answers;

    /**
     * Constructor for HistoryTextIO class
     * @param filePath
     */
    public HistoryTextIO(String filePath) {

        this.historyFilePath = filePath;
        questions = new ArrayList<Question>();
        answers = new ArrayList<Answer>();

        try {
            File historyFile = new File(filePath);
            if (!historyFile.isFile())
                historyFile.createNewFile();
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
                if ((tick++) % 2 == 0)
                    questions.add(new Question(line));
                else
                    answers.add(new Answer(line));
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
