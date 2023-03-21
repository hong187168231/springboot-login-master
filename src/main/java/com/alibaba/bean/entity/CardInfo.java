package com.alibaba.bean.entity;

import com.alibaba.utils.DateUtils;
import lombok.Data;

/**
 * 卡信息
 */
@Data
public class CardInfo {
    private Long id;
    private Long userId;//登录ID
    private String cardNoPrefix;//前缀
    private String cardNoSerial;//序号
    private String cardNo;//卡号=前缀+序号
    private String cardPwd;
    private Double cardAmount;//卡面金额
    private Double additionalAmount;//增送金额
    private String isActivation;//0否   1激活
    private String isExp;//0否   1导出
    private String isHandle;//0否   1处理
    private String activationAcct;//激活账号
    private String is_delete;//0否   1删除
    private String create_time;
    private String update_time;
    private String start_effectiove_time;
    private String end_effectiove_time;

}
