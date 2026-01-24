package org.server.chatapp.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.server.chatapp.util.RMIUtil;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class AdminDashboardController {
    @FXML
    private Circle statusCircle;
    @FXML
    private Label lblStatus;
    @FXML
    private Button btnStartStop;
    @FXML
    private BorderPane root;

    public void initialize() {
        btnStartStop.getStyleClass().add("service-button");
        updateUI(RMIUtil.isRunning());
    }

    public void handleStartStop() {
        try {
            if (!RMIUtil.isRunning()) {
                RMIUtil.startServices();
                updateUI(true);
            } else {
                RMIUtil.stopServices();
                updateUI(false);
            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private void updateUI(boolean online) {
        btnStartStop.getStyleClass().removeAll("btn-start", "btn-stop");

        if (online) {
            statusCircle.setFill(Color.web("#22c55e"));
            lblStatus.setText("SERVER ONLINE");
            btnStartStop.setText("STOP SERVICE");
            btnStartStop.getStyleClass().add("btn-stop");
        } else {
            statusCircle.setFill(Color.web("#ef4444"));
            lblStatus.setText("SERVER OFFLINE");
            btnStartStop.setText("START SERVICE");
            btnStartStop.getStyleClass().add("btn-start");
        }
    }
}

