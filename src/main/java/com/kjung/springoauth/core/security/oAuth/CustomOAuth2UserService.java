package com.kjung.springoauth.core.security.oAuth;

import com.kjung.springoauth.core.security.oAuth.parser.OAuthAttributesParserComposite;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuthAttributesParserComposite attributesParser;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google/naver/kakao
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // provider별 사용자 정보 파싱 로직 구현 필요
        Map<String, Object> parsedAttributes = attributesParser.parse(registrationId, attributes);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                parsedAttributes,
                "name" // 사용자명 필드명
        );
    }
}
