package org.server.chatapp.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Users;
import model.enums.Role;
import org.server.chatapp.util.AdminSession;
import org.server.chatapp.util.RMIUtil;

import java.io.IOException;

public class AdminDashboardController {
    @FXML
    private VBox contentArea;
    @FXML
    private Button btnControl;
    @FXML
    private Button btnStatistics;
    @FXML
    private Button btnAnnouncements;
    @FXML
    private Button btnAdminManagement;
    @FXML
    private Button btnUsers;
    private Users currentAdmin;

    public void initialize() {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/server/chatapp/server-control-view.fxml"));
//            Parent view = loader.load();
//            contentArea.getChildren().setAll(view);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        updateActiveButton(btnControl);
    }
    public void setCurrentAdmin(Users admin) {
        this.currentAdmin = admin;
        configureUIBasedOnRole();
        showServerControl();
    }
    private void configureUIBasedOnRole() {
        if (currentAdmin == null) {
            return;
        }
        if (currentAdmin.getRole() == Role.MASTER_ADMIN) {
            btnAdminManagement.setVisible(true);
            btnAdminManagement.setManaged(true);
        } else {
            btnAdminManagement.setVisible(false);
            btnAdminManagement.setManaged(false);
        }
    }
    @FXML
    private void showServerControl() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/server/chatapp/server-control-view.fxml"));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        updateActiveButton(btnControl);
    }

    @FXML
    private void showStatistics() {
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/org/server/chatapp/statistics-view.fxml"));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        updateActiveButton(btnStatistics);
    }

    @FXML
    private void showAnnouncement() {
        if (!RMIUtil.isRunning()) {
            showWarningAlert("Server Offline", "You cannot send announcements while the server is stopped. Please start the server first.");
            return;
        }
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/org/server/chatapp/announcement-view.fxml"));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        updateActiveButton(btnAnnouncements);
    }
    @FXML
    private void showAdminManagement(){
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/org/server/chatapp/admin-management-view.fxml"));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        updateActiveButton(btnAdminManagement);
    }

    private void updateActiveButton(Button activeBtn) {
        btnControl.getStyleClass().remove("nav-button-active");
        btnStatistics.getStyleClass().remove("nav-button-active");
        btnAnnouncements.getStyleClass().remove("nav-button-active");
        btnAdminManagement.getStyleClass().remove("nav-button-active");
        btnUsers.getStyleClass().remove("nav-button-active");

        activeBtn.getStyleClass().add("nav-button-active");
    }

    @FXML
    public void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Log Out of System");
        alert.setContentText("Are you sure you want to log out?");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/admin.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                AdminSession.terminate();
                navigateToLogin();
            }
        });
    }

    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/server/chatapp/login-server-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/admin.css").toExternalForm());
        alert.showAndWait();
    }
}