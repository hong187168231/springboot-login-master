package com.alibaba.service;

import com.alibaba.bean.Result;
import com.alibaba.bean.req.SettingsReq;

public interface ISettingsService {

    public Result querySettings();

    public Result modifySettings(SettingsReq settingsReq);
}
