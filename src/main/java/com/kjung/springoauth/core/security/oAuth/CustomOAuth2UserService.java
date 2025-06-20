package com.kjung.springoauth.core.security.oAuth;

import com.kjung.springoauth.app.user.entity.UserEntity;
import com.kjung.springoauth.app.user.repository.UserRepository;
import com.kjung.springoauth.core.security.oAuth.parser.OAuthAttributesParserComposite;
import com.kjung.springoauth.core.security.oAuth.vo.CustomOAuth2User;
import com.kjung.springoauth.core.security.oAuth.vo.OAuthUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuthAttributesParserComposite attributesParser;

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google/naver/kakao
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // provider별 사용자 정보 파싱 로직 구현 필요
        OAuthUser oAuthUser = attributesParser.parse(registrationId, attributes);

        Optional<UserEntity> user
                = userRepository.findByProviderAndProviderId(oAuthUser.getRegisterId(), oAuthUser.getId());

        if (user.isPresent()) {

            return CustomOAuth2User.create(
                    oAuthUser,
                    attributes,
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
            );
        } else {
            // 아직 회원가입되지 않은 사용자 → 세션에 정보 저장 후 redirect
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest().getSession();

            session.setAttribute("oauthUser", oAuthUser);
            throw new OAuth2AuthenticationException(new OAuth2Error("need_register"), "추가 정보 필요");
        }
    }
}
