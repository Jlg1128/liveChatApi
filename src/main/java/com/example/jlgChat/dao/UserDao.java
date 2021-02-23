package com.example.jlgChat.dao;

import com.example.jlgChat.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserDao {
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
