package com.example.jlgChat.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.example.jlgChat.Responese.MyResponese;
import com.example.jlgChat.common.ChatMsgType;
import com.example.jlgChat.domain.Message;
import com.example.jlgChat.domain.SocketMessage;
import com.example.jlgChat.service.ISocketIOService;
import com.example.jlgChat.service.MessageService;
import com.example.jlgChat.util.ObjectToMap;
import com.example.jlgChat.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service(value = "socketIOService")
public class SocketIOServiceImpl implements ISocketIOService {

    /**
     * 存放已连接的客户端
     */
    private static Map<String, SocketIOClient> clientMap = new ConcurrentHashMap<>();

    /**
     * 自定义事件`push_data_event`,用于服务端与客户端通信
     */
    private static final String PUSH_DATA_EVENT = "chat";
    private static final String SET_UID = "setUid";
    private static final String SET_MESSAGELIST_CHANGED = "setMessageListChange";

    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    public MessageService messageService;

    /**
     * Spring IoC容器创建之后，在加载SocketIOServiceImpl Bean之后启动
     */
    @PostConstruct
    private void autoStartup() {
        start();
    }

    /**
     * Spring IoC容器在销毁SocketIOServiceImpl Bean之前关闭,避免重启项目服务端口占用问题
     */
    @PreDestroy
    private void autoStop() {
        stop();
    }

    private void customListener(String EventName) {
        // 自定义事件`client_info_event` -> 监听客户端消息
        System.out.println("EventName" + EventName);
        System.out.println("事件名称" + EventName);
        String[] ids = EventName.split("-");
        String uidString = ids[0];
        String targetIdString = ids[1];
        String targetClientId = targetIdString + "-" + uidString;
        if (clientMap.get(EventName) != null) {
            System.out.println(clientMap.get(EventName));
            return;
        }
        socketIOServer.addEventListener(EventName, Map.class, (client, data, ackSender) -> {
            // 客户端推送`client_info_event`事件时，onData接受数据，这里是string类型的json数据，还可以为Byte[],object其他类型
            String clientIp = getIpByClient(client);
            System.out.println(data);
            int targetId = Integer.parseInt(data.get("targetId").toString());
            int sendId = Integer.parseInt(data.get("sendId").toString());
            String chatId = data.get("chatId").toString();
            String msg = data.get("msg").toString();
            String targetNickName = data.get("targetNickName").toString();
            String sendNickName = data.get("sendNickName").toString();
            String targetAvatar = data.get("targetAvatar").toString();
            String sendAvatar = data.get("sendAvatar").toString();
            List<Message> messageList = this.insertMsg(targetId, sendId, msg, targetNickName, sendNickName, targetAvatar, sendAvatar);
            SocketIOClient myClient = this.clientMap.get(EventName);
            System.out.println("targetId🐶" + targetId);
            SocketIOClient messageListClient = this.clientMap.get(String.valueOf(targetId));
            SocketIOClient myClient2 = this.clientMap.get(targetClientId);
            myClient.sendEvent(EventName, messageList);
//            myClient2.sendEvent(targetClientId, messageList);
            System.out.println("事件名称" + targetId);
            messageListClient.sendEvent(String.valueOf(targetId), true);
//            messageListClient.sendEvent(targetClientId, messageList);
            myClient2.sendEvent(targetClientId, messageList);
            log.debug(clientIp + " ************ 客户端：" + data);
        });
    }

