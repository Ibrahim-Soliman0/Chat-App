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

    public LoginController() throws RemoteException {
    }

    @FXML
    private void handleLogin() {
        String phone = phoneField.getText().trim();
        String password = passwordField.getText();

        if (phone.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        try {
            Users user = loginService.login(phone, password , null);

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

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void moveToDashboard(Users admin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/server/chatapp/main-view.fxml"));
            Parent root = loader.load();

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
        // هنا الكود اللي بيعمل Load لصفحة تغيير الباسورد
    }
}