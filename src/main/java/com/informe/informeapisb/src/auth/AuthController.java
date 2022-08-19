package com.informe.informeapisb.src.auth;

import com.informe.informeapisb.src.auth.model.PostLoginReq;
import com.informe.informeapisb.src.auth.model.PostLoginRes;
import com.informe.informeapisb.utils.JwtService;
import com.informe.informeapisb.src.auth.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.config.BaseResponse;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

import static com.informe.informeapisb.config.BaseResponseStatus.*;
import static com.informe.informeapisb.utils.ValidationRegex.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final AuthProvider authProvider;
    @Autowired
    private final AuthService authService;
    @Autowired
    private final JwtService jwtService;

    public AuthController(AuthProvider authProvider, AuthService authService, JwtService jwtService) {
        this.authProvider = authProvider;
        this.authService = authService;
        this.jwtService = jwtService;
    }

    /**
     * 로그인 API
     * [POST] /auth/login
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq) {
        try {
            // validation
            if(postLoginReq.getEmail() == null) {
                return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
            }
            if(postLoginReq.getPassword() == null) {
                return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
            }

            // 정규표현
            if(!isRegexEmail(postLoginReq.getEmail())) {
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
            }

            // 회원이 맞는지 확인
            if(authProvider.checkEmail(postLoginReq.getEmail()) == 0) {
                throw new BaseException(FAILED_TO_LOGIN);
            }

            PostLoginRes postLoginRes = authService.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // https://itstudy-mary.tistory.com/212
    // https://jhkang-tech.tistory.com/16
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login"; //주소 요청으로 변경
    }



    /**
     * 카카오 회원가입 API
     * [GET] /auth/kakao
     * https://kauth.kakao.com/oauth/authorize?client_id=7b1226bbe5a44b31381508a6b3ea9630&redirect_uri=http://localhost:8080/auth/kakao&response_type=code
     */
    @ResponseBody
    @RequestMapping("/kakao")
    public void kakaoUser(@RequestParam String code) {
        // 인가 코드 받기 test (원래 클라에서)
        //System.out.println(code);

        // access_Token 받기
        String access_Token = AuthService.getKakaoAccessToken(code);
        System.out.println("controller access_token : " + access_Token);

        // userInfo 받아오기
        HashMap<String, Object> userInfo = AuthService.getUserInfo(access_Token);
        System.out.println("login Controller : " + userInfo);

        /*if(userInfo.get("email") != null) {
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }*/
    }
}
