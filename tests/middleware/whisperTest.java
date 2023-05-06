package middleware;

import org.junit.jupiter.api.Test;


import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

class mockWhipser implements iWhisper{
    public static String handleErrorResponse(){
        return "ERROR";
    }
    @Override
    public String getPrompt() throws IOException {
        return "I am the question Prompt!";
    }

}

public class whisperTest {
    mockWhipser test = new mockWhipser();
    String actual = "";
    String expected = "I am the question Prompt";

    @Test
    public void testGetPrompt(){
        try {
            actual = test.getPrompt();
         } catch (Exception e) {
             assertFalse(true);
         }
    }

    @Test
    public void testErrorHandle(){
        String expected = mockWhipser.handleErrorResponse();
        assertEquals("ERROR", expected);
    }
    
}
