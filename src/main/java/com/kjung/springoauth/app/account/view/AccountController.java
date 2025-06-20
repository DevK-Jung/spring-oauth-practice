package com.kjung.springoauth.app.account.view;

import com.kjung.springoauth.app.account.dto.SignUpReqDto;
import com.kjung.springoauth.app.role.entity.RoleEntity;
import com.kjung.springoauth.app.role.repository.RoleRepository;
import com.kjung.springoauth.app.user.entity.UserEntity;
import com.kjung.springoauth.app.user.repository.UserRepository;
import com.kjung.springoauth.core.security.oAuth.vo.CustomOAuth2User;
import com.kjung.springoauth.core.security.oAuth.vo.OAuthUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;

import static com.kjung.springoauth.core.constants.SessionKey.TMP_SESSION;

@Controller
@RequiredArgsConstructor
@Transactional
@RequestMapping("/signup")
public class AccountController {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public static final String DEFAULT_AUTH_CODE = "ROLE_USER";

    @GetMapping
    public String signupForm(HttpSession session, Model model) {
        OAuthUser oauthUser = (OAuthUser) session.getAttribute("oauthUser");
        if (oauthUser == null) return "redirect:/login";

        model.addAttribute("user", oauthUser);
        return "signup-form";
    }

    @PostMapping
    public String completeSignup(@ModelAttribute SignUpReqDto param,
                                 HttpSession session) {
        OAuthUser oauthUser = (OAuthUser) session.getAttribute(TMP_SESSION.name());

        if (oauthUser == null) return "redirect:/login";

        RoleEntity defaultAuth = roleRepository.findByRoleCode(DEFAULT_AUTH_CODE)
                .orElseThrow();

        // 회원가입 처리
        userRepository.save(UserEntity.create(oauthUser, param, defaultAuth));

        session.removeAttribute(TMP_SESSION.name());

        CustomOAuth2User authenticatedUser = CustomOAuth2User.create(oauthUser, null, Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));

        // OAuth2AuthenticationToken 사용
        OAuth2AuthenticationToken authToken = new OAuth2AuthenticationToken(
                authenticatedUser,
                authenticatedUser.getAuthorities(),
                oauthUser.getRegisterId() // registrationId ("google", "naver" 등)
        );

        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 로그인 처리 후 홈으로 이동
        return "redirect:/";

    }
}