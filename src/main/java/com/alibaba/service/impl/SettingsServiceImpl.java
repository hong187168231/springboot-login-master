package com.alibaba.service.impl;

import com.alibaba.bean.Result;
import com.alibaba.bean.entity.Settings;
import com.alibaba.bean.req.SettingsReq;
import com.alibaba.mapper.SettingsMapper;
import com.alibaba.service.ISettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class SettingsServiceImpl implements ISettingsService {

    @Autowired
    private SettingsMapper settingsMapper;

    public Result querySettings(){
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        try {
            List<Settings> settingsList = settingsMapper.selectSettings();
            result.setSuccess(true);
            result.setMsg("管理设置查询成功");
            result.setDetail(settingsList);
        } catch (Exception e) {
            result.setMsg("管理设置查询失败");
            e.printStackTrace();
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result modifySettings(SettingsReq settingsReq){
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);

        Settings settings = new Settings();
        settings.setId(settingsReq.getId());
        settings.setSwhLitIpActi(settingsReq.getSwhLitIpActi());//限制同一个ip激活开关 0开 1关闭
        settings.setLimitIpActi(settingsReq.getLimitIpActi());//限制同一个ip激活数量
        //终身限制
        settings.setSwhLitAcctActi(settingsReq.getSwhLitAcctActi());//限制同一个id账号激活开关 0开 1关闭
        settings.setLitAcctActi(settingsReq.getLitAcctActi());//限制同一个id账号只能激活数量
        //每天限制
        settings.setSwhLitAcctActiDay(settingsReq.getSwhLitAcctActiDay());//限制同一个id账号激活开关 0开 1关闭
        settings.setLitAcctActiDay(settingsReq.getLitAcctActiDay());//限制同一个id账号只能激活数量
        settingsMapper.updateSettings(settings);
        result.setSuccess(true);
        result.setMsg("管理设置修改成功");
        List<Settings> settingsList = new ArrayList<Settings>();
        settingsList.add(settings);
        result.setDetail(settingsList);

        return result;
    }
}
