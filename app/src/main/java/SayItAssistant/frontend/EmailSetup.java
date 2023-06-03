package SayItAssistant.frontend;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.json.JSONObject;

public class EmailSetup{


    public EmailSetup(String username, String pwd){
        int columns = 10;
        JTextField last = new JTextField(columns);
        JTextField first = new JTextField(columns);
        JTextField display = new JTextField(columns);
        JTextField email = new JTextField(columns);
        JTextField smtp = new JTextField(columns);
        JTextField tls = new JTextField(columns);
        JTextField password = new JPasswordField(columns);

        JPanel panel = new JPanel();
        panel.add(new JLabel("First Name"));
        panel.add(first);
        panel.add(new JLabel("Last Name"));
        panel.add(last);
        panel.add(new JLabel("display name"));
        panel.add(display);
        panel.add(new JLabel("email address"));
        panel.add(email);
        panel.add(new JLabel("SMTP host"));
        panel.add(smtp);
        panel.add(new JLabel("TLS Port"));
        panel.add(tls);
        panel.add(new JLabel("email password"));
        panel.add(password);       
        Icon i = new ImageIcon("emailicon.png");
        if(JOptionPane.showConfirmDialog(null,panel, "Setup Email", JOptionPane.OK_CANCEL_OPTION,1, i) == JOptionPane.OK_OPTION){
            // Create the JSON object
            JSONObject jsonObject = new JSONObject();
            // Get the text from each JTextField and add it to the JSON object
            String lastName = last.getText();
            jsonObject.put("last_name", lastName);

            String firstName = first.getText();
            jsonObject.put("first_name", firstName);

            String displayName = display.getText();
            jsonObject.put("display_name", displayName);

            String emailAddress = email.getText();
            jsonObject.put("email_address", emailAddress);

            String smtpHost = smtp.getText();
            jsonObject.put("smtp_host", smtpHost);

            String tlsPort = tls.getText();
            jsonObject.put("tls_port", tlsPort);

            String emailPassword = password.getText();
            jsonObject.put("email_password", emailPassword);

            // Convert the JSON object to a string
            String jsonString = jsonObject.toString();

            // Print the JSON string
            System.out.println(jsonString);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("https://hlnm.pythonanywhere.com/emails?user=%s&pass=%s",username,pwd)))
                .header("Content-Type", "application/json")
                .PUT(BodyPublishers.ofString(jsonString))
                .build();
            client.sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();
        }
    }

}