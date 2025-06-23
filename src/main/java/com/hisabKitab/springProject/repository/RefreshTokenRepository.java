package com.hisabKitab.springProject.repository;

import com.hisabKitab.springProject.entity.UserEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.hisabKitab.springProject.entity.RefreshToken;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    RefreshToken findByUser_UserId(Long userId);
    @Modifying
    int deleteByUser(UserEntity user);
}