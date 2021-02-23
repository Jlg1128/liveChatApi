package com.example.jlgChat.common;



public class ChatMsgType {
    public static String getMsgType(String type, String msg) {
        switch (type) {
            case "sound":
                return "[语音]";
            case "text":
                return msg;
            case "image":
                return "[图片]";
            case "video":
                return "[视频]";
            default:
                return msg;
        }
    }
}
