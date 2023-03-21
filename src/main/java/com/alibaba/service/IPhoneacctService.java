package com.alibaba.service;

import com.alibaba.bean.PageRequest;
import com.alibaba.bean.Result;
import com.alibaba.bean.entity.PhoneAcct;
import com.alibaba.bean.entity.User;
import com.alibaba.bean.req.*;

import java.util.List;

public interface IPhoneacctService {
    public Result addPhoneAcct(PhoneAcctWebReq phoneAcctReq);
    public Result selectActivationCard(PhoneAcctManageReq phoneAcctReq, PageRequest pageRequest, User user);
    public Result isHandleCard(PhoneAcctHandleReq phoneAcctReq, User user);
    public Result selectStatiActivationCard(PhoneAcctReq phoneAcctReq, PageRequest pageRequest);

    public Result expPhoneAcct(List<PhoneAcctExport> phoneAcctExportList,User user);

    List<PhoneAcct> selectActivationCardexp(PhoneAcctManageReq phoneAcctReq, User user);
}
