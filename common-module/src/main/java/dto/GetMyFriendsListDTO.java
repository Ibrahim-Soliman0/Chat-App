package dto;

import java.io.Serializable;

public class GetMyFriendsListDTO implements Serializable {

    private long id;

    public GetMyFriendsListDTO() {}

    public GetMyFriendsListDTO(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
