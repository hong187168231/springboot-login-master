package com.alibaba.service;

import com.alibaba.bean.Result;

import javax.servlet.http.HttpServletResponse;

public interface IExportService {

    public Result exportCardInfo(HttpServletResponse response, String cardNoPrefix);
}
