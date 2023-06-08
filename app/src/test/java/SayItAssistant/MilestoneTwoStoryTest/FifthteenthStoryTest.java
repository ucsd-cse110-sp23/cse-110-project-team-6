package SayItAssistant.MilestoneTwoStoryTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.json.JSONObject;

import SayItAssistant.middleware.MockLoginLogic;
import SayItAssistant.middleware.EmailSetupLogic;

/**
 * This class is responsible for testing the email setup feature  
 * 
 * User Story: As a person who frequently emails others,
 * I want to be able to setup my email using SA2 by saying "Setup email"
 * so that I can use SA2 to send emails
 */
public class FifthteenthStoryTest {
    
    // Constant for the path to the data.json file from the root of the code base
    private static final String CURR_DIR = System.getProperty("user.dir");
    private static final String ROOT_DIR = CURR_DIR.substring(0, CURR_DIR.length() - 4);
    private static final String DATA_PATH = ROOT_DIR + "/data.json";
    private static final String AUTO_LOGIN_PATH = ROOT_DIR + "/login.txt";
    private static String dataContent = "";

    // Constants for testing
    private static final String HELEN_USER = "hishelen@ucsd.edu";
    private static final String HELEN_PASSWORD = "supersecure";
    private static final String HELEN_FIRST = "Helen";
    private static final String HELEN_LAST = "Historia";
    private static final String HELEN_DISPLAY = "Helen Historia";
    private static final String HELEN_EMAIL = "hishelen@ucsd.edu";
    private static final String HELEN_SMTP = "smtp.ucsd.edu";
    private static final String HELEN_PORT = "587";

    // Constants related to JSON keys
    private static final String FIRST_NAME_KEY = "first_name";
    private static final String LAST_NAME_KEY = "last_name";
    private static final String DISPLAY_NAME_KEY = "display_name";
    private static final String EMAIL_KEY = "email_address";
    private static final String SMTP_KEY = "smtp_host";
    private static final String PORT_KEY = "tls_port";
    private static final String PASSWORD_KEY = "email_password";

    private static MockLoginLogic login;
    private static EmailSetupLogic emailSetup;


    /**
     * Setup method which preserves the contents of the data.json file
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
        emailSetup = new EmailSetupLogic(HELEN_USER, HELEN_PASSWORD);
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
     * Scenario 1: Setup a new email for the first time
     * Given: Helen has opened and logged in to SayIt Assistant 2
     * And: She has not yet set up an email account for sending emails
     * When: Helen presses the “Start” button and says “Setup Email”
     * Then: SayIt Assistant displays a screen for email setup
     * And: The screen prompts Helen to enter her first name, last name, display name, email 
     * address, SMTP host, TLP port, and email password  
     * When: Helen enters her information correctly, (first name being Helen, last name being 
     * Historia, display name being Helen Historia, email address as hishelen@ucsd.edu, SMTP host 
     * as smtp.ucsd.edu, with TLS Port 587)
     * And: She presses “Save”
     * Then: Her email information is saved 
     * And: And the window is closed.
     * And: SayIt Assistant confirms the successful setup in the Response Area with “Successfully 
     * saved the information.”
     * And: The Prompt Area displays “Setup Email”
     * And: The prompt is not saved to the Prompt History.
     */
    @Test
    public void testScenario1() {
        // Verify that requesting for email info will return a JSONObject with the correct keys
        JSONObject emailInfo = emailSetup.getEmailInfo();
        assertTrue(emailInfo.has(LAST_NAME_KEY));
        assertTrue(emailInfo.has(FIRST_NAME_KEY));
        assertTrue(emailInfo.has(DISPLAY_NAME_KEY));
        assertTrue(emailInfo.has(EMAIL_KEY));
        assertTrue(emailInfo.has(SMTP_KEY));
        assertTrue(emailInfo.has(PORT_KEY));
        assertTrue(emailInfo.has(PASSWORD_KEY));

        // Verify that the content of the JSONObject is correct (i.e., blank currently)
        assertEquals("", emailInfo.getString(LAST_NAME_KEY));
        assertEquals("", emailInfo.getString(FIRST_NAME_KEY));
        assertEquals("", emailInfo.getString(DISPLAY_NAME_KEY));
        assertEquals("", emailInfo.getString(EMAIL_KEY));
        assertEquals("", emailInfo.getString(SMTP_KEY));
        assertEquals("", emailInfo.getString(PORT_KEY));
        assertEquals("", emailInfo.getString(PASSWORD_KEY));

        // Helen enters her information correctly
        emailSetup.updateEmailInfo(HELEN_LAST, HELEN_FIRST, HELEN_DISPLAY, HELEN_EMAIL, HELEN_SMTP, HELEN_PORT, HELEN_PASSWORD);

        // Verify that the content of the JSONObject is correct (her info)
        emailInfo = emailSetup.getEmailInfo();
        assertEquals(HELEN_LAST, emailInfo.getString(LAST_NAME_KEY));
        assertEquals(HELEN_FIRST, emailInfo.getString(FIRST_NAME_KEY));
        assertEquals(HELEN_DISPLAY, emailInfo.getString(DISPLAY_NAME_KEY));
        assertEquals(HELEN_EMAIL, emailInfo.getString(EMAIL_KEY));
        assertEquals(HELEN_SMTP, emailInfo.getString(SMTP_KEY));
        assertEquals(HELEN_PORT, emailInfo.getString(PORT_KEY));
        assertEquals(HELEN_PASSWORD, emailInfo.getString(PASSWORD_KEY));
    }

    /**
     * Scenario 2: Canceled email setup
     * Given: Helen has loaded up SayIt Assistant 2
     * And: She has initiated the email setup process
     * When: Helen is on the email setup screen
     * And: She clicks the "Cancel" button
     * Then: SayIt Assistant cancels the email setup process
     * And: No email information is saved
     * And: Helen remains on the main screen of SayIt Assistant
     */
    @Test
    public void testScenario2() {
        
    }
}
