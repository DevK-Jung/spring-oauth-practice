package com.kjung.springoauth.app.login.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginViewController {
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
