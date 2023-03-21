package com.alibaba.controller;

import com.alibaba.bean.PageRequest;
import com.alibaba.bean.Result;
import com.alibaba.bean.entity.User;
import com.alibaba.bean.req.UserReq;
import com.alibaba.service.IUserService;
import com.alibaba.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
@Api(tags = "用户信息")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 注册
     * @param userReq 参数封装
     * @return Result
     */
    @PostMapping(value = "/regist")
    @ApiOperation("注册")
    public Result regist(UserReq userReq){
        return userService.regist(userReq);
    }

    /**
     * 登录
     * @param userReq 参数封装
     * @return Result
     */
    @PostMapping(value = "/login")
    @ApiOperation("登录")
    public Result login(UserReq userReq){
        return userService.login(userReq);
    }

    /**
     * 查询用户
     * @param userReq
     * @return
     */
    @PostMapping(value = "/queryUser")
    @ApiOperation("查询用户")
    public Result queryUser(UserReq userReq, PageRequest pageRequest){
        User user = UserContext.getCurrebtUser();
        if(null==user) {
            Result result = new Result();
            result.setSuccess(false);
            result.setMsg("用户登录已失效请重新登录");
            return result;
        }
        return userService.queryUser(userReq,pageRequest);
    }

    /**
     * 修改用户
     * @param userReq
     * @return
     */
    @PostMapping(value = "/modifyUser")
    @ApiOperation("修改用户")
    public Result modifyUser(UserReq userReq){
        User user = UserContext.getCurrebtUser();
        if(null==user) {
            Result result = new Result();
            result.setSuccess(false);
            result.setMsg("用户登录已失效请重新登录");
            return result;
        }
        return userService.modifyUser(userReq);
    }
}

