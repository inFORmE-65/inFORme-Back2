package com.informe.informeapisb.src.user;

import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.src.user.model.*;
import com.informe.informeapisb.config.BaseResponse;
import com.informe.informeapisb.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        if(postUserReq.getNickname() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
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
        if(postUserReq.getBirth() == null) {
            return new BaseResponse<>(POST_USERS_EMPTY_NICKNAME);
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
     * 회원 정보 수정 API
     * [PATCH] /users
     * @return BaseResponse<PostProfileRes>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<String> modifyProfile(@PathVariable("userIdx") int userIdx, @RequestBody PostProfileReq postProfileReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            userService.modifyProfile(userIdx, postProfileReq);
            String result = "회원 프로필을 수정했습니다.";

            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 탈퇴 API
     * [PATCH] /users/{userIdx}/status
     */
    // 회원 삭제
    @ResponseBody
    @PatchMapping("/{userIdx}/status")
    public BaseResponse<String> deleteUser(@PathVariable("userIdx") int userIdx){
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            userService.deleteUser(userIdx);

            String result = "삭제되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 정책 스크랩 API
     * [POST] /users/scrap/{userIdx}
     */
    @ResponseBody
    @PostMapping("/scrap/{userIdx}")      // (POST) localhost:8080/users/scrap/{userIdx}
    public BaseResponse<String> scrapService(@PathVariable("userIdx") int userIdx, @RequestBody PostScrapReq postScrapReq) {
        try {
            // 유효한 사용자인지 검사
            int userIdxByJwt = jwtService.getUserIdx();

            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            userService.scrapService(userIdx, postScrapReq);
            String result = "스크랩 했습니다.";

            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 정책 스크랩 취소
     * [DELETE] /users/scrap/{userIdx}
     */
    @ResponseBody
    @DeleteMapping("/scrap/delete/{userIdx}")
    public BaseResponse<String> deleteScrap(@PathVariable("userIdx") int userIdx, @RequestBody PostScrapReq postScrapReq) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            userService.deleteScrap(userIdx, postScrapReq);

            String result = "스크랩 취소했습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 정책 스크랩 목록 확인
     * [GET] /users/scrap/{userIdx}
     */
    @ResponseBody
    @GetMapping("/scrap/{userIdx}")
    public BaseResponse<List<GetScrapRes>> scrapList(@PathVariable("userIdx") int userIdx) {
        try {
            int userIdxByJwt = jwtService.getUserIdx();
            if (userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetScrapRes> getscrap = userProvider.scrapList(userIdx);

            return new BaseResponse<>(getscrap);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}

























