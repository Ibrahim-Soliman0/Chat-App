package org.client.chatapp.ui.component;

import dto.BidirectionalFriendStatusDTO;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import model.Users;
import model.enums.FriendStatus;
import org.client.chatapp.ClientChatApp;
import org.client.chatapp.ui.controller.FriendProfileController;
import org.client.chatapp.ui.utils.ImageUtil;
import rmi.FriendRequestService;
import rmi.GetUserService;

import java.awt.*;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Objects;

public class FriendRequestItemView extends HBox {

    private static ListView<FriendRequestItemView> listView;
    private static GetUserService getUserService;
    private Users me, other;
    private Button requestButton;

    public FriendRequestItemView(Users me, Users other) {
        this.me = me;
        this.other = other;

        buildUI();
        registerHandlers();
    }

    private void buildUI() {
        getStyleClass().add("chat-item");

        setAlignment(Pos.CENTER_LEFT);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Image userProfileImage = ImageUtil.getImageFromByteArray(other.getPictureBytes());

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

        Label name = new Label(other.getName());
        name.getStyleClass().add("chat-name");
        name.setPadding(new Insets(0, 0, 0, 10));

        requestButton = new Button();
        requestButton.getStyleClass().add("icon-button");
        setRequestButtonIcon();

        getChildren().addAll(avatar, name, spacer, requestButton);
    }

    private void registerHandlers() {
        setOnMouseClicked(mouseEvent -> {

            // Todo: fix this later so the back button returns you to the search page
            Parent root = null;
            try {
                FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(
                        "/org/client/chatapp/friend-profile-view.fxml")));

                root = loader.load();

                FriendProfileController friendProfileController = loader.getController();
                friendProfileController.setUser(other);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().addAll(ClientChatApp.allStyles);
            stage.setScene(scene);
            stage.show();
        });

