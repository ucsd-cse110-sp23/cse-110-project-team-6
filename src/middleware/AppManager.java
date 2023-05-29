package middleware;
import java.util.ArrayList;

import frontend.*;

public class AppManager {
    
    private AppFrame appFrame;
    private HistoryPanel historyPanel;
    private PromptAndResponsePanel promptAndResponsePanel;
    private HistoryManager historyManager;
    private SayItAssistant sayItAssistant;

    public AppManager() {
        this.sayItAssistant = new SayItAssistant(new WhisperRequest());
        this.historyManager = new HistoryManager(this.sayItAssistant);
        this.appFrame = new AppFrame(sayItAssistant, historyManager); 
        this.historyPanel = appFrame.getHistoryPanel();
        this.promptAndResponsePanel = appFrame.getPromptAndResponsePanel();
        run();
    }

    public void run() {
        populateHistoryPanel();
    }

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
            historyButton.registerHistoryButtonObserver(promptAndResponsePanel);
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


}