    @Override
    public void start() {
        // 监听客户端连接
        socketIOServer.addConnectListener(client -> {
            System.out.println("************ 客户端： " + getIpByClient(client) + " 已连接 ************");
            // 自定义事件`connected` -> 与客户端通信  （也可以使用内置事件，如：Socket.EVENT_CONNECT）
            client.sendEvent("connected", "你成功连接上了哦...");
            String userId = getParamsByClient(client);
            System.out.println("userid" + userId);
            if (userId != null) {
                clientMap.put(userId, client);
            }
        });

        // 监听客户端断开连接
        socketIOServer.addDisconnectListener(client -> {
            String clientIp = getIpByClient(client);
            System.out.println("客户端断开连接");
            String userId = getParamsByClient(client);
            if (userId != null) {
                clientMap.remove(userId);
                client.disconnect();
            }
        });

//         自定义事件`client_info_event` -> 监听客户端消息
        socketIOServer.addEventListener(SET_UID, String.class, (client, data, ackSender) -> {
            // 客户端推送`client_info_event`事件时，onData接受数据，这里是string类型的json数据，还可以为Byte[],object其他类型
//            String clientIp = getIpByClient(client);
            System.out.println("id是" + data);
            if (clientMap.get(data) != null) {
                clientMap.remove(data);
            } else {
                this.customListener(data);
            }
            clientMap.put(data, client);
            client.sendEvent(SET_UID, data);
//            socketIOServer.
//            log.debug(clientIp + " ************ 客户端：" + data);
        });
        //         自定义事件`client_info_event` -> 监听客户端消息
        socketIOServer.addEventListener(SET_MESSAGELIST_CHANGED, String.class, (client, data, ackSender) -> {
            // 客户端推送`client_info_event`事件时，onData接受数据，这里是string类型的json数据，还可以为Byte[],object其他类型
//            String clientIp = getIpByClient(client);
            System.out.println("SET_MESSAGELIST_CHANGEDid是" + data);
            if (clientMap.get(data) != null) {
                clientMap.remove(data);
            }
            clientMap.put(data, client);
//            socketIOServer.
//            log.debug(clientIp + " ************ 客户端：" + data);
        });
        // 启动服务
        socketIOServer.start();

        // broadcast: 默认是向所有的socket连接进行广播，但是不包括发送者自身，如果自己也打算接收消息的话，需要给自己单独发送。
//        new Thread(() -> {
//            int i = 0;
//            while (true) {
//                try {
//                    // 每3秒发送一次广播消息
//                    Thread.sleep(3000);
//                    socketIOServer.getBroadcastOperations().sendEvent("myBroadcast", "广播消息 " + String.valueOf(System.currentTimeMillis()));
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    List<Message> insertMsg(int targetId, int sendId, String msg, String targetNickName, String sendNickName, String targetAvatar, String sendAvatar) {
        if (String.valueOf(sendId) == null || String.valueOf(targetId) == null) {
            return null;
        }
        List<Message> messageList = new LinkedList();
        try {
            messageList = messageService.getMessageListByBothId(sendId, targetId);
            System.out.println(messageList.size());
        } catch (Exception e) {
            System.out.println(e);
        }
        String now = "";
        String chatId = "";
        System.out.println("消息列表");
        System.out.println(messageList);
        if (messageList == null || messageList.isEmpty()) {
            chatId = String.valueOf(System.currentTimeMillis());
            now = String.valueOf(System.currentTimeMillis());
        } else {
//            chatId = "12312";
//            System.out.println(messageList);
//            System.out.println(messageList.get(0));
            chatId = messageList.get(0).chatId;
            now = messageList.get(0).createTime;
        }
        System.out.println(chatId);
        Message myMes = new Message(now, String.valueOf(System.currentTimeMillis()), chatId, targetId, sendId, 1, 0, "text", msg, targetNickName, targetAvatar, sendAvatar, sendNickName);
        try {
            messageService.insertMessage(myMes);
            messageList = messageService.getMessageListByBothId(sendId, targetId);
            System.out.println(messageList.size());
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        return messageList;
    }

    @Override
    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
            socketIOServer = null;
        }
    }

    @Override
    public void pushMessageToUser(String userId, String msgContent) {
        SocketIOClient client = clientMap.get(userId);
        if (client != null) {
            client.sendEvent(PUSH_DATA_EVENT, msgContent);
        }
    }

    /**
     * 获取客户端url中的userId参数（这里根据个人需求和客户端对应修改即可）
     *
     * @param client: 客户端
     * @return: java.lang.String
     */
    private String getParamsByClient(SocketIOClient client) {
        // 获取客户端url参数（这里的userId是唯一标识）
        System.out.println(client.getHandshakeData().getUrlParams().get("targetId"));
//        Map<String, List<String>> params = client.getHandshakeData().getUrlParams();
//        List<String> userIdList = params.get("userId");
//        if (!CollectionUtils.isEmpty(userIdList)) {
//            return userIdList.get(0);
//        }
        return null;
    }

    /**
     * 获取连接的客户端ip地址
     *
     * @param client: 客户端
     * @return: java.lang.String
     */
    private String getIpByClient(SocketIOClient client) {
        String sa = client.getRemoteAddress().toString();
        System.out.println(sa);
        String clientIp = sa.substring(1, sa.indexOf(":"));
        return clientIp;
    }

}
