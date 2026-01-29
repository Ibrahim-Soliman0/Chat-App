package org.client.chatapp.ui.utils;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;

public class ImageUtil {

    public static Image getImageFromByteArray(byte[] imageBytes) {

        ByteArrayInputStream inStream = new ByteArrayInputStream(imageBytes);

        return new Image(inStream);
    }
}
