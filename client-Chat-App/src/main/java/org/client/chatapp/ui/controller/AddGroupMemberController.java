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
    private ListView FriendListView;
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

    private final ObservableList<HBox> allFriendRows = FXCollections.observableArrayList();
    private String search;

    private Users user;
    private List<Users>friendUsers=new ArrayList<>();
    private CreateGroupController createGroupController;

    public void initialize() {
        notFoundLabel.setVisible(false);

        searchField.textProperty().addListener((obs, oldText, newText) -> {
            filterFriends(newText);
        });

    }

    @FXML
    private void onClickBackButton(MouseEvent mouseEvent){
        try {
            root = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource(
                            "/org/client/chatapp/group-screen-view.fxml")));
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

    public void loadUser() {
        if (this.user == null) {
            this.user = new Users();
            this.user.setId(2L); // default/fallback user
        }
    }

    public void setUser(Users user) {
        this.user = user;
       loadUser();

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
                    Users friendUser = finalGetUserService.getUser(friend.getReceiverUserId());
                    FriendItemView friendView = new FriendItemView(friendUser);
                    CheckBox checkBox = new CheckBox();
                    checkBox.setFocusTraversable(false);
                    checkBox.setOnMouseClicked(e->e.consume());
                    friendView.setOnMouseClicked(e->e.consume());

                    HBox row = new HBox(12);
                    row.setAlignment(Pos.CENTER_LEFT);
                    row.setPadding(new Insets(8, 12, 8, 12));

                    HBox.setHgrow(friendView, Priority.ALWAYS);

                    row.getChildren().addAll(friendView,checkBox);

                    allFriendRows.add(row);
                    friendUsers.add(friendUser);
                    FriendListView.setItems(allFriendRows);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void filterFriends(String text) {

        if (text == null || text.isBlank()) {
            FriendListView.setItems(allFriendRows);
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

        FriendListView.setItems(filtered);
        notFoundLabel.setVisible(filtered.isEmpty());
    }

    @FXML
    private void createGroup(){
        List<Long> selectedUserId =new ArrayList<>();
        for(int i=0;i<FriendListView.getItems().size();i++){
            HBox row = (HBox) FriendListView.getItems().get(i);
            CheckBox checkBox=(CheckBox) row.getChildren().get(1);

            if(checkBox.isSelected()){
                selectedUserId.add(friendUsers.get(i).getId());

            }
        }
        if(selectedUserId.isEmpty()){
            showError("Select at least one memeber");
            return;
        }
        createGroupOnServer(selectedUserId);


    }
    private void createGroupOnServer(List<Long>memberId){
        GroupDTO groupDTO=new GroupDTO();
        groupDTO.setGroupName(createGroupController.getName());
        groupDTO.setCreatorId(user.getId());
        groupDTO.setMembersId(memberId);

        try{
            GroupService groupService=
                    (GroupService) ClientChatApp.registry.lookup("GroupService");

            Room room = groupService.createGroup(groupDTO);

        } catch (NotBoundException | RemoteException e) {
        e.printStackTrace();
        }

    }

    public void setCreateGroupController(CreateGroupController controller) {
        this.createGroupController = controller;
    }

}