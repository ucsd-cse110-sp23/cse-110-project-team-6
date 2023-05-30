package SayItAssistant.MilestoneOneStoryTest;

import org.junit.jupiter.api.*;

import SayItAssistant.Server;
import SayItAssistant.middleware.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import java.io.File;
import java.io.IOException;

public class FifthStoryTest {

    private static final String EXPECT_HISTORY_PATH = 
        System.getProperty("user.dir") + "/history.json";

    private static final String TEST_USER = "test";
    private static final String TEST_PASSWORD = "password";

    @AfterEach
    public void tearDown() {
        File file = new File(EXPECT_HISTORY_PATH);
        file.delete();
    }

    /**
     * Scenario 2: There are no questions in SayIt Assistant’s history
     * Given: there are no questions SayIt Assistant’s history
     * When: History Helen wants to click the clear all button
     * Then: Nothing will happen.
     */
    @Test
    public void testClearAllEmpty() {
        try {
            Server.startServer();
        } catch (IOException e) {
            assertTrue(false);
        }

        HistoryManager newHistoryManager = 
            new HistoryManager(new SayItAssistant(new MockWhisperRequest()), TEST_USER, TEST_PASSWORD);

        // Get the path of the class
        String path = System.getProperty("user.dir");

        System.out.println(path);
        assertEquals(new ArrayList<>(), newHistoryManager.getPrompts());
        newHistoryManager.clearAll();
        assertEquals(newHistoryManager.getPrompts(), new ArrayList<>());
        assertEquals(newHistoryManager.getHistorySize(), 0);
        Server.stopServer();
    }
}
