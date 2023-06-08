package SayItAssistant.middleware;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.*;

import java.io.BufferedReader;
// Java IO imports
import java.io.IOException;
import java.io.InputStreamReader;


// Java net imports
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.List;

import SayItAssistant.frontend.*;

import javax.swing.*;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;

/**
 * Class which manages the logic of the UI of the app.
 * <p>
 * Coordinates the various frames and panels of the app.
 *
 * @field appFrame: the main frame of the app
 * @field historyPanel: the panel which displays the history of questions
 * @field displayPanel: the panel which displays the question and answer
 * @field historyManager: the manager which handles the history of questions
 * @field sayItAssistant: the assistant which handles the logic of the app
 */
public class AppManager implements Observer {

    private final String HOST = "http://127.0.0.1:5000/";
    //private final String HOST = "https://hlnm.pythonanywhere.com/";
    private final String ENDPOINT = "question";
    private final String USER_PARAM = "?user=";
    private final String PASS_PARAM = "&pass=";

    private AppFrame       appFrame;
    private HistoryPanel   historyPanel;
    private DisplayPanel   displayPanel;
    private LoginWindow    loginWindow;
    private HistoryManager historyManager;
    private SayItAssistant sayItAssistant;
    private static String  currUsername;
    private static String  currPassword;
    private static int     recentPromptNumber;
    /*
     * Sets up the empty AppFrame and inner panels
     */
    public AppManager() {
        this.appFrame = new AppFrame();
        this.loginWindow = appFrame.getLoginWindow();
        loginScreen();
    }

    /*
     * Fills in all the history information and gets the logic started.
     */
    public void run() {
        appFrame.closeLoginWindow();
        appFrame.setUpPanels();
        appFrame.revalidate();

        this.historyPanel = appFrame.getHistoryPanel();
        this.displayPanel = appFrame.getDisplayPanel();
        this.sayItAssistant = new SayItAssistant(new WhisperRequest()/*new MockWhisperRequest()*/);
        this.historyManager = new HistoryManager(this.sayItAssistant, currUsername, currPassword);
        this.sayItAssistant.setHistoryManager(historyManager);
        historyManager.registerObserver(this);
        System.out.println("App is now running");
        populateHistoryPanel(); // fills the history panel with buttons for all of the prompts in the history
        populateStartPanel();   // fills the start panel with the start button and its logic
        System.out.println("Everything has been populated");
    }


