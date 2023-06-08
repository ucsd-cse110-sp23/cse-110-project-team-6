package SayItAssistant.frontend;

import java.util.ArrayList;
import javax.swing.*;

import SayItAssistant.middleware.IPrompt;
import SayItAssistant.middleware.IResponse;
import SayItAssistant.middleware.Observer;
import SayItAssistant.middleware.Subject;

import java.awt.*;

/*
 * The history button displays the full question and answer associated with it when pressed.
 */
public class HistoryButton extends AppButtons implements Subject {

    private final ArrayList<Observer> observers;
    private final IPrompt prompt;
    private final IResponse response;

    /*
     * Creates the history button.
     *
     * @param id:           the question number
     * @param displayText:  the text to be displayed
     */
    public HistoryButton(int id, IPrompt displayText, IResponse newResponse) {
        super("");
        this.setText(getDisplayString(displayText));
        prompt = displayText;
        response = newResponse;
        this.buttonWidth = 200;
        this.buttonHeight = 50;
        this.id = id;
        this.observers = new ArrayList<Observer>();
        this.setBackground(BLACK);
        this.setForeground(WHITE);
        setHorizontalAlignment(SwingConstants.LEFT);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setPreferredSize(new Dimension(buttonWidth, buttonHeight));
    }

    /*
     * Formats the string in the history button so that the command and part of prompt are 
     * displayed, but there is not a lot of text.
     */
    private String getDisplayString(IPrompt displayText) {
        String displayString = "<html>" + displayText.getMessage() + "<br>" + displayText.toString() + "</html>";
        if (displayString.length() > 30) {
            displayString = displayString.substring(0,30);
            displayString += "...";
        }
        return displayString;
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(prompt, response);
        }
    }

}
