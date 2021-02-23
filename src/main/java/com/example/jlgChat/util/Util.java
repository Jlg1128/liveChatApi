package com.example.jlgChat.util;

import com.example.jlgChat.domain.Message;
import com.example.jlgChat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

enum MsgType {

}

public class Util{
    @Autowired
    UserService userService;
    public static int getUnReadMessageLength(int uid, List<Message> messageList) {
        int unreadMsgLength = 0;
        for (Message message1: messageList) {
            if (message1.targetId == uid) {
                if (message1.targetIfread == 0) {
                    unreadMsgLength+=1;
                }
            }
        }
        return unreadMsgLength;
    }
}

