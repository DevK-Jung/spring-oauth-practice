package com.kjung.springoauth.core.security.oAuth.parser;

import java.util.Map;

public interface OAuthAttributesParser {
    boolean supports(String registrationId);

    Map<String, Object> parse(Map<String, Object> attributes);
}
