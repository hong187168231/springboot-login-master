package com.alibaba.service;

import com.alibaba.bean.PageRequest;
import com.alibaba.bean.Result;
import com.alibaba.bean.req.UserReq;

import javax.servlet.http.HttpServletRequest;

public interface IUserService {

    public Result regist(UserReq userReq);

    public Result login(UserReq userReq);

    public Result queryUser(UserReq userReq, PageRequest pageRequest);

    public Result modifyUser(UserReq userReq);
}
