package SayItAssistant.middlewareTests;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

// Packages to monitor output to console
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import SayItAssistant.middleware.MockLoginLogic;

/**
 * These series of tests are for the manual login functionality
 * 
 * @source https://stackoverflow.com/questions/1119385/junit-test-for-system-out-println
 */
public class ManualLoginTests {

    // Constants for testing the console output
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    // Constant expected messages to console
    private final static String EMPTY_CREDENTIALS = "Username and password cannot be empty";
    private final static String BAD_CREDENTIALS   = "Incorrect username or password";
    
    // Constants for testing
    private static final String DEFAULT_USERNAME = "test";
    private static final String DEFAULT_PASSWORD = "password";
    private static MockLoginLogic login = MockLoginLogic.getInstance();

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
     * Tear down method to reset console output and deconstruct the login instance
     */
    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
        MockLoginLogic.deconstruct();
    }

    /**
     * Tests that the default user can be logged in from an unlogged state
     */
    @Test
    public void testDefaultLogin() {
        assertFalse(login.isLoggedIn());
        boolean successLogin = login.checkValid(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        assertTrue(successLogin);
        assertTrue(login.isLoggedIn());
    }

    /**
     * Tests that giving the wrong password will not log in
     */
    @Test
    public void testWrongPassword() {
        boolean successLogin = login.checkValid(DEFAULT_USERNAME, "wrong");
        assertFalse(successLogin);
        assertEquals(BAD_CREDENTIALS, errContent.toString().trim());
        assertFalse(login.isLoggedIn());
    }

    /**
     * Tests that giving the wrong username will not log in
     */
    @Test
    public void testWrongUsername() {
        boolean successLogin = login.checkValid("wrong", DEFAULT_PASSWORD);
        assertFalse(successLogin);
        assertEquals(BAD_CREDENTIALS, errContent.toString().trim());
        assertFalse(login.isLoggedIn());
    }

    /**
     * Tests that giving the wrong username and password will not log in
     */
    @Test
    public void testWrongUsernameAndPassword() {
        boolean successLogin = login.checkValid("wrong", "wrong");
        assertFalse(successLogin);
        assertEquals(BAD_CREDENTIALS, errContent.toString().trim());
        assertFalse(login.isLoggedIn());
    }

    /**
     * Tests that giving an empty username and password will not log in
     */
    @Test
    public void testEmptyUsernameAndPassword() {
        boolean successLogin = login.checkValid("", "");
        assertFalse(successLogin);
        assertEquals(EMPTY_CREDENTIALS, errContent.toString().trim());
        assertFalse(login.isLoggedIn());
    }

    /**
     * Tests that giving an empty username will not log in
     */
    @Test
    public void testEmptyUsername() {
        boolean successLogin = login.checkValid("", DEFAULT_PASSWORD);
        assertFalse(successLogin);
        assertEquals(EMPTY_CREDENTIALS, errContent.toString().trim());
        assertFalse(login.isLoggedIn());
    }

    /**
     * Tests that giving an empty password will not log in
     */
    @Test
    public void testEmptyPassword() {
        boolean successLogin = login.checkValid(DEFAULT_USERNAME, "");
        assertFalse(successLogin);
        assertEquals(EMPTY_CREDENTIALS, errContent.toString().trim());
        assertFalse(login.isLoggedIn());
    }

    /**
     * Tests that closing out of the login instance will not log back in
     */
    @Test
    public void testCloseLogin() {
        boolean successLogin = login.checkValid(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        assertTrue(successLogin);
        assertTrue(login.isLoggedIn());
        
        loginNew();
        assertFalse(login.isLoggedIn());
    }

    /**
     * Tests that relogging in from closed instance will work
     */
    @Test
    public void testRelogin() {
        boolean successLogin = login.checkValid(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        assertTrue(successLogin);
        assertTrue(login.isLoggedIn());
        
        loginNew();
        successLogin = login.checkValid(DEFAULT_USERNAME, DEFAULT_PASSWORD);
        assertTrue(successLogin);
        assertTrue(login.isLoggedIn());
    }

}
