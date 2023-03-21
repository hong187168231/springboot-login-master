package com.alibaba.bean.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemberInfoJHKReq{
    @ApiModelProperty("IP地址")
    private String ipAddress;//IP地址
    @ApiModelProperty("激活账号")
    private String activationAcct;//激活账号
    @ApiModelProperty("设备信息")
    private String deviceInfo;//设备信息
    @ApiModelProperty("卡号=前缀+序号")
    private String cardNo;//卡号=前缀+序号
    @ApiModelProperty("卡密码")
    private String cardPwd;
    @ApiModelProperty("手机号")
    private String phoneno;//手机号

}
