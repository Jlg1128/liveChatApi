package com.example.jlgChat.domain;

import com.example.jlgChat.common.ChatMsgType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageUnRead {
        public int unreadMsgLength;
        public String createTime;
        public String modifyTime;
        public String chatId;
        public Integer targetId;
        public Integer sendId;
        public Integer ifread;
        public Integer targetIfread;
        public String type;
        public String msg;
        public String targetNickName;
        public String targetAvatar;
        public String sendAvatar;
        public String sendNickName;

}
