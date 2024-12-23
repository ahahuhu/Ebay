package com.soft.demo.beans;


public class Message {

  private long id;
  private long senderId;
  private long receiverId;
  private String content;
  private java.sql.Timestamp timestamp;
  private long visited;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getSenderId() {
    return senderId;
  }

  public void setSenderId(long senderId) {
    this.senderId = senderId;
  }


  public long getReceiverId() {
    return receiverId;
  }

  public void setReceiverId(long receiverId) {
    this.receiverId = receiverId;
  }


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public java.sql.Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(java.sql.Timestamp timestamp) {
    this.timestamp = timestamp;
  }


  public long getVisited() {
    return visited;
  }

  public void setVisited(long visited) {
    this.visited = visited;
  }

}
