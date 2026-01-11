package org.server.chatapp;

import javafx.application.Application;
import org.server.chatapp.dao.implement.UsersImpl;

public class Launcher {
    public static void main(String[] args) {
        UsersImpl user = new UsersImpl();
        var users = user.getUserByPhoneNumber("0100000003");
        System.out.println(users);
        Application.launch(HelloApplication.class, args);
    }
}
