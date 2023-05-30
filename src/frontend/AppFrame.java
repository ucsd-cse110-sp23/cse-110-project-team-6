package frontend;

import middleware.HistoryManager;
import middleware.SayItAssistant;
import middleware.WhisperRequest;

import javax.swing.*;

import org.json.JSONObject;

import java.awt.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/*
 * The AppFrame is the overall skeleton of the app, providing the structure and delegating function.
 */
public class AppFrame extends JFrame {

    // formatting
    private final String HISTORY_FONT_FILE = "src/fonts/OpenSans-Regular.ttf";
    private final String ICON_FILE         = "src/icon.png";
    private final String APPFRAME_TITLE    = "SayIt Assistant";
    private final float  FONT_SIZE         = 16f;
    private final int    APPFRAME_WIDTH    = 1280;
    private final int    APPFRAME_HEIGHT   = 720;
    
    // paneling
    private DisplayPanel displayPanel;
    private HistoryPanel historyPanel;
    private SayItAssistant sayItAssistant;
    private HistoryManager historyManager;
    private boolean loggedIn = false;
    /**
     * Constructor for AppFrame class which coordinates the GUI
     */
    public AppFrame() {
        // Get the relative path of HISTORY_FONT_FILE
        File file = new File(HISTORY_FONT_FILE);
        String font_file = file.getAbsolutePath();
        // Sets the font, overall display panel, and the history sidepanel
        MyFont myFont = new MyFont (font_file, FONT_SIZE);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(myFont.getFont());
        setInformation();
        LoginWindow loginWindow = new LoginWindow();
        this.add(loginWindow);
        AbstractButton loginWindowButton = loginWindow.getButton();
        loginWindowButton.addActionListener(e -> {
            try {
                String username = loginWindow.getData()[0];
                String password = loginWindow.getData()[1];
                loggedIn = this.checkValid(username,password);
                if(loggedIn){
                    this.remove(loginWindow);
                    setupQuestion(username, password);
                    revalidate();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } 
            
        });
        loginWindow.signUpButton.addActionListener(e -> {
            try {
                String username = loginWindow.getData()[0];
                String password = loginWindow.getData()[1];
                loggedIn = this.signUp(username,password);
                if(loggedIn){
                    this.remove(loginWindow);
                    setupQuestion(username, password);
                    revalidate();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } 
            
        });
        revalidate();
    }

    private void setupQuestion(String username, String password){
        // Initalizes the history manager and SayIt Assistant for Panels
        sayItAssistant = new SayItAssistant(new WhisperRequest());
        historyManager = new HistoryManager(sayItAssistant, username, password);

        // Sets the font, overall display panel, and the history sidepanel
        setUpPanels();

        // adds the history panel and display panel to the appframe
        this.add(historyPanel.getScrollPane(), BorderLayout.WEST);
        this.add(displayPanel, BorderLayout.CENTER);
    }
    /**
     * Sets the information for the appframe
     */
    private void setInformation() {
        this.setTitle(APPFRAME_TITLE);
        this.setSize(APPFRAME_WIDTH, APPFRAME_HEIGHT);
        this.setLayout(new BorderLayout());
        this.setIconImage(new ImageIcon(ICON_FILE).getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Sets up the panels for the appframe
     */
    private void setUpPanels() {
        historyPanel = new HistoryPanel(historyManager);
        displayPanel = new DisplayPanel(sayItAssistant, historyPanel, historyManager);
        historyPanel.revalidateHistory(displayPanel.getQnAPanel());
    }

    /**
     * Checks if username and password are valid
     * @param userName
     * @param userPassword
     * @return true if the user is valid, false otherwise
     */
    private boolean checkValid(String userName, String userPassword) throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(String.format("http://localhost:1337/question?u=%s&p=%s", URLEncoder.encode(userName,"UTF-8"), URLEncoder.encode(userPassword,"UTF-8"))))
        .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        return !body.equals("Incorrect");
    }

    /**
     * Checks if username and password are valid for signup
     * @param userName
     * @param userPassword
     * @return true if the sign-up is successful, false otherwise
     */
    private boolean signUp(String userName, String userPassword) throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(String.format("http://localhost:1337/question?u=%s&p=%s&new=true", URLEncoder.encode(userName,"UTF-8"), URLEncoder.encode(userPassword,"UTF-8"))))
        .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();
        if(body.equals("Incorrect")){
            return false;
        }else{
            return true;
        }
    }
}