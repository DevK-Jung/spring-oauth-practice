package com.kjung.springoauth.app.login.view;

import com.kjung.springoauth.core.security.oAuth.vo.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.kjung.springoauth.core.constants.SessionKey.TMP_SESSION;

@Controller
public class LoginViewController {
    @GetMapping("/login")
    public String loginPage(@AuthenticationPrincipal CustomOAuth2User user,
                            HttpServletRequest request) {
        // 1. 이미 인증된 사용자라면 홈으로

        if (user != null)
            return "redirect:/";

        HttpSession session = request.getSession(false);

        // 2. 임시 OAuth 사용자 세션이 있으면 → 회원가입 페이지로
        if (session != null && session.getAttribute(TMP_SESSION.name()) != null)
            return "redirect:/signup";

        // 3. 일반 로그인 화면
        return "login";
    }
}
