package SayItAssistant.middlewareTests;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

// Packages to monitor output to console
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import SayItAssistant.middleware.MockLoginLogic;

/**
 * These series of tests are for the signup functionality
 * 
 * @source https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
 */
public class SignupTests {
    // Constants for testing the console output
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    // Constant expected messages to console
    private final static String EMPTY_CREDENTIALS = "Username and password cannot be empty";
    private final static String TAKEN_USERNAME    = "Username is not available";
    private final static String SUCCESSFUL_SIGNUP = "Account Created Successfully";
    
    // Constants for testing
    private static final String DEFAULT_USERNAME = "test";
    private static final String DEFAULT_PASSWORD = "password";
    private static final String NEW_USERNAME = "other";
    private static final String NEW_PASSWORD = "otherpassword";
    private static MockLoginLogic login = MockLoginLogic.getInstance();

    // Constant for the path to the data.json file from the root of the code base
    private static final String CURR_DIR = System.getProperty("user.dir");
    private static final String ROOT_DIR = CURR_DIR.substring(0, CURR_DIR.length() - 4);
    private static final String DATA_PATH = ROOT_DIR + "/data.json";
    private static String dataContent = "";


    /**
     * Helper method which simulates starting up a new instance of logging in
     */
    @BeforeEach
    public void loginNew() {
        MockLoginLogic.deconstruct();
        login = MockLoginLogic.getInstance();
    }

    /**
     * Setup method to redirect console output to a stream
     */
    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent)); 
    }

    /**
     * Setup method which preserves the current content of the data.json file
     */
    @BeforeEach
    public void preserveData() {
        try {
            dataContent = Files.readString(Paths.get(DATA_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * Test that a new user can be created
     */
    @Test
    public void testNewUser() {
        // Verify that an untaken username is available
        assertTrue(login.checkAvailableUsername(NEW_USERNAME));

        // Create a new user
        login.signUp(NEW_USERNAME, NEW_PASSWORD);
        assertEquals(SUCCESSFUL_SIGNUP, outContent.toString().trim());

        // Verify that the username is no longer available
        assertFalse(login.checkAvailableUsername(NEW_USERNAME));

        // Verify that the new user has been logged in
        assertTrue(login.isLoggedIn());
    }

    /**
     * Test that attempting to create a user with an existing username fails
     */
    @Test
    public void testExistingUser() {
        // Verify that an existing username is not available
        assertFalse(login.checkAvailableUsername(DEFAULT_USERNAME));
        assertEquals(TAKEN_USERNAME, errContent.toString().trim());

        restoreStreams(); // Refresh the console output

        // Attempt to create a new user with an existing username
        login.signUp(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        assertEquals(TAKEN_USERNAME, errContent.toString().trim());

        // Verify that the username is still unavailable
        assertFalse(login.checkAvailableUsername(DEFAULT_USERNAME));

        // Verify that the user has not been logged in
        assertFalse(login.isLoggedIn());
    }

    /**
     * Test that attempting to create a user with an empty username fails
     */
    @Test
    public void testEmptyUsername() {
        // Attempt to create a new user with an empty username
        login.checkAvailableUsername("");
        assertEquals(EMPTY_CREDENTIALS, errContent.toString().trim());

        restoreStreams(); // Refresh the console output

        login.signUp("", DEFAULT_PASSWORD);
        assertEquals(EMPTY_CREDENTIALS, errContent.toString().trim());

        // Verify that the user has not been logged in
        assertFalse(login.isLoggedIn());
    }

    /**
     * Test that attempting to create a user with an empty password fails
     */
    @Test
    public void testEmptyPassword() {
        // Attempt to create a new user with an empty password
        login.signUp(NEW_USERNAME, "");
        assertEquals(EMPTY_CREDENTIALS, errContent.toString().trim());

        // Verify that the user has not been logged in
        assertFalse(login.isLoggedIn());
    }
}
