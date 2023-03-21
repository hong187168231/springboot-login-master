package com.alibaba.bean.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AuditReq {
    @JSONField(name = "Amount")
    private BigDecimal Amount;
    @JSONField(name = "Type")
    private Integer Type;
}
