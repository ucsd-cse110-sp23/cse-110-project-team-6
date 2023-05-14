package middleware;

import java.util.ArrayList;

/**
 * This class is responsible for delegating user inputs to the appropriate API
 * request class and returning the response from the API request to the
 * History and UI.
 */
public class SayItAssistant implements Subject {
    
    private IAPIRequest chatRequest, whisperRequest;
    private Question question;
    private Answer answer;
    private ArrayList<Observer> observers;

    /**
     * Constructor for SayItAssistant class
     * 
     * @require VoiceRecorder recorded AudioFile beforehand
     * @param whisperRequest WhisperRequest object which contains the whisper request
     * @param historyManager HistoryManager object which contains the history of
     *                       questions and answers
     */
    public SayItAssistant(IAPIRequest whisperRequest) {
        observers = new ArrayList<Observer>();
        this.whisperRequest = whisperRequest;
    }

    /**
     * Gets the prompt from the API request class
     * @return
     */
    private Question getPrompt() {
        question = new Question(whisperRequest.callAPI());
        return question;
    }

    /**
     * Gets the response from the API request class
     * @param question
     * @return
     */
    private String getAnswer(Question question) {
        // Checks if we are utilizing a MockAPIRequest or a ChatGPTRequest based on whisperRequest
        chatRequest = (whisperRequest instanceof MockWhisperRequest) ? 
            new MockChatGPTRequest(question) : new ChatGPTRequest(question);
        
        String response = chatRequest.callAPI();
        return response;
    }

    /**
     * This method is responsible for delegating user inputs to the appropriate
     * API request class and returning the response from the API request to the
     * History and UI.
     * 
     * @param file File object containing the audio file
     * @return String containing the response from the API request
     */
    public String[] respond() {
        question       = getPrompt();
        answer         = new Answer(getAnswer(question));

        // Notify that new question was made
        notifyObservers();

        String[] responseArray = {question.toString(), answer.toString()};

        return responseArray;
    }

    /**
     * Registers an observer
     * @param o
     */
    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    /**
     * Removes an observer
     * @param o
     */
    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    /**
     * Notifies observers
     */
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(question, answer);
        }
    }
}
