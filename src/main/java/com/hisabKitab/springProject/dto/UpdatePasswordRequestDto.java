package com.hisabKitab.springProject.dto;

public class UpdatePasswordRequestDto {
    private String passWord;

    public UpdatePasswordRequestDto() {
    }

    public UpdatePasswordRequestDto(String passWord) {
        this.passWord = passWord;
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
