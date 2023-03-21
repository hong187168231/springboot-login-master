package com.alibaba.bean.entity;

import lombok.Data;

@Data
public class PhoneAcct {
    private Long id;
    private String activationAcct;//激活账号
    private String cardno;//手机号
    private String ipAddress;//IP地址
    private String deviceInfo;//设备信息
    private String is_delete;//0否   1删除
    private Double cardAmount;//卡面金额
    private Double additionalAmount;//增送金额
    private String isHandle;//0否   1处理  2拒绝
    private String create_time;
    private String update_time;
    private Long userid;//处理人ID
    private String username;//处理人
    private String remark;//备注

    private String start_effectiove_time;
    private String end_effectiove_time;

}
