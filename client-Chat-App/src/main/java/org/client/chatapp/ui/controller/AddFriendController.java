package org.client.chatapp.ui.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import model.Users;
import org.client.chatapp.ClientChatApp;
import org.client.chatapp.ui.component.FriendRequestItemView;
import rmi.GetUserService;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddFriendController {

    private Users user;
    @FXML
    private Group goBackArrow, searchIconGroup;
    @FXML
    private TextField searchBar;
    @FXML
    private ListView<FriendRequestItemView> friendsToAddList;
    private Parent root;
    private Stage stage;
    private Scene scene;

    public void initialize() {
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

        Circle searchHeadIcon = new Circle(11, 11, 8);
        searchHeadIcon.setFill(Color.TRANSPARENT);
        searchHeadIcon.setStroke(Color.web("#abacad"));
        searchHeadIcon.setStrokeWidth(2);

        SVGPath searchLine = new SVGPath();
        searchLine.setContent("m21 21-4.34-4.34");
        searchLine.setStroke(Color.web("#abacad"));
        searchLine.setStrokeWidth(2);
        searchIconGroup.getChildren().addAll(searchHeadIcon, searchLine);

        searchBar.setPromptText("+20 10-6866-6406");
        searchBar.setStyle(
                """
                        -fx-background-color: transparent;
                        -fx-border-color: rgb(0,0,0);
                        -fx-border-width: 1;
                        -fx-border-radius: 10;
                        -fx-background-radius: 10;
                        -fx-padding: 0 0 0 35px;
                        -fx-prompt-text-fill: #abacad;""");
        StackPane.setAlignment(searchIconGroup, Pos.CENTER_LEFT);
        StackPane.setMargin(searchIconGroup, new Insets(0, 0, 0, 8));

        TextFormatter<String> digitsOnlyFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*") && newText.length() <= 11) {
                return change;
            }
            return null;
        });

        searchBar.setTextFormatter(digitsOnlyFormatter);

        GetUserService getUserService = null;
        try {
            getUserService = (GetUserService) ClientChatApp.registry.lookup("GetUserService");
            FriendRequestItemView.setGetUserService(getUserService);
            FriendRequestItemView.setListView(friendsToAddList);
        } catch (RemoteException | NotBoundException e) {
            System.out.println("Couldn't get (GetUserService) From Registry");
            e.printStackTrace();
        }

        GetUserService finalGetUserService = getUserService;
        searchBar.textProperty()
                .addListener((obs, oldValue, newValue) -> {

                    if (newValue == null || newValue.isEmpty()) {
                        return;
                    }

                    List<Users> matchedUsers = new ArrayList<>();
                    try {
                        matchedUsers = finalGetUserService
                                .searchUsersByPhoneNumber(newValue, user.getId());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    List<FriendRequestItemView> matchedUsersToView = matchedUsers.stream()
                            .map((u -> new FriendRequestItemView(user, u)))
                            .toList();

                    friendsToAddList.setItems(FXCollections.observableArrayList(matchedUsersToView));
                });
    }

    @FXML
    private void onGoBackArrowClick(MouseEvent mouseEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(getClass().getResource(
                            "/org/client/chatapp/friends-list-screen-view.fxml")));

            root = loader.load();
            FriendsListController friendsListController = loader.getController();
            friendsListController.setUser(user);
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
    }
}
