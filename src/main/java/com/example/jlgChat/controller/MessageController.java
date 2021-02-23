package com.example.jlgChat.controller;

import com.example.jlgChat.Responese.MyResponese;
import com.example.jlgChat.common.ChatMsgType;
import com.example.jlgChat.domain.Message;
import com.example.jlgChat.domain.MessageUnRead;
import com.example.jlgChat.service.MessageService;
import com.example.jlgChat.serviceImpl.SocketIOServiceImpl;
import com.example.jlgChat.util.Util;
import com.example.jlgChat.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/chat")
public class MessageController {
    @Autowired
    MessageService messageService;
    @Autowired
    SocketIOServiceImpl socketIOService;
    @GetMapping("/getCurrentMsg")
    ResultVO getCurrentMessageList(@RequestParam int targetId, @RequestParam int sendId) {
        List messageList = messageService.getMessageListByBothId(sendId, targetId);
        return MyResponese.success(messageList);
    }

    // 获取聊天人员列表
    @GetMapping("/getChatList")
    ResultVO getChatList(@RequestParam int uid) throws Exception {
        List<Message> messageList = messageService.getMessageListById(uid);
//        Object a = new Message("123", 123, 456, 1,0, "text", "xixi", "123", "123", "123", "123");
        Collections.sort(messageList,new Comparator<Message>(){
            public int compare(Message arg0, Message arg1) {
                return arg0.chatId.compareTo(arg1.chatId);
            }
        });
        Collections.sort(messageList,new Comparator<Message>(){
            public int compare(Message arg0, Message arg1) {
                return arg0.chatId.compareTo(arg1.chatId);
            }
        });
        List<MessageUnRead> resultArr = new LinkedList<MessageUnRead>();
        int preIndex = 0;
        int breakPoint = 0;
        List<Message> currentMessageList = new LinkedList<Message>();
        for (int index = 0; index < messageList.size(); index++) {
            Message message = messageList.get(index);
            if (messageList.isEmpty()) {
                break;
            }
            if (index == 0) {
                if (messageList.size() == 1) {
                    MessageUnRead messageUnRead = new MessageUnRead(
                        Util.getUnReadMessageLength(uid, messageList),
                        message.createTime,
                        message.modifyTime,
                        message.chatId,
                        message.targetId,
                        message.sendId,
                        message.ifread,
                        message.targetIfread,
                        message.type,
                        message.msg,
                        message.targetNickName,
                        message.targetAvatar,
                        message.sendAvatar,
                        message.sendNickName
                    );
                    resultArr.add(messageUnRead);
                }
                preIndex = 0;
                continue;
            }
            Message prevMessage = messageList.get(preIndex);
            int prevId1 = prevMessage.targetId ;
            int prevId2 = prevMessage.sendId ;
            int Id1 = message.targetId;
            int Id2 = message.sendId;
            if ((Id1 == prevId1 || Id1 == prevId2) && (Id2 == prevId1 || Id2 == prevId2)
            ) {
                if (index == messageList.size() - 1) {
//                    Map responseMap = ObjectToMap.objectToMap(message);
//                    responseMap.put("unreadMsgLength", Util.getUnReadMessageLength(uid, messageList.subList(breakPoint, index)));
                    MessageUnRead messageUnRead = new MessageUnRead(
                        Util.getUnReadMessageLength(uid, messageList.subList(breakPoint, index + 1)),
                        message.createTime,
                        message.modifyTime,
                        message.chatId,
                        message.targetId,
                        message.sendId,
                        message.ifread,
                        message.targetIfread,
                        message.type,
                        message.msg,
                        message.targetNickName,
                        message.targetAvatar,
                        message.sendAvatar,
                        message.sendNickName
                    );
                    resultArr.add(messageUnRead);
                }
                preIndex++;
                continue;
            } else {
//                Map responseMap = ObjectToMap.objectToMap(prevMessage);
//                responseMap.put("unreadMsgLength", Util.getUnReadMessageLength(uid, messageList.subList(breakPoint, index)));
                MessageUnRead messageUnRead = new MessageUnRead(
                    Util.getUnReadMessageLength(uid, messageList.subList(breakPoint, index + 1)),
                    prevMessage.createTime,
                    prevMessage.modifyTime,
                    prevMessage.chatId,
                    prevMessage.targetId,
                    prevMessage.sendId,
                    prevMessage.ifread,
                    prevMessage.targetIfread,
                    prevMessage.type,
                    ChatMsgType.getMsgType(prevMessage.type, prevMessage.msg),
                    prevMessage.targetNickName,
                    prevMessage.targetAvatar,
                    prevMessage.sendAvatar,
                    prevMessage.sendNickName
                );
                if (index == messageList.size() - 1) {
//                    Map responseMap = ObjectToMap.objectToMap(message);
//                    responseMap.put("unreadMsgLength", Util.getUnReadMessageLength(uid, messageList.subList(breakPoint, index)));
                    MessageUnRead messageUnRead1 = new MessageUnRead(
                        Util.getUnReadMessageLength(uid, messageList.subList(breakPoint, index + 1)),
                        message.createTime,
                        message.modifyTime,
                        message.chatId,
                        message.targetId,
                        message.sendId,
                        message.ifread,
                        message.targetIfread,
                        message.type,
                        message.msg,
                        message.targetNickName,
                        message.targetAvatar,
                        message.sendAvatar,
                        message.sendNickName
                    );
                    resultArr.add(messageUnRead1);
                }
                resultArr.add(messageUnRead);
                breakPoint = index;
                preIndex++;
            }
        }
        Collections.sort(resultArr,new Comparator<MessageUnRead>(){
            public int compare(MessageUnRead arg0, MessageUnRead arg1) {
                return arg1.modifyTime.compareTo(arg0.modifyTime);
            }
        });
        return MyResponese.success(resultArr);
    }
    @PostMapping("/messageRead")
    ResultVO messageRead(@RequestBody Map uids) {
        int targetId = Integer.parseInt(uids.get("targetId").toString()) ;
        int sendId = Integer.parseInt(uids.get("sendId").toString());
        System.out.println("targetId" + targetId);
        System.out.println("sendId" + sendId);
        try {
            messageService.updateRead(sendId, targetId);
        } catch (Exception e) {
            return MyResponese.error("数据库错误");
        }
        return MyResponese.success(null, "插入成功");
    }
}
