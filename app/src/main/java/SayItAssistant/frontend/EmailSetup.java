package SayItAssistant.frontend;

import SayItAssistant.middleware.EmailSetupLogic;

import org.json.JSONObject;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * Class which estalishes the setup for the Email Panels
 */
public class EmailSetup{

    private final String EMAIL_ICON = "images/emailicon.png";

    public EmailSetup(String username, String pwd) {
        int columns = 10;
        JTextField last     = new JTextField(columns);
        JTextField first    = new JTextField(columns);
        JTextField display  = new JTextField(columns);
        JTextField email    = new JTextField(columns);
        JTextField smtp     = new JTextField(columns);
        JTextField tls      = new JTextField(columns);
        JTextField password = new JPasswordField(columns);
        UIManager.put("OptionPane.okButtonText", "Save");

        // Get the email information from the server
        EmailSetupLogic setupLogic = new EmailSetupLogic(username, pwd);

        // Get the JSON object from the server
        JSONObject jsonResponse = setupLogic.getEmailInfo();

        // Access the "userinfo" object within the JSON response
        //JSONObject userInfo = jsonResponse.getJSONObject("userinfo");
        last.setText(jsonResponse.getString("last_name"));
        first.setText(jsonResponse.getString("first_name"));
        display.setText(jsonResponse.getString("display_name"));
        email.setText(jsonResponse.getString("email_address"));
        smtp.setText(jsonResponse.getString("smtp_host"));
        tls.setText(jsonResponse.getString("tls_port"));
        password.setText(jsonResponse.getString("email_password"));

        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10));
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
        
        // Load in the image icon for emails
        ClassLoader classLoader = getClass().getClassLoader();
        ImageIcon imageIcon = new ImageIcon(classLoader.getResource(EMAIL_ICON));
        Image image = imageIcon.getImage();
        Image newimg = image.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newimg);

        // Add logic for Confirmations
        if(JOptionPane.showConfirmDialog(null,panel, "Setup Email", JOptionPane.OK_CANCEL_OPTION,1, imageIcon) == JOptionPane.OK_OPTION) {
            String lasName = last.getText();
            String firName = first.getText();
            String disName = display.getText();
            String emaAdd = email.getText();
            String smtHost = smtp.getText();
            String tlsPort = tls.getText();
            String emaPass = password.getText();

            setupLogic.updateEmailInfo(lasName, firName, disName, emaAdd, smtHost, tlsPort, emaPass);
        }
    }

}