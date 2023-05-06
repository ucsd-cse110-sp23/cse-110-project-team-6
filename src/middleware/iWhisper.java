package middleware;
import java.io.*;
import java.net.*;
import org.json.*;
import org.junit.runner.Request;

public interface iWhisper {
    public String getPrompt() throws IOException;
}
