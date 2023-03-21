package com.alibaba.controller;

import com.alibaba.bean.PageRequest;
import com.alibaba.bean.Result;
import com.alibaba.bean.entity.MemberInfo;
import com.alibaba.bean.entity.User;
import com.alibaba.bean.req.MemberInfoExpReq;
import com.alibaba.bean.req.MemberInfoJHKReq;
import com.alibaba.bean.req.MemberInfoReq;
import com.alibaba.service.IMemberInfoService;
import com.alibaba.utils.DateUtils;
import com.alibaba.utils.ExcelUtils;
import com.alibaba.utils.IpUtil;
import com.alibaba.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/member")
@Api(tags = "激活卡")
public class MemberInfoController {

    @Autowired
    private IMemberInfoService memberInfoService;

    /**
     * 会员激活卡
     * @param memberInfoReq
     * @return
     */
    @PostMapping(value = "/activationCard")
    @ApiOperation("会员激活卡")
    public Result activationCard(HttpServletRequest request, MemberInfoJHKReq memberInfoReq){
//        MemberInfoReq memberInfoReq = new MemberInfoReq();
//        memberInfoReq.setActivationAcct("yixiu");//激活账号
        if(null==memberInfoReq.getIpAddress()||"".equals(memberInfoReq.getIpAddress())){
            memberInfoReq.setIpAddress(IpUtil.getIpAddr(request));//IP地址
        }
//        memberInfoReq.setCardNo("24live00000002");
//        memberInfoReq.setCardPwd("654432");
        return memberInfoService.addMemberInfo(memberInfoReq);
    }

    /**
     * 查询激活信息
     * @param memberInfoReq
     * @return
     */
    @PostMapping(value = "/queryActivationCard")
    @ApiOperation("查询激活信息")
    public Result queryActivationCard(MemberInfoReq memberInfoReq, PageRequest pageRequest){
        return memberInfoService.selectActivationCard(memberInfoReq,pageRequest);
    }

    @ApiOperation("导出账号激活信息")
    @GetMapping("/export")
    public void export(MemberInfoExpReq memberInfoReq, HttpServletResponse response) {
        // 表头数据
        List<Object> head = Arrays.asList("卡号=前缀+序号","手机号","激活账号","IP地址","设备信息","创建时间","修改时间","有效时间开始","有效时间结束","卡面金额","增送金额","处理状态","是否删除","处理人ID","处理人","备注");
        // 将数据汇总
        List<List<Object>> sheetDataList = new ArrayList<>();
        sheetDataList.add(head);
//        User user = UserContext.getCurrebtUser();
//        if(null!=user) {
//
//        }
        List<MemberInfo> memberInfoList = memberInfoService.selectActivationCardexp(memberInfoReq);
        if(null!=memberInfoList && memberInfoList.size()>0){
            for(MemberInfo memberInfo:memberInfoList) {
                List<Object> user1 = new ArrayList<>();
                user1.add(memberInfo.getCardNo());
                user1.add(memberInfo.getPhoneno());
                user1.add(memberInfo.getActivationAcct());
                user1.add(memberInfo.getIpAddress());
                user1.add(memberInfo.getDeviceInfo());
                user1.add(memberInfo.getCreate_time());
                user1.add(memberInfo.getUpdate_time());
                user1.add(memberInfo.getStart_effectiove_time());
                user1.add(memberInfo.getEnd_effectiove_time());
                user1.add(memberInfo.getCardAmount());
                user1.add(memberInfo.getAdditionalAmount());
                user1.add(null!=memberInfo.getIsHandle()&&!"".equals(memberInfo.getIsHandle())?("0".equals(memberInfo.getIsHandle())?"未使用":("1".equals(memberInfo.getIsHandle())?"已处理":("2".equals(memberInfo.getIsHandle())?"拒绝":"激活"))):"");
                user1.add(null!=memberInfo.getIs_delete()&&!"".equals(memberInfo.getIs_delete())?("1".equals(memberInfo.getIs_delete())?"已删除":"未删除"):"");
                user1.add(memberInfo.getUserid());
                user1.add(memberInfo.getUsername());
                user1.add(memberInfo.getRemark());
                sheetDataList.add(user1);
            }
        }
        // 导出数据
        ExcelUtils.export(response, DateUtils.formatDate(new Date(),DateUtils.DEFAULT_TIMESTAMP), sheetDataList);
    }

    /**
     * 查询统计激活信息
     * @param memberInfoReq
     * @return
     */
    @PostMapping(value = "/queryStatiActivationCard")
    @ApiOperation("查询统计激活信息")
    public Result queryStatiActivationCard(MemberInfoReq memberInfoReq, PageRequest pageRequest){
        return memberInfoService.selectStatiActivationCard(memberInfoReq,pageRequest);
    }

    /**
     * 处理卡
     * @param memberInfoReq
     * @return
     */
    @PostMapping(value = "/isHandleCard")
    @ApiOperation("处理卡")
    public Result isHandleCard(MemberInfoReq memberInfoReq){
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
        return memberInfoService.isHandleCard(memberInfoReq,user);
    }
}
