package org.client.chatapp.ui.controller;

import dto.GetMyFriendsListDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import model.Friend;
import model.Users;
import org.client.chatapp.ClientChatApp;
import org.client.chatapp.model.FriendItem;
import org.client.chatapp.ui.component.FriendItemView;
import rmi.GetUserService;
import rmi.LoadFriendsListService;
import rmi.LoginService;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FriendsListController {

    @FXML
    private Button addFriendButton;
    @FXML
    private ListView<FriendItemView> friendsList;
    @FXML
    private Label noFriendsLabel;
    @FXML
    private Group goBackArrow;
    private Parent root;
    private Stage stage;
    private Scene scene;

    public void initialize() {

        addFriendButton.getStyleClass().add("button");

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

        List<Friend> myFriends = new ArrayList<>();
        GetUserService getUserService = null;
        try {
            // TODO: change the id to the actual logged in user's id
            GetMyFriendsListDTO getMyFriends = new GetMyFriendsListDTO(1);
            LoadFriendsListService friendsListService =
                    (LoadFriendsListService) ClientChatApp.registry.lookup("LoadFriendsListService");
            getUserService = (GetUserService) ClientChatApp.registry.lookup("GetUserService");
            myFriends = friendsListService.getUserFriendsList(getMyFriends);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

        List<Users> myFriendsToUser;
        if (!myFriends.isEmpty()) {
            noFriendsLabel.setVisible(false);

            GetUserService finalGetUserService = getUserService;
            myFriendsToUser = myFriends.stream()
                    .map((friend) -> {
                        try {
                            return finalGetUserService.getUser(friend.getReceiverUserId());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        return null;
                    })
                    .toList();

            List<FriendItemView> userListToFriendItemView = myFriendsToUser.stream()
                    .map((user) ->
                        new FriendItemView(new FriendItem(user.getPicturePath(), user.getName()), user))
                    .toList();

            friendsList.getItems().addAll(userListToFriendItemView);
        }
    }

    @FXML
    private void onAddFriendButtonClick(ActionEvent actionEvent) {

        try {
            root = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource(
                            "/org/client/chatapp/addFriend-screen-view.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().addAll(ClientChatApp.allStyles);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void onGoBackArrowClick(MouseEvent mouseEvent) {

        try {
            root = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource(
                            "/org/client/chatapp/home-screen-view.fxml")));
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
