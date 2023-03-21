package com.alibaba.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PhoneAcctVo {
    @ApiModelProperty("手机号")
    private String cardno;//手机号
    @ApiModelProperty("总记录")
    private int countnumber;
    @ApiModelProperty("创建时间")
    private String create_time;

}
