package SayItAssistant.MilestoneTwoStoryTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

// Packages to monitor output to console
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import SayItAssistant.middleware.MockLoginLogic;

/**
 * This class is for the eighth story of the milestone 2
 * 
 * User Story: Manual Login
 * As a security-minded individual away from a secure computer, 
 * I want SayIt Assistant 2 to request for me to login each time I open up SA2 
 * so that I can keep my previous activity with SA2 private.
 */
public class EigthStoryTest {
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

    /**
     * Setup method which creates an account for Helen,
     * simulates starting up a new instance of logging in, 
     * and redirects console output to a stream.
     * 
     * Also preserve the data before creating Helen
     */
    @BeforeEach
    public void setup() {
        try {
            dataContent = Files.readString(Paths.get(DATA_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        login = MockLoginLogic.getInstance();
        login.signUp(HELEN_USER, HELEN_PASSWORD);
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent)); 
    }

   /**
     * Helper method which simulates starting up a new instance of logging in
     */    
    public void loginNew() {
        MockLoginLogic.deconstruct();
        login = MockLoginLogic.getInstance();
    }
 
    /**
     * Tear down method to reset console output and deconstruct the login instance
     */
    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        MockLoginLogic.deconstruct();
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
     * Helper method to check that automatic login is not enabled
     * (i.e., login.txt does not exist)
     */
    public boolean isAutoLogged() {
        return Files.exists(Path.of(AUTO_LOGIN_PATH));
    }

    /**
     * Scenario 1: Opt-out of automatic login after account creation
     * 
     * Given: History Helen has just successfully created an account with the email 
     * “hishelen@ucsd.edu” and password “supersecure” and pressed “Confirm”
     * And: SayIt Assistant displays a screen asking Helen if she would like to login automatically
     * from this computer.
     * When: History Helen clicks “No”
     * And: The history panel, prompt and response, and start button will be displayed
     * When: History Helen closes SayIt Assistant
     * And: She reopens SayIt Assistant from the same device.
     * Then: History Helen is prompted to enter her email address and password to login.
     * When: History Helen correctly enters in her email address “hishelen@ucsd.edu” and password
     * “supersecure”
     * And: She presses “Login”
     * Then: History Helen is logged in
     * And: SayIt Assistant displays a screen asking if she would like to automatically login
     * When: History Helen selects “No”
     * Then: The history panel, prompt and response, and start button will be displayed
     * 
     * (Essentially, this assumes Helen has just made an account)
     */
    @Test
    public void testScenario1() {
        // Reset the server to its original state before Helen's account was created
        restoreData();

        // Helen has just successfully created an account
        login.signUp(HELEN_USER, HELEN_PASSWORD);
        assertTrue(login.isLoggedIn());

        // Helen says no to automatic login
        login.saveLogin(false, HELEN_USER, HELEN_PASSWORD);
        assertFalse(isAutoLogged());

        // Helen has logged into SayItAssistant
        assertTrue(login.isLoggedIn());

        // Helen closes SayItAssistant and reopens it
        loginNew();

        // Helen is prompted to enter her email address and password to login
        assertFalse(login.isLoggedIn());

        // Helen correctly enters in her email address and password
        login.checkValid(HELEN_USER, HELEN_PASSWORD);

        // Helen is logged in and says no to automatic login
        assertTrue(login.isLoggedIn());
        login.saveLogin(false, HELEN_USER, HELEN_PASSWORD);
        assertFalse(isAutoLogged());
    }

    /**
     * Scenario 2: Successfully Loggin In after Opening SayItAssistant
     * Given: History Helen has a valid account with email “hishelen@ucsd.edu” and password 
     * “supersecure”
     * When: History Helen opens SayIt Assistant from her school computer for the first time
     * Then: She is prompted to enter in an email address and password
     * When: History Helen correctly enters in her email address “hishelen@ucsd.edu” and password 
     * “supersecure”
     * And: She presses “Login”
     * Then: History Helen is logged in
     * And: SayIt Assistant displays a screen asking if she would like to automatically login
     * When: History Helen selects “No”
     * Then: The main content of the app is displayed (history panels, prompt and response 
     * displays, start button).
     * 
     * (Essentially, this assumes that Helen already has an account)
     */
    @Test
    public void testScenario2() {
        // Helen says no to automatic login
        login.saveLogin(false, HELEN_USER, HELEN_PASSWORD);
        assertFalse(isAutoLogged());

        // Helen has logged into SayItAssistant
        assertTrue(login.isLoggedIn());

        // Helen closes SayItAssistant and reopens it
        loginNew();

        // Helen is prompted to enter her email address and password to login
        assertFalse(login.isLoggedIn());

        // Helen correctly enters in her email address and password
        login.checkValid(HELEN_USER, HELEN_PASSWORD);

        // Helen is logged in and says no to automatic login
        assertTrue(login.isLoggedIn());
        login.saveLogin(false, HELEN_USER, HELEN_PASSWORD);
        assertFalse(isAutoLogged());
    }

    /**
     * Scenario 3: Incorrect Information Provided During Login Attempt
     * Given: History Helen opens SayIt Assistant on her device
     * And: History Helen has a valid account with the email "hishelen@ucsd.edu" and password 
     * "supersecure"
     * When: History Helen enters an incorrect email address "helen@ucsd.edu" and correct password 
     * "supersecure" into the login fields
     * And: She clicks on the "Login" button
     * Then: SayIt Assistant displays an error message stating "Invalid email or password"
     * And: History Helen remains on the login screen without being logged into the app
     * When: History Helen enters the correct email address "hishelen@ucsd.edu" and incorrect 
     * password "supersecured" into the login fields
     * And: She clicks on the "Login" button
     * Then: SayIt Assistant displays an error message stating "Invalid email or password"
     * And: History Helen remains on the login screen without being logged into the app
     */
    @Test
    public void testScenario3() {
        // Helen has SayItAssistant open, but not logged in
        loginNew();
        assertFalse(login.isLoggedIn());

        // Helen enters an incorrect email address and correct password
        login.checkValid(HELEN_WRONG, HELEN_PASSWORD);

        // Helen is not logged in and sees an error message
        assertFalse(login.isLoggedIn());
        assertEquals(BAD_CREDENTIALS, errContent.toString().trim());
        restoreStreams();

        // Helen enters a correct email address and incorrect password
        login.checkValid(HELEN_USER, HELEN_WRONG_PASSWORD);

        // Helen is not logged in and sees an error message
        assertFalse(login.isLoggedIn());
        assertEquals(BAD_CREDENTIALS, errContent.toString().trim());
        restoreStreams();
    }
}
