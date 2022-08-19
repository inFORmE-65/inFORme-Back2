package com.informe.informeapisb.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserReq {
    private String name;
    private String nickname;
    private String birth;
    private String phone;
    private String email;
    private String password;
    private String imgUrl;
    private String status;
}
