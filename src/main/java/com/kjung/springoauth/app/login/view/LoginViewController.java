package com.kjung.springoauth.app.login.view;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginViewController {
    @GetMapping("/login")
    public String loginPage(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated())
            return "redirect:/"; // 이미 로그인 상태 → 홈으로 리디렉션

        return "login";
    }
}
