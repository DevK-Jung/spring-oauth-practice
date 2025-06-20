package com.kjung.springoauth.app.home.view;

import com.kjung.springoauth.core.security.oAuth.vo.CustomOAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(@AuthenticationPrincipal CustomOAuth2User user, Model model) {
        if (user != null)
            model.addAttribute("userInfo", user.getOAuthUserInfo());

        return "index";
    }
}
