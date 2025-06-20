package com.kjung.springoauth.core.security.oAuth.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final OAuthUser oAuthUserInfo;
    private final Map<String, Object> attributes;
    private final Collection<? extends GrantedAuthority> authorities;


    public static CustomOAuth2User create(OAuthUser user,
                                          Map<String, Object> attributes,
                                          Collection<? extends GrantedAuthority> authorities) {
        return new CustomOAuth2User(user, attributes, authorities);
    }

    @Override
    public String getName() {
        return this.oAuthUserInfo.getId();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

}