        requestButton.setOnAction((actionEvent -> {
            try {
                BidirectionalFriendStatusDTO relationStatus = getUserService.getUserFriendStatus(me, other);
                FriendRequestService friendRequestService = (FriendRequestService)
                        ClientChatApp.registry.lookup("FriendRequestService");

                FriendStatus meToOther = relationStatus.getMe() != null ?
                        relationStatus.getMe().getStatus() : null;

                FriendStatus otherToMe = relationStatus.getOther() != null
                        ? relationStatus.getOther().getStatus() : null;

                if (otherToMe == FriendStatus.PENDING) {
                    friendRequestService.acceptFriendRequest(me, other);
                    requestButton.setGraphic(getIcon("accept"));
                    listView.getItems().remove(this);
                } else if (meToOther == FriendStatus.PENDING) {
                    friendRequestService.cancelFriendRequest(me, other);
                    requestButton.setGraphic(getIcon("add"));
                } else {

                    if (meToOther == FriendStatus.ACCEPTED || otherToMe == FriendStatus.ACCEPTED) {
                        return;
                    }

                    friendRequestService.sendFriendRequest(me, other);
                    requestButton.setGraphic(getIcon("pending"));
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    private void setRequestButtonIcon() {
        try {
            BidirectionalFriendStatusDTO relationStatus = getUserService.getUserFriendStatus(me, other);

            FriendStatus meToOther = relationStatus.getMe() != null ?
                    relationStatus.getMe().getStatus() : null;

            FriendStatus otherToMe = relationStatus.getOther() != null
                    ? relationStatus.getOther().getStatus() : null;

            if (otherToMe == FriendStatus.PENDING) {
                requestButton.setGraphic(getIcon("accept"));
            } else if (meToOther == FriendStatus.PENDING) {
                requestButton.setGraphic(getIcon("pending"));
            } else {
                requestButton.setGraphic(getIcon("add"));
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private Group getIcon(String type) {
        return switch (type.toLowerCase()) {
            case "add" -> addIcon();
            case "pending" -> pendingIcon();
            default -> acceptIcon();
        };
    }

    private Group addIcon() {
        SVGPath bodyIcon = new SVGPath();
        bodyIcon.setContent("M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2");
        bodyIcon.getStyleClass().add("icon-black");

        Circle headIcon = new Circle(9, 7, 4);
        headIcon.getStyleClass().add("icon-black");

        Line vLine = new Line(19, 8, 19, 14);
        vLine.getStyleClass().add("icon-black");

        Line hLine = new Line(22, 11, 16, 11);
        hLine.getStyleClass().add("icon-black");

        Group addFriendIcon = new Group();
        addFriendIcon.getChildren().addAll(headIcon, bodyIcon, hLine, vLine);

        return addFriendIcon;
    }

    private Group pendingIcon() {
        SVGPath hourglassIcon = new SVGPath();
        hourglassIcon.setContent("M16.6,7c-0.3,0-0.5,0.2-0.5,0.5c0,0.3,0.2,0.5,0.5,0.5h0.2v0.4c0,0.8,0.3,1.6,0.9,2.1l1.6,1.6l-1.6,1.6\n" +
                "\tc-0.6,0.6-0.9,1.3-0.9,2.1v0.4h-0.2c-0.3,0-0.5,0.2-0.5,0.5c0,0.3,0.2,0.5,0.5,0.5h6.7c0.3,0,0.5-0.2,0.5-0.5c0-0.3-0.2-0.5-0.5-0.5\n" +
                "\th-0.2v-0.4c0-0.8-0.3-1.6-0.9-2.1l-1.6-1.6l1.6-1.6c0.6-0.6,0.9-1.3,0.9-2.1V8h0.2c0.3,0,0.5-0.2,0.5-0.5c0-0.3-0.2-0.5-0.5-0.5\n" +
                "\tH16.6z M20,12.8l1.6,1.6c0.4,0.4,0.6,0.9,0.6,1.5v0.4h-4.4v-0.4c0-0.5,0.2-1.1,0.6-1.5L20,12.8z M20,11.4l-1.6-1.6\n" +
                "\tc-0.4-0.4-0.6-0.9-0.6-1.5V8h4.4v0.4c0,0.5-0.2,1.1-0.6,1.5L20,11.4L20,11.4z");
        hourglassIcon.getStyleClass().add("icon-black");

        Circle headIcon = new Circle(8.3, 7, 4);
        headIcon.getStyleClass().add("icon-black");

        SVGPath bodyIcon = new SVGPath();
        bodyIcon.setContent("M15.3,21v-2c0-2.2-1.8-4-4-4h-6c-2.2,0-4,1.8-4,4v2");
        bodyIcon.getStyleClass().add("icon-black");

        Group pendingRequestIcon = new Group();
        pendingRequestIcon.getChildren().addAll(headIcon, bodyIcon, hourglassIcon);

        return pendingRequestIcon;
    }

    private Group acceptIcon() {
        SVGPath bodyIcon = new SVGPath();
        bodyIcon.setContent("M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2");
        bodyIcon.getStyleClass().add("icon-black");

        Circle headIcon = new Circle(9, 7, 4);
        headIcon.getStyleClass().add("icon-black");

        SVGPath correctMarkIcon = new SVGPath();
        correctMarkIcon.setContent("m16 11 2 2 4-4");
        correctMarkIcon.getStyleClass().add("icon-black");

        Group acceptRequestIcon = new Group();
        acceptRequestIcon.getChildren().addAll(headIcon, bodyIcon, correctMarkIcon);

        return acceptRequestIcon;
    }

    public Users getMe() {
        return me;
    }

    public void setMe(Users me) {
        this.me = me;
    }

    public Users getOther() {
        return other;
    }

    public void setOther(Users other) {
        this.other = other;
    }

    public static void setGetUserService(GetUserService getUserService) {
        FriendRequestItemView.getUserService = getUserService;
    }

    public static void setListView(ListView<FriendRequestItemView> listView) {
        FriendRequestItemView.listView = listView;
    }
}
