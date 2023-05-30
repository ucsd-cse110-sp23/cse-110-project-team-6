package SayItAssistant;

import com.sun.net.httpserver.HttpServer;

import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.*;

public class Server {
    private static final String DATA_FILE = System.getProperty("user.dir") + "/data.json";
    private static final int PORT         = 1337;
    private static final String HOST      = "localhost";
    private static HttpServer server;
    
    public static void startServer() throws IOException {
        ThreadPoolExecutor threadPoolExecutor = 
            (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        
        server = HttpServer.create(new InetSocketAddress(HOST, PORT), 0);

        // Write empty json file if it doesn't exist
        if (!Files.exists(Paths.get(DATA_FILE)) ){
            JSONObject testUser = new JSONObject();
            JSONObject userInfo = new JSONObject();
            userInfo.put("password", "password");
            userInfo.put("history", new JSONObject());
            testUser.put("test", userInfo);

            Files.write(Paths.get(DATA_FILE), testUser.toString().getBytes());
        }

        server.createContext("/question", new HandleRequest());
        server.setExecutor(threadPoolExecutor);
        server.start();

        System.out.println("Server started at " + HOST + ":" + PORT);
    }

    /**
     * Function to stop the server
     */
    public static void stopServer() {
        server.stop(0);
    }
}