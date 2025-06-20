package com.kjung.springoauth.core.security.oAuth.parser;

import com.kjung.springoauth.core.security.oAuth.vo.OAuthUser;

import java.util.Map;

public interface OAuthAttributesParser {
    boolean supports(String registrationId);

    OAuthUser parse(Map<String, Object> attributes);
}
