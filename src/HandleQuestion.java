import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.net.URLDecoder;
import middleware.Answer;
import middleware.Question;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HandleQuestion implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "Request Received";
        String method = exchange.getRequestMethod();
        if(method.equals("POST")) {
            response = handlePost(exchange);
        }
        else if(method.equals("GET")) {
            response = handleGet(exchange);
        }
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    private static String IStoStr(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = br.readLine()) != null)
            sb.append(line);
        return sb.toString();
    }
    private String handlePost(HttpExchange exchange) throws IOException {
        FileWriter file = new FileWriter("data.txt", true);
        file.write(IStoStr(exchange.getRequestBody())+"\n");
        file.close();
        return "posted";
    }

    private String handleGet(HttpExchange exchange) throws IOException {
        String question = exchange.getRequestURI().getQuery();

        Map<String,String> params = parseQueryParams(question);
        StringBuilder answer = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader("data.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                answer.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return answer.toString();
    }
    private static Map<String, String> parseQueryParams(String query) throws UnsupportedEncodingException {
        Map<String, String> queryParams = new HashMap<>();

        if (query != null) {
            String[] pairs = query.split("&");

            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");

                queryParams.put(key, value);
            }
        }

        return queryParams;
    }
}
