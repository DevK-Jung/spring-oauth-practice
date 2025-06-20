package com.kjung.springoauth.app.account.service;

import com.kjung.springoauth.app.account.dto.SignUpReqDto;
import com.kjung.springoauth.core.security.oAuth.vo.OAuthUser;
import jakarta.servlet.http.HttpSession;

public interface AccountService {
    void signUp(SignUpReqDto param,
                OAuthUser oauthUser,
                HttpSession session);
}
