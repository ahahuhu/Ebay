package com.soft.demo.beans;

public class Book {

  private long id;
  private long userid;
  private String bookname;
  private String bookimage;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getUserid() {
    return userid;
  }

  public void setUserid(long userid) {
    this.userid = userid;
  }


  public String getBookname() {
    return bookname;
  }

  public void setBookname(String bookname) {
    this.bookname = bookname;
  }


  public String getBookimage() {
    return bookimage;
  }

  public void setBookimage(String bookimage) {
    this.bookimage = bookimage;
  }

}
