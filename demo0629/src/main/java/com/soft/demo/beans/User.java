package com.soft.demo.beans;

import com.fasterxml.jackson.annotation.JsonFormat;

public class User {

  private long id;
  private String account;
  private String password;
  private String nickname;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
  private java.sql.Timestamp registrationTime;
  private String status;
  private String avatarName;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }


  public java.sql.Timestamp getRegistrationTime() {
    return registrationTime;
  }

  public void setRegistrationTime(java.sql.Timestamp registrationTime) {
    this.registrationTime = registrationTime;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public String getAvatarName() {
    return avatarName;
  }

  public void setAvatarName(String avatarName) {
    this.avatarName = avatarName;
  }

}
