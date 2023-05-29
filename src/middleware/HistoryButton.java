package middleware;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
/*
 * The history button displays the full question and answer associated with it when pressed.
 */
public class HistoryButton extends frontend.AppButtons implements HistoryButtonSubject {
    
    private ArrayList<HistoryButtonObserver> observers;

    /*
     * Creates the history button.
     * 
     * @param id:           the question number
     * @param displayText:  the text to be displayed
     */
    public HistoryButton(int id, String displayText) {
        super(displayText + " ".repeat(100));
        this.buttonWidth = 200;
        this.buttonHeight = 50;
        this.id = id;
        this.observers = new ArrayList<HistoryButtonObserver>();
        this.setBackground(BLACK);
        this.setForeground(LIGHT_GREY);
        setHorizontalAlignment(SwingConstants.LEFT);
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setPreferredSize(new Dimension(buttonWidth, buttonHeight));
    }

    @Override
    public void registerHistoryButtonObserver(HistoryButtonObserver o) {
        observers.add(o);
    }

    @Override
    public void removeHistoryButtonObserver(HistoryButtonObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyHistoryButtonObservers(IPrompt prompt, IResponse response) {
        System.out.println("History Button is notifying observers");
        for (HistoryButtonObserver o : observers) {
            o.update(prompt, response);
        }
    }
    
}
