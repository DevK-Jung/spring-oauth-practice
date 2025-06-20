package com.kjung.springoauth.app.user.entity;

import com.kjung.springoauth.app.account.dto.SignUpReqDto;
import com.kjung.springoauth.app.role.entity.RoleEntity;
import com.kjung.springoauth.core.security.oAuth.vo.OAuthUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Comment("사용자 정보")
@Table(name = "tb_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_provider_providerId", columnNames = {"provider", "provider_id"})
        }
)
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    @Comment("일련번호")
    private Long seq;

    @Column(length = 50, nullable = false)
    @Comment("사용자 명")
    private String username;

    @Column(length = 50, nullable = false)
    @Comment("닉네임")
    private String nickname;

    @Column(length = 100, unique = true)
    @Comment("이메일")
    private String email;

    @Column
    @Comment("나이")
    private Integer age;

    @Column(length = 20, nullable = false)
    @Comment("OAuth 제공자 (google, naver, kakao)")
    private String provider;

    @Column(length = 100, nullable = false)
    @Comment("OAuth 제공자의 고유 식별자 (sub, id 등)")
    private String providerId;

    @Comment("사용자 권한")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_seq", foreignKey = @ForeignKey(name = "fk_user_role"))
    private RoleEntity role;

    public static UserEntity create(OAuthUser oauthUser,
                                    SignUpReqDto signUpDto,
                                    RoleEntity role) {
        return UserEntity.builder()
                .username(oauthUser.getName())
                .email(oauthUser.getEmail())
                .nickname(signUpDto.getNickname())
                .age(signUpDto.getAge())
                .provider(oauthUser.getRegisterId())
                .providerId(oauthUser.getId())
                .role(role)
                .build();
    }

}
