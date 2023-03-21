package com.alibaba.service;

import com.alibaba.bean.Result;
import com.alibaba.bean.req.CardInfoAddReq;
import com.alibaba.bean.req.CardInfoReq;

public interface ICardInfoService {

    public Result insertCardInfoBatch(CardInfoAddReq cardInfoReq);

    public Result selectCardInfoByisActivation();

    public Result selectCardNoPrefix();
}
