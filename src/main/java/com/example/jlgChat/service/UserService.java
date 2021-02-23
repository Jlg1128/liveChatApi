package com.example.jlgChat.service;

import com.example.jlgChat.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {
    List<User> getAllUser();
    User getUserByUserId(long uid);
    User getUserByNickName(String nickName);
    void insertUser(User user);
    void deleteUserById(long uid);
    void updateUserByNickName(String nickName);
    User getUserByEmail(String email);
    void updateAvatar(Map map);
    void updateFriendsList(int uid, String friendsList);
    String getFriendsList(int uid);
}
