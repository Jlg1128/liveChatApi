package com.example.jlgChat.dao;

import com.example.jlgChat.domain.Message;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface MessageDao {
    List<Message> getMessageListByBothId(int sendId, int targetId);
    List<Message> getMessageListById(int uid);
    void insertMessage(Message message);
    void deleteMessageById(int id);
    void updateRead(int sendId, int targetId);
}



