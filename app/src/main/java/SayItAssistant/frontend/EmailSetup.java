package SayItAssistant.frontend;

import java.awt.GridLayout;
import java.awt.Image;
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
import javax.swing.UIManager;

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
        UIManager.put("OptionPane.okButtonText", "Save");
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(String.format("https://hlnm.pythonanywhere.com/emails?user=%s&pass=%s", username, pwd)))
            .build();

        client.sendAsync(request, BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(responseBody -> {
                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(responseBody);

                // Access the "userinfo" object within the JSON response
                //JSONObject userInfo = jsonResponse.getJSONObject("userinfo");
                last.setText(jsonResponse.getString("last_name"));
                first.setText(jsonResponse.getString("first_name"));
                display.setText(jsonResponse.getString("display_name"));
                email.setText(jsonResponse.getString("email_address"));
                smtp.setText(jsonResponse.getString("smtp_host"));
                tls.setText(jsonResponse.getString("tls_port"));
                password.setText(jsonResponse.getString("email_password"));
            })
            .join();
            
        
        JPanel panel = new JPanel(new GridLayout(7,1,10,10));
        panel.add(new JLabel("First Name"));
        panel.add(first);
        panel.add(new JLabel("Last Name"));
        panel.add(last);
        panel.add(new JLabel("Display Name"));
        panel.add(display);
        panel.add(new JLabel("Email Address"));
        panel.add(email);
        panel.add(new JLabel("SMTP Host"));
        panel.add(smtp);
        panel.add(new JLabel("TLS Port"));
        panel.add(tls);
        panel.add(new JLabel("Email Password"));
        panel.add(password);       
        ImageIcon imageIcon = new ImageIcon("emailicon.png");
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newimg);
        if(JOptionPane.showConfirmDialog(null,panel, "Setup Email", JOptionPane.OK_CANCEL_OPTION,1, imageIcon) == JOptionPane.OK_OPTION){
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
            //HttpClient client = HttpClient.newHttpClient();
            HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create(String.format("https://hlnm.pythonanywhere.com/emails?user=%s&pass=%s",username,pwd)))
                .header("Content-Type", "application/json")
                .PUT(BodyPublishers.ofString(jsonString))
                .build();
            client.sendAsync(request1, BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();
        }
    }

}