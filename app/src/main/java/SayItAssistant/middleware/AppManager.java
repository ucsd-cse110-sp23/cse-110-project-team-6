package SayItAssistant.middleware;
import java.util.ArrayList;

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
    
    private AppFrame       appFrame;
    private HistoryPanel   historyPanel;
    private DisplayPanel   displayPanel;
    private HistoryManager historyManager;
    private SayItAssistant sayItAssistant;

    /*
     * Sets up the empty AppFrame and inner panels
     */
    public AppManager() {

        this.appFrame     = new AppFrame(); 
        this.historyPanel = appFrame.getHistoryPanel();
        this.displayPanel = appFrame.getDisplayPanel();

        this.sayItAssistant = new SayItAssistant(new WhisperRequest());
        this.historyManager = new HistoryManager(this.sayItAssistant);

        // Starts main functionality of the app
        run();
    }

    /*
     * Fills in all the history information and gets the logic started.
     */
    public void run() {
        System.out.println("App is now running");
        populateHistoryPanel();
        populateStartPanel();
        System.out.println("Everything has been populated");
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
