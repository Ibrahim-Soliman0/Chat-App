package org.server.chatapp.ui.controller;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import model.Users;
import model.enums.Role;
import org.server.chatapp.dao.implement.UsersImpl;
import org.server.chatapp.dao.dao.UsersDao;

import java.util.List;
import java.util.Optional;

public class AdminManagementController {

    @FXML
    private TextField searchField;
    @FXML
    ScrollPane rootPane;
    @FXML
    private TableView<Users> searchResultsTable;
    @FXML
    private TableColumn<Users, String> colPhone;
    @FXML
    private TableColumn<Users, String> colName;
    @FXML
    private TableColumn<Users, String> colEmail;
    @FXML
    private TableColumn<Users, Role> colRole;
    @FXML
    private TableColumn<Users, Void> colAction;
    @FXML
    private Label lblResultCount;

    @FXML
    private TableView<Users> adminsTable;
    @FXML
    private TableColumn<Users, String> colAdminPhone;
    @FXML
    private TableColumn<Users, String> colAdminName;
    @FXML
    private TableColumn<Users, String> colAdminEmail;
    @FXML
    private TableColumn<Users, Role> colAdminRole;
    @FXML
    private TableColumn<Users, Void> colAdminAction;
    @FXML
    private Label lblAdminCount;

    private UsersDao usersDao;
    private ObservableList<Users> searchResults;
    private ObservableList<Users> adminsList;

    @FXML
    public void initialize() {
        usersDao = new UsersImpl();
        searchResults = FXCollections.observableArrayList();
        adminsList = FXCollections.observableArrayList();

        setupSearchResultsTable();
        setupAdminsTable();
        loadAllAdmins();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty()) {
                handleSearch();
            } else {
                searchResults.clear();
                lblResultCount.setText("0 users found");
            }
        });
        rootPane.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(500), rootPane);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    private void setupSearchResultsTable() {
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button actionButton = new Button();

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }

                Users user = getTableRow().getItem();

                if (user.getRole() == Role.MASTER_ADMIN) {
                    setGraphic(null);
                } else if (user.getRole() == Role.ADMIN) {
                    actionButton.setText("Remove Admin");
                    actionButton.getStyleClass().setAll("button", "btn-demote");
                    actionButton.setOnAction(e -> handleRemoveAdmin(user));
                    setGraphic(actionButton);
                } else {
                    actionButton.setText("Make Admin");
                    actionButton.getStyleClass().setAll("button", "btn-promote");
                    actionButton.setOnAction(e -> handleMakeAdmin(user));
                    setGraphic(actionButton);
                }
            }
        });

        searchResultsTable.setItems(searchResults);
    }

    private void setupAdminsTable() {
        colAdminPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colAdminName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAdminEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAdminRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        colAdminAction.setCellFactory(param -> new TableCell<>() {
            private final Button removeButton = new Button("Remove Admin");

            {
                removeButton.getStyleClass().setAll("button", "btn-demote");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }

                Users user = getTableRow().getItem();

                if (user.getRole() == Role.MASTER_ADMIN) {
                    setGraphic(null);
                } else {
                    removeButton.setOnAction(e -> handleRemoveAdmin(user));
                    setGraphic(removeButton);
                }
            }
        });

        adminsTable.setItems(adminsList);
    }

    @FXML
    private void handleSearch() {
        String searchQuery = searchField.getText().trim();

        if (searchQuery.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Empty Search", "Please enter a phone number or name to search.");
            return;
        }


        List<Users> results = usersDao.getAll().stream()
                .filter(user -> user.getPhoneNumber().contains(searchQuery) ||
                        user.getName().toLowerCase().contains(searchQuery.toLowerCase()))
                .toList();

        searchResults.clear();
        searchResults.addAll(results);

        lblResultCount.setText(results.size() + " user" + (results.size() != 1 ? "s" : "") + " found");
    }

    private void handleMakeAdmin(Users user) {
        showConfirmationAlert(
                "Make Admin",
                "Are you sure you want to promote " + user.getName() + " to Admin?",
                () -> {
                    boolean roleUpdated = usersDao.updateUserRole(user.getId(), Role.ADMIN);
                    boolean flagUpdated = usersDao.updateFirstLoginFlag(user.getId(), true);

                    if (roleUpdated && flagUpdated) {
                        handleSearch();
                        loadAllAdmins();
                    }
                }
        );
    }

    private void handleRemoveAdmin(Users user) {
        showConfirmationAlert(
                "Remove Admin",
                "Are you sure you want to remove admin privileges from " + user.getName() + "?",
                () -> {
                    boolean roleUpdated = usersDao.updateUserRole(user.getId(), Role.USER);
                    boolean flagUpdated = usersDao.updateFirstLoginFlag(user.getId(), false);

                    if (roleUpdated && flagUpdated) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", user.getName() + " has been demoted.");
                        handleSearch();
                        loadAllAdmins();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to update user role.");
                    }
                }
        );
    }

    private void loadAllAdmins() {
        List<Users> admins = usersDao.getAllAdmins();
        adminsList.clear();
        adminsList.addAll(admins);

        lblAdminCount.setText(admins.size() + " admin" + (admins.size() != 1 ? "s" : ""));
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        DialogPane dialogPane = alert.getDialogPane();
        String cssPath = getClass().getResource("/css/admin.css").toExternalForm();
        dialogPane.getStylesheets().add(cssPath);
        dialogPane.getStyleClass().add("dialog-pane");

        alert.showAndWait();
    }

    private void showConfirmationAlert(String title, String message, Runnable onConfirm) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/css/admin.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                onConfirm.run();
            }
        });
    }
}