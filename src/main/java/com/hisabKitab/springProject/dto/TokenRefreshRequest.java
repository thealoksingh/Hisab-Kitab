package com.hisabKitab.springProject.dto;

public class TokenRefreshRequest {
    
    private String refreshToken;
  
    public String getRefreshToken() {
      return refreshToken;
    }
  
    public void setRefreshToken(String refreshToken) {
      this.refreshToken = refreshToken;
    }
  }
