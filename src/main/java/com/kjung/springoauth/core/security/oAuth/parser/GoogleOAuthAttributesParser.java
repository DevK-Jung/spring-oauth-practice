package com.kjung.springoauth.core.security.oAuth.parser;

import com.kjung.springoauth.core.security.oAuth.constants.OAuthRegisterType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GoogleOAuthAttributesParser implements OAuthAttributesParser {
    @Override
    public boolean supports(String registrationId) {
        return OAuthRegisterType.GOOGLE.getRegisterId().equals(registrationId);
    }

    @Override
    public Map<String, Object> parse(Map<String, Object> attributes) {
        return attributes;
    }
}
