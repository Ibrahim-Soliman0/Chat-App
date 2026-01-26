package org.client.chatapp.ui.controller;

import dto.ChatRoomDTO;

public class ChatRoomController {

    private ChatRoomDTO chatRoomDTO;

    public void initialize() {

    }

    public void setChatRoomDTO(ChatRoomDTO chatRoomDTO) {
        this.chatRoomDTO = chatRoomDTO;
        //! Don't forget to load data used from that object here as
        //! in the initialize method it will still be null because it gets called before this method
    }
}
