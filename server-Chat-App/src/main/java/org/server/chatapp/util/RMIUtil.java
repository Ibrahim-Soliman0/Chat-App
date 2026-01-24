package org.server.chatapp.util;

import org.server.chatapp.rmi.GetUserServiceImpl;
import org.server.chatapp.rmi.LoadFriendsListServiceImpl;
import org.server.chatapp.rmi.LoginServiceImpl;
import org.server.chatapp.rmi.RegisterServiceImpl;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIUtil {
    private static Registry registry;
    private static boolean isRunning = false;

    public static void startServices() throws RemoteException {
        if (!isRunning) {
            registry = LocateRegistry.createRegistry(5000);
            registry.rebind("LoginService", new LoginServiceImpl());
            registry.rebind("LoadFriendsListService", new LoadFriendsListServiceImpl());
            registry.rebind("GetUserService", new GetUserServiceImpl());
            registry.rebind("RegisterService", new RegisterServiceImpl());
            isRunning = true;
            System.out.println("Server start...");
        }
    }
    public static void stopServices() throws RemoteException, NotBoundException {
        if (isRunning && registry != null) {
            for (String bound : registry.list()) {
                registry.unbind(bound);
            }
            UnicastRemoteObject.unexportObject(registry, true);
            registry = null;
            isRunning = false;
            System.out.println("Server stopped");
        }
    }
    public static boolean isRunning() {
        return isRunning;
    }
}