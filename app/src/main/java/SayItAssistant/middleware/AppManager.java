package SayItAssistant.middleware;
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

import SayItAssistant.frontend.*;

/**
 * Class which manages the logic of the UI of the app. 
 * 
 * Coordinates the various frames and panels of the app.
 * 
 * @field appFrame: the main frame of the app
 * @field historyPanel: the panel which displays the history of questions
 * @field displayPanel: the panel which displays the question and answer
 * @field historyManager: the manager which handles the history of questions
 * @field sayItAssistant: the assistant which handles the logic of the app
 */
public class AppManager implements Observer {
    
    private final String  HOST = "http://localhost:1337/";
    private final String  ENDPOINT = "question";
    private final String USER_PARAM = "?user=";
    private final String PASS_PARAM = "&pass=";

    private AppFrame       appFrame;
    private HistoryPanel   historyPanel;
    private DisplayPanel   displayPanel;
    private LoginWindow    loginWindow;
    private HistoryManager historyManager;
    private SayItAssistant sayItAssistant;
    private boolean        loggedIn;
    private String         currUsername;
    private String         currPassword;

    /*
     * Sets up the empty AppFrame and inner panels
     */
    public AppManager() {
        this.loggedIn     = false;
        this.appFrame     = new AppFrame();
        this.loginWindow  = appFrame.getLoginWindow();
        loginScreen(loggedIn);
    }

    /*
     * Fills in all the history information and gets the logic started.
     */
    public void run() {
        this.historyPanel = appFrame.getHistoryPanel();
        this.displayPanel = appFrame.getDisplayPanel();
        this.sayItAssistant = new SayItAssistant(new MockWhisperRequest());
        this.historyManager = new HistoryManager(this.sayItAssistant, currUsername, currPassword);
        System.out.println("App is now running");
        populateHistoryPanel(); // fills the history panel with buttons for all of the prompts in the history
        populateStartPanel();   // fills the start panel with the start button and its logic
        System.out.println("Everything has been populated");
    }

    /**
     * Runs the logic of logging in
     */
    public void loginScreen(boolean logStatus) {
        if (logStatus) {
            appFrame.setUpPanels();
            appFrame.revalidate();
            run();
        }

        AbstractButton loginWindowButton = loginWindow.getLoginButton();

        // Sets up listeners for activity on the login window
        loginWindowButton.addActionListener(e -> {
            
            String username = loginWindow.getData()[0];
            String password = loginWindow.getData()[1];

            boolean verifiedLogin = checkValid(username, password);

            if (verifiedLogin) {
                loggedIn = true;
                appFrame.closeLoginWindow();
                appFrame.setUpPanels();
                appFrame.revalidate();
                run();
            }
        });

        AbstractButton signUpButton = loginWindow.getSignupButton();

        // Sets up listeners for signup button
        signUpButton.addActionListener(e -> {

            String username = loginWindow.getData()[0];
            String password = loginWindow.getData()[1];

            boolean verifiedSignup = signUp(username, password);

            if (verifiedSignup) {
                loggedIn = true;
                appFrame.closeLoginWindow();
                appFrame.setUpPanels();
                appFrame.revalidate();
                run();
            }
        });
        
        return;
    }

    /**
     * Checks if the username and password are valid
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
        } catch (IOException e) {
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
     * @param username Username to check
     * @param password Password associated to username
     * @return True if valid, false otherwise
     * @throws IOException
     * @throws InterruptedException
     */
    private boolean signUp(String username, String password) {

        // Validate that username and password are not empty strings
        if (username.equals("") || password.equals("")) {
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
                currUsername = username;
                currPassword = password;
                return true;
            }
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
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

        historyPanel.removeAll();

        ArrayList<IPrompt> questions = historyManager.getPrompts();

        // Creates button to get the previous prompt and response for each question
        for (int i = 0; i < questions.size(); i++) {

            // Sets prompt associated with button
            IPrompt prompt = questions.get(i);

            // Set the question with its index from the history
            prompt.setPromptNumber(i);

            // Set the response associated with the prompt
            IResponse response = historyManager.getResponse(i);

            HistoryButton historyButton = new HistoryButton(i, prompt, response);
            historyButton.setFont(historyPanel.myFont.getFont());

            historyButton.registerObserver(displayPanel.getPromptAndResponsePanel());
            
            // updates the question and answer panels when clicked
            historyButton.addActionListener(e -> {
                historyButton.notifyObservers(); 
            });

            historyPanel.addHistoryButton(historyButton); // add the button to the display
        }

        appFrame.revalidate();
        System.out.println("History populated");

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
        startButton.registerObserver(displayPanel.getPromptAndResponsePanel());
        displayPanel.addStartButton(startButton);

        appFrame.revalidate();
        System.out.println("Start panel populated");
    }

    /*
     * When a new prompt is created, the history panel is updated.
     */
    @Override
    public void update(IPrompt prompt, IResponse response) {
        populateHistoryPanel();
    }
}
