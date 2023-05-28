package frontend;

import javax.swing.*;

public class LoginWindow extends AppPanels {
    JLabel usernameLabel;
    JLabel passwordLabel;
    JButton loginButton;
    JTextField usernameField;
    JPasswordField passwordField;

    public LoginWindow(){

        this.add(usernameLabel = new JLabel("Username: "));

        usernameLabel.setText("Username:");
        this.add(usernameField = new JTextField(20));
        this.add(passwordLabel = new JLabel("Password: "));
        passwordLabel.setText("Password:");
        this.add(passwordField = new JPasswordField(20));
        this.add(loginButton = new JButton("Login"));
        loginButton.setText("Login");
        usernameLabel.setFont(myFont.getFont());
        passwordLabel.setFont(myFont.getFont());
        loginButton.setFont(myFont.getFont());
        usernameField.setFont(myFont.getFont());
        passwordField.setFont(myFont.getFont());
    }

    public AbstractButton getButton(){
        return loginButton;
    }
}
