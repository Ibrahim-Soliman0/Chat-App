package org.client.chatapp.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.client.chatapp.ClientChatApp;

import java.io.IOException;

public class RegisterController {
    @FXML
    private TextField phoneField;

    @FXML
    private Button continueButton;


    @FXML
    public void initialize() {


        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue.matches("\\d*")) {
                phoneField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.length() > 11) {
                phoneField.setText(newValue.substring(0, 11));
            }
            continueButton.setDisable(!isValidPhoneNumber(phoneField.getText()));
        });

        continueButton.setOnAction(event -> handleContinue());
        continueButton.setDisable(true);
    }


    public void handleContinue() {
        String phoneNumber = phoneField.getText().trim();
        if (isValidPhoneNumber(phoneNumber)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/client/chatapp/setup-profile-view.fxml"));
                Parent root = loader.load();
                SetupProfileController controller = loader.getController();
                controller.setPhoneNumber(phoneNumber);

                Stage stage = (Stage) continueButton.getScene().getWindow();
                Scene scene = new Scene(root);
                scene.getStylesheets().addAll(ClientChatApp.allStyles);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                showError("Error loading registration page: " + e.getMessage());
            }
        }
    }

    private boolean isValidPhoneNumber(String phone) {
        if (phone == null) return false;

        return phone.matches("^01[0125][0-9]{8}$");
    }

    private void showError(String message) {

        Alert alert = new Alert(
                Alert.AlertType.ERROR
        );
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
