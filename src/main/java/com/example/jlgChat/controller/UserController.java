package com.example.jlgChat.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.jlgChat.Responese.MyResponese;
import com.example.jlgChat.domain.User;
import com.example.jlgChat.service.UserService;
import com.example.jlgChat.util.ObjectToMap;
import com.example.jlgChat.vo.ResultVO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    ResultVO Register(@RequestBody Map userMap) {
        if (userMap.get("nickName") == null) {
            return MyResponese.error("用户名不能为空");
        }
        if (userMap.get("password") == null) {
            return MyResponese.error("密码不能为空");
        }
        if (userMap.get("email") == null) {
            return MyResponese.error("邮箱不能为空");
        }
        if (userMap.get("avatar") == null) {
            return MyResponese.error("头像不能为空");
        }
        String nickName = userMap.get("nickName").toString();
        if (userService.getUserByNickName(nickName) != null) {
            return MyResponese.error("用户已经存在");
        }
        String email = userMap.get("email").toString();
        if (userService.getUserByEmail(email) != null) {
            return MyResponese.error("邮箱已经存在");
        }
        String password = userMap.get("password").toString();
        String avatar = userMap.get("avatar").toString();
        long timeStamp = System.currentTimeMillis();
        String now = String.valueOf(timeStamp);
        long id = Math.round((Math.random() + 1) * 1000);
        User user = new User(id, now, nickName, password, avatar, null, null, email);
        try {
            userService.insertUser(user);
        } catch (
            Exception e
        ) {
            return MyResponese.error(e.getMessage());
        }
        return MyResponese.success(user);

    }

    @PostMapping("/quit")
    ResultVO UserQuit(@RequestBody Map idmap) {
        if (idmap.get("uid") == null) {
            return MyResponese.error("用户id不能为空");
        } else {
            return MyResponese.success(null, "退出成功");
        }
    }

    @PostMapping("/login")
    ResultVO login(@RequestBody Map user) {
        String nickName = user.get("nickName").toString();
        String password = user.get("password").toString();
        if (nickName == null || password == null) {
            return MyResponese.error("用户名或密码不能为空");
        }
        System.out.println("用户名" + nickName);
        System.out.println("密码" + password);
        User queryUser = userService.getUserByNickName(nickName);
        if (queryUser == null) {
            return MyResponese.error("用户不存在");
        }
        if (queryUser.getPassword().equals(password) == false) {
            return MyResponese.error("用户名或者密码错误");
        }
        String avatar = queryUser.getAvatar();
        String signature = queryUser.getSignature();
        String email = queryUser.getEmail();
        String phoneNumber = queryUser.getPhoneNumber();
        String id = String.valueOf(queryUser.getUid());
        Map<String, String> resMap = new HashMap();
        resMap.put("nickName", nickName);
        resMap.put("uid", id);
        resMap.put("avatar", avatar);
        resMap.put("signature", signature);
        resMap.put("email", email);
        resMap.put("phoneNumber", phoneNumber);
        return MyResponese.success(resMap);
    }

    @GetMapping("/getUserByNickName")
    ResultVO getUerByNickName(@RequestParam("nickName") @Valid @NotNull(message = "用户名不能为空") String nickName) {
        User user = userService.getUserByNickName(nickName);
        if (user == null) {
            return MyResponese.error("用户不存在");
        } else {
            return MyResponese.success(user);
        }
    }

    @GetMapping("/getUserByUserId")
    ResultVO getUserByUserId(@RequestParam("uid") @Valid @NotNull(message = "uid不能为空") long uid) {
        User user = userService.getUserByUserId(uid);
        if (user == null) {
            return MyResponese.error("用户不存在");
        } else {
            return MyResponese.success(user);
        }
    }

    @PostMapping("/updateAvatar")
    ResultVO updateAvatar(@RequestBody Map map) {
        if (map.get("nickName") == null && map.get("uid") == null) {
            return MyResponese.error("用户名或用户id为空");
        }
        try {
            Map avatarMap = new HashMap();
            String avatar = map.get("avatar").toString();
            avatarMap.put("avatar", avatar);
            User user = null;
            if (map.get("nickName") != null) {
                user = userService.getUserByNickName("nickName");
                avatarMap.put("nickName", map.get("nickName").toString());
            }
            if (map.get("uid") != null) {
                user = userService.getUserByNickName("nickName");
                avatarMap.put("uid", map.get("uid").toString());
            }
            if (user == null) {
                return MyResponese.error("用户不存在");
            }
            userService.updateAvatar(avatarMap);
        } catch (Exception e) {
            MyResponese.error(e.getMessage());
        }
        return MyResponese.success("更新成功");
    }

    @PostMapping("/addFriends")
    ResultVO addFriends(@RequestBody Map ids) {
        if (ids.get("uid") == null || ids.get("targetId") == null) {
            return MyResponese.error("请求参数不完整");
        }
        int uid = Integer.parseInt(ids.get("uid").toString());
        int targetId = Integer.parseInt(ids.get("targetId").toString());
        User targetuser = userService.getUserByUserId(targetId);
        if (targetuser == null) {
            return MyResponese.error("所要添加的对象不存在");
        }
        try {
            String friendsListJson = userService.getFriendsList(uid);
            List<User> friendsList = new LinkedList<>();
            System.out.println(friendsListJson);
            if (friendsListJson == null) {
                System.out.println();
                friendsList.add(targetuser);
            } else {
                friendsList = JSON.parseArray(friendsListJson, User.class);
                for (User user : friendsList) {
                    System.out.println(user.getUid());
                    System.out.println(targetId);
                    System.out.println(targetId == user.getUid());
                    if (user.getUid() == targetId) {
                        return MyResponese.success(null, "用户已经是你的好友了");
                    }
                }
                friendsList.add(targetuser);
            }
            String friendsJson = JSON.toJSONString(friendsList);
            userService.updateFriendsList(uid, friendsJson);
            return MyResponese.success(friendsList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return MyResponese.error(e.getMessage());
        }
    }

    @PostMapping("/deleteFriends")
    ResultVO deleteFriends(@RequestBody Map ids) {
        if (ids.get("uid") == null || ids.get("targetId") == null) {
            return MyResponese.error("请求参数不完整");
        }
        int uid = Integer.parseInt(ids.get("uid").toString());
        int targetId = Integer.parseInt(ids.get("targetId").toString());
        try {
            // 获取friendsJson
            String friendsListJson = userService.getFriendsList(uid);
            List<User> friendsList = new LinkedList<>();
            System.out.println(friendsListJson);
            if (friendsListJson == null) {
                return MyResponese.error("对象不存在");
            } else {
                // json对象转化为数组
                friendsList = JSON.parseArray(friendsListJson, User.class);
                friendsList = friendsList.stream().filter((User user) -> user.getUid() != targetId)
                    .collect(Collectors.toList());
                ;
            }
            String friendsJson = JSON.toJSONString(friendsList);
            userService.updateFriendsList(uid, friendsJson);
            return MyResponese.success(friendsList);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return MyResponese.error(e.getMessage());
        }
    }

    @GetMapping("/getFriendsList")
    ResultVO getFriendsList(@RequestParam @Valid @NotNull() int uid) {
        User user = userService.getUserByUserId(uid);
        if (user == null) {
            return MyResponese.error("用户不存在");
        }
        String friendsListJson = userService.getFriendsList(uid);
//        JSONObject jsonObject= JSONObject.parseObject(friendsListJson);
//        User user1 = JSONObject.toJavaObject(jsonObject, User.class);
        List<User> friendsList = JSON.parseArray(friendsListJson, User.class);
        return MyResponese.success(friendsList);
    }
}
