package com.soft.demo.beans;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Seeker {

  private long seekerId;
  private String name;
  private String description;
  private double price;
  private long category;
  private String address;
  private long userId;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
  private java.sql.Timestamp releaseTime;
  private long isOpen;


  public long getSeekerId() {
    return seekerId;
  }

  public void setSeekerId(long seekerId) {
    this.seekerId = seekerId;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }


  public long getCategory() {
    return category;
  }

  public void setCategory(long category) {
    this.category = category;
  }


  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }


  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }


  public java.sql.Timestamp getReleaseTime() {
    return releaseTime;
  }

  public void setReleaseTime(java.sql.Timestamp releaseTime) {
    this.releaseTime = releaseTime;
  }


  public long getIsOpen() {
    return isOpen;
  }

  public void setIsOpen(long isOpen) {
    this.isOpen = isOpen;
  }

}
