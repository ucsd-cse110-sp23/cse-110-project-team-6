package SayItAssistant.middleware;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Mock class for the LoginLogic class. Used for testing purposes. Entirely the same besides
 * JOptionPanes
 */
public class MockLoginLogic {
    
    // Serverside constants
    private final static String HOST = "http://127.0.0.1:5000/";
    private final static String ENDPOINT = "question";
    private final static String TEST_ENDPOINT = "test";
    private final static String USER_PARAM = "?user=";
    private final static String PASS_PARAM = "&pass=";

    // Error messages
    private final static String NO_LOGIN_FILE     = "No login file found";
    private final static String EMPTY_CREDENTIALS = "Username and password cannot be empty";
    private final static String BAD_CREDENTIALS   = "Incorrect username or password";
    private final static String TAKEN_USERNAME    = "Username is not available";
    private final static String BAD_URL           = "Invalid URL: ";
    private final static String CONNECTION_ERROR  = "Server Error. Please try again later.";
    private final static String DEFAULT_ERROR = "An error has occurred. Please try again later.";
    
    // Success messages
    private final static String SUCCESSFUL_SIGNUP = "Account Created Successfully";

    // Location of Saved Login File
    private final static String LOGIN_FILE = "login.txt";

    // Singleton instance
    private static MockLoginLogic instance = null;

    private boolean loggedIn = false;

    private MockLoginLogic() {
        this.loggedIn = false;
    }

    /**
     * Returns the singleton instance of the class
     * @return Singleton instance of the class
     */
    public static MockLoginLogic getInstance() {
        if (instance == null) {
            instance = new MockLoginLogic();
        }
        return instance;
    }

    /**
     * Deconstructs the singleton instance of the class (simulates logout)
     */
    public static void deconstruct() {
        instance = null;
    }

    /**
     * Saves the login information to a file
     */
    public void saveLogin(boolean remembered, String username, String password) {
        if (remembered) {
            // Base64 encoding
            String encodedUsername = Base64.getEncoder().encodeToString(username.getBytes());
            String encodedPassword = Base64.getEncoder().encodeToString(password.getBytes());

            // Save to file
            try {
                FileWriter fw = new FileWriter(LOGIN_FILE);
                fw.write(encodedUsername + "\n");
                fw.write(encodedPassword + "\n");
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Retrieves login information from the file
     */
    public List<String> retrieveLogin() {
        List<String> loginInfo = new ArrayList<>();
        // Read from file login.txt
        try {
            loginInfo = Files.readAllLines(Paths.get(LOGIN_FILE));
            // Base64 decode
            for (int i = 0; i < loginInfo.size(); i++) {
                loginInfo.set(i, new String(Base64.getDecoder().decode(loginInfo.get(i))));
            }
            loggedIn = true;
        } catch (IOException e) {
            System.out.println(NO_LOGIN_FILE);
        }
        return loginInfo;
    }

    /**
     * Checks if a username and password combination is valid
     * 
     * @param username Username to check
     * @param password Password to check
     * @return True if valid, false otherwise
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean checkValid(String username, String password) {
        // Validate that the username and password are not empty strings
        if (username.equals("") || (password.equals(""))) {
            System.err.println(EMPTY_CREDENTIALS);
            return false;
        }

        // Connect to the Server and check if the username and password combination is valid
        String urlString = HOST + ENDPOINT + USER_PARAM + username + PASS_PARAM + password;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Retrieve the response message
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String serverResponse = in.readLine();
            in.close();

            if (serverResponse.equals("Incorrect") || serverResponse.equals("None")) {
                System.err.println(BAD_CREDENTIALS);
                return false;
            } else {
                loggedIn = true;
                return true;
            }
        } catch (MalformedURLException e) {
            System.err.println(BAD_URL);
            return false;
        } catch (ConnectException e) {
            System.err.println(CONNECTION_ERROR);
            return false;
        } catch (Exception e) {
            System.err.println(DEFAULT_ERROR);
            return false;
        }
    }

    /**
     * Checks if username and password are valid for signup, and signs them up
     * 
     * @param username Username to check
     * @param password Password to check
     * @return True if valid, false otherwise
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean signUp(String username, String password) {

        // Validate that the username and password are not empty strings
        if (username.equals("") || (password.equals(""))) {
            System.err.println(EMPTY_CREDENTIALS);
            return false;
        }

        String urlString = HOST + ENDPOINT + USER_PARAM + username + PASS_PARAM + password;

        try {
            // Sign up the user
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");

            // Retrieve the response message
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String serverResponse = in.readLine();
            in.close();

            if (serverResponse.equals("Taken")) {
                System.err.println(TAKEN_USERNAME);
                return false;
            } else {
                System.out.println(SUCCESSFUL_SIGNUP);
                loggedIn = true;
                return true;
            }
        } catch (MalformedURLException e) {
            System.err.println(BAD_URL);
            return false;
        } catch (ConnectException e) {
            System.err.println(CONNECTION_ERROR);
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println(DEFAULT_ERROR);
            return false;
        }
    }

    /**
     * Check available username
     * 
     * @param username Username to check
     * @return True if valid, false otherwise
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean checkAvailableUsername(String username) {
        // Validate that username is not empty
        if (username.equals("")) {
            System.err.println(EMPTY_CREDENTIALS);
            return false;
        }

        String urlString = HOST + ENDPOINT + USER_PARAM + username;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Retrieve the response message
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String serverResponse = in.readLine();
            in.close();

            // String is None if the username is not taken
            if (!serverResponse.equals("None")) {
                System.err.println(TAKEN_USERNAME);
            }
            return serverResponse.equals("None");

        } catch (MalformedURLException e) {
            System.err.println(BAD_URL);
            return false;
        } catch (ConnectException e) {
            System.err.println(CONNECTION_ERROR);
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println(DEFAULT_ERROR);
            return false;
        }
    }

    /**
     * Check if the user is logged in (used for testing)
     * 
     * @return True if logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Resets the login status (used for testing)
     */
    public void resetServer() {
        String urlString = HOST + TEST_ENDPOINT;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.getInputStream();

        } catch (MalformedURLException e) {
            System.err.println(BAD_URL);
        } catch (ConnectException e) {
            System.err.println(CONNECTION_ERROR);
        } catch (Exception e) {
            System.err.println(DEFAULT_ERROR);
        }
    }
}
