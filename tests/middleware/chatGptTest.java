package middleware;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.Test;

class mockChatGPT implements iChatGpt{
    String prompt;
    mockChatGPT(String prompt){
        this.prompt = prompt;
    }
    @Override
    public String getAsnwer() throws IOException, InterruptedException {
        return prompt; 
    }

}


public class chatGptTest {
    @Test
    public void testObject() throws IOException, InterruptedException{
        mockChatGPT testObject = new mockChatGPT("what is the meaning of life?");
        String expected = "what is the meaning of life?";
        assertEquals(expected, testObject.getAsnwer());
    }
}
