package com.example.jlgChat.service;

import com.example.jlgChat.domain.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface MessageService {
    List<Message> getMessageListByBothId(int sendId, int targetId);
    List<Message> getMessageListById(int uid);
    void insertMessage(Message message);
    void deleteMessageById(int id);
    void updateRead(int sendId, int targetId);

}
