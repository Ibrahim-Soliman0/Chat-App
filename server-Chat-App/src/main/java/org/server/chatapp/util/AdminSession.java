package org.server.chatapp.util;

import model.Users;

public class AdminSession {
    private static Users currentAdmin;

    public static void setInstance(Users admin) {
        currentAdmin = admin;
    }
    public static Users getInstance() {
        return currentAdmin;
    }
    public static void terminate() {
        currentAdmin = null;
    }
}