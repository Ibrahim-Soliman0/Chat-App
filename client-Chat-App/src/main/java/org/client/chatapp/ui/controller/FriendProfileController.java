package org.client.chatapp.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import model.Users;
import org.client.chatapp.ClientChatApp;

import java.io.IOException;
import java.util.Objects;

public class FriendProfileController {

    private Users user;
    @FXML
    private Group goBackArrow;
    @FXML
    private ImageView profilePic;
    @FXML
    private Label nameLabel, phoneNumberLabel, emailLabel, bioLabel;
    @FXML
    private TextArea bioTextArea;
    private Parent root;
    private Stage stage;
    private Scene scene;

    public void initialize() {
        bioTextArea.setEditable(false);

        SVGPath arrowHead = new SVGPath();
        arrowHead.setContent("m12 19-7-7 7-7");
        arrowHead.getStyleClass().add("icon");

        SVGPath arrowTail = new SVGPath();
        arrowTail.setContent("M19 12H5");
        arrowTail.getStyleClass().add("icon");

        goBackArrow.getChildren().addAll(arrowHead, arrowTail);

        goBackArrow.setOnMouseEntered((mouseEvent -> {
            arrowTail.getStyleClass().setAll("onIconHover");
            arrowHead.getStyleClass().setAll("onIconHover");
        }));

        goBackArrow.setOnMouseExited((mouseEvent -> {
            arrowTail.getStyleClass().setAll("icon");
            arrowHead.getStyleClass().setAll("icon");
        }));
    }

    public void setUser(Users user) {
        this.user = user;

        nameLabel.setText(user.getName());
        phoneNumberLabel.setText(user.getPhoneNumber());
        emailLabel.setText(user.getEmail());
        bioTextArea.setText(user.getBio());

        profilePic.setImage(new Image(user.getPicturePath()));
        profilePic.setPreserveRatio(false);
        profilePic.setSmooth(true);

        Circle clip = new Circle(50);
        clip.centerXProperty().bind(profilePic.fitWidthProperty().divide(2));
        clip.centerYProperty().bind(profilePic.fitHeightProperty().divide(2));

        profilePic.setClip(clip);
    }

    @FXML
    private void onGoBackArrowClick(MouseEvent mouseEvent) {

        try {
            root = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource(
                            "/org/client/chatapp/friends-list-screen-view.fxml")));
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
}
