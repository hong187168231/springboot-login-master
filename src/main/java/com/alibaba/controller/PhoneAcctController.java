package com.alibaba.controller;

import com.alibaba.bean.PageRequest;
import com.alibaba.bean.Result;
import com.alibaba.bean.entity.PhoneAcct;
import com.alibaba.bean.entity.User;
import com.alibaba.bean.req.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.service.IPhoneacctService;
import com.alibaba.utils.DateUtils;
import com.alibaba.utils.ExcelUtils;
import com.alibaba.utils.IpUtil;
import com.alibaba.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/phoneacct")
@Slf4j
@Api(tags = "手机号")
public class PhoneAcctController {

    @Autowired
    private IPhoneacctService phoneacctService;

    /**
     * 会员激活卡
     * @param phoneAcctReq
     * @return
     */
    @ApiOperation("前台会员使用手机号激活")
    @PostMapping(value = "/activationPhone")
    public Result activationCard(HttpServletRequest request, PhoneAcctWebReq phoneAcctReq){
//        phoneAcctReq phoneAcctReq = new phoneAcctReq();
//        phoneAcctReq.setActivationAcct("yixiu");//激活账号
        if(null==phoneAcctReq.getIpAddress()||"".equals(phoneAcctReq.getIpAddress())){
            phoneAcctReq.setIpAddress(IpUtil.getIpAddr(request));//IP地址
        }
//        phoneAcctReq.setCardNo("24live00000002");
//        phoneAcctReq.setCardPwd("654432");
        return phoneacctService.addPhoneAcct(phoneAcctReq);
    }

    @PostMapping("/import")
    @ApiOperation("导入手机号数据")
    public Result importUser(@RequestPart("file") MultipartFile file) throws Exception {

        List<PhoneAcctExport> phoneAcctExportList = ExcelUtils.readMultipartFile(file,PhoneAcctExport.class);
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("admin");
        User user = UserContext.getCurrebtUser();
        if(null==user) {
            Result result = new Result();
            result.setSuccess(false);
            result.setMsg("用户登录已失效请重新登录");
            return result;
        }
        if("1".equals(user.getAuthority())){//1普通会员
            Result result = new Result();
            result.setSuccess(false);
            result.setMsg("您是普通会员，此功能只对管理员开放。");
            return result;
        }
        boolean b = true;
        for(PhoneAcctExport phoneAcctExport : phoneAcctExportList) {

            if(null!=phoneAcctExport.getRowTips() && !"".equals(phoneAcctExport.getRowTips())){
                b = false;
                Result result = new Result();
                result.setSuccess(false);
                result.setMsg("导入失败");
                result.setDetail(phoneAcctExportList);
                return result;
            }
        }
        if(null!=phoneAcctExportList&&phoneAcctExportList.size()>10000){
            Result result = new Result();
            result.setSuccess(false);
            result.setMsg("导入失败，导入数据不能超过10000条");
            result.setDetail(phoneAcctExportList);
            return result;
        }
        return phoneacctService.expPhoneAcct(phoneAcctExportList,user);
    }
    @ApiOperation("导出手机账号激活信息")
    @GetMapping("/export")
    public void export(PhoneAcctManageReq phoneAcctReq,HttpServletResponse response) {
        // 表头数据
        List<Object> head = Arrays.asList("手机号","激活账号","IP地址","设备信息","创建时间","修改时间","有效时间开始","有效时间结束","卡面金额","增送金额","处理状态","是否删除","处理人ID","处理人","备注");
        // 将数据汇总
        List<List<Object>> sheetDataList = new ArrayList<>();
        sheetDataList.add(head);
        User user = UserContext.getCurrebtUser();
        if(null!=user) {
            List<PhoneAcct> phoneAcctList = phoneacctService.selectActivationCardexp(phoneAcctReq, user);
            if(null!=phoneAcctList && phoneAcctList.size()>0){
                for(PhoneAcct phoneAcct:phoneAcctList) {
                    List<Object> user1 = new ArrayList<>();
                    user1.add(phoneAcct.getCardno());
                    user1.add(phoneAcct.getActivationAcct());
                    user1.add(phoneAcct.getIpAddress());
                    user1.add(phoneAcct.getDeviceInfo());
                    user1.add(phoneAcct.getCreate_time());
                    user1.add(phoneAcct.getUpdate_time());
                    user1.add(phoneAcct.getStart_effectiove_time());
                    user1.add(phoneAcct.getEnd_effectiove_time());
                    user1.add(phoneAcct.getCardAmount());
                    user1.add(phoneAcct.getAdditionalAmount());
                    user1.add(null!=phoneAcct.getIsHandle()&&!"".equals(phoneAcct.getIsHandle())?("0".equals(phoneAcct.getIsHandle())?"未使用":("1".equals(phoneAcct.getIsHandle())?"已处理":("2".equals(phoneAcct.getIsHandle())?"拒绝":"激活"))):"");
                    user1.add(null!=phoneAcct.getIs_delete()&&!"".equals(phoneAcct.getIs_delete())?("1".equals(phoneAcct.getIs_delete())?"已删除":"未删除"):"");
                    user1.add(phoneAcct.getUserid());
                    user1.add(phoneAcct.getUsername());
                    user1.add(phoneAcct.getRemark());
                    sheetDataList.add(user1);
                }
            }
        }
        // 导出数据
        ExcelUtils.export(response, DateUtils.formatDate(new Date(),DateUtils.DEFAULT_TIMESTAMP), sheetDataList);
    }
    /**
     * 查询激活信息
     * @param phoneAcctReq
     * @return
     */
    @PostMapping(value = "/queryActivationCard")
    @ApiOperation("后端管理查询使用手机号信息")
    public Result queryActivationCard(PhoneAcctManageReq phoneAcctReq, PageRequest pageRequest){
        User user = UserContext.getCurrebtUser();
        if(null==user) {
            Result result = new Result();
            result.setSuccess(false);
            result.setMsg("用户登录已失效请重新登录");
            return result;
        }
        if("1".equals(user.getAuthority())){//1普通会员
            Result result = new Result();
            result.setSuccess(false);
            result.setMsg("您是普通会员，此功能只对管理员开放。");
            return result;
        }
//        User user = new User();
//        user.setId(1L);
//        user.setUsername("admin");
        return phoneacctService.selectActivationCard(phoneAcctReq,pageRequest,user);
    }

    /**
     * 查询统计激活信息
     * @param phoneAcctReq
     * @return
     */
    @PostMapping(value = "/queryStatiActivationCard")
    @ApiOperation("查询统计激活信息")
    public Result queryStatiActivationCard(PhoneAcctReq phoneAcctReq, PageRequest pageRequest){
        return phoneacctService.selectStatiActivationCard(phoneAcctReq,pageRequest);
    }

    /**
     * 处理卡
     * @param phoneAcctReq
     * @return
     */
    @PostMapping(value = "/isHandleCard")
    @ApiOperation("后台管理处理卡")
    public Result isHandleCard(PhoneAcctHandleReq phoneAcctReq){
        User user = UserContext.getCurrebtUser();
        if(null==user) {
            Result result = new Result();
            result.setSuccess(false);
            result.setMsg("用户登录已失效请重新登录");
            return result;
        }
        if("1".equals(user.getAuthority())){//1普通会员
            Result result = new Result();
            result.setSuccess(false);
            result.setMsg("您是普通会员，此功能只对管理员开放。");
            return result;
        }
        return phoneacctService.isHandleCard(phoneAcctReq,user);
    }
}
