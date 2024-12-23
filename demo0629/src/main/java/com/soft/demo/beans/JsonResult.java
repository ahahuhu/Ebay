package com.soft.demo.beans;

/*
* 统一的数据结构
* */

import io.swagger.annotations.Api;

public class JsonResult<T> {
    private int code;
    private T result;
    private String msg;

    //只返回数据，执行成功，
    public JsonResult(T result) {
        this.result = result;
        this.code = 200;
        this.msg = "成功";
    }

    public JsonResult() {
        this.code = 200;
        this.msg =  "成功";
    }

    //只返回代码，默认失败
    public JsonResult(int code) {
        this.code = code;
        this.msg = "失败";
    }

    public JsonResult(int code, String msg) {
        this.code = code;
        this.msg =  msg;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public void setData(T data){
        this.result = data;
    }

    public T getData(){
        return this.result;
    }
}
