package com.example.jlgChat.mapper;

import com.example.jlgChat.dao.MessageDao;
import com.example.jlgChat.domain.Message;
import com.example.jlgChat.service.MessageService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
@Service
public class MessageServiceIml implements MessageService {
    @Resource
    MessageDao messageDao;
    @Override
    public List<Message> getMessageListByBothId(int sendId, int targetId) {
        return messageDao.getMessageListByBothId(sendId, targetId);
    }
    @Override
    public List<Message> getMessageListById(int uid) {
        return messageDao.getMessageListById(uid);
    }

    @Override
    public void insertMessage(Message message) {
        messageDao.insertMessage(message);
    }

    @Override
    public void deleteMessageById(int id) {
        messageDao.deleteMessageById(id);
    }

    @Override
    public void updateRead(int sendId, int targetId) {
        messageDao.updateRead(sendId, targetId);
    }
}