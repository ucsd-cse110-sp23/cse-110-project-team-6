package SayItAssistant.frontend;

import javax.swing.*;

public class LoginWindow extends AppPanels {
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton signUpButton;
    private JButton loginButton;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginWindow(){

        // Sets the Username Field
        this.add(usernameLabel = new JLabel("Username: "));
        usernameLabel.setText("Username:");
        usernameLabel.setFont(myFont.getFont());
        this.add(usernameField = new JTextField(20));
        usernameField.setFont(myFont.getFont());

        // Sets the Password Field
        this.add(passwordLabel = new JLabel("Password: "));
        passwordLabel.setText("Password:");
        passwordLabel.setFont(myFont.getFont());
        this.add(passwordField = new JPasswordField(20));
        passwordField.setFont(myFont.getFont());

        // Sets the Login Button
        this.add(loginButton = new JButton("Login"));
        loginButton.setText("Login");
        loginButton.setFont(myFont.getFont());

        // Sets the signup button
        this.add(signUpButton = new JButton("Sign Up"));
        signUpButton.setFont(myFont.getFont());
    }

    public AbstractButton getLoginButton(){
        return loginButton;
    }

    public AbstractButton getSignupButton() {
        return signUpButton;
    }

    public String[] getData(){
        return new String[]{usernameField.getText(), String.valueOf(passwordField.getPassword())};
    }
}
