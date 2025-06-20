package com.kjung.springoauth.app.account.view;

import com.kjung.springoauth.app.account.dto.SignUpReqDto;
import com.kjung.springoauth.app.account.service.AccountService;
import com.kjung.springoauth.core.security.oAuth.vo.OAuthUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.kjung.springoauth.core.constants.SessionKey.TMP_SESSION;

@Controller
@RequiredArgsConstructor
@Transactional
@RequestMapping("/signup")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public String signupForm(HttpSession session, Model model) {
        OAuthUser oauthUser = (OAuthUser) session.getAttribute(TMP_SESSION.name());

        if (oauthUser == null) return "redirect:/login";

        model.addAttribute("userInfo", oauthUser);

        return "signup-form";
    }

    @PostMapping
    public String completeSignup(@ModelAttribute SignUpReqDto param,
                                 HttpSession session) {

        OAuthUser oauthUser = (OAuthUser) session.getAttribute(TMP_SESSION.name());

        if (oauthUser == null) return "redirect:/login";

        accountService.signUp(param, oauthUser, session);

        // 로그인 처리 후 홈으로 이동
        return "redirect:/";

    }
}