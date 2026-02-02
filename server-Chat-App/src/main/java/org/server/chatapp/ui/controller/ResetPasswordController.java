package org.server.chatapp.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Users;
import org.server.chatapp.dao.dao.UsersDao;
import org.server.chatapp.dao.implement.UsersImpl;
import org.server.chatapp.util.AdminSession;
import org.server.chatapp.util.PasswordUtil;

import java.io.IOException;

public class ResetPasswordController {
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private ProgressBar strengthBar;
    @FXML
    private Label strengthLabel;
    @FXML
    private Button btnUpdate;

    @FXML
    private Label reqLength;
    @FXML
    private Label reqSymbol;
    private final UsersDao usersDao = new UsersImpl();

    public void handleUpdatePassword(ActionEvent actionEvent) {
        String newPass = newPasswordField.getText();
        String confirmPass = confirmPasswordField.getText();
        Users currentAdmin = AdminSession.getInstance();

        if (!newPass.equals(confirmPass)) {
            showAlert("Passwords do not match!");
            return;
        }

        if (calculateStrength(newPass) < 0.7) {
            showAlert("Password is too weak. Please follow the requirements.");
            return;
        }

        try {
            String hashedPass = PasswordUtil.hashPassword(newPass);
            boolean success = usersDao.updatePasswordAndClearFirstLogin(currentAdmin.getId(), hashedPass);

            if (success) {
                currentAdmin.setFirstLogin(false);
                currentAdmin.setPassword(hashedPass);

                moveToDashboard();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error updating password. Please try again.");
        }
    }

    @FXML
    private void handleKeyReleased() {
        String pass = newPasswordField.getText();
        double score = calculateStrength(pass);

        strengthBar.setProgress(score);

        strengthBar.getStyleClass().removeAll("weak", "medium", "strong");
        if (score < 0.4) {
            strengthBar.getStyleClass().add("weak");
            strengthLabel.setText("Strength: Weak");
        } else if (score < 0.8) {
            strengthBar.getStyleClass().add("medium");
            strengthLabel.setText("Strength: Good");
        } else {
            strengthBar.getStyleClass().add("strong");
            strengthLabel.setText("Strength: Excellent");
        }
    }
    private double calculateStrength(String password) {
        if (password == null || password.isEmpty())
            return 0;
        double score = 0;
        if (password.length() >= 8) score += 0.4;
        if (password.matches(".*[0-9].*")) score += 0.2;
        if (password.matches(".*[a-z].*")) score += 0.1;
        if (password.matches(".*[A-Z].*")) score += 0.1;
        if (password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) score += 0.2;

        return score;
    }

    private void moveToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/server/chatapp/main-view.fxml"));
            Parent root = loader.load();

            AdminDashboardController controller = loader.getController();
            controller.setCurrentAdmin(AdminSession.getInstance());

            Stage stage = (Stage) btnUpdate.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.show();
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/admin.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog-pane");
    }
}
