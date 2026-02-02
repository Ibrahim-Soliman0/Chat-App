module org.client.chatapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.common.module;
    requires java.rmi;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires jakarta.xml.bind;
    requires com.sun.xml.bind;
    requires javafx.media;

    exports org.client.chatapp;
    exports org.client.chatapp.ui.utils;
    exports org.client.chatapp.config;

    opens org.client.chatapp.config to jakarta.xml.bind, com.sun.xml.bind;
    opens org.client.chatapp.ui.utils to jakarta.xml.bind, com.sun.xml.bind;

    opens org.client.chatapp to javafx.fxml;
    opens org.client.chatapp.ui.controller to javafx.fxml;
    exports org.client.chatapp.ui.controller;

}