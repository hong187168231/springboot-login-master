package com.alibaba.service;

import com.alibaba.bean.PageRequest;
import com.alibaba.bean.Result;
import com.alibaba.bean.entity.MemberInfo;
import com.alibaba.bean.entity.User;
import com.alibaba.bean.req.MemberInfoExpReq;
import com.alibaba.bean.req.MemberInfoJHKReq;
import com.alibaba.bean.req.MemberInfoReq;

import java.util.List;

public interface IMemberInfoService {
    public Result addMemberInfo(MemberInfoJHKReq memberInfoReq);
    public Result selectActivationCard(MemberInfoReq memberInfoReq, PageRequest pageRequest);
    public List<MemberInfo> selectActivationCardexp(MemberInfoExpReq memberInfoReq);
    public Result isHandleCard(MemberInfoReq memberInfoReq, User user);
    public Result selectStatiActivationCard(MemberInfoReq memberInfoReq, PageRequest pageRequest);
}
