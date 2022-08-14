package com.informe.informeapisb.src.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int userIdx;
    private String name;
    private String nickname;
    private String birth;
    private String phone;
    private String email;
    private String password;
    private String imgUrl;
    private String status;
}
