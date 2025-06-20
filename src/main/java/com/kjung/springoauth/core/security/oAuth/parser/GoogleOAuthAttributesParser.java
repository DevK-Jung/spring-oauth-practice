package com.kjung.springoauth.core.security.oAuth.parser;

import com.kjung.springoauth.core.security.oAuth.constants.OAuthRegisterType;
import com.kjung.springoauth.core.security.oAuth.vo.OAuthUser;
import com.kjung.springoauth.core.util.StringUtilsEx;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GoogleOAuthAttributesParser implements OAuthAttributesParser {

    @Override
    public boolean supports(String registrationId) {
        return OAuthRegisterType.GOOGLE.getRegisterId().equals(registrationId);
    }

    @Override
    public OAuthUser parse(Map<String, Object> attributes) {

        return OAuthUser.builder()
                .registerId(OAuthRegisterType.GOOGLE.getRegisterId())
                .id(StringUtilsEx.toStringOrNull(attributes.get("sub")))
                .picture(StringUtilsEx.toStringOrNull(attributes.get("picture")))
                .name(StringUtilsEx.toStringOrNull(attributes.get("name")))
                .email(StringUtilsEx.toStringOrNull(attributes.get("email")))
                .attributes(attributes)
                .build();
    }
}
