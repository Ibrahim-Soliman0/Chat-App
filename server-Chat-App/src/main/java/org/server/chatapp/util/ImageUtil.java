package org.server.chatapp.util;

import model.Room;
import model.Users;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ImageUtil {

    public static void setImageBytes(Users user) {

        byte[] imageBytes = helperGetImageFile(user.getPicturePath());

        user.setPictureBytes(imageBytes);
    }

    public static void setImageBytes(Room room) {

        byte[] imageBytes = helperGetImageFile(room.getPicturePath());

        room.setPictureBytes(imageBytes);
    }

    private static byte[] helperGetImageFile(String path) {
        try {
            File image;
            if (path != null) {
                image = new File(path);
                if (!image.exists()) {
                    image = new File("server-Chat-App\\uploads\\profiles\\defaultProfilePic.png");
                }
            } else {
                image = new File("server-Chat-App\\uploads\\profiles\\defaultProfilePic.png");
            }

            return Files.readAllBytes(image.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
