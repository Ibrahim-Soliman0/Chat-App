package org.client.chatapp.ui.component;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Users;
import org.client.chatapp.ClientChatApp;
import org.client.chatapp.model.FriendItem;
import org.client.chatapp.ui.controller.FriendProfileController;

import java.io.IOException;
import java.util.Objects;

public class FriendItemView extends HBox {

    private FriendItem friendItem;
    private Users user;

    public FriendItemView(FriendItem friendItem, Users user) {
        this.friendItem = friendItem;
        this.user = user;

        buildUI();
        registerHandlers();
    }

    private void buildUI() {
        getStyleClass().add("chat-item");

        setAlignment(Pos.CENTER_LEFT);

        ImageView avatar = new ImageView(friendItem.getProfilePicPath());
        avatar.setFitWidth(60);
        avatar.setFitHeight(60);
        avatar.setPreserveRatio(true);

        Circle clip = new Circle(30, 30, 24);
        avatar.setClip(clip);

        Label name = new Label(friendItem.getName());
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
                friendProfileController.setUser(user);
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

    public FriendItem getFriendItem() {
        return friendItem;
    }

    public void setFriendItem(FriendItem friendItem) {
        this.friendItem = friendItem;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
