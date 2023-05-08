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
    
    private static final File AUDIO_FILE  = new File("src/middleware/prompt.wav");
    private HistoryGrabber historyGrabber = new HistoryGrabber("src/backend/history.txt");
    private IAPIRequest chatRequest;
    private IAPIRequest whisperRequest;
    private Question question;
    private Answer answer;
    private String response;
    private TargetDataLine targetDataLine;
    private VoiceRecorder recorder = new VoiceRecorder(targetDataLine);

    /**
     * Constructor for SayItAssistant class
     * 
     * @param historyGrabber HistoryGrabber object which contains the history of
     *                       questions and answers
     */
    public SayItAssistant(IAPIRequest chatRequest, IAPIRequest whisperRequest) {
        this.chatRequest = chatRequest;
        this.whisperRequest = whisperRequest;
    }

    public void startRecording() {
        recorder.startRecording();
    }

    public void stopRecording() {
        recorder.stopRecording();
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
        if (whisperRequest instanceof MockAPIRequest)
            whisperRequest = new MockAPIRequest(AUDIO_FILE);
        else if (whisperRequest instanceof WhisperRequest)
            whisperRequest = new WhisperRequest(AUDIO_FILE);
        response = whisperRequest.callAPI();
        question = new Question(response);
        answer = historyGrabber.getAnswer(question);

        if (answer == null) {
            if (chatRequest instanceof MockAPIRequest)
                chatRequest = new MockAPIRequest(question);
            else if (chatRequest instanceof ChatGPTRequest)
                chatRequest = new ChatGPTRequest(question);
            response = chatRequest.callAPI();
            answer = new Answer(response);
            historyGrabber.addQuestionAndAnswer(question, answer);
        } else {
            response = answer.getAnswer();
        }

        String[] responseArray = {question.getQuestion(), response};

        return responseArray;
    }
}
