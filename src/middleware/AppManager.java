package middleware;
import java.util.ArrayList;

import frontend.*;

public class AppManager implements StartButtonObserver{
    
    private AppFrame appFrame;
    private HistoryPanel historyPanel;
    private DisplayPanel displayPanel;
    private HistoryManager historyManager;
    private SayItAssistant sayItAssistant;

    /*
     * Sets up the empty AppFrame and inner panels
     */
    public AppManager() {

        this.appFrame = new AppFrame(); 
        this.historyPanel = appFrame.getHistoryPanel();
        this.displayPanel = appFrame.getDisplayPanel();

        this.sayItAssistant = new SayItAssistant(new WhisperRequest());
        this.historyManager = new HistoryManager(this.sayItAssistant);

        // this statement starts the main functionality and logic of the app
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
     * Creates buttons for each prompt in the history and then puts the buttons in the history panel.
     */
    public void populateHistoryPanel() {

        historyPanel.removeAll();

        ArrayList<Question> questions = historyManager.getQuestions();  // list of the questions that have been asked

        // for each question, creates a button that when clicked displays the full question and the answer associated with it
        for (int i = 0; i < questions.size(); i++) {

            // sets up the question and answer that are to be associated with the button
            IPrompt prompt = questions.get(i);

            //set the question with its index from the history:
            prompt.setPromptNumber(i);
            IResponse response = historyManager.getAnswer(i);
            HistoryButton historyButton = new HistoryButton(i, prompt.toString());
            historyButton.registerHistoryButtonObserver(displayPanel.getPromptAndResponsePanel());
            historyButton.setFont(historyPanel.myFont.getFont());
            
            // updates the question and answer panels when clicked
            historyButton.addActionListener(e -> {
                historyButton.notifyHistoryButtonObservers(prompt, response); 
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
        startButton.registerStartButtonObserver(this);
        startButton.registerStartButtonObserver(displayPanel.getPromptAndResponsePanel());
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
