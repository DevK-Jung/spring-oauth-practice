package com.kjung.springoauth.core.security.oAuth.parser;

import com.kjung.springoauth.core.security.oAuth.constants.OAuthRegisterType;
import com.kjung.springoauth.core.security.oAuth.vo.OAuthUser;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.kjung.springoauth.core.util.StringUtilsEx.toStringOrNull;

@Component
public class KakaoOAuthAttributesParser implements OAuthAttributesParser {
    @Override
    public boolean supports(String registrationId) {
        return OAuthRegisterType.KAKAO.getRegisterId().equals(registrationId);
    }

    @Override
    public OAuthUser parse(Map<String, Object> attributes) {
        Object kakaoAccountObj = attributes.get("kakao_account");

        if (!(kakaoAccountObj instanceof Map<?, ?> kakaoAccountMap))
            throw new RuntimeException("Invalid kakao_account format");

        Object profileObj = kakaoAccountMap.get("profile");

        if (!(profileObj instanceof Map<?, ?> profileMap))
            throw new RuntimeException("Invalid profile format");

        return OAuthUser.builder()
                .registerId(OAuthRegisterType.KAKAO.getRegisterId())
                .id(toStringOrNull(attributes.get("id")))
                .picture(toStringOrNull(profileMap.get("profile_image_url")))
                .name(toStringOrNull(profileMap.get("nickname")))
                .attributes(attributes)
                .build();
    }

}
