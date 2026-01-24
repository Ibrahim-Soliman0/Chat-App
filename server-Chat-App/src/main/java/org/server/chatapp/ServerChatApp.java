package org.server.chatapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.server.chatapp.rmi.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ServerChatApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ServerChatApp.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        try {
            var reg = LocateRegistry.createRegistry(5000);
            LoginServiceImpl loginService = new LoginServiceImpl();
            LoadFriendsListServiceImpl friendsListService = new LoadFriendsListServiceImpl();
            GetUserServiceImpl getUserService = new GetUserServiceImpl();
            RegisterServiceImpl registerService = new RegisterServiceImpl();
            FriendRequestServiceImpl friendRequestService = new FriendRequestServiceImpl();
            reg.rebind("LoginService", loginService);
            reg.rebind("LoadFriendsListService", friendsListService);
            reg.rebind("GetUserService", getUserService);
            reg.rebind("RegisterService", registerService);
            reg.rebind("FriendRequestService", friendRequestService);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        launch(args);
    }
}
