package org.server.chatapp.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.server.chatapp.util.RMIUtil;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

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
    private Button btnUsers;

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/server/chatapp/statistics-view.fxml"));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        updateActiveButton(btnStatistics);
    }

    private void updateActiveButton(Button activeBtn) {
        btnControl.getStyleClass().remove("nav-button-active");
        btnStatistics.getStyleClass().remove("nav-button-active");

        activeBtn.getStyleClass().add("nav-button-active");
    }


}

