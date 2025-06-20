package com.kjung.springoauth.core.security.oAuth;

import com.kjung.springoauth.app.user.entity.UserEntity;
import com.kjung.springoauth.app.user.repository.UserRepository;
import com.kjung.springoauth.core.error.constants.ErrorCode;
import com.kjung.springoauth.core.security.oAuth.parser.OAuthAttributesParserComposite;
import com.kjung.springoauth.core.security.oAuth.vo.CustomOAuth2User;
import com.kjung.springoauth.core.security.oAuth.vo.OAuthUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.Map;

import static com.kjung.springoauth.core.constants.SessionKey.TMP_SESSION;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuthAttributesParserComposite attributesParser;

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google/naver/kakao

        Map<String, Object> attributes = oAuth2User.getAttributes();

        // provider별 사용자 정보 파싱
        OAuthUser parsedUser = attributesParser.parse(registrationId, attributes);

        return userRepository.findByProviderAndProviderId(parsedUser.getRegisterId(), parsedUser.getId())
                .map(user -> createAuthenticatedUser(parsedUser, attributes, user))
                .orElseGet(() -> handleUnregisteredUser(parsedUser));
    }

    private OAuth2User createAuthenticatedUser(OAuthUser parsedUser,
                                               Map<String, Object> attributes,
                                               UserEntity userEntity) {
        return CustomOAuth2User.create(
                parsedUser,
                attributes,
                Collections.singleton(new SimpleGrantedAuthority(userEntity.getRole().getRoleCode()))
        );
    }

    private OAuth2User handleUnregisteredUser(OAuthUser parsedUser) {
        HttpSession session = getCurrentHttpSession();
        session.setAttribute(TMP_SESSION.name(), parsedUser);

        throw new OAuth2AuthenticationException(
                new OAuth2Error(ErrorCode.NEED_REGISTER.name()),
                "추가 정보 필요"
        );
    }

    private HttpSession getCurrentHttpSession() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest()
                .getSession();
    }
}
