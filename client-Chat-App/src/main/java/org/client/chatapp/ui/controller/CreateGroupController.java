package org.client.chatapp.ui.controller;

import dto.GroupDTO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
import java.nio.file.Files;
import java.util.Objects;

public class CreateGroupController {
    private Users user;
    private GroupDTO groupDTO=new GroupDTO();
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
    private TextArea description;
    @FXML
    private Label groupNameErrorLabel;
    private String name;
    private String groupDescription;

    private boolean validateGroupName() {
        name = groupName.getText();
        groupDescription = description.getText();
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

        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(
                    "/org/client/chatapp/addGroupMembers-screen-view.fxml")));

            root = loader.load();
            AddGroupMemberController addGroupMemberController = loader.getController();
            addGroupMemberController.setUser(user);
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
            byte[] imageBytes=  getGroupImages(selectedImageFile);
            if(imageBytes!=null) {
                groupDTO.setGroupImage(imageBytes);
            }
        }

    }

    private byte[] getGroupImages(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public GroupDTO getGroupDTO() {
        return groupDTO;
    }

}


