package com.kjung.springoauth.app.user.repository;

import com.kjung.springoauth.app.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByProviderAndProviderId(String provider, String providerId);
}
