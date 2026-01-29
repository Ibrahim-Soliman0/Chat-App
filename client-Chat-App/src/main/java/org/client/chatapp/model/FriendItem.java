package org.client.chatapp.model;

public class FriendItem {

    private String profilePicPath, name;

    public FriendItem(String profilePicPath, String name) {
        this.profilePicPath = profilePicPath;
        this.name = name;
    }

    public String getProfilePicPath() {
        return profilePicPath;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
