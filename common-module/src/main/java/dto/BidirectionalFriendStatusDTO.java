package dto;

import model.Friend;

import java.io.Serializable;

public class BidirectionalFriendStatusDTO implements Serializable {

    private Friend me, other;

    public BidirectionalFriendStatusDTO(Friend me, Friend other) {
        this.me = me;
        this.other = other;
    }

    public Friend getMe() {
        return me;
    }

    public void setMe(Friend me) {
        this.me = me;
    }

    public Friend getOther() {
        return other;
    }

    public void setOther(Friend other) {
        this.other = other;
    }
}
