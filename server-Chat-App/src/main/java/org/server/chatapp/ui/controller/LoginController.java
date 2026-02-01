package org.server.chatapp.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Users;
import model.enums.Role;
import org.server.chatapp.dao.dao.UsersDao;
import org.server.chatapp.dao.implement.UsersImpl;
import org.server.chatapp.rmi.LoginServiceImpl;
import org.server.chatapp.util.AdminSession;

import java.io.IOException;
import java.rmi.RemoteException;

public class LoginController {

    @FXML
    private TextField phoneField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button loginButton;

    private final UsersDao usersDao = new UsersImpl();
    private final LoginServiceImpl loginService = new LoginServiceImpl();
    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phoneField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.length() > 11) {
                phoneField.setText(newValue.substring(0, 11));
            }
        });
        passwordField.setOnAction(event -> handleLogin());
    }

    public LoginController() throws RemoteException {
    }

    @FXML
    private void handleLogin() {
        String phone = phoneField.getText().trim();
        String password = passwordField.getText();

        if (validateLogin(phone, password))
            return;

        try {
            Users user = loginService.login(phone, password, null);

            if (user != null) {
                if (user.getRole() == Role.ADMIN || user.getRole() == Role.MASTER_ADMIN) {
                    if (user.isFirstLogin()) {
                        moveToForceChange(user);
                    } else {
                        moveToDashboard(user);
                    }

                } else {
                    showError("Access Denied: You do not have admin privileges.");
                }
            } else {
                showError("Invalid phone number or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Server error occurred. Please try again.");
        }
    }

    private boolean validateLogin(String phone, String password) {
        if (phone.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields.");
            return true;
        }
        String egyptPhoneRegex = "^01[0125][0-9]{8}$";

        if (!phone.matches(egyptPhoneRegex)) {
            showError("Please enter a valid Egyptian phone number (e.g., 01012345678).");
            return true;
        }

        if (password.length() < 6) {
            showError("Password is too short.");
            return true;
        }
        return false;
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void moveToDashboard(Users admin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/server/chatapp/main-view.fxml"));
            Parent root = loader.load();
            AdminSession.setInstance(admin);

            AdminDashboardController controller = loader.getController();
            controller.setCurrentAdmin(admin);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load Dashboard.");
        }
    }

    private void moveToForceChange(Users admin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/server/chatapp/reset-password-view.fxml"));
            Parent root = loader.load();
            AdminSession.setInstance(admin);


            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Failed to load Reset Password.");
        }
    }

}