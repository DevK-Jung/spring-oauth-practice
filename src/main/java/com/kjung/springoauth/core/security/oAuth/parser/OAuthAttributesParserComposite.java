package com.kjung.springoauth.core.security.oAuth.parser;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthAttributesParserComposite {

    private final List<OAuthAttributesParser> parsers;

    public Map<String, Object> parse(String registrationId, Map<String, Object> attributes) {
        return parsers.stream()
                .filter(p -> p.supports(registrationId))
                .findFirst()
                .map(p -> p.parse(attributes))
                .orElse(attributes); // 기본은 Google
    }
}
