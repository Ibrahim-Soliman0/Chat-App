package rmi;

import dto.GroupDTO;
import model.Room;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GroupService extends Remote {
    public Room createGroup(GroupDTO groupDTO)throws RemoteException;
}
