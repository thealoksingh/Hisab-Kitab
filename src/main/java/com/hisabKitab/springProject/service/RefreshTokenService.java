package com.hisabKitab.springProject.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hisabKitab.springProject.exception.TokenRefreshException;
import com.hisabKitab.springProject.entity.RefreshToken;
import com.hisabKitab.springProject.repository.RefreshTokenRepository;
import com.hisabKitab.springProject.repository.UserRepository;

@Service
public class RefreshTokenService {
  @Value("${hisab-kitab.app.jwtRefreshExpirationMonth}")
  private Long refreshTokenDurationMonth;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Autowired
  private UserRepository userRepository;

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  public RefreshToken createRefreshToken(Long userId) {
    
    RefreshToken refreshToken = refreshTokenRepository.findByUser_UserId(userId);
    System.out.println("refreshToken inside service = "+refreshToken);
    if (refreshToken != null) {
      refreshToken = verifyExpiration(refreshToken) ;
      return refreshToken;
    }
    refreshToken = new RefreshToken();
    refreshToken.setUser(userRepository.findById(userId).get());
    refreshToken.setExpiryDate(Instant.now().plus(refreshTokenDurationMonth * 30, ChronoUnit.DAYS));
    refreshToken.setToken(UUID.randomUUID().toString());

    refreshToken = refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
    }

    return token;
  }

  @Transactional
  public int deleteByUserId(Long userId) {
    return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
  }
}