package com.example.jlgChat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SocketMessage {
    String targetId;
    String sendId;
    String msg;
    String chatId;
    String targetAvatar;
    String targetNickName;
    String sendAvatar;
    String sendNickName;
    public SocketMessage(String targetId, String sendId, String msg, String chatId, String targetAvatar, String targetNickName, String sendAvatar, String sendNickName) {
        this.targetId = targetId;
        this.sendId = sendId;
        this.msg = msg;
        this.chatId = chatId;
        this.targetAvatar = targetAvatar;
        this.targetNickName = targetNickName;
        this.sendAvatar = sendAvatar;
        this.sendNickName = sendNickName;
    }
}
