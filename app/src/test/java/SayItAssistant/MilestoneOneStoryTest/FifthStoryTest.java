package SayItAssistant.MilestoneOneStoryTest;

import org.junit.jupiter.api.*;

import SayItAssistant.middleware.HistoryManager;
import SayItAssistant.middleware.SayItAssistant;
import SayItAssistant.middleware.WhisperRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.io.File;

public class FifthStoryTest {

    private static final String EXPECT_HISTORY_PATH = 
        System.getProperty("user.dir") + "/history.json";

    @AfterEach
    public void tearDown() {
        File file = new File(EXPECT_HISTORY_PATH);
        file.delete();
    }

    @Test
    public void testClearAllEmpty() {
       /* Scenario 2: There are no questions in SayIt Assistant’s history
        Given: there are no questions SayIt Assistant’s history
        When: History Helen wants to click the clear all button
        Then: Nothing will happen.*/
        HistoryManager newHistoryManager = new HistoryManager(new SayItAssistant(new WhisperRequest()));

        // Get the path of the class
        String path = System.getProperty("user.dir");

        System.out.println(path);
        Assertions.assertEquals(new ArrayList<>(), newHistoryManager.getPrompts());
        newHistoryManager.clearAll();
        Assertions.assertEquals(newHistoryManager.getPrompts(), new ArrayList<>());
        Assertions.assertEquals(newHistoryManager.getHistorySize(), 0);
    }
}
