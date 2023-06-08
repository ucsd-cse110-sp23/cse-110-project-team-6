package SayItAssistant.MilestoneTwoStoryTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

// Packages to monitor output to console
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import SayItAssistant.middleware.MockLoginLogic;
import SayItAssistant.middleware.*;

import java.io.File;


 /*
  * This class is the sixth story of the milestone 2: 
  * 
  * User Story: Create Account
  * 
  * As a person who uses multiple devices, 
  * I want to be able to create an account with my
  * email address and password so that I can access my unique 
  * questions and answer history from any location.
  */

public class SixthStoryTest{
    // Constants for testing the console output
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    // Constant expected messages to console
    private final static String BAD_CREDENTIALS   = "Incorrect username or password";
    
    // Constants for testing
    private static final String HELEN_USER = "hishelen@ucsd.edu";
    private static final String HELEN_WRONG = "helen@ucsd.edu";
    private static final String HELEN_PASSWORD = "supersecure";
    private static final String HELEN_WRONG_PASSWORD = "supersecured";
    private static MockLoginLogic login;

    // Constant for the path to the data.json file from the root of the code base
    private static final String CURR_DIR = System.getProperty("user.dir");
    private static final String ROOT_DIR = CURR_DIR.substring(0, CURR_DIR.length() - 4);
    private static final String DATA_PATH = ROOT_DIR + "/data.json";
    private static final String AUTO_LOGIN_PATH = ROOT_DIR + "/login.txt";
    private static String dataContent = "";

    //constants for history manager
    private static final String EXPECT_HISTORY_PATH =
        System.getProperty("user.dir") + "/history.json";
    private static final String EXPECT_DATA_PATH =
        System.getProperty("user.dir") + "/data.json";

    private static final int MAX_WINDOW_QUESTION_SIZE = 10;


    private static final String TEST_USER = "test";
    private static final String TEST_PASSWORD = "password";
    private static HistoryManager historyManager;
    private static SayItAssistant sayItAssistant;
    private static MockWhisperRequest mockWhisperRequest;

    /*
     * Setup method that creates an instance of the login
     */
    @BeforeEach
    public void setup() {
        try {
            dataContent = Files.readString(Paths.get(DATA_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //adding the given that a quesiton was asked for 
        openSayItAssistant();
        String command = "Question. Who was Louis Braille?";
        String question = "Who was Louis Braille?";
        askQuestion(command);

        historyManager.clearAll();
        login = MockLoginLogic.getInstance();
        login.signUp(HELEN_USER, HELEN_PASSWORD);
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent)); 
    }

    /**
     * Tear down method which restores the data.json file to its original state within server
     */
    @AfterEach
    public void restoreData() {
        try {
            Files.writeString(Paths.get(DATA_PATH), dataContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        login.resetServer();
    }

    /**
     * Tear down method to reset console output and deconstruct the login instance
     */
    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        // MockLoginLogic.deconstruct();
    }

    /**
     * Erases the history file after each test
     */
    @AfterEach
    public void tearDown() {
        sayItAssistant = null;
        historyManager = null;
        File file = new File(EXPECT_HISTORY_PATH);
        file.delete();
        file = new File(EXPECT_DATA_PATH);
        file.delete();
    }

    /**
     * Helper method which simulates starting up a new instance of logging in
     */    
    public void loginNew() {
        MockLoginLogic.deconstruct();
        login = MockLoginLogic.getInstance();
    }

    /**
     * Function which mimics opening SayIt Assistant
     */
    private void openSayItAssistant() {
        mockWhisperRequest = new MockWhisperRequest();
        sayItAssistant = new SayItAssistant(mockWhisperRequest);
        historyManager = new HistoryManager(sayItAssistant, TEST_USER, TEST_PASSWORD);
    }

    /**
     * Function which mimics asking a question to SayIt Assistant
     */
    private void askQuestion(String question) {
        mockWhisperRequest.testString = question;
        sayItAssistant = new SayItAssistant(mockWhisperRequest);
        historyManager = new HistoryManager(sayItAssistant, TEST_USER, TEST_PASSWORD);
        sayItAssistant.setHistoryManager(historyManager);
        sayItAssistant.respond();
    }


    /*
     * Scenario 1: Creating an account for the first time
     * Given: History Helen has loaded up SayIt Assistant 2
     * And:  She has not previously created an account
     * When: History Helen enters her email address “hishelen@ucsd.edu” and password “supersecure” 
     * And: She presses “Create Account”
     * Then: A password verification field appears to verify her password
     * When: History Helen re-enters “supersecure” into the password verification field. 
     * And: History Helen presses “Verify”
     * Then: SayIt Assistant confirms her account has been created with a “Success” message
     * And: An account is created for History Helen using the email address and password she provided.
     */
    @Test
    public void testScenario1(){

        // Reset the server to its original state before Helen's account was created
        restoreData();

        //Verify that the user does not exist:
        assertTrue(login.isLoggedIn());

        //TODO: add test statement that checks for password verification

        // Helen has just successfully created an account
        login.signUp(HELEN_USER, HELEN_PASSWORD);
        assertTrue(login.isLoggedIn());


    }


    /*
     * Given: History Helen has typed in her email address, “hishelen@ucsd.edu” and password, “supersecure”
     * And:  Historic Helena, previous graduate from UCSD whose accounts are no longer valid, has previously created an account with the same email address “hishelen@ucsd.edu” (since UCSD simply reused that email for Helen)
     * When: History Helen presses “Create Account”
     * Then: An error message appears saying “Email has been taken”.
     */
    @Test
    public void testScenario2(){
        assertFalse(login.signUp(HELEN_USER, HELEN_PASSWORD));
    }

    /*
     * Given: History Helen forgets to type in a password and email address
     * When: History Helen presses “Create Account”
     * Then: An error message appears saying, “Not all required fields have been filled”.
     * When: History Helen enters in her email address, “hishelen@ucsd.edu”
     * And: She still forgets to put in a password
     * And: She presses “Create Account”
     * Then: An error message appears saying “No Password Entered”
     * When: History Helen enters in her password “supersecure”
     * And: Helen had for some reason erased her email entry
     * And: She presses “Create Account”
     * Then: An error message appears saying “No Email Entered”
     */
    @Test
    public void testScenario3(){
        assertFalse(login.signUp("", ""));
        assertFalse(login.signUp(HELEN_USER, ""));
        assertFalse(login.signUp("", HELEN_PASSWORD));
    }

    /*
     * Given: History Helen opens up SayIt Assistant on her school computer for the first time.
     * And: History Helen has successfully created an account with the email “hishelen@ucsd.edu” and password “supersecure”
     * And: Helen had previously used SayIt Assistant on her personal computer to ask the question “What caused the downfall of the Roman Empire?”
     * When: Helen enters in “hishelen@ucsd.edu” into the email field and “supersecure” into the password field
     * And: She presses “Login”
     * Then: Helen is successfully logged into her account of SayIt Assistant
     * And: SayIt Assistant displays a screen asking if she would like to automatically login
     * When: History Helen presses “No”
     * Then: The main content of the app is displayed (history panels, prompt and response displays, start button).
     * And: Helen can see her previously asked question about the downfall of the Roman Empire.
     */
    @Test
    public void testScenario4(){
        //It is given that the account already exists and there is a question in the prompt
        assertTrue(login.isLoggedIn());



    }
}