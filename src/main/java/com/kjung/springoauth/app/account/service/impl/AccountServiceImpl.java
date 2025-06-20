package com.kjung.springoauth.app.account.service.impl;

import com.kjung.springoauth.app.account.dto.SignUpReqDto;
import com.kjung.springoauth.app.account.service.AccountService;
import com.kjung.springoauth.app.role.entity.RoleEntity;
import com.kjung.springoauth.app.role.repository.RoleRepository;
import com.kjung.springoauth.app.user.entity.UserEntity;
import com.kjung.springoauth.app.user.repository.UserRepository;
import com.kjung.springoauth.core.security.oAuth.vo.CustomOAuth2User;
import com.kjung.springoauth.core.security.oAuth.vo.OAuthUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static com.kjung.springoauth.core.constants.SessionKey.TMP_SESSION;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    public static final String DEFAULT_AUTH_CODE = "ROLE_USER";

    @Override
    public void signUp(SignUpReqDto param,
                       OAuthUser oauthUser,
                       HttpSession session) {

        // 사용자 저장
        UserEntity savedUser = saveNewUser(param, oauthUser);

        // 임시 세션 제거
        session.removeAttribute(TMP_SESSION.name());

        // 로그인 처리
        authenticateUser(savedUser, oauthUser, session);
    }

    private void authenticateUser(UserEntity savedUser,
                                  OAuthUser oauthUser,
                                  HttpSession session) {

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(savedUser.getRole().getRoleCode());

        CustomOAuth2User authenticatedUser = CustomOAuth2User.create(
                oauthUser,
                oauthUser.getAttributes(),
                Collections.singleton(authority)
        );

        OAuth2AuthenticationToken authToken = new OAuth2AuthenticationToken(
                authenticatedUser,
                authenticatedUser.getAuthorities(),
                oauthUser.getRegisterId() // registrationId ("google", "naver" 등)
        );

        // 인증 객체를 SecurityContext에 저장
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);

        // 세션 저장
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

    }

    private UserEntity saveNewUser(SignUpReqDto param, OAuthUser oauthUser) {
        RoleEntity defaultAuth = getDefaultAuth();

        // 사용자 저장
        return userRepository.save(UserEntity.create(oauthUser, param, defaultAuth));
    }

    private RoleEntity getDefaultAuth() {
        return roleRepository.findByRoleCode(DEFAULT_AUTH_CODE)
                .orElseThrow();
    }
}
