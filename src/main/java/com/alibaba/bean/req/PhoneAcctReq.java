package com.alibaba.bean.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("创建手机号请求参数")
public class PhoneAcctReq {
    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("激活账号")
    private String activationAcct;//激活账号
    @ApiModelProperty("手机号")
    private String cardno;//手机号
    @ApiModelProperty("IP地址")
    private String ipAddress;//IP地址
    @ApiModelProperty("设备信息")
    private String deviceInfo;//设备信息
    @ApiModelProperty("是否删除 0否   1删除")
    private String is_delete;//0否   1删除
    @ApiModelProperty("卡面金额")
    private Double cardAmount;//卡面金额
    @ApiModelProperty("增送金额")
    private Double additionalAmount;//增送金额
    @ApiModelProperty("0否   1处理  2拒绝")
    private String isHandle;//0否   1处理  2拒绝
//    @ApiModelProperty("创建时间")
//    private String create_time;
//    @ApiModelProperty("修改时间")
//    private String update_time;
    @ApiModelProperty("处理人ID")
    private Long userid;//处理人ID
    @ApiModelProperty("处理人")
    private String username;//处理人
    @ApiModelProperty("备注")
    private String remark;//备注
    @ApiModelProperty("有效时间开始")
    private String start_effectiove_time;
    @ApiModelProperty("有效时间结束")
    private String end_effectiove_time;

    @ApiModelProperty("创建时间")
    private String start_create_time;

    @ApiModelProperty("创建时间")
    private String end_create_time;

}
