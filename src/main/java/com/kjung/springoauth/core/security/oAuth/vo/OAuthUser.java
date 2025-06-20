package com.kjung.springoauth.core.security.oAuth.vo;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuthUser {
    private final String registerId;
    private final String id;
    private final String name;
    private final String email;
    private final String picture;
    private Map<String, Object> attributes;
}
