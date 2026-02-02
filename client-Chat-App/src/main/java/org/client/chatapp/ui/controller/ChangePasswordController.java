package org.client.chatapp.ui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import model.Users;
import org.client.chatapp.ClientChatApp;
import org.mindrot.jbcrypt.BCrypt;
import rmi.GetUserService;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.Optional;

public class ChangePasswordController {

    @FXML
    private Group profileIcon, chatsIcon;

    @FXML
    private PasswordField oldPasswordField, newPasswordField, confirmPasswordField;

    @FXML
    private Label oldPasswordError, newPasswordError, confirmPasswordError;

    @FXML
    private Button updatePasswordButton, cancelButton;

    private Parent root;
    private Stage stage;
    private Scene scene;
    private Users user;

    @FXML
    public void initialize() {
        // Initialize bottom navigation icons
        initializeNavigationIcons();
    }

    private void initializeNavigationIcons() {
        // Profile Icon (selected state)
        Circle profileHeadIcon = new Circle(12, 7, 4);
        profileHeadIcon.getStyleClass().add("selectedIcon");

        SVGPath profileBodyIcon = new SVGPath();
        profileBodyIcon.setContent("M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2");
        profileBodyIcon.getStyleClass().add("selectedIcon");

        profileIcon.getChildren().addAll(profileHeadIcon, profileBodyIcon);
        profileIcon.setScaleX(1.75);
        profileIcon.setScaleY(1.75);

        // Chats Icon
        SVGPath chatsSvg = new SVGPath();
        chatsSvg.setContent("M2.992 16.342a2 2 0 0 1 .094 1.167l-1.065 3.29a1 1 0 0 0 1.236 1.168l3.413-.998a2 2 0 0 1 1.099.092 10 10 0 1 0-4.777-4.719");
        chatsSvg.getStyleClass().add("icon");

        chatsIcon.getChildren().add(chatsSvg);
        chatsIcon.setScaleX(1.5);
        chatsIcon.setScaleY(1.5);

        chatsIcon.setOnMouseEntered(e -> chatsSvg.getStyleClass().setAll("onIconHover"));
        chatsIcon.setOnMouseExited(e -> chatsSvg.getStyleClass().setAll("icon"));
    }

    @FXML
    private void onUpdatePasswordClick(ActionEvent actionEvent) throws NotBoundException, RemoteException {
        // Clear previous errors
        clearErrors();

        boolean valid = true;

        // Validate old password
        if (oldPasswordField.getText() == null || oldPasswordField.getText().isEmpty() || !verifyPassword(oldPasswordField.getText(), user.getPassword())) {
            oldPasswordError.setText("Please enter your correct old password");
            oldPasswordError.setVisible(true);
            valid = false;
        }

        // Validate new password
        if (newPasswordField.getText() == null || newPasswordField.getText().length() < 6) {
            newPasswordError.setText("Password must be at least 6 characters");
            newPasswordError.setVisible(true);
            valid = false;
        }

        // Validate confirm password
        if (!newPasswordField.getText().equals(confirmPasswordField.getText())) {
            confirmPasswordError.setText("Passwords do not match");
            confirmPasswordError.setVisible(true);
            valid = false;
        }

        // Check if new password is same as old password
        if (valid && oldPasswordField.getText().equals(newPasswordField.getText())) {
            newPasswordError.setText("New password must be different from old password");
            newPasswordError.setVisible(true);
            valid = false;
        }

        if (!valid) {
            return;
        }

        // Here you would normally verify the old password and update to the new one
        // For now, we'll just show a success message
        updatePassword(actionEvent, newPasswordField.getText());
    }

    private void updatePassword(ActionEvent actionEvent, String newPassword) throws NotBoundException, RemoteException {
        // Password update logic with the database
        String newHashedPassword = hashPassword(newPassword);
        user.setPassword(newHashedPassword);
        GetUserService getUserService = (GetUserService) ClientChatApp.registry.lookup("GetUserService");
        getUserService.updateUser(user);

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText("Password Updated");
        successAlert.setContentText("Your password has been successfully updated!");

        Optional<ButtonType> result = successAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Navigate back to profile screen
            navigateToProfile(actionEvent, user);
        }
    }

    @FXML
    private void onCancelClick(ActionEvent actionEvent) {
        // Navigate back to profile screen without saving
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(
                    "/org/client/chatapp/profile-screen-view.fxml")));
            root = loader.load();
            ProfileScreenController profileScreenController = loader.getController();
            profileScreenController.setUser(user);
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().addAll(ClientChatApp.allStyles);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("here");
        }
    }

    private void navigateToProfile(ActionEvent actionEvent, Users user) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(
                    "/org/client/chatapp/profile-screen-view.fxml")));
            root = loader.load();
            ProfileScreenController profileScreenController = loader.getController();
            profileScreenController.setUser(user);
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.getStylesheets().addAll(ClientChatApp.allStyles);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearErrors() {
        oldPasswordError.setVisible(false);
        newPasswordError.setVisible(false);
        confirmPasswordError.setVisible(false);
    }

    @FXML
    private void onChatsIconClick(MouseEvent event) {
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

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    public void setUser(Users user) {
        this.user = user;
    }
}