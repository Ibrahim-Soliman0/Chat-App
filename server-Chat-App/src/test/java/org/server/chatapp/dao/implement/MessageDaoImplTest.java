package org.server.chatapp.dao.implement;

import model.Message;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MessageDaoImplTest {
    private static MessageDaoImpl messageDao;
    private static Message testMessage;

    @BeforeAll
    static void setup() {
        messageDao = new MessageDaoImpl();
    }

    @Test
    @Order(1)
    void testInsert() {
        testMessage = new Message();
        testMessage.setSenderId(1);
        testMessage.setRoomId(1);
        testMessage.setText("JUnit Test Message");
        testMessage.setFontFamily("Arial");
        testMessage.setFontSize(14);
        testMessage.setFontColor("#000000");
        testMessage.setBackgroundColor("#FFFFFF");
        testMessage.setBold(false);
        testMessage.setItalic(false);
        testMessage.setUnderline(false);
        testMessage.setAttachedFile(null);
        testMessage.setFileName(null);
        testMessage.setFileType(null);
        testMessage.setFileSize(0);
        testMessage.setSentAt(LocalDateTime.now());
        testMessage.setDeleted(false);

        long inserted = messageDao.insert(testMessage);
        assertEquals(1, inserted, "Insert should affect 1 row");
        assertTrue(testMessage.getId() > 0, "Inserted message should have generated ID");
    }

    @Test
    @Order(2)
    void testGetById() {
        Message fetched = messageDao.get(testMessage.getId());
        assertNotNull(fetched, "Fetched message should not be null");
        assertEquals(testMessage.getText(), fetched.getText(), "Text should match");
    }

    @Test
    @Order(3)
    void testUpdate() {
        testMessage.setText("Updated by JUnit");
        testMessage.setBold(true);
        int updated = messageDao.update(testMessage);
        assertEquals(1, updated, "Update should affect 1 row");

        Message fetched = messageDao.get(testMessage.getId());
        assertEquals("Updated by JUnit", fetched.getText());
        assertTrue(fetched.isBold());
    }

    @Test
    @Order(4)
    void testGetMessagesByRoomId() {
        List<Message> messages = messageDao.getMessagesByRoomId(testMessage.getRoomId());
        assertFalse(messages.isEmpty(), "Messages list should not be empty");
        assertTrue(messages.stream().anyMatch(m -> m.getId() == testMessage.getId()), "List should contain test message");
    }

    @Test
    @Order(5)
    void testDelete() {
        int deleted = messageDao.delete(testMessage);
        assertEquals(1, deleted, "Delete should affect 1 row");

        List<Message> messages = messageDao.getMessagesByRoomId(testMessage.getRoomId());
        assertFalse(messages.stream().anyMatch(m -> m.getId() == testMessage.getId()), "Deleted message should not appear in room messages");
    }
}