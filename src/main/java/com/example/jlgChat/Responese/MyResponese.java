package com.example.jlgChat.Responese;

import com.example.jlgChat.vo.ResultVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

enum ResStatus {
    SUCCESS,
    ERROR,
    NOAUTH,
    PARAMWRONG
}
@Data
@NoArgsConstructor
public class MyResponese {
    public ResStatus status;
    public String msg;
    public Object data;

    public static ResultVO success(Object data) {
        ResultVO resultVOSuccess = setResponese(ResStatus.SUCCESS, data, 200, true);
        System.out.println(resultVOSuccess);
        return resultVOSuccess;
    }
    public static ResultVO success(Object data,  String msg) {
        ResultVO resultVOSuccess = setResponese(data, 401, msg, false);
        System.out.println(resultVOSuccess);
        return resultVOSuccess;
    }
    public static ResultVO error() {
        ResultVO responeseError = setResponese(ResStatus.ERROR, null, 401, false);
        return responeseError;
    }
    public static ResultVO error(String msg) {
        ResultVO responeseError = setResponese(null, 401, msg, false);
        return responeseError;
    }
    public static ResultVO noauth() {
        ResultVO responeseNoAuth = setResponese(ResStatus.NOAUTH, null, 200, false);
        return responeseNoAuth;
    }
    public static ResultVO noauth(String msg) {
        ResultVO responeseNoAuth = setResponese(null, 200, msg, false);
        return responeseNoAuth;
    }

    public static ResultVO paramwrong(String msg) {
        ResultVO responeseParamWrong = setParamsResponese(msg, null, 200, false);
        return responeseParamWrong;
    }

    public static ResultVO myres(ResStatus resStatus, Object data, int code, boolean success) {
        ResultVO responeseSelf = setResponese(resStatus, data, code, success);
        return responeseSelf;
    }

    public static ResultVO setResponese(ResStatus resStatus, Object data, int code, boolean success) {
        String msg = StateType.getMsg(resStatus);
        System.out.println(msg);
        ResultVO resultVO = new ResultVO(msg, code, data, success);
        return resultVO;
    }
    public static ResultVO setResponese(Object data, int code, String msg, boolean success) {
        ResultVO resultVO = new ResultVO(msg, code, data, success);
        return resultVO;
    }
    public static ResultVO setParamsResponese(String msg, Object data, int code, boolean success) {
        ResultVO resultVO = new ResultVO(msg, code, data, success);
        return resultVO;
    }
}

enum StateType {
    SUCCESS(ResStatus.SUCCESS, "执行成功"),
    NOAUTH(ResStatus.NOAUTH, "没有授权"),
    ERROR(ResStatus.ERROR, "执行失败"),
    PARAMWRONG(ResStatus.PARAMWRONG, "参数校验失败");
    private ResStatus code;
    private String msg;

    StateType(ResStatus code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResStatus getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void SetStatus(ResStatus Code, String msg){
        this.code = Code;
        this.msg = msg;
    }
    public static String getMsg(ResStatus code) {
        for (StateType c: StateType.values()) {
            if (c.getCode() == code) {
                return c.getMsg();
            }
        }
        return "执行成功";
    }
}