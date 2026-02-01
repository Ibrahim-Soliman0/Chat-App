package org.server.chatapp.dao;

import rmi.ClientCallBack;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {
    private static final Map<String, ClientCallBack> onlineClients = new ConcurrentHashMap<>();

    public static void addClient(String phone, ClientCallBack callback) {
        if (phone != null && callback != null) {
            onlineClients.put(phone, callback);
        }
    }

    public static void removeClient(String phone) {
        onlineClients.remove(phone);
    }

    public static ClientCallBack getClient(String phone) {
        return onlineClients.get(phone);
    }

    public static Map<String, ClientCallBack> getAllOnlineClients() {
        return Collections.unmodifiableMap(onlineClients);
    }
}