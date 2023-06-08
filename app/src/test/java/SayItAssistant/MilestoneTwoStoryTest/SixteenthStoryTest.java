package SayItAssistant.MilestoneTwoStoryTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONObject;

import SayItAssistant.middleware.MockLoginLogic;
import SayItAssistant.middleware.EmailSetupLogic;

/**
 * This class is responsible for testing the email setup feature to make sure revisions possible
 * 
 * User Story: As a person with a propensity to misspeak, 
 * I want buttons to cancel or save my email information 
 * so that I can fix it if I stated it incorrectly. 
 */
public class SixteenthStoryTest {
    
    // Constant for the path to the data.json file from the root of the code base
    private static final String CURR_DIR = System.getProperty("user.dir");
    private static final String ROOT_DIR = CURR_DIR.substring(0, CURR_DIR.length() - 4);
    private static final String DATA_PATH = ROOT_DIR + "/data.json";
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
    private static final String NEW_PORT = "577";

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
     * Tests which mocks that the email has already been set up
     */
    public void mockEmailSetup() {
        emailSetup.updateEmailInfo(HELEN_LAST, HELEN_FIRST, HELEN_DISPLAY, HELEN_EMAIL, HELEN_SMTP, HELEN_PORT, HELEN_PASSWORD);
    }

    /**
     * Scenario 1: Editing Email Settings
     * Given: Given: History Helen has already set up her email with SayIt Assistant 2
     * When: Helen says "Setup Email" after correctly using the Start button
     * Then: The email setup screen appears with her previously entered information pre-filled
     * When: Helen makes changes to any of the email settings (first name, last name, display name, 
     * email address, SMTP host, TLS port, or email password) such as changing her TLS port to 577
     * And: She clicks the "Save" button
     * Then: The email settings are updated with the new information
     */
    @Test
    public void testScenario1() {
        mockEmailSetup();

        // Verify that the email setup appears with her previously entered information pre-filled
        JSONObject emailInfo = emailSetup.getEmailInfo();
        assertEquals(HELEN_LAST, emailInfo.getString(LAST_NAME_KEY));
        assertEquals(HELEN_FIRST, emailInfo.getString(FIRST_NAME_KEY));
        assertEquals(HELEN_DISPLAY, emailInfo.getString(DISPLAY_NAME_KEY));
        assertEquals(HELEN_EMAIL, emailInfo.getString(EMAIL_KEY));
        assertEquals(HELEN_SMTP, emailInfo.getString(SMTP_KEY));
        assertEquals(HELEN_PORT, emailInfo.getString(PORT_KEY));
        assertEquals(HELEN_PASSWORD, emailInfo.getString(PASSWORD_KEY));

        // Helen updates her TLS port to 577
        emailSetup.updateEmailInfo(HELEN_LAST, HELEN_FIRST, HELEN_DISPLAY, HELEN_EMAIL, HELEN_SMTP, NEW_PORT, HELEN_PASSWORD);

        // Verify that the content of the JSONObject is correct (her info)
        emailInfo = emailSetup.getEmailInfo();
        assertEquals(HELEN_LAST, emailInfo.getString(LAST_NAME_KEY));
        assertEquals(HELEN_FIRST, emailInfo.getString(FIRST_NAME_KEY));
        assertEquals(HELEN_DISPLAY, emailInfo.getString(DISPLAY_NAME_KEY));
        assertEquals(HELEN_EMAIL, emailInfo.getString(EMAIL_KEY));
        assertEquals(HELEN_SMTP, emailInfo.getString(SMTP_KEY));
        assertEquals(NEW_PORT, emailInfo.getString(PORT_KEY));
        assertNotEquals(HELEN_PORT, emailInfo.getString(PORT_KEY));
        assertEquals(HELEN_PASSWORD, emailInfo.getString(PASSWORD_KEY));
    }
}
