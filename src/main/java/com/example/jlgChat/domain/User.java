package com.example.jlgChat.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long uid;
    private String createTime;
    @Size(max = 20, message = "用户名不能超过20位")
    @NotNull(message = "用户名不能为空")
    private String nickName;
    @NotNull(message = "密码不能为空")
    @Size(max = 20, message = "密码不能超过20位")
    @Size(min = 8,message = "密码不能少于8位")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{0,100}", message = "格式不正确")
    private String password;
    private String avatar;
    private String signature;
    @NotNull(message = "手机号不能为空")
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    private String phoneNumber;
    private String email;
}
