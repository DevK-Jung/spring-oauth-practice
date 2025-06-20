package com.kjung.springoauth.core.security.oAuth.parser;

import com.kjung.springoauth.core.security.oAuth.vo.OAuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthAttributesParserComposite {

    private final List<OAuthAttributesParser> parsers;

    public OAuthUser parse(String registrationId, Map<String, Object> attributes) {
        return parsers.stream()
                .filter(p -> p.supports(registrationId))
                .findFirst()
                .map(p -> p.parse(attributes))
                .orElseThrow();
    }
}
