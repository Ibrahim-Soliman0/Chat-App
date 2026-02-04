package org.client.chatapp.ui.controller;

import dto.GetMyFriendsListDTO;
import dto.GroupDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Friend;
import model.Room;
import model.Users;
import org.client.chatapp.ClientChatApp;
import org.client.chatapp.ui.component.FriendItemView;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;
import rmi.GetUserService;
import rmi.GroupService;
import rmi.LoadFriendsListService;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class AddGroupMemberController {

    @FXML
    private Button backButton;
    @FXML
    private Button createGroupButton;
    @FXML
    private ListView friendListView;
    @FXML
    private Label noFriendsLabel;
    @FXML
    private VBox containerBox;
    @FXML
    private Parent root;
    private Stage stage;
    private Scene scene;
    @FXML
    private TextField searchField;
    @FXML
    private Label notFoundLabel;
    @FXML
    private Label addMemberFlag;

    private final ObservableList<HBox> allFriendRows = FXCollections.observableArrayList();
    private String search;

    private Users user;
    private List<Users> friendUsers = new ArrayList<>();
    private CreateGroupController createGroupController;

    public void initialize() {
        notFoundLabel.setVisible(false);

        searchField.textProperty().addListener((obs, oldText, newText) -> {
            filterFriends(newText);
        });

    }

    @FXML
    private void onClickBackButton(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(getClass().getResource(
                            "/org/client/chatapp/group-screen-view.fxml")));

            root = loader.load();
            CreateGroupController createGroupController = loader.getController();
            createGroupController.setUser(user);
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

        if (!myFriends.isEmpty()) {
            noFriendsLabel.setVisible(false);
            GetUserService finalGetUserService = getUserService;

            for (Friend friend : myFriends) {
                try {
                    Users friendUser = finalGetUserService.getUser(friend.getReceiverUserId() == user.getId() ?
                                                        friend.getSenderUserId() : friend.getReceiverUserId());
                    FriendItemView friendView = new FriendItemView(friendUser, user);
                    CheckBox checkBox = new CheckBox();
                    checkBox.setFocusTraversable(false);
                    checkBox.setOnMouseClicked(e -> e.consume());
                    friendView.setOnMouseClicked(e -> e.consume());

                    HBox row = new HBox(12);
                    row.setAlignment(Pos.CENTER_LEFT);
                    row.setPadding(new Insets(8, 12, 8, 12));

                    HBox.setHgrow(friendView, Priority.ALWAYS);

                    row.getChildren().addAll(friendView, checkBox);

                    allFriendRows.add(row);
                    friendUsers.add(friendUser);
                    friendListView.setItems(allFriendRows);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void filterFriends(String text) {

        if (text == null || text.isBlank()) {
            friendListView.setItems(allFriendRows);
            notFoundLabel.setVisible(false);
            return;
        }

        search = text.toLowerCase();

        ObservableList<HBox> filtered = allFriendRows.stream()
                .filter(row -> {
                    FriendItemView view =
                            (FriendItemView) row.getChildren().get(0);
                    return view.getUser().getName().toLowerCase().startsWith(search);
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        friendListView.setItems(filtered);
        notFoundLabel.setVisible(filtered.isEmpty());
    }

    @FXML
    private void createGroup() {
        List<Long> selectedUserId = new ArrayList<>();
        for (int i = 0; i < friendListView.getItems().size(); i++) {
            HBox row = (HBox) friendListView.getItems().get(i);
            CheckBox checkBox = (CheckBox) row.getChildren().get(1);

            if (checkBox.isSelected()) {
                selectedUserId.add(friendUsers.get(i).getId());

            }
        }
        if (selectedUserId.isEmpty()) {
            addMemberFlag.setVisible(true);
            return;
        }
        selectedUserId.add(user.getId());
        createGroupOnServer(selectedUserId);
        openHomeScreen();

    }

    private void createGroupOnServer(List<Long> memberId) {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setGroupName(createGroupController.getName());
        groupDTO.setCreatorId(user.getId());
        groupDTO.setMembersId(memberId);
        groupDTO.setDescription(createGroupController.getGroupDescription());
        groupDTO.setGroupImage(createGroupController.getGroupDTO().getGroupImage());

        try {
            GroupService groupService =
                    (GroupService) ClientChatApp.registry.lookup("GroupService");
            groupService.createGroup(groupDTO);

        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }

    }

    private void openHomeScreen(){
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(
                    "/org/client/chatapp/home-screen-view.fxml")));

            root = loader.load();
            HomeScreenController homeScreenController = loader.getController();
            homeScreenController.setUser(user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        stage = (Stage) createGroupButton .getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().addAll(ClientChatApp.allStyles);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void setCreateGroupController(CreateGroupController controller) {
        this.createGroupController = controller;
    }

}