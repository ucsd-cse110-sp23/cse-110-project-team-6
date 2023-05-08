/*package backend;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    public void testWriteFile () {
        //History history = new History();
        JSON_IO json_io = new JSON_IO();
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
       json_io.write(expectedQuestionsList, expectedAnswersList, "src/backend/historyFileTest.json");

    }
}*/