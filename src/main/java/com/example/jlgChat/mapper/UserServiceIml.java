package com.example.jlgChat.mapper;

import com.example.jlgChat.dao.UserDao;
import com.example.jlgChat.domain.User;
import com.example.jlgChat.service.UserService;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
@Service
public class UserServiceIml implements UserService {
    @Resource
    UserDao userDao;

    @Override
    public List<User> getAllUser() {
        return userDao.getAllUser();
    }

    @Override
    public User getUserByUserId(long uid) {
        return userDao.getUserByUserId(uid);
    }

    @Override
    public User getUserByNickName(String nickName) {
        return userDao.getUserByNickName(nickName);
    }

    @Override
    public void insertUser(User user) {
        userDao.insertUser(user);
    }

    @Override
    public void deleteUserById(long uid) {
        userDao.deleteUserById(uid);
    }

    @Override
    public void updateUserByNickName(String nickName) {
        userDao.updateUserByNickName(nickName);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Override
    public void updateAvatar(Map map) {
        userDao.updateAvatar(map);
    }

    @Override
    public void updateFriendsList(int uid, String friendsList) {
        userDao.updateFriendsList(uid, friendsList);
    }

    @Override
    public String getFriendsList(int uid) {
        String friendsList = userDao.getFriendsList(uid);
        return friendsList;
    }

}
