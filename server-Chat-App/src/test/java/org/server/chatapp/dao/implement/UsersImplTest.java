package org.server.chatapp.dao.implement;

import model.Users;
import model.enums.Gender;
import model.enums.Status;
import org.junit.jupiter.api.*;
import org.server.chatapp.dao.Database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsersImplTest {
    private static UsersImpl usersDao;
    private static Users testUser;
    private static long tempUserId = 0;

    @BeforeAll
    static void setup() throws IOException {
        System.setProperty("testMode", "true");
        usersDao = new UsersImpl();

        Path folderPath = Paths.get("server-Chat-App", "uploads", "profiles");
        Files.createDirectories(folderPath);

        Path defaultPic = folderPath.resolve("defaultProfilePic.png");
        if (Files.notExists(defaultPic)) {
            Files.createFile(defaultPic);
        }
    }

    @Test
    @Order(5)
    void getUserByPhoneNumber() {
        Users user = usersDao.getUserByPhoneNumber("01153726499");
        assertNotNull(user);
        assertEquals("Ahmed Updated", user.getName());
        assertEquals("ahmedmohamed085.am@gmail.com" , user.getEmail());
    }

    @Test
    void get() {
    }

    @Test
    void getAll() {
    }

    @Test
    @Order(2)
    void update() {
        // 1. Arrange:
        Users user = usersDao.get(tempUserId);
        assertNotNull(user, "User should exist in H2 DB");

        user.setName("Ahmed Updated");
        user.setBio("New Bio for Testing");

        // 2. Act:
        int rowsAffected = usersDao.update(user);

        // 3. Assert:
        assertEquals(1, rowsAffected, "One row should be updated");

        Users updatedUser = usersDao.get(tempUserId);
        assertEquals("Ahmed Updated", updatedUser.getName());
        assertEquals("New Bio for Testing", updatedUser.getBio());
    }

    @Test
    @Order(1)
    void insert() {
        // 1. Arrange:
        Users user = new Users();
        user.setPhoneNumber("01153726499");
        user.setName("Ahmed Ramadan");
        user.setEmail("ahmedmohamed085.am@gmail.com");
        user.setPassword("123456HashPassword");
        user.setGender(Gender.MALE);
        user.setCountry("Egypt");
        user.setDob(LocalDate.of(2003, 9, 18));
        user.setStatus(Status.ONLINE);

        // 2. Act:
        long generatedId = usersDao.insert(user);

        // 3. Assert:
        assertTrue(generatedId > 0, "id must be greater than 0 after insert");

        tempUserId = generatedId;
    }

    @Test
    @Order(8)
    void delete() {
        int result = usersDao.delete(usersDao.get(tempUserId));
        assertEquals(1, result);
        assertNull(usersDao.get(tempUserId), "User should be gone from DB");
    }

    @Test
    @Order(3)
    void isPhoneNumberExists() {
        Users users = usersDao.get(tempUserId);
        boolean act = usersDao.isPhoneNumberExists(users.getPhoneNumber());
        assertTrue(act, "Phone number is exists");
    }
    @Test
    @Order(4)
    void isPhoneNumberNotExists() {
        boolean exists = usersDao.isPhoneNumberExists("00000000000");
        assertFalse(exists, "Wrong random phone number should NOT exist");
    }

    @Test
    void isEmailExists() {
    }

    @Test
    @Order(6)
    void searchUsersByPhoneNumber() {
        // 1. Arrange:
        Users anotherUser = new Users();
        anotherUser.setPhoneNumber("01234567890");
        anotherUser.setName("Search Target");
        anotherUser.setEmail("search@test.com");
        anotherUser.setPassword("pass");
        anotherUser.setGender(Gender.MALE);
        anotherUser.setCountry("Egypt");
        anotherUser.setDob(LocalDate.of(2000, 1, 1));
        anotherUser.setStatus(Status.ONLINE);
        usersDao.insert(anotherUser);

        // 2. Act:
        var list = usersDao.searchUsersByPhoneNumber("01234", tempUserId);
        var list1 = usersDao.searchUsersByPhoneNumber("01153" , tempUserId);
        // 3. Assert:
        assertFalse(list.isEmpty(), "Search should find the second user");
        assertTrue(list1.isEmpty() , "User should'nt find himself");
        assertTrue(list.stream().anyMatch(u -> u.getPhoneNumber().equals("01234567890")));
    }

    @Test
    @Order(7)
    void updateStatus() {
        boolean rows = usersDao.updateStatus("01153726499" , Status.BUSY);
        assertTrue(rows);
        assertEquals(Status.BUSY, usersDao.get(tempUserId).getStatus());
    }

    @Test
    void updateUserRole() {
    }

    @Test
    void updateFirstLoginFlag() {
    }

    @Test
    void isAdmin() {
    }

    @Test
    void getAllAdmins() {
    }

    @Test
    void getUserRole() {
    }

    @Test
    void updatePasswordAndClearFirstLogin() {
    }

    @AfterAll
    static void tearDown() {
        if (Database.getDataSource() != null) {
            Database.getDataSource().close();
            System.out.println("Database connection closed.");
        }

        try {
            Path defaultPic = Paths.get("server-Chat-App", "uploads", "profiles", "defaultProfilePic.png");
            Files.deleteIfExists(defaultPic);

            Files.deleteIfExists(Paths.get("server-Chat-App/uploads/profiles"));
            Files.deleteIfExists(Paths.get("server-Chat-App/uploads"));
            Files.deleteIfExists(Paths.get("server-Chat-App"));

            System.out.println("Cleanup successful: Dummy files removed.");
        } catch (IOException e) {
            System.err.println("Cleanup failed: " + e.getMessage());
        }
    }
}