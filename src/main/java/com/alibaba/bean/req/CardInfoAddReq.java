package com.alibaba.bean.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CardInfoAddReq {
    @ApiModelProperty("登录用户ID")
    private Long userId;//登录ID
    @ApiModelProperty("序号")
    private String cardNoSerial;//序号
    @ApiModelProperty("前缀")
    private String cardNoPrefix;//前缀
    @ApiModelProperty("序号长度")
    private int serialLength;//序号长度
    @ApiModelProperty("生成条数")
    private int total;//生成条数
    @ApiModelProperty("密码生成规则    0：字母 1：数字（字母数字都传表示数字与字母组合）")
    private String[] pwdLetterNumber;//密码生成规则    0：字母 1：数字（字母数字都传表示数字与字母组合）
    @ApiModelProperty("密码长度")
    private int pwdLength;//密码长度
    @ApiModelProperty("卡号生成规则   0：字母 1：数字（字母数字都传表示数字与字母组合）")
    private String[] letterNumber;//卡号生成规则   0：字母 1：数字（字母数字都传表示数字与字母组合）
    @ApiModelProperty("卡号随机数长度")
    private int letterNumberLength;//卡号随机数长度
//    private String startingTime;//开始时间
//    private String endTime;//结束时间
    @ApiModelProperty("卡面金额")
    private Double cardAmount;//卡面金额
    @ApiModelProperty("增送金额")
    private Double additionalAmount;//增送金额
    @ApiModelProperty("有效时间开始")
    private String start_effectiove_time;
    @ApiModelProperty("有效时间结束")
    private String end_effectiove_time;
}
