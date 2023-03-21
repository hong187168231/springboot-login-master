package com.alibaba.bean.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("创建手机号请求参数")
public class PhoneAcctHandleReq {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("手机号")
    private String cardno;//手机号
    @ApiModelProperty("IP地址")
    private String ipAddress;//IP地址
    @ApiModelProperty("0否   1处理  2拒绝 3 激活")
    private String isHandle;//0否   1处理  2拒绝
    @ApiModelProperty("备注")
    private String remark;//备注


}
