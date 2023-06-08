package SayItAssistant.middleware;

import java.util.ArrayList;

import javax.swing.*;

// Java IO imports
import java.util.List;

import SayItAssistant.frontend.*;

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

    private AppFrame       appFrame;
    private HistoryPanel   historyPanel;
    private DisplayPanel   displayPanel;
    private LoginWindow    loginWindow;
    private HistoryManager historyManager;
    private SayItAssistant sayItAssistant;
    private LoginLogic     loginLogic;
    private static String  currUsername;
    private static String  currPassword;
    private static int     recentPromptNumber;
    /*
     * Sets up the empty AppFrame and inner panels
     */
    public AppManager() {
        this.appFrame = new AppFrame();
        this.loginLogic = LoginLogic.getInstance();
        this.loginWindow = appFrame.getLoginWindow();
        loginScreen();
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
        populateHistoryPanel(); // fills the history panel with buttons for all of the prompts in the history
        populateStartPanel();   // fills the start panel with the start button and its logic
    }


    /**
     * Runs the logic of logging in
     */
    public void loginScreen() {
        List<String> loginInfo = this.loginLogic.retrieveLogin();

        if (!loginInfo.isEmpty() && this.loginLogic.checkValid(loginInfo.get(0), loginInfo.get(1))) {
            currUsername = loginInfo.get(0);
            currPassword = loginInfo.get(1);
            run();
        }

        AbstractButton loginWindowButton = loginWindow.getLoginButton();
        AbstractButton signUpButton      = loginWindow.getSignupButton();

        // Sets up listeners for activity on the login window
        loginWindowButton.addActionListener(e -> {

            String username = loginWindow.getData()[0];
            String password = loginWindow.getData()[1];

            boolean verifiedLogin = this.loginLogic.checkValid(username, password);

            if (verifiedLogin) {
                //saveLogin();
                this.loginLogic.saveLogin(loginWindow.getRememberMe(), username, password);
                EmailSetupLogic emailSetupLogic = new EmailSetupLogic(username, password);
                emailSetupLogic.updateName(username, password);
                currUsername = username;
                currPassword = password;
                run();
            }
        });

        // Sets up listeners for signup button
        signUpButton.addActionListener(e -> {

            String username = loginWindow.getData()[0];
            String password = loginWindow.getData()[1];

            if (this.loginLogic.checkAvailableUsername(username)) {
                String verification = JOptionPane.showInputDialog("Please re-enter your password");
                if (!this.loginLogic.checkPassword(verification, password)) {
                    return;
                }

                boolean verifiedSignup = this.loginLogic.signUp(username, password);

                if (verifiedSignup) {
                    this.loginLogic.saveLogin(loginWindow.getRememberMe(), username, password);
                    EmailSetupLogic emailSetupLogic = new EmailSetupLogic(username, password);
                    emailSetupLogic.updateName(username, password);
                    currUsername = username;
                    currPassword = password;
                    run();
                }
            }
        });

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
        populateHistoryPanel();
        if ((prompt != null) && (prompt.isStorable())) {
            setRecentPromptNumber(prompt.getPromptNumber());
        }
    }
}
