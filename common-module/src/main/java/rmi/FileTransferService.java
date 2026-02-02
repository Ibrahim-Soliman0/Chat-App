package rmi;//This class interfaces directly between the client and the server

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileTransferService extends Remote {

    String STORAGE_PATH = "server-Chat-App/uploads/message files";
    String CLIENT_MESSAGE_PATH = "client-Chat-App/src/main/resources/Message Downloads";

    void uploadFileToServer(byte[] myByte, String serverPath) throws RemoteException;

    byte[] downloadFileFromServer(String servername) throws RemoteException;
}
