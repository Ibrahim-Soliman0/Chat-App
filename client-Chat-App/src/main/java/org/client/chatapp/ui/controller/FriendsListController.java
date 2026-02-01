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
import org.client.chatapp.ui.component.FriendItemView;
import rmi.GetUserService;
import rmi.LoadFriendsListService;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FriendsListController {

    private Users user;
    @FXML
    private Button addFriendButton;
    @FXML
    private ListView<FriendItemView> friendsList;
    @FXML
    private Label noFriendsLabel;
    @FXML
    private Group noFriendsIcon;
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
    }

    @FXML
    private void onAddFriendButtonClick(ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(getClass().getResource(
                            "/org/client/chatapp/addFriend-screen-view.fxml")));

            root = loader.load();
            AddFriendController addFriendController = loader.getController();
            addFriendController.setUser(user);
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
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(getClass().getResource(
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

    public void setUser(Users user) {
        this.user = user;

        List<Friend> myFriends = new ArrayList<>();
        GetUserService getUserService = null;
        try {
            GetMyFriendsListDTO getMyFriends = new GetMyFriendsListDTO(user.getId());
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
            noFriendsIcon.setVisible(false);

            GetUserService finalGetUserService = getUserService;
            myFriendsToUser = myFriends.stream()
                    .map((friend) -> {
                        try {
                            if (friend.getReceiverUserId() == user.getId()) {
                                return finalGetUserService.getUser(friend.getSenderUserId());
                            }

                            return finalGetUserService.getUser(friend.getReceiverUserId());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        return null;
                    })
                    .toList();

            List<FriendItemView> userListToFriendItemView = myFriendsToUser.stream()
                    .map(friend -> new FriendItemView(friend, user))
                    .toList();

            friendsList.getItems().addAll(userListToFriendItemView);
        }
    }
}
