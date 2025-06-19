package com.kjung.springoauth.core.security.oAuth;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google/naver/kakao
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // provider별 사용자 정보 파싱 로직 구현 필요
        Map<String, Object> parsedAttributes = parseAttributes(registrationId, attributes);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                parsedAttributes,
                "name" // 사용자명 필드명
        );
    }

    private Map<String, Object> parseAttributes(String registrationId, Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            return (Map<String, Object>) attributes.get("response");
        } else if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            Map<String, Object> result = new HashMap<>();
            result.put("email", kakaoAccount.get("email"));
            result.put("name", profile.get("nickname"));
            return result;
        }
        return attributes; // google
    }
}
