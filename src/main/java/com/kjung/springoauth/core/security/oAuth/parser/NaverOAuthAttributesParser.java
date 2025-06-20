package com.kjung.springoauth.core.security.oAuth.parser;

import com.kjung.springoauth.core.security.oAuth.constants.OAuthRegisterType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NaverOAuthAttributesParser implements OAuthAttributesParser {
    @Override
    public boolean supports(String registrationId) {
        return OAuthRegisterType.NAVER.getRegisterId().equals(registrationId);
    }

    @Override
    public Map<String, Object> parse(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return Map.of(
                "email", response.get("email"),
                "picture", response.get("profile_image"),
                "id", response.get("id"),
                "name", response.get("name")
        );
    }
}
