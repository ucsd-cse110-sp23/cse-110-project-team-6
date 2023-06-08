package SayItAssistant.frontend;

import javax.swing.*;

public class LoginWindow extends AppPanels {
    private final JButton signUpButton;
    private final JButton loginButton;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JCheckBox checkBox = new JCheckBox();

    public LoginWindow() {
        // Sets the Username Field
        JLabel usernameLabel;
        this.add(usernameLabel = new JLabel("Username: "));
        usernameLabel.setText("Username:");
        usernameLabel.setFont(myFont.getFont());
        this.add(usernameField = new JTextField(20));
        usernameField.setFont(myFont.getFont());

        // Sets the Password Field
        JLabel passwordLabel;
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

        JLabel rememberMe = new JLabel("Remember Me");
        this.add(rememberMe);
        this.add(checkBox);
        rememberMe.setFont(myFont.getFont());
        revalidate();
    }

    public AbstractButton getLoginButton() {
        return loginButton;
    }

    public AbstractButton getSignupButton() {
        return signUpButton;
    }

    public String[] getData() {
        return new String[]{usernameField.getText(), String.valueOf(passwordField.getPassword())};
    }

    public boolean getRememberMe() {
        return checkBox.isSelected();
    }
}
