package com.alibaba.service;

import org.springframework.beans.factory.annotation.Autowired;

import java.text.NumberFormat;
import java.util.*;

public class TimeCard {
    private static void getCardNo(String str,int strLength,int number){
        for (int i=1 ; i<=number; i++){
            NumberFormat format = NumberFormat.getNumberInstance();
            format.setMinimumIntegerDigits(strLength);
            format.setGroupingUsed(false);
            String numberStr = format.format(i);
            System.out.println(str+numberStr);
        }
    }
    private  static void getCardPassword(int number){
        Random rand = new Random();
        for(int i=0;i<10;i++) {
            int rInt = rand.nextInt(number);
            System.out.println(rInt);
        }
    }
    private static String getFixLenthString(int strLength) {
        Random rm = new Random();

// 获得随机数

        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);

// 将获得的获得随机数转化为字符串

        String fixLenthString = String.valueOf(pross);

// 返回固定的长度的随机数  ,如果随机数前面有“.”，把2调大。

        return fixLenthString.substring(2, strLength + 1);

    }

    private static String getUUID(){
        String s = UUID.randomUUID().toString();

//去掉“-”符号

        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24);

    }

    /**

     * 卡号N位随机数

     * 卡密用uuid,不够再加几位随机数

     * 存入数据库

     * @param args

     */
    @Autowired
    static ICardInfoService iCardInfoService;
    public static void main(String[] args) {
// TODO Auto-generated method stub

//        int num = 10;//点卡数目
//
//        ITimeCard tc = new TimeCardDAO();
//
//        for(int i = 0;i
//
//        tc.addTimeCard(getFixLenthString(18), getUUID());//给数据库添加记录
//
//        try {
//            Thread.sleep(5);
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//
//        }
        Set r = new LinkedHashSet(10);
        Random random = new Random();
        while (r.size()<10){
            int i = random.nextInt(10);
            r.add(i);
        }
        System.out.println(Arrays.toString(r.toArray()));
//
//        System.out.println(getUUID());

//        getCardNo(8,5);
//        getCardPassword(10);
    }

}

