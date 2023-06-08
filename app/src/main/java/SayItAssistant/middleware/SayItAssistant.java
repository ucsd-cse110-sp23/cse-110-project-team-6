package SayItAssistant.middleware;

import java.util.ArrayList;

/**
 * This class is responsible for delegating user inputs to the appropriate API
 * request class and returning the response from the API request to the
 * History and UI.
 */
public class SayItAssistant implements Subject {

    private final String NO_COMMAND_WARN = "No command found";
    private final String NO_COMMAND_RESPONSE = 
    "Apologies, no valid command was found within your request. I can assist you if you start your requests with Question, Delete Prompt, Clear All, Setup Email, Create Email, or Send Email to <email>.";

    private IAPIRequest whisperRequest;
    private ArrayList<Observer> observers;
    private PromptFactory promptFactory;
    private ResponseCoordinator responseCoordinator;
    private IPrompt   prompt;
    private IResponse response;

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
        this.promptFactory = new PromptFactory();
    }

    public void setHistoryManager(HistoryManager history) {
        this.responseCoordinator = new ResponseCoordinator(history, whisperRequest);

    }

    /**
     * Gets the prompt from the API request class
     * @return
     */
    private IPrompt getPrompt() {
        return promptFactory.createPrompt(whisperRequest.callAPI());
    }

    /**
     * Gets the response from the API request class
     * @param question
     * @return
     */
    private IResponse getResponse(IPrompt prompt) {
        // Checks if we are utilizing a MockAPIRequest or a ChatGPTRequest based on whisperRequest
       /*chatRequest = (whisperRequest instanceof MockWhisperRequest) ? 
            new MockChatGPTRequest(prompt) : new ChatGPTRequest(prompt);
        
        String response = chatRequest.callAPI();
        return new Answer(response);*/
        return responseCoordinator.createResponse(prompt);
    }

    /**
     * This method is responsible for delegating user inputs to the appropriate
     * API request class and returning the response from the API request to the
     * History and UI.
     * 
     * @param file File object containing the audio file
     * @return String containing the response from the API request
     */
    public PromptResponsePair respond() {
        PromptResponsePair promptResponse;
        prompt = getPrompt();

        if (prompt != null) {
            response = getResponse(prompt);
            promptResponse = new PromptResponsePair(prompt, response);
            notifyObservers();
        }

        else {
            Question warning       = new Question(NO_COMMAND_WARN);
            Answer   warning_reply = new Answer(NO_COMMAND_RESPONSE);
            promptResponse = new PromptResponsePair(warning, warning_reply);
        }

        return promptResponse;
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
            observer.update(prompt, response);
        }
    }
}