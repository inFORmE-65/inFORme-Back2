package com.informe.informeapisb.src.user;

import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.src.user.model.*;
import com.informe.informeapisb.config.BaseResponse;
import com.informe.informeapisb.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.informe.informeapisb.config.BaseResponseStatus.*;
import static com.informe.informeapisb.utils.ValidationRegex.*;

@RestController
@RequestMapping("/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;

    @Autowired
    private final UserService userService;

    @Autowired
    private final JwtService jwtService;

    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService) {
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }


     /**
        * 회원가입 API
        * [POST] /users
        * @return BaseResponse<PostUserRes>
     */
    @ResponseBody
    @PostMapping("")        // (POST) localhost:8080/users
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // validation
        if(postUserReq.getName() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_NAME);
        }
        if(postUserReq.getPhone() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PHONE);
        }
        if(postUserReq.getEmail() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        if(postUserReq.getPassword() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }

        // 정규 표현
          if(!isRegexPhone(postUserReq.getPhone())){
              return new BaseResponse<>(POST_USERS_INVALID_PHONE);
          }
          if(!isRegexEmail(postUserReq.getEmail())){
              return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
          }
          if(!isRegexPassword(postUserReq.getPassword())){
              return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
          }

        // DB 저장
        try {
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 정보 등록 API
     * [POST] /users/profile
     * @return BaseResponse<PostProfileRes>
     */

    @ResponseBody
    @PostMapping("/{userIdx}")        // (POST) localhost:8080/users/{userIdx}
    public BaseResponse<String> setProfile(@PathVariable("userIdx") int userIdx, @RequestBody PostProfileReq postProfileReq) {
        try {
            // 접근한 유저가 권한이 있는지 확인
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            // userIdx를 통해 DB저장
            userService.setProfile(userIdx, postProfileReq);
            String result = "회원 프로필을 설정했습니다";

            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}

























