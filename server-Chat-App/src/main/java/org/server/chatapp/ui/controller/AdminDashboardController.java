package org.server.chatapp.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import model.Users;
import model.enums.Role;

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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/server/chatapp/server-control-view.fxml"));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        updateActiveButton(btnControl);
    }
    public void setCurrentAdmin(Users admin) {
        this.currentAdmin = admin;
        configureUIBasedOnRole();
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
        activeBtn.getStyleClass().add("nav-button-active");
    }

}