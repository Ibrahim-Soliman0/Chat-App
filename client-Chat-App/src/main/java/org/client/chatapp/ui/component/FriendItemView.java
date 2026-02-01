package org.client.chatapp.ui.component;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Users;
import org.client.chatapp.ClientChatApp;
import org.client.chatapp.ui.controller.FriendProfileController;
import org.client.chatapp.ui.utils.ImageUtil;

import java.io.IOException;
import java.util.Objects;

public class FriendItemView extends HBox {

    private Users user, loggedInUser;

    public FriendItemView(Users user, Users loggedInUser) {
        this.user = user;
        this.loggedInUser = loggedInUser;

        buildUI();
        registerHandlers();
    }

    private void buildUI() {
        getStyleClass().add("chat-item");

        setAlignment(Pos.CENTER_LEFT);

        Image userProfileImage = ImageUtil.getImageFromByteArray(user.getPictureBytes());

        ImageView avatar = new ImageView(userProfileImage);
        avatar.setFitWidth(60);
        avatar.setFitHeight(60);
//        avatar.setPreserveRatio(true);
        avatar.setSmooth(true);

        Circle clip = new Circle();
        clip.centerXProperty().bind(avatar.fitWidthProperty().divide(2));
        clip.centerYProperty().bind(avatar.fitHeightProperty().divide(2));
        clip.radiusProperty().bind(avatar.fitWidthProperty().divide(2));
        avatar.setClip(clip);

        Label name = new Label(user.getName());
        name.getStyleClass().add("chat-name");
        name.setPadding(new Insets(0, 0, 0, 10));

        getChildren().addAll(avatar, name);
    }

    private void registerHandlers() {
        setOnMouseClicked(mouseEvent -> {

            Parent root = null;
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(
                                "/org/client/chatapp/friend-profile-view.fxml")));

                root = loader.load();

                FriendProfileController friendProfileController = loader.getController();
                friendProfileController.setFriendUser(user, loggedInUser);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(ClientChatApp.allStyles);
            stage.setScene(scene);
            stage.show();
        });
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
