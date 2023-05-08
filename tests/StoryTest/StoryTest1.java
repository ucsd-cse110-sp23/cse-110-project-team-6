package StoryTest;

import javax.sound.sampled.*; // For sound recording and playback
import org.junit.jupiter.api.*;

import backend.Answer;
import backend.History;
import backend.Question;
import middleware.IAPIRequest;
import middleware.MockTargetDataLine;
import middleware.VoiceRecorder;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;


class MockWhisperAPIQ1 implements IAPIRequest{
    File file; 

    MockWhisperAPIQ1(File filePath){
        this.file = filePath;
    }

    @Override
    public String callAPI() {
        return "Who is Louis Braille?";
    }
    
}

class MockChatGPTAPIQ1 implements IAPIRequest{
    String prompt; 

    MockChatGPTAPIQ1(String prompt){
        this.prompt = prompt; 
    }
    @Override
    public String callAPI() {
        return "He was a french educator";
    }
    
}

class MockWhisperAPIQ2 implements IAPIRequest{
    File file; 

    MockWhisperAPIQ2(File filePath){
        this.file = filePath;
    }

    @Override
    public String callAPI() {
        return "How did Louis Braille come up with the braille system?";
    }
    
}

class MockChatGPTAPIQ2 implements IAPIRequest{
    String prompt; 

    MockChatGPTAPIQ2(String prompt){
        this.prompt = prompt; 
    }
    @Override
    public String callAPI() {
        return "He just did";
    }
    
}
public class StoryTest1 {

    MockTargetDataLine mockMic;
    VoiceRecorder newAudio; 
    History previousQuestions;
    Path path; 

    @BeforeEach
    public void initialize(){
        mockMic = new MockTargetDataLine();
        newAudio = new VoiceRecorder(mockMic);
        previousQuestions = new History(); 
    }

    @Test
    public void BDD1(){
        assertEquals(0, previousQuestions.numberOfQuestions());

        //testing audio
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

        //testing Whisper API
        path = Path.of("prompt.wav");
        assertTrue(Files.exists(path));

        File mockFile = new File("prompt.wav");
        MockWhisperAPIQ1 mockWhipser = new MockWhisperAPIQ1(mockFile);
        assertEquals("Who is Louis Braille?", mockWhipser.callAPI());

        //test ChatGPT API
        MockChatGPTAPIQ1 mockCHPT = new MockChatGPTAPIQ1("Who is Louis Braille");
        assertEquals("He was a french educator", mockCHPT.callAPI());


        //test adding to history 1
        Question question = new Question("Who is Louis Braille");
        Answer answer = new Answer("He was a french educator");
        previousQuestions.addQuestionAndAnswer(question, answer);
        assertEquals(1, previousQuestions.numberOfQuestions());

        //whisper call 2
        File mockFile2 = new File("prompt.wav");
        MockWhisperAPIQ2 mockWhipser2 = new MockWhisperAPIQ2(mockFile2);
        assertEquals("How did Louis Braille come up with the braille system?", mockWhipser2.callAPI());

        //chatGPT 2
        MockChatGPTAPIQ2 mockCHPT2 = new MockChatGPTAPIQ2("How did Louis Braille come up with the braille system?");
        assertEquals("He just did", mockCHPT2.callAPI());

        //adding to history 2
        Question question2 = new Question("How did Louis Braille come up with the braille system?");
        Answer answer2 = new Answer("He just did");
        previousQuestions.addQuestionAndAnswer(question2, answer2);
        assertEquals(2, previousQuestions.numberOfQuestions());

    }
        


}   
