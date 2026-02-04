package org.client.chatapp.config;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.client.chatapp.ui.utils.SavedUserUtil;

import java.io.File;
import java.util.Optional;

public class ConfigManager {
    private static final String FILE_PATH = "client-Chat-App/config.xml";

    public static void saveConfig(UserConfig config) {
        try {
            File file = new File(FILE_PATH);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            JAXBContext context = JAXBContext.newInstance(UserConfig.class);
            Marshaller marshaller = context.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            marshaller.marshal(config, new File(FILE_PATH));
//            System.out.println("Config saved to: " + FILE_PATH);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public static UserConfig loadConfig() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
//            System.out.println("No config file found, creating a new one.");
            return new UserConfig();
        }

        try {
            JAXBContext context = JAXBContext.newInstance(UserConfig.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (UserConfig) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
            return new UserConfig();
        }
    }

    public static void addUser(SavedUserUtil newUser) {
        UserConfig currentConfig = loadConfig();
        Optional<SavedUserUtil> existingUser = currentConfig.getUsers().stream()
                .filter(u -> u.getPhoneNumber().equals(newUser.getPhoneNumber()))
                .findFirst();
        if(existingUser.isPresent()){
            existingUser.get().setName(newUser.getName());
            existingUser.get().setEncryptedPassword(newUser.getEncryptedPassword());
        }else{
            currentConfig.getUsers().add(newUser);
        }
        saveConfig(currentConfig);
    }
    public static void removeUser(String phoneNumber) {
        UserConfig config = loadConfig();
        if (config != null) {
            config.getUsers().removeIf(u -> u.getPhoneNumber().equals(phoneNumber));
            saveConfig(config);
        }
    }
    public static void logoutUser(String phoneNumber) {
        UserConfig config = loadConfig();
        if (config != null) {
            config.getUsers().stream()
                    .filter(u -> u.getPhoneNumber().equals(phoneNumber))
                    .findFirst()
                    .ifPresent(u -> u.setEncryptedPassword(null));
            saveConfig(config);
        }
    }
}
