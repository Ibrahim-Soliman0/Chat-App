package org.client.chatapp.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Users;
import org.client.chatapp.ClientChatApp;
import org.client.chatapp.rmi.ClientCallBackImp;
import rmi.LoginService;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;

public class LoginController {
    private Parent root;
    private Stage stage;
    private Scene scene;
    private static ClientCallBackImp client;


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

        // Add an input validation listener for phone number (digits only)
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
    private void handleLogin(ActionEvent event) throws IOException, NotBoundException {
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

        // Construct a full phone number with country code
        String fullPhone = "0" + phone;

        LoginService loginService = (LoginService) ClientChatApp.registry.lookup("LoginService");
        client = new ClientCallBackImp();
        Users success = loginService.login(fullPhone, password , client);
        if (success != null) {
            moveToMainApp(event, success);
        }
        else
        {
            showError("The phone number and the password don't match");
        }
    }

    private void moveToMainApp(ActionEvent event, Users user) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(
                    "/org/client/chatapp/home-screen-view.fxml")));
            root = loader.load();
            HomeScreenController homeScreenController = loader.getController();
            homeScreenController.setUser(user);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().addAll(ClientChatApp.allStyles);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleForgotPassword(ActionEvent event) {
        System.out.println("Forgot password clicked");
        // TODO: Navigate to forgot password screen
        showError("Forgot password feature coming soon");
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
         try {
             FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/client/chatapp/register-view.fxml"));
             Parent root = loader.load();
             Stage stage = (Stage) loginButton.getScene().getWindow();
             stage.setScene(new Scene(root));
         } catch (IOException e) {
             e.printStackTrace();
         }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}