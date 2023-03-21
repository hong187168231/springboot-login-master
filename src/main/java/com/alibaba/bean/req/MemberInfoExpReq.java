package com.alibaba.bean.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemberInfoExpReq {
    @ApiModelProperty("IP地址")
    private String ipAddress;//IP地址
    @ApiModelProperty("激活账号")
    private String activationAcct;//激活账号
    @ApiModelProperty("开始时间")
    private String startingTime;//开始时间
    @ApiModelProperty("结束时间")
    private String endTime;//结束时间
    @ApiModelProperty("卡号=前缀+序号")
    private String cardNo;//卡号=前缀+序号
    @ApiModelProperty("0否   1处理  2拒绝")
    private String isHandle;//0否   1处理  2拒绝
    @ApiModelProperty("前缀")
    private String cardNoPrefix;//前缀
    @ApiModelProperty("手机号")
    private String phoneno;//手机号

}
