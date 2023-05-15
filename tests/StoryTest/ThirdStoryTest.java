package StoryTest;

import javax.sound.sampled.*; // For sound recording and playback
import org.junit.jupiter.api.*;

import frontend.HistoryPanel;
import frontend.NewQuestionPanel;
import frontend.QnAPanel;
import middleware.Answer;
import middleware.HistoryManager;
import middleware.IAPIRequest;
import middleware.MockTargetDataLine;
import middleware.Question;
import middleware.SayItAssistant;
import middleware.VoiceRecorder;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class StoryTestThree {

     class MockWhisperAPIQ1 implements IAPIRequest{
        File file; 
    
        MockWhisperAPIQ1(File filePath){
            this.file = filePath;
        }
    
        @Override
        public String callAPI() {
            return "Summarize the history of inequality of the visually impaired in relation to literacy before Braille";
        }
        
    }
    
    class MockChatGPTAPIQ1 implements IAPIRequest{
        String prompt; 
    
        MockChatGPTAPIQ1(String prompt){
            this.prompt = prompt; 
        }
        @Override
        public String callAPI() {
            return "There are many things to list";
        }
        
    }

    MockTargetDataLine mockMic;
    VoiceRecorder newAudio; 
    HistoryManager previousQuestions;
    Path path; 

    @BeforeEach
    public void initialize(){
        mockMic = new MockTargetDataLine();
        newAudio = new VoiceRecorder(mockMic);
    }

    /*
     * Scenario 1: Voice prompt button is pressed to ask a new question
     * Given: History Helen has pressed on the new question button
     * When: History Helen holds the voice prompt button 
     * And: Helen starts asking the SayIt Assistant the prompt “Summarize the history of inequality of the visually impaired…”
     * Then: SayIt Assistant’s voice prompt button changes color 
     * And: Helen’s speech is recorded
     * When: History Helen continues to expand on her question by asking “...in relation to literacy before Braille.”
     * Then: Helen’s speech will still be recorded
     */
    @Test
    public void TestBDD1() {
        AudioFormat format = newAudio.getAudioFormat();
        assertNotNull(format);
        assertEquals(44100, format.getSampleRate(), 0.1);
        assertEquals(16, format.getSampleSizeInBits());
        assertEquals(1, format.getChannels());
        assertEquals(true, format.isBigEndian());

        newAudio.startRecording();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        newAudio.stopRecording();

        path = Path.of("prompt.wav");
        assertTrue(Files.exists(path));

        File mockFile = new File("prompt.wav");
        MockWhisperAPIQ1 mockWhipser = new MockWhisperAPIQ1(mockFile);
        assertEquals("Summarize the history of inequality of the visually impaired in relation to literacy before Braille", mockWhipser.callAPI());

        SayItAssistant sayItAssistant = new SayItAssistant(mockWhipser);
        previousQuestions = new HistoryManager(sayItAssistant);
        NewQuestionPanel newQuestionPanel = new NewQuestionPanel(sayItAssistant, new QnAPanel(), new HistoryPanel(previousQuestions));

        
    }
    
    /*
     * Given: History Helen has finished asking a question about “Summarizing the history of inequality of the visually impaired in relation to literacy before Braille” 
     * When: History Helen releases the voice prompt button
     * Then: SayIt Assistant transcribes her question.
     * And: SayIt Assistant displays the answer to her question.
     */
    @Test
    public void TestBDD2() {

    }

}
