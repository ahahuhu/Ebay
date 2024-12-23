package com.soft.demo.beans;

import java.sql.Timestamp;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

public class Requires {
    long seekerId;
    long userId;
    String name;
    String description;
    double price;
    String address;
    Timestamp releaseTime;
    String nickname;
    String pic;
    long isOpen;
    String GoodTime;
    private long category;

    public Requires(User u,Seeker s) throws UnknownHostException {
        this.seekerId = s.getSeekerId();
        this.userId = s.getUserId();
        this.name = s.getName();
        this.description = s.getDescription();
        this.price = s.getPrice();
        this.address = s.getAddress();
        this.releaseTime = s.getReleaseTime();
        this.nickname = u.getNickname();
        this.pic = u.getAvatarName();
        this.isOpen = s.getIsOpen();
        this.category = s.getCategory();
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    public void setIsOpen(long isOpen) {
        this.isOpen = isOpen;
    }

    public String getGoodTime() {
        return GoodTime;
    }

    public void setGoodTime(String goodTime) {
        GoodTime = goodTime;
    }

    public long getSeekerId() {
        return seekerId;
    }

    public void setSeekerId(long seekerId) {
        this.seekerId = seekerId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Timestamp releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public long getIsOpen() {
        return isOpen;
    }

    public Requires() {
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }
}

