package com.alibaba.bean.req;


import com.alibaba.utils.ExcelImport;
import lombok.Data;

import java.util.Date;


@Data
public class PhoneAcctExport {
    private int rowNum;//excel行号
    @ExcelImport(value = "手机号" ,maxLength = 11,required = true,unique = true)
    private String cardno;//手机号
    @ExcelImport("卡面金额")
    private String cardAmount;//卡面金额
    @ExcelImport("增送金额")
    private String additionalAmount;//增送金额
    @ExcelImport(value = "有效时间开始")
    private String start_effectiove_time;
    @ExcelImport(value = "有效时间结束")
    private String end_effectiove_time;
    private String rowTips;//错误提示
    private String rowData;//原始数据
    @ExcelImport("备注")
    private String remark;

}
