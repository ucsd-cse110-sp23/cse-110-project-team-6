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
    private PromptFactory promptFactory;
    private Object[] prompt_command_pair;
    private IPrompt prompt;
    private IResponse response;
    private String command;

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

    /**
     * Gets the prompt from the API request class
     * @return
     */
    private void getPromptandCommand() {
        //question = new Question(whisperRequest.callAPI());
        prompt_command_pair = promptFactory.createPrompt(whisperRequest.callAPI());
        prompt = (IPrompt) prompt_command_pair[0];
        command = (String) prompt_command_pair[1];
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
        getPromptandCommand();
        String[] responseArray;

        if(command == null) {
            responseArray = new String[]{"No command found.", "Apologies, no valid command was found within your request. I can assist you if you start your requests with Question, Delete Prompt, Clear All, Setup Email, Create Email, or Send Email to <email>."};
            question = new Question (responseArray[0]);
            answer = new Answer (responseArray[1]);
        }

        else {
            answer = new Answer(getAnswer((Question) prompt_command_pair[0]));
            question = new Question(prompt_command_pair[1] + "\n" + prompt_command_pair[0]);
            responseArray = new String[]{question.toString(), answer.toString()};//{question.toString(), answer.toString()};

        }
        System.out.println(responseArray[0] + "\n" + responseArray[1] + "\n");
        notifyObservers(); 
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