package backend;

import org.json.JSONArray;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileReader;
import java.util.*;
import org.json.JSONArray;
//import org.json.simple.*;
import org.json.JSONObject;
import org.json.JSONPointer;

public class JSON_IOTests {
    
    //private HashMap<Question, Answer> expectedHistory = createHistory();

    //private HashMap<Question, Answer> createHistory() {
       // HashMap<Question, Answer> expectedHistory = new HashMap<Question, Answer>();
        //for (int i = 0; i < this.expectedQuestionsList.size(); ++i) {
        //    expectedHistory.put(expectedQuestionsList.get(i), expectedAnswersList.get(i));
       // }
        //return expectedHistory;
   // }

    private ArrayList<Question> expectedQuestionsList;
    private ArrayList<Answer> expectedAnswersList;
    private ArrayList<Question> newQList;
    private ArrayList<Answer> newAList;
    private HashMap<Question, Answer> expectedHistory;

    private HashMap<Question, Answer> createHistory() {
        HashMap<Question, Answer> expectedHistory = new HashMap<Question, Answer>();
        for (int i = 0; i < this.expectedQuestionsList.size(); ++i) {
            expectedHistory.put(expectedQuestionsList.get(i), expectedAnswersList.get(i));
        }
        return expectedHistory;
    }

    @BeforeEach
    public void setUp () {
        expectedQuestionsList = new ArrayList<>(Arrays.asList(
            new Question("Question 1"), new Question("Question 2"),
            new Question("Question 3"), new Question("Question 4"),
            new Question("Question 5")));
        expectedAnswersList = new ArrayList<>(Arrays.asList(
                new Answer("Answer 1"), new Answer("Answer 2"),
                new Answer("Answer 3"), new Answer("Answer 4"),
                new Answer("Answer 5")));
        newQList = new ArrayList<>(Arrays.asList(
                new Question("Q1"), new Question("Q2"),
                new Question("Q3"), new Question("Q4"),
                new Question("Q5")));
        newAList = new ArrayList<>(Arrays.asList(
                new Answer("A1"), new Answer("A2"),
                new Answer("A3"), new Answer("A4"),
                new Answer("A5")));
        expectedHistory = this.createHistory();

        for (int i = 0; i < expectedQuestionsList.size(); i++) {
            System.out.println(expectedQuestionsList.get(i).getQuestion());
        }
    }

    @Test
    public void testCreateJSONIO() {
        JSON_IO json_io = new JSON_IO("history.json");

        for (int i = 0; i < expectedQuestionsList.size(); i++) {
            System.out.println(expectedQuestionsList.get(i).getQuestion());
        }
        json_io.write(expectedQuestionsList, expectedAnswersList);
        ArrayList<Question> actualQuestionsList = json_io.getQuestions();
        ArrayList<Answer>   actualAnswersList = json_io.getAnswers();
        
        for (int i = 0; i < actualAnswersList.size(); i++) {
            assertEquals(expectedQuestionsList.get(i).getQuestion(), actualQuestionsList.get(i).getQuestion());
            assertEquals(expectedAnswersList.get(i).getAnswer(), actualAnswersList.get(i).getAnswer());
        }
    }

    @Test
    public void testWriteFile () {
        //History history = new History();
        JSON_IO json_io = new JSON_IO("history.json");
        ArrayList<Question> expectedQuestionsList = new ArrayList<>(Arrays.asList(
            new Question("Who am I?"), new Question("What is 9 + 10?"),
            new Question("What is the meaning of life?"), new Question("Where am I?"),
            new Question("What is my favorite color?")));
        ArrayList<Answer> expectedAnswersList = new ArrayList<>(Arrays.asList(
            new Answer("I am SayIt."), new Answer("That would be 21."),
            new Answer("42"), new Answer("I am in the Matrix"),
            new Answer("Yellow.  No, Red.")));
        //history.setHistory(expectedQuestionsList, expectedAnswersList);
        System.out.println("Sdif");
        json_io.write(expectedQuestionsList, expectedAnswersList);
    }

    @Test
    public void testReadFile() {
        
    }
}