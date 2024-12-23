package com.soft.demo.beans;


import com.fasterxml.jackson.annotation.JsonFormat;

public class Product {

  private long id;
  private String name;
  private String description;
  private double price;
  private long userId;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "GMT+8")
  private java.sql.Timestamp releaseTime;
  private String deliveryArea;
  private long category;
  private long onSale;
  private String URL;
  private String userName;

  @Override
  public String toString() {
    return "Product{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", price=" + price +
            ", userId=" + userId +
            ", releaseTime=" + releaseTime +
            ", deliveryArea='" + deliveryArea + '\'' +
            ", category=" + category +
            ", onSale=" + onSale +
            ", URL='" + URL + '\'' +
            ", userName='" + userName + '\'' +
            '}';
  }

  public String getURL() {
    return URL;
  }

  public void setURL(String URL) {
    this.URL = URL;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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


  public String getDeliveryArea() {
    return deliveryArea;
  }

  public void setDeliveryArea(String deliveryArea) {
    this.deliveryArea = deliveryArea;
  }


  public long getCategory() {
    return category;
  }

  public void setCategory(long category) {
    this.category = category;
  }


  public long getOnSale() {
    return onSale;
  }

  public void setOnSale(long onSale) {
    this.onSale = onSale;
  }

}
