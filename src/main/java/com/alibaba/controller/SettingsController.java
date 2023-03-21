package com.alibaba.controller;

import com.alibaba.bean.Result;
import com.alibaba.bean.entity.User;
import com.alibaba.bean.req.SettingsReq;
import com.alibaba.service.ISettingsService;
import com.alibaba.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settings")
@Api(tags = "后台设置")
public class SettingsController {

    @Autowired
    private ISettingsService settingsService;

    /**
     * 查询后台设置
     * @return
     */
    @GetMapping(value = "/querySettings")
    @ApiOperation("查询后台设置")
    public Result querySettings(){
        User user = UserContext.getCurrebtUser();
        if(null==user) {
            Result result = new Result();
            result.setSuccess(false);
            result.setMsg("用户登录已失效请重新登录");
            return result;
        }
        return settingsService.querySettings();
    }

    /**
     * 修改后台设置
     * @param settingsReq
     * @return
     */
    @PostMapping(value = "/modifySettings")
    @ApiOperation("修改后台设置")
    public Result modifySettings(SettingsReq settingsReq){
        User user = UserContext.getCurrebtUser();
        if(null==user) {
            Result result = new Result();
            result.setSuccess(false);
            result.setMsg("用户登录已失效请重新登录");
            return result;
        }
        return settingsService.modifySettings(settingsReq);
    }
}
