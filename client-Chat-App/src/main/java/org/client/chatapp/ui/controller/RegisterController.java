package org.client.chatapp.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
            if (newValue.length() > 10) {
                phoneField.setText(newValue.substring(0, 10));
            }
            continueButton.setDisable(!isValidPhoneNumber(phoneField.getText()));
        });

        continueButton.setOnAction(event -> handleContinue());
        continueButton.setDisable(true);
    }



    public void handleContinue() {
        String phoneNumber = phoneField.getText().trim();
        String fullPhoneNumber = "+20" + phoneNumber;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/client/chatapp/setup-profile-view.fxml"));
            Parent root = loader.load();
            SetupProfileController controller = loader.getController();
            controller.setPhoneNumber(fullPhoneNumber);

            Stage stage = (Stage) continueButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            showError("Error loading registration page: " + e.getMessage());
        }
    }

    private boolean isValidPhoneNumber(String phone) {

        if (phone == null || phone.isEmpty()) {
            return false;
        }

        if (phone.length() != 10) {
            return false;
        }

        if (!phone.startsWith("1")) {
            return false;
        }
        return true;
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
