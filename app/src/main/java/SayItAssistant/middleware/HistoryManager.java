package SayItAssistant.middleware;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.MalformedParametersException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.io.*;

/**
 * Class responsible for grabbing history of prompts and responses
 * from the JSON and storing it within the History class.
 */
public class HistoryManager implements Subject, Observer {
    
    private final String HOST = "http://localhost:1337/";
    private final String ENDPOINT = "question";
    private final String USER_PARAM = "?user=";
    private final String PASS_PARAM = "&pass=";

    private static final String HISTORY_DIR = System.getProperty("user.dir");
    private static final String HISTORY_PATH = HISTORY_DIR + "/history.json";
    private JSON_IO jsonIO;
    private SayItAssistant assistantSubject;
    private LinkedHashMap<Integer, PromptResponsePair> history;
    private ArrayList<IPrompt> prompts;
    private ArrayList<Observer> observers;
    
    /**
     * Constructor for HistoryManager class
     * @param HISTORY_PATH path to the JSON file containing the history of
     */
    public HistoryManager(SayItAssistant assistantSubject, String username, String password) {
        System.out.println("Path of history file: " + HISTORY_PATH);
        observers = new ArrayList<Observer>();
        jsonIO    = new JSON_IO(username,password);
        history = jsonIO.readHistory();
        this.assistantSubject = assistantSubject;
        this.assistantSubject.registerObserver(this);
    }

    /**
     * Getter method for the response to a given prompt indicated by that prompt's number
     * @require promptNum >= 0 && promptNum < history.size() && knowledge of promptNum
     * @param promptNum index of the prompt in the history of all prompts asked
     * @return Response object containing the response to the given prompt
     */
    public IResponse getResponse(int promptNum) {
        return history.get(promptNum).getResponse();
    }

    /**
     * Getter method for the list of all prompts asked
     * @return ArrayList<Prompt> list of all prompts asked
     */
    public ArrayList<IPrompt> getPrompts() {
        return prompts;
    }

    /**
     * Getter method for the number of prompts asked
     * @return int number of prompts asked
     */
    public int getHistorySize() {
        return history.size();
    }

    /**
     * Registers the observer to the list of observers
     */
    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    /**
     * Removes the observer from the list of observers
     */
    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    /**
     * Notifies all observers of the change in the history of prompts and responses
     */
    @Override
    public void notifyObservers() {
        IPrompt   recentPrompt = null;
        IResponse recentResponse = null;
        if (history.size() != 0) {
            recentPrompt   = history.get(history.size() - 1).getPrompt();
            recentResponse     = history.get(history.size() - 1).getResponse();
        }
        
        for (Observer o : observers) {
            o.update(recentPrompt, recentResponse);
        }
    }

    /**
     * Updates the history of prompts and responses with the most recent prompt and response
     * @require newPrompt != null && newResponse != null && 
     *          newPrompt instanceof Prompt && newResponse instanceof Response
     * @param prompt Prompt object containing the prompt asked
     * @param response Response object containing the response to the prompt asked
     */
    public void add(IPrompt newPrompt, IResponse newResponse) {
        if (newPrompt == null || newResponse == null) {
            throw new MalformedParametersException("Attempted use of null ArrayList.");
        } else {
            int newIdx = history.size();
            PromptResponsePair newQA = new PromptResponsePair(newPrompt, newResponse);
            history.put(newIdx, newQA);
            notifyObservers();
            prompts.add(newPrompt);
        }
    }

    /**
     * Updates the history of prompts and responses with the most recent prompt and response
     * @param promptNum
     */
    @Override
    public void update(IPrompt newPrompt, IResponse newResponse) {
        add(newPrompt, newResponse);
    }

    /**
     * Deletes the prompt and response indicated by the prompt number
     * @require promptNum >= 0 && promptNum < history.size() && knowledge of promptNum
     * @param prompt
     */
    public void delete(int promptNum) {
        // Store responses to rebuild history
        ArrayList<IResponse> responses = new ArrayList<IResponse>();
        for (Integer i : history.keySet()) { responses.add(history.get(i).getResponse()); }
        
        history.remove(promptNum);
        prompts.remove(promptNum);
        responses.remove(promptNum);

        // Rebuild the history to update the indices
        history = new LinkedHashMap<Integer, PromptResponsePair>();
        for (int i = 0; i < prompts.size(); i++) {
            history.put(i, new PromptResponsePair(prompts.get(i), responses.get(i)));
        }
        notifyObservers();
    }

    /**
     * Clears all prompts and responses from the history
     * @param prompt
     */
    public void clearAll() {
        if (history.size() == 0) { return; }
        
        // Delete backwards to avoid index out of bounds
        for (int i = history.size() - 1; i >= 0; i--) {
            delete(i);
        }
    }

