package org.client.chatapp.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField phoneField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;

    @FXML
    public void initialize() {
        // Initialize any default values or listeners here
        errorLabel.setVisible(false);

        // Add input validation listener for phone number (digits only)
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phoneField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            // Limit to 10 digits for Egyptian phone numbers
            if (newValue.length() > 10) {
                phoneField.setText(newValue.substring(0, 10));
            }
        });
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        // Hide previous error messages
        errorLabel.setVisible(false);

        String phone = phoneField.getText().trim();
        String password = passwordField.getText();

        // Validation
        if (phone.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        if (phone.length() != 10) {
            showError("Phone number must be 10 digits");
            return;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            return;
        }

        // Construct full phone number with country code
        String fullPhone = "+20" + phone;

        // TODO: Implement actual login logic here
        // For now, just print to console
        System.out.println("Login attempt:");
        System.out.println("Phone: " + fullPhone);
        System.out.println("Password: " + password);

        // Example: Call authentication service
        // boolean success = authService.login(fullPhone, password);
        // if (success) {
        //     navigateToMainApp();
        // } else {
        //     showError("Invalid credentials");
        // }

        // Placeholder success message
        showError("Login functionality not yet implemented");
    }

    @FXML
    private void handleForgotPassword(ActionEvent event) {
        System.out.println("Forgot password clicked");
        // TODO: Navigate to forgot password screen
        showError("Forgot password feature coming soon");
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        System.out.println("Sign up clicked");
        // TODO: Navigate to sign up screen
        // Example:
        // try {
        //     FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SignUpView.fxml"));
        //     Parent root = loader.load();
        //     Stage stage = (Stage) loginButton.getScene().getWindow();
        //     stage.setScene(new Scene(root));
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}