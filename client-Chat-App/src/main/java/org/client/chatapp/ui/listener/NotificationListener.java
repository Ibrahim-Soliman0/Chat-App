package org.client.chatapp.ui.listener;

public interface NotificationListener {

    void onNewNotification();

    default void onNewMessage() {
    }
}
