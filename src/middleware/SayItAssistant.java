package middleware;

import java.io.File;
import java.lang.annotation.Target;

import javax.sound.sampled.TargetDataLine;

import backend.*; 

/**
 * This class is responsible for delegating user inputs to the appropriate API
 * request class and returning the response from the API request to the
 * History and UI.
 */
public class SayItAssistant implements IAssistant{
    
    private static final File AUDIO_FILE  = new File("prompt.wav");
    private static final String HISTORY_FILE = "src/backend/history.txt";
    private HistoryGrabber historyGrabber; 
    private IAPIRequest chatRequest;
    private IAPIRequest whisperRequest;
    private Question question;
    private Answer answer;

    /**
     * Constructor for SayItAssistant class
     * 
     * @require VoiceRecorder recorded AudioFile beforehand
     * @param historyGrabber HistoryGrabber object which contains the history of
     *                       questions and answers
     */
    public SayItAssistant(IAPIRequest whisperRequest) {
        this.whisperRequest = whisperRequest;
    }

    /**
     * Gets the prompt from the API request class
     * @return
     */
    private Question getPrompt() {
        whisperRequest = (whisperRequest instanceof MockAPIRequest) ? 
            new MockAPIRequest(AUDIO_FILE) : new WhisperRequest(AUDIO_FILE);
        question = new Question(whisperRequest.callAPI());
        return question;
    }

    /**
     * Gets the response from the API request class
     * @param question
     * @return
     */
    private String getResponse(Question question) {

        // Checks if we are utilizing a MockAPIRequest or a ChatGPTRequest based on whisperRequest
        chatRequest = (whisperRequest instanceof MockAPIRequest) ? 
            new MockAPIRequest(question) : new ChatGPTRequest(question);
        
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
    @Override
    public String[] respond() {
        historyGrabber = new HistoryGrabber(HISTORY_FILE);
        question = getPrompt();
        answer   = new Answer(getResponse(question));

        // Save the question and answer to the history
        historyGrabber.addQuestionAndAnswer(question, answer);

        String[] responseArray = {question.getQuestion(), answer.getAnswer()};

        return responseArray;
    }
}
