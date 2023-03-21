package com.alibaba.bean.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("创建手机号请求参数")
public class PhoneAcctWebReq {
    @ApiModelProperty("激活账号")
    private String activationAcct;//激活账号
    @ApiModelProperty("手机号")
    private String cardno;//手机号
    @ApiModelProperty("IP地址")
    private String ipAddress;//IP地址
    @ApiModelProperty("设备信息")
    private String deviceInfo;//设备信息

}
