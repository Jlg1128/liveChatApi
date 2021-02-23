package com.example.jlgChat.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
public class ResultVO {
    private String msg;
    private int code;
    private Object data;
    private boolean success;

    public String getMsg() {
        return msg;
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public static ResultVO getResult(String msg, int code, Object data, boolean success) {
        ResultVO resultVO = new ResultVO(msg, code, data, success);
        resultVO.setSuccess(success);
        resultVO.setData(data);
        resultVO.setMsg(msg);
        resultVO.setCode(code);
        return  resultVO;
    }

}
