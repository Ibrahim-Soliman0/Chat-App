package org.server.chatapp.rmi;

import model.Users;
import model.enums.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.server.chatapp.dao.ClientManager;
import org.server.chatapp.dao.dao.UsersDao;
import org.server.chatapp.dao.implement.UsersImpl;
import org.server.chatapp.util.PasswordUtil;
import rmi.ClientCallBack;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {
    @Mock
    private UsersDao usersDao;
    @Mock
    private ClientCallBack clientCallBack;
    @InjectMocks
    private LoginServiceImpl loginService;
    @BeforeEach
    void setUp() throws RemoteException {
        loginService = new LoginServiceImpl();
        try {
            Field field = LoginServiceImpl.class.getDeclaredField("usersDao");
            field.setAccessible(true);
            field.set(loginService, usersDao);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeAll
    static void setup() throws IOException {
        System.setProperty("testMode", "true");
    }

    @Test
    void login_ShouldReturnUser_WhenCredentialsAreValid() throws RemoteException {
        //Arrange:
        String phone = "01153726499";
        String plainPassword = "123456";
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);

        Users mockUser = new Users();
        mockUser.setPhoneNumber(phone);
        mockUser.setPassword(hashedPassword);

        when(usersDao.isPhoneNumberExists(phone)).thenReturn(true);
        when(usersDao.getUserByPhoneNumber(phone)).thenReturn(mockUser);
        when(usersDao.updateStatus(phone, Status.ONLINE)).thenReturn(true);

        // 2. Act
        Users result = loginService.login(phone, plainPassword, clientCallBack);

        // 3. Assert
        assertNotNull(result);
        assertEquals(phone, result.getPhoneNumber());

        verify(usersDao).isPhoneNumberExists(phone);
        verify(usersDao).updateStatus(phone, Status.ONLINE);

    }

    @Test
    void login_ShouldReturnNull_WhenPasswordIsWrong() throws RemoteException {
        // 1. Arrange
        String phone = "01153726499";
        String wrongPass = "wrongPass";

        Users mockUser = new Users();
        mockUser.setPhoneNumber(phone);
        mockUser.setPassword(PasswordUtil.hashPassword("correctPass"));

        when(usersDao.isPhoneNumberExists(phone)).thenReturn(true);
        when(usersDao.getUserByPhoneNumber(phone)).thenReturn(mockUser);

        // 2. Act
        Users result = loginService.login(phone, wrongPass, clientCallBack);

        // 3. Assert
        assertNull(result, "Should return null");
        verify(usersDao, never()).updateStatus(anyString(), any());
    }

    @Test
    void logout_ShouldUpdateStatusToOffline() throws RemoteException {
        // 1. Arrange
        String phone = "01153726499";
        when(usersDao.updateStatus(phone, Status.OFFLINE)).thenReturn(true);

        // 2. Act
        loginService.logout(phone);

        // 3. Assert & Verify
        verify(usersDao, times(1)).updateStatus(phone, Status.OFFLINE);
    }

    @Test
    void broadcastAnnouncement_ShouldAttemptToReachClients() throws RemoteException {
        // Arrange
        String phone = "01153726499";
        String title = "System Alert";
        String content = "Server Maintenance";
        ClientManager.addClient(phone, clientCallBack);

        Users mockUser = new Users();
        mockUser.setStatus(Status.ONLINE);
        when(usersDao.getUserByPhoneNumber(phone)).thenReturn(mockUser);

        // Act
        loginService.broadcastAnnouncement(title, content);

        // 3. Verify
        verify(usersDao, atLeastOnce()).getUserByPhoneNumber(phone);
        verify(clientCallBack, times(1)).receiveAnnouncement(title, content);
        //Cleanup
        ClientManager.removeClient(phone);
    }

    @Test
    void getUserProfilePicture() {
    }
}