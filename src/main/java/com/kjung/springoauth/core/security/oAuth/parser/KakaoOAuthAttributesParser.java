package com.kjung.springoauth.core.security.oAuth.parser;

import com.kjung.springoauth.core.security.oAuth.constants.OAuthRegisterType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class KakaoOAuthAttributesParser implements OAuthAttributesParser {
    @Override
    public boolean supports(String registrationId) {
        return OAuthRegisterType.KAKAO.getRegisterId().equals(registrationId);
    }

    @Override
    public Map<String, Object> parse(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        Map<String, Object> result = new HashMap<>();
        result.put("id", attributes.get("id"));
        result.put("picture", profile.get("profile_image_url"));
        result.put("name", profile.get("nickname"));
        return result;
    }
}
