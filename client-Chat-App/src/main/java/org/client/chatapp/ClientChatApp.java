package org.client.chatapp;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;

public class ClientChatApp extends Application {

    public static Registry registry;
    public static ObservableList<String> allStyles = FXCollections.observableArrayList();
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientChatApp.class.getResource("profile-screen-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        allStyles.addAll(Objects.requireNonNull(getClass().getResource("/css/chat-light.css"))
                        .toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/css/chat-components.css"))
                        .toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/css/login.css"))
                        .toExternalForm(),
                Objects.requireNonNull(getClass().getResource("/css/combobox.css"))
                        .toExternalForm());
        scene.getStylesheets().addAll(allStyles);
        stage.setTitle("!What's App");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {

        try {
            registry = LocateRegistry.getRegistry(5000);
        } catch (RemoteException e) {
            e.printStackTrace();

            return;
        }

        launch(args);
    }
}
