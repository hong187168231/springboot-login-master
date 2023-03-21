package com.alibaba.bean.req;

import com.alibaba.bean.entity.CardInfo;

public class CardInfoReq extends CardInfo{

    private int serialLength;//序号长度
    private int total;//生成条数
    private String[] pwdLetterNumber;//密码生成规则    0：字母 1：数字（字母数字都传表示数字与字母组合）
    private int pwdLength;//密码长度
    private String[] letterNumber;//卡号生成规则   0：字母 1：数字（字母数字都传表示数字与字母组合）
    private int letterNumberLength;//卡号随机数长度
    private String startingTime;//开始时间
    private String endTime;//结束时间

    public int getSerialLength() {
        return serialLength;
    }

    public void setSerialLength(int serialLength) {
        this.serialLength = serialLength;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPwdLength() {
        return pwdLength;
    }

    public void setPwdLength(int pwdLength) {
        this.pwdLength = pwdLength;
    }

    public int getLetterNumberLength() {
        return letterNumberLength;
    }

    public void setLetterNumberLength(int letterNumberLength) {
        this.letterNumberLength = letterNumberLength;
    }

    public String[] getPwdLetterNumber() {
        return pwdLetterNumber;
    }

    public void setPwdLetterNumber(String[] pwdLetterNumber) {
        this.pwdLetterNumber = pwdLetterNumber;
    }

    public String[] getLetterNumber() {
        return letterNumber;
    }

    public void setLetterNumber(String[] letterNumber) {
        this.letterNumber = letterNumber;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
