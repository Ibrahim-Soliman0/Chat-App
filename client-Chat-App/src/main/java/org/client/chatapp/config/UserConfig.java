package org.client.chatapp.config;

import jakarta.xml.bind.annotation.*;
import org.client.chatapp.ui.utils.SavedUserUtil;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "user-configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserConfig {
    @XmlElementWrapper(name = "saved-users")
    @XmlElement(name = "user")
    private List<SavedUserUtil> users = new ArrayList<>();

    public List<SavedUserUtil> getUsers() {
        return users;
    }

    public void setUsers(List<SavedUserUtil> users) {
        this.users = users;
    }

    //expected XML
    /*
    <user-configuration>
    <saved-users>
        <user>
            <name>Ahmed</name>
            <phoneNumber>01012345678</phoneNumber>
            <encryptedPassword>...</encryptedPassword>
        </user>
        <user>
            <name>Mohamed</name>
            <phoneNumber>01222222222</phoneNumber>
            <encryptedPassword>...</encryptedPassword>
        </user>
    </saved-users>
</user-configuration>
     */
}