    /**
     * Saves the login information to a file
     */
    public void saveLogin() {
        if (loginWindow.getRememberMe()) {
            //base64 encode
            String encodedUsername = Base64.getEncoder().encodeToString(currUsername.getBytes());
            String encodedPassword = Base64.getEncoder().encodeToString(currPassword.getBytes());
            //save to file
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
     * Retrieves the login information from the file
     */
    public List<String> retrieveLogin() {
        List<String> loginInfo = new ArrayList<>();
        //read from file login.txt
        try {
            loginInfo = Files.readAllLines(Paths.get("login.txt"));
            //base64 decode
            for (int i = 0; i < loginInfo.size(); i++) {
                loginInfo.set(i, new String(Base64.getDecoder().decode(loginInfo.get(i))));
            }
        } catch (IOException e) {
            System.out.println("No login file found");
        }
        return loginInfo;
    }

    /**
     * Runs the logic of logging in
     */
    public void loginScreen() {
        List<String> loginInfo = LoginLogic.retrieveLogin();

        if (!loginInfo.isEmpty() && LoginLogic.checkValid(loginInfo.get(0), loginInfo.get(1))) {
            run();
        }

        AbstractButton loginWindowButton = loginWindow.getLoginButton();

        // Sets up listeners for activity on the login window
        loginWindowButton.addActionListener(e -> {

            String username = loginWindow.getData()[0];
            String password = loginWindow.getData()[1];

            boolean verifiedLogin = LoginLogic.checkValid(username, password);

            if (verifiedLogin) {
                //saveLogin();
                LoginLogic.saveLogin(loginWindow.getRememberMe(), username, password);
                updateName(username, password);
                currUsername = username;
                currPassword = password;
                run();
            }
        });

        AbstractButton signUpButton = loginWindow.getSignupButton();

        // Sets up listeners for signup button
        signUpButton.addActionListener(e -> {

            String username = loginWindow.getData()[0];
            String password = loginWindow.getData()[1];

            if (LoginLogic.checkAvailableUsername(username)) {
                String verification = JOptionPane.showInputDialog("Please re-enter your password");
                if (!password.equals(verification)) {
                    JOptionPane.showMessageDialog(null, "Passwords do not match");
                    return;
                }

                boolean verifiedSignup = LoginLogic.signUp(username, password);

                if (verifiedSignup) {
                    LoginLogic.saveLogin(loginWindow.getRememberMe(), username, password);
                    updateName(username, password);
                    currUsername = username;
                    currPassword = password;
                    run();
                }
            }
        });

    }

    /**
     * Sets up an email data storage for the user
     * @param username
     * @param pwd
     */
    public static void updateName(String username, String pwd) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(String.format("https://hlnm.pythonanywhere.com/emails?user=%s&pass=%s", username, pwd)))
            .build();

        client.sendAsync(request, BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(responseBody -> {
                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(responseBody);
                String name = jsonResponse.getString("display_name");
                try {
                    FileWriter fw = new FileWriter("name.txt");
                    fw.write(name);
                    fw.close();
                } catch (IOException e) {
                    System.out.println("writing name error");
                }
            })
            .join();       
    }
    
    /**
     * Checks if the username and password are valid
     *
     * @param username Username to check
     * @param password Password associated to username
     * @return True if valid, false otherwise
     * @throws IOException
     * @throws InterruptedException
     */
    private boolean checkValid(String username, String password) {

        try {
            URL url =
                    new URL(HOST + ENDPOINT + USER_PARAM + username + PASS_PARAM + password);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String serverResponse = in.readLine();
            in.close();

            if (serverResponse.equals("Incorrect")) {
                JOptionPane.showMessageDialog(null, "Incorrect username or password");
                return false;
            } else {
                currUsername = username;
                currPassword = password;
                return true;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(null, "Server is not running");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error logging in");
            return false;
        }
    }

    /**
     * Checks if username and password are valid for signup
     *
     * @param username Username to check
     * @param password Password associated to username
     * @return True if valid, false otherwise
     * @throws IOException
     * @throws InterruptedException
     */
    private boolean signUp(String username, String password, boolean checkTaken) {

        // Validate that username and password are not empty strings
        if (username.equals("") || (password.equals("") && !checkTaken)) {
            JOptionPane.showMessageDialog(null, "Username and password cannot be empty");
            return false;
        }

        try {
            URL url =
                    new URL(HOST + ENDPOINT + USER_PARAM + username + PASS_PARAM + password);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String serverResponse = in.readLine();
            in.close();

            System.out.println(serverResponse);
            if (serverResponse.equals("Taken")) {
                JOptionPane.showMessageDialog(null, "Username already exists");
                return false;
            } else {
                if (!checkTaken) {
                    currUsername = username;
                    currPassword = password;
                }
                return true;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
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

    /*
     * Creates buttons for each prompt in the history
     * and then puts the buttons in the history panel.
     */
    public void populateHistoryPanel() {
        //System.out.println("Removing all");
        historyPanel.removeAll();
        historyPanel.revalidate();
        appFrame.revalidate();
        ArrayList<IPrompt> prompts = historyManager.getPrompts();

        // Creates button to get the previous prompt and response for each question
        for (int i = 0; i < prompts.size(); i++) {
            //System.out.println ("Creating button " + i);
            // Sets prompt associated with button
            IPrompt prompt = prompts.get(i);

            // Set the question with its index from the history
            prompt.setPromptNumber(i);

            // Set the response associated with the prompt
            IResponse response = historyManager.getResponse(i);

            HistoryButton historyButton = new HistoryButton(i, prompt, response);
            historyButton.setFont(historyPanel.myFont.getFont());

            historyButton.registerObserver(displayPanel);
            // updates the question and answer panels when clicked
            historyButton.addActionListener(e -> {
                historyButton.notifyObservers(); 
                AppManager.setRecentPromptNumber(prompt.getPromptNumber());
            });

            historyPanel.addHistoryButton(historyButton); // add the button to the display
        }

        setRecentPromptNumber(-1);

        historyPanel.revalidate();
        appFrame.revalidate();
    }

    /*
     * Sets up the start button and puts it into the start panel.
     */
    public void populateStartPanel() {
        StartButton startButton = new StartButton(sayItAssistant);

        // the order that the observers are registered below is important!!!
        // If the order changes, the new prompt will not be displayed and the
        // user will instead have to click on it in the history panel to see it.
        startButton.registerObserver(this);
        startButton.registerObserver(displayPanel);
        displayPanel.addStartButton(startButton);

        appFrame.revalidate();
        //System.out.println("Start panel populated");
    }

    /*
     * When a new prompt is created, the history panel is updated.
     */
    @Override
    public void update(IPrompt prompt, IResponse response) {
        if ((prompt != null) && (prompt.isStorable())) {
            recentPromptNumber = prompt.getPromptNumber();
        }
        populateHistoryPanel();
    }

    public static int getRecentPromptNumber() {
        return recentPromptNumber;
    }

    public static void setRecentPromptNumber(int newPromptNumber) {
        //System.out.println("New prompt #: " + newPromptNumber);
        recentPromptNumber = newPromptNumber;
    }

    public static String getUsername() {
        return currUsername;
    }

    public static String getPassword() {
        return currPassword;
    }
}
