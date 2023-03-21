package com.alibaba.bean.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositReq {
    @JSONField(name = "Account")
    private String Account;
    @JSONField(name = "Amount")
    private BigDecimal Amount;
    @JSONField(name = "Audit")
    private AuditReq Audit;
    @JSONField(name = "IsReal")
    private Boolean IsReal;
    @JSONField(name = "Memo")
    private String Memo;
    @JSONField(name = "PortalMemo")
    private String PortalMemo;
    @JSONField(name = "Type")
    private Integer Type;

}
