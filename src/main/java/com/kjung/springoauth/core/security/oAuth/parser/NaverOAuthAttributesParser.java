package com.kjung.springoauth.core.security.oAuth.parser;

import com.kjung.springoauth.core.security.oAuth.constants.OAuthRegisterType;
import com.kjung.springoauth.core.security.oAuth.vo.OAuthUser;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.kjung.springoauth.core.util.StringUtilsEx.toStringOrNull;

@Component
public class NaverOAuthAttributesParser implements OAuthAttributesParser {
    @Override
    public boolean supports(String registrationId) {
        return OAuthRegisterType.NAVER.getRegisterId().equals(registrationId);
    }

    @Override
    public OAuthUser parse(Map<String, Object> attributes) {
        Object response = attributes.get("response");

        if (!(response instanceof Map<?, ?> responseMap))
            throw new RuntimeException("Invalid naver_response format");

        return OAuthUser.builder()
                .registerId(OAuthRegisterType.NAVER.getRegisterId())
                .id(toStringOrNull(responseMap.get("id")))
                .picture(toStringOrNull(responseMap.get("profile_image")))
                .name(toStringOrNull(responseMap.get("name")))
                .email(toStringOrNull(responseMap.get("email")))
                .attributes(attributes)
                .build();
    }
}
