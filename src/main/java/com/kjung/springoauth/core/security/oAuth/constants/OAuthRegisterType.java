package com.kjung.springoauth.core.security.oAuth.constants;

import lombok.Getter;

@Getter
public enum OAuthRegisterType {
    GOOGLE("google"),
    KAKAO("kakao"),
    NAVER("naver");

    private final String registerId;

    OAuthRegisterType(String registerId) {
        this.registerId = registerId;
    }
}