        /**
     * Nested class of HistoryManager responsible for reading and writing
     * the history of prompts and responses to a JSON file
     */
    private class JSON_IO implements Observer {
        private Subject historySubject;
        private JSONObject storedJSON;
        private String username,password;
        /**
         * Constructor for JSON_IO class which initializes the relevant fields for the class and
         * History Manager
         * 
         * @param HISTORY_PATH path to the JSON file containing the history of prompts and responses
         */
        private JSON_IO(String username,String password) {
            try {
                HistoryManager.this.prompts = new ArrayList<IPrompt>();
                historySubject = HistoryManager.this;
                historySubject.registerObserver(this);
                this.username = username;
                this.password = password;
                storedJSON = openJSON();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Open JSON file and read in the history of prompts and responses as JSONObject
         * Creates the directory and file for history if they do not exist
         * 
         * @param HISTORY_PATH path to the JSON file containing the history of prompts and responses
         */
        private JSONObject openJSON() throws IOException, InterruptedException {

            // Getting history from user account from server
            URL url = new URL(HOST + ENDPOINT + USER_PARAM + username + PASS_PARAM + password);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
            String serverResponse = in.readLine();
            in.close();

            // If the file does not exist, create it
            if (serverResponse == null) {
                try (FileWriter f = new FileWriter(HISTORY_PATH)) {
                    f.write("{}");
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                return new JSONObject("{}");
            }

            // Debugging purposes to see what the server response is
            //System.out.println(serverResponse);

            // Writes history locally to file
            JSONObject json = new JSONObject(serverResponse);
            try (FileWriter f = new FileWriter(HISTORY_PATH)) {
                f.write(json.toString());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return json;
        }

        /**
         * Updates the JSON file with the most recent prompt and response
         * 
         * @require args[0] instanceof PromptResponsePair
         * @param args Object... args containing the history of prompts and responses
         */
        @Override
        public void update(IPrompt newPrompt, IResponse newResponse) {
            storedJSON = new JSONObject();
            for (int i = 0; i < history.size(); i++) {
                PromptResponsePair qaPair = history.get(i);
                add(qaPair.getPrompt(), qaPair.getResponse());
            }
            write();
        }

        /**
         * Reads in the history of prompts and responses from the JSON file
         * @return LinkedHashMap<Integer, PromptResponsePair> history of prompts and responses
         */
        public LinkedHashMap<Integer, PromptResponsePair> readHistory() {
            LinkedHashMap<Integer, PromptResponsePair> history 
                = new LinkedHashMap<Integer, PromptResponsePair>();

            if (storedJSON == null) {
                return history;
            }

            JSONArray storedQA = storedJSON.names();
            
            if (storedQA == null) {
                return history;
            }

            // Iterate through the JSON and split it into prompts and responses
            for (int idx = 0; idx < storedQA.length(); idx++) {
                JSONObject commandPromptResponse = storedJSON.getJSONObject(Integer.toString(idx));

                IPrompt promptObj = null;
                IResponse responseObj = null;

                String command = commandPromptResponse.getString("Command");

                switch (command) {
                    case "Question":
                        promptObj = new Question(commandPromptResponse.getString("Prompt"));
                        responseObj = new Answer(commandPromptResponse.getString("Response"));
                        break;
                }

                // Tracks prompts in order for HistoryManager
                HistoryManager.this.prompts.add(promptObj);
                PromptResponsePair qaPair = new PromptResponsePair(promptObj, responseObj);

                history.put(idx, qaPair);
            }

            return history;
        }

        /**
         * Updates the stored JSON with the most recent prompt and response
         * @require newPrompt != null && newResponse != null &&
         *          newPrompt instanceof Prompt && newResponse instanceof Response
         * @param newPrompt Prompt object containing the prompt asked
         * @param newResponse Response object containing the response to the prompt asked
         */
        public void add(IPrompt newPrompt, IResponse newResponse) {
            int idx = storedJSON.length();

            JSONObject commandPromptResponse = new JSONObject();

            // Look at the type of the prompt to figure out the command associated 
            if (newPrompt instanceof Question) {
                commandPromptResponse.put("Command", "Question");
            } else {
                commandPromptResponse.put("Command", "None");
            }

            commandPromptResponse.put("Prompt", newPrompt.toString());
            commandPromptResponse.put("Response", newResponse.toString());

            storedJSON.put(Integer.toString(idx), commandPromptResponse);
        }

        /**
         * Writes the history of prompts and responses to a JSON file
         */
        public void write() {

            // Writes the JSON to the local file
            try {
                Files.write(Paths.get(HISTORY_PATH), storedJSON.toString().getBytes());

                // Writes the JSON to the server
                URL url = new URL(HOST + ENDPOINT + USER_PARAM + username + PASS_PARAM + password);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("PUT");
                conn.setDoOutput(true);

                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());

                // Write the JSON to the server via PUT request

                out.write(storedJSON.toString());
                out.flush();
                out.close();

                BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
                String serverResponse = in.readLine();
                in.close();

                System.out.println(serverResponse);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}