package SayItAssistant;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;


//HTTPHandler for /question
/**
 * Handler for Requests to the server via endpoint /question
 */
public class HandleRequest implements HttpHandler {

    private static final String DATA = "data.json";
    private static final String USER_PARAM = "user";
    private static final String PASSWORD_PARAM = "pass";

    /**
     * handles requests to /question
     * @param exchange the exchange containing the request from the
     *                 client and used to send the response
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "Request Received";
        String method = exchange.getRequestMethod();
        
        try {
            if(method.equals("POST")) {
                response = handlePost(exchange);
            } else if(method.equals("PUT")) {
                response = handlePut(exchange);
            } else if(method.equals("GET")) {
                response = handleGet(exchange);
            } else if (method.equals("DELETE")) {
                response = handleDelete(exchange);
            } else {
                throw new Exception("Not valid Request Method");
            }
        } catch (Exception e) {
            System.out.println("An erroneous request");
            response = e.toString();
            e.printStackTrace();
        }

        // Send response back to client
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    /**
     * Converts an InputStream to a String
     * @param is InputStream to be converted
     * @return String of the InputStream
     * @throws IOException
     */
    private static String IStoStr(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb  = new StringBuilder();
        
        String line;
        while((line = br.readLine()) != null)
            sb.append(line);
        
        return sb.toString();
    }

    /**
     * Posts the new account as a valid user
     * @param exchange
     * @return
     * @throws IOException
     */
    private String handlePost(HttpExchange exchange) throws IOException {
        String question           = exchange.getRequestURI().getQuery();
        Map<String,String> params = parseQueryParams(question);
        String content            = new String(Files.readAllBytes(Paths.get(DATA)));
        JSONObject json           = new JSONObject(content);

        // Verifies that username is not already taken
        if (!json.has(params.get(USER_PARAM))){
            JSONObject userData = new JSONObject();
            userData.put("password", params.get(PASSWORD_PARAM));
            userData.put("history",new JSONObject());
            json.put(params.get(USER_PARAM), userData);
            FileWriter file = new FileWriter(DATA);
            file.write(json.toString());
            file.close();
            return "Created";
        } else { // Username already taken
            return "Taken";
        }
    }

    /**
     * Update the history of the user  
     * 
     * @require the user exists and the password is correct
     * @param exchange http exchange containing the request
     * @return String of the response (not used)
     */
    private String handlePut(HttpExchange exchange) throws IOException {
        String question = exchange.getRequestURI().getQuery();
        String body     = IStoStr(exchange.getRequestBody());
        
        JSONObject history        = new JSONObject(body);
        Map<String,String> params = parseQueryParams(question);
        String content            = new String(Files.readAllBytes(Paths.get(DATA)));
        
        JSONObject json = new JSONObject(content);
        
        String password = json.getJSONObject(params.get(USER_PARAM)).getString("password");
        
        if(password.equals(params.get(PASSWORD_PARAM))){
            json.getJSONObject(params.get(USER_PARAM)).put("history", history);
            FileWriter file = new FileWriter(DATA);
            file.write(json.toString());
            file.close();
            return "Placed";
        }

        return "Taken";
    }

    /**
     * Handles GET requests
     * If the username and pwd match, the history is returned
     * If user does not exist and new is true, the user is added
     * If wrong password or user does not exist, "Incorrect" is returned
     *
     * @param exchange http exchange containing the request
     * @return String of the response
     */
    private String handleGet(HttpExchange exchange) throws IOException {
        String question = exchange.getRequestURI().getQuery();
        Map<String,String> params = parseQueryParams(question);
        String content = new String(Files.readAllBytes(Paths.get(DATA)));
        JSONObject json = new JSONObject(content);

        // Handles signing up of new users
        if(params.containsKey("new")){

            // Verifies that username is not already taken
            if(!json.has(params.get(USER_PARAM))){
                JSONObject userData = new JSONObject();
                userData.put("password", params.get(PASSWORD_PARAM));
                userData.put("history",new JSONObject());
                json.put(params.get(USER_PARAM), userData);
                FileWriter file = new FileWriter(DATA);
                file.write(json.toString());
                file.close();
                return "True";
            } else { // Username already taken
                return "Taken";
            }
        }

        // Handles login of existing users
        if(json.has(params.get(USER_PARAM))){
            // True password
            String password = json.getJSONObject(params.get(USER_PARAM)).getString("password");
            // Verifies if the password is correct
            if(password.equals(params.get(PASSWORD_PARAM))){
                return json.getJSONObject(params.get(USER_PARAM)).getJSONObject("history").toString();
            }
        }   
        return "Incorrect";
    }

    /**
     * Handles DELETE requests
     * @param exchange http exchange containing the request
     * @return
     */
    private String handleDelete(HttpExchange exchange) throws IOException {
        String question = exchange.getRequestURI().getQuery();
        String body     = IStoStr(exchange.getRequestBody());
        
        JSONObject history        = new JSONObject(body);
        Map<String,String> params = parseQueryParams(question);
        String content            = new String(Files.readAllBytes(Paths.get(DATA)));
        
        JSONObject json = new JSONObject(content);
        
        String password = json.getJSONObject(params.get(USER_PARAM)).getString("password");
        
        if(password.equals(params.get(PASSWORD_PARAM))){
            json.getJSONObject(params.get(USER_PARAM)).put("history", history);
            FileWriter file = new FileWriter(DATA);
            file.write(json.toString());
            file.close();
            return "Deleted";
        }

        return "Incorrect";
    }


    //getting query parameters from the url into a Map
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
