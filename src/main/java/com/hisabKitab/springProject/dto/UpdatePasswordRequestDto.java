package com.hisabKitab.springProject.dto;

public class UpdatePasswordRequestDto {
    private String email;
    private String passWord;

    public UpdatePasswordRequestDto() {
    }

    public UpdatePasswordRequestDto(String email,String passWord) {
        this.email = email;
        this.passWord = passWord;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    @Override
    public String toString() {
        return "UpdatePasswordRequestDto{" +
                "passWord='" + passWord + '\'' +
                '}';
    }


}
