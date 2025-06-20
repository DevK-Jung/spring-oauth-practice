package com.kjung.springoauth.app.role.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Comment("권한 정보")
@Table(name = "tb_role")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_seq")
    @Comment("권한 일련번호")
    private Long seq;

    @Column(name = "role_name", nullable = false, unique = true, length = 50)
    @Comment("권한명 (예: ROLE_USER, ROLE_ADMIN)")
    private String roleCode;
}
