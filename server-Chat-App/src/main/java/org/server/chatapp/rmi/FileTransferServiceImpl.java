package org.server.chatapp.rmi;

import rmi.FileTransferService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class FileTransferServiceImpl extends UnicastRemoteObject implements FileTransferService {

    public FileTransferServiceImpl() throws RemoteException {
        File storageDir = new File(FileTransferService.STORAGE_PATH);

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
    }

    @Override
    public void uploadFileToServer(byte[] myByte, String serverPath) throws RemoteException {

        File serverPathfile = new File(STORAGE_PATH + "/" + serverPath);
        try (FileOutputStream out = new FileOutputStream(serverPathfile)) {
            out.write(myByte);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] downloadFileFromServer(String serverPath) throws RemoteException {

        File serverPathfile = new File(STORAGE_PATH, serverPath);
        byte[] myData = new byte[(int) serverPathfile.length()];

        try (FileInputStream in = new FileInputStream(serverPathfile)) {
            in.read(myData, 0, myData.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return myData;
    }
}
