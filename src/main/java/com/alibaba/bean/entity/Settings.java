package com.alibaba.bean.entity;

public class Settings {

    private int id;
    private String swhLitIpActi;//限制同一个ip激活开关 0开 1关闭
    private int limitIpActi;//限制同一个ip激活数量

    //终身限制
    private String swhLitAcctActi;//限制同一个id账号激活开关 0开 1关闭
    private int litAcctActi;//限制同一个id账号只能激活数量

    //每天限制
    private String swhLitAcctActiDay;//限制同一个id账号激活开关 0开 1关闭
    private int litAcctActiDay;//限制同一个id账号只能激活数量

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSwhLitIpActi() {
        return swhLitIpActi;
    }

    public void setSwhLitIpActi(String swhLitIpActi) {
        this.swhLitIpActi = swhLitIpActi;
    }

    public String getSwhLitAcctActi() {
        return swhLitAcctActi;
    }

    public void setSwhLitAcctActi(String swhLitAcctActi) {
        this.swhLitAcctActi = swhLitAcctActi;
    }

    public int getLimitIpActi() {
        return limitIpActi;
    }

    public void setLimitIpActi(int limitIpActi) {
        this.limitIpActi = limitIpActi;
    }

    public int getLitAcctActi() {
        return litAcctActi;
    }

    public void setLitAcctActi(int litAcctActi) {
        this.litAcctActi = litAcctActi;
    }

    public String getSwhLitAcctActiDay() {
        return swhLitAcctActiDay;
    }

    public void setSwhLitAcctActiDay(String swhLitAcctActiDay) {
        this.swhLitAcctActiDay = swhLitAcctActiDay;
    }

    public int getLitAcctActiDay() {
        return litAcctActiDay;
    }

    public void setLitAcctActiDay(int litAcctActiDay) {
        this.litAcctActiDay = litAcctActiDay;
    }
}
