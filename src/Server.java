import com.sun.net.httpserver.HttpServer;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.*;
public class Server {
    private static final int PORT = 1337;
    private static final String HOST = "localhost";
    public static void main(String[] args) throws IOException {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        HttpServer server = HttpServer.create(new InetSocketAddress(HOST, PORT), 0);

        //write empty json file if it doesn't exist
        if (!Files.exists(Paths.get("data.json"))){
            FileWriter f = new FileWriter("data.json");
            f.write("{\"a\":{\"password\":\"1\",\"history\":{}}}");
            f.close();
        }
        server.createContext("/question", new HandleQuestion());
        server.setExecutor(threadPoolExecutor);
        server.start();

        System.out.println("Server started at " + HOST + ":" + PORT);
    }

}
