package org.client.chatapp.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Users;
import org.client.chatapp.ClientChatApp;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class CreateGroupController {
    private Users user;
    @FXML
    ImageView groupProfile;
    @FXML
    private Button cancelButton;
    @FXML
    private Button nextButton;
    @FXML
    private Parent root;
    private Stage stage;
    private Scene scene;
    @FXML
    private ImageView profileImageView;
    private File selectedImageFile;
    @FXML
    private Group profilePlaceholder;
    @FXML
    private TextField groupName;
    @FXML
    private Label groupNameErrorLabel;
    private String name;

    public void initialize() {

    }

    private boolean validateGroupName() {
         name = groupName.getText();
        if (name == null || name.trim().isEmpty()) {
            groupNameErrorLabel.setVisible(true);
            return false;
        }
        groupNameErrorLabel.setVisible(false);
        return true;

    }

    @FXML
    private void onClickCancelButton(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(
                    "/org/client/chatapp/home-screen-view.fxml")));

            root = loader.load();
            HomeScreenController homeScreenController = loader.getController();
            homeScreenController.setUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().addAll(ClientChatApp.allStyles);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void onClickNextButton(MouseEvent mouseEvent) {
        if (!validateGroupName()) {
            return;
        }
        if (this.user == null) {
            this.user = new Users();
            this.user.setId(1L);

        }
            try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(
                    "/org/client/chatapp/addGroupMembers-screen-view.fxml")));

            root = loader.load();
            AddGroupMemberController addGroupMemberController = loader.getController();
            addGroupMemberController.setUser(this.user);
            addGroupMemberController.setCreateGroupController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().addAll(ClientChatApp.allStyles);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void handleUploadPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");

        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Image Files", "*.png", "*.jpg", "*.jpeg"
                )
        );

        File file = fileChooser.showOpenDialog(
                profileImageView.getScene().getWindow()
        );

        if (file != null) {
            selectedImageFile = file;

            Image image = new Image(
                    file.toURI().toString(),
                    100, 100,
                    false,
                    true
            );

            profileImageView.setImage(image);

            profilePlaceholder.setVisible(false);
        }
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }
}


