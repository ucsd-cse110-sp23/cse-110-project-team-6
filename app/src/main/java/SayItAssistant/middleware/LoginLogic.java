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

import javax.swing.JOptionPane;

/**
 * Class which contains all the functionality of determining what login behaviors should be executed
 * 
 * Maintains a Singleton instance of the class as there should not be more than one instance
 */
public class LoginLogic {
    
    private final static String HOST = "http://127.0.0.1:5000/";
    //private final String HOST = "https://hlnm.pythonanywhere.com/";
    private final static String ENDPOINT = "question";
    private final static String USER_PARAM = "?user=";
    private final static String PASS_PARAM = "&pass=";
    // Singleton instance
    private static LoginLogic instance = null;

    private LoginLogic() {}

    /**
     * Returns the singleton instance of the class
     * @return Singleton instance of the class
     */
    public static LoginLogic getInstance() {
        if (instance == null) {
            instance = new LoginLogic();
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
                FileWriter fw = new FileWriter("login.txt");
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
            loginInfo = Files.readAllLines(Paths.get("login.txt"));
            // Base64 decode
            for (int i = 0; i < loginInfo.size(); i++) {
                loginInfo.set(i, new String(Base64.getDecoder().decode(loginInfo.get(i))));
            }
        } catch (IOException e) {
            System.out.println("No login file found");
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
                JOptionPane.showMessageDialog(null, "Incorrect username or password");
                return false;
            } else {
                return true;
            }
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, "Bad URL: " + urlString);
            return false;
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(null, "Error connecting to server. Please try again.");
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error logging in. Please try again.");
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
            JOptionPane.showMessageDialog(null, "Username and password cannot be empty");
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
                JOptionPane.showMessageDialog(null, "Username is taken");
                return false;
            } else {
                JOptionPane.showMessageDialog(null, "Account created");
                return true;
            }
        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, "Bad URL: " + urlString);
            return false;
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(null, "Server is not running");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error signing up");
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
            JOptionPane.showMessageDialog(null, "Username cannot be empty");
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
                JOptionPane.showMessageDialog(null, "Username is taken");
            }
            return serverResponse.equals("None");

        } catch (MalformedURLException e) {
            JOptionPane.showMessageDialog(null, "Bad URL: " + urlString);
            return false;
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(null, "Server is not running");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error signing up");
            return false;
        }
    }
}
