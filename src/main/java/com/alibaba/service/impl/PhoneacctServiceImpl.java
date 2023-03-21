package com.alibaba.service.impl;

import com.alibaba.bean.PageRequest;
import com.alibaba.bean.Result;
import com.alibaba.bean.entity.PhoneAcct;
import com.alibaba.bean.entity.Settings;
import com.alibaba.bean.entity.User;
import com.alibaba.bean.req.*;
import com.alibaba.bean.vo.PhoneAcctVo;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.mapper.PhoneacctMapper;
import com.alibaba.mapper.SettingsMapper;
import com.alibaba.service.IPhoneacctService;
import com.alibaba.utils.DateUtils;
import com.alibaba.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional(rollbackFor = RuntimeException.class)
@Slf4j
public class PhoneacctServiceImpl implements IPhoneacctService {
    public static void main(String args[]) {
        System.out.println(HSSFDateUtil.getJavaDate(Double.parseDouble("43819")));

    }
    @Autowired
    private PhoneacctMapper phoneacctMapper;
    @Autowired
    private SettingsMapper settingsMapper;

    private String formatWithMakingup(String src){
        String formatStr = "0000000000";
        int delta = formatStr.length()-src.length();
        if(delta<=0){
            return src;
        }
        return formatStr.substring(0,delta)+src;
    }
    @Transactional(rollbackFor = Exception.class)
    public Result addPhoneAcct(PhoneAcctWebReq phoneAcctReq){
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        if(null==phoneAcctReq.getCardno()||"".equals(phoneAcctReq.getCardno())){
//            result.setMsg("手机号不能为空");
            result.setMsg("Số điện thoại di động không được để trống");
            return result;
        }
        phoneAcctReq.setCardno(formatWithMakingup(phoneAcctReq.getCardno().trim()));

//        if(null==PhoneAcctReq.getCardPwd()||"".equals(PhoneAcctReq.getCardPwd())){
////            result.setMsg("充值卡密码不能为空");
//            result.setMsg("Mã thẻ không được để trống");
//            return result;
//        }
        if(null==phoneAcctReq.getActivationAcct()||"".equals(phoneAcctReq.getActivationAcct())){
//            result.setMsg("会员账号不能为空");
            result.setMsg("Tài khoản thành viên không được để trống");
            return result;
        }
        if(null==phoneAcctReq.getIpAddress()||"".equals(phoneAcctReq.getIpAddress())){
//            result.setMsg("IP地址不能为空");
            result.setMsg("IP không được để trống");
            return result;
        }
        Settings settings = settingsMapper.selectSettingsByid("1");
        if("0".equals(settings.getSwhLitIpActi())){//限制同一个ip激活开关 0开 1关闭
            List<PhoneAcct> IPList = phoneacctMapper.selectPhoneacctByIp(phoneAcctReq.getIpAddress());
            if(null!=IPList){
                if(IPList.size()>=settings.getLimitIpActi()){;//限制同一个ip激活数量
//                    result.setMsg("同一个ip只能激活（"+settings.getLimitIpActi()+"）个卡密");
                    result.setMsg("Cùng 1 địa chỉ IP chỉ có thể sự dụng （"+settings.getLimitIpActi()+"） thẻ");
                    return result;
                }
            }
        }
        //终身
        if ("0".equals(settings.getSwhLitAcctActi())){//限制同一个id账号激活开关 0开 1关闭
            List<PhoneAcct> activationAcctList = phoneacctMapper.selectPhoneacctByActivationAcct(phoneAcctReq.getActivationAcct());
            if(null!=activationAcctList){
                if(activationAcctList.size()>=settings.getLitAcctActi()){//限制同一个id账号只能激活数量
//                    result.setMsg("同一个会员账号只能激活（"+settings.getLitAcctActi()+"）张卡密");
                    result.setMsg("Cùng một tài khoản thành viên chỉ có thể kích hoạt（"+settings.getLitAcctActi()+"） thẻ");
                    return result;
                }
            }
        }
        //每天
        if ("0".equals(settings.getSwhLitAcctActiDay())){//限制同一个id账号激活开关 0开 1关闭
            List<PhoneAcct> activationAcctList = phoneacctMapper.selectPhoneacctByActivationAcctToDays(phoneAcctReq.getActivationAcct());
            if(null!=activationAcctList){
                if(activationAcctList.size()>=settings.getLitAcctActiDay()){//限制同一个id账号只能激活数量
//                    result.setMsg("同一个会员账号只能激活（"+settings.getLitAcctActi()+"）张卡密");
                    result.setMsg("Cùng một tài khoản thành viên chỉ có thể kích hoạt ("+settings.getLitAcctActiDay()+")thẻ mỗi ngày");
                    return result;
                }
            }
        }


        PhoneAcct phoneAcct = phoneacctMapper.selectPhoneacctByCardnoOne(phoneAcctReq.getCardno());
            if(null==phoneAcct){
//                result.setMsg("手机号（"+PhoneAcctReq.getCardno()+"）不存在,请输入正确的手机号");
                result.setMsg("Số điện thoại（"+phoneAcctReq.getCardno()+"） không tồn tại, Xin vui lòng nhập một số điện thoại hợp lệ");
                return result;
            }
            if("1".equals(phoneAcct.getIs_delete())){
//                result.setMsg("手机号（"+PhoneAcctReq.getCardno()+"）已经删除");
                result.setMsg("Số điện thoại（"+phoneAcctReq.getCardno()+"）đã bị xóa");
                return result;
            }
//            if("0".equals(cardInfo.getIsExp())){
////                result.setMsg("手机号（"+PhoneAcctReq.getCardno()+"）还未分发，暂不能激活");
//                result.setMsg("Số điện thoại（"+phoneAcctReq.getCardno()+"）chưa được phát hành và tạm thời không kích hoạt được");
//                return result;
//            }
            if("1".equals(phoneAcct.getIsHandle())){
//                result.setMsg("手机号（"+PhoneAcctReq.getCardno()+"）已经处理，相同手机号不能重复激活");
                result.setMsg("Số điện thoại（"+phoneAcctReq.getCardno()+"）đã được xử lý, Cùng một số điện thoại không thể được kích hoạt nhiều lần");
                return result;
            }
        if("2".equals(phoneAcct.getIsHandle())){
//                result.setMsg("手机号（"+PhoneAcctReq.getCardno()+"）已经拒绝，请联系客服");
            result.setMsg("Số điện thoại（"+phoneAcctReq.getCardno()+"）đã từ chối, Vui lòng liên hệ bộ phận chăm sóc khách hàng");
            return result;
        }
        if(null!=phoneAcct.getEnd_effectiove_time()&&!"".equals(phoneAcct.getEnd_effectiove_time())){
            Date newDate = DateUtils.parseDate(DateUtils.formatDate(new Date(),DateUtils.FORMAT_YYYY_MM_DD),DateUtils.FORMAT_YYYY_MM_DD);
            Date effectiove_time = DateUtils.parseDate(phoneAcct.getEnd_effectiove_time(),DateUtils.FORMAT_YYYY_MM_DD);
            if(newDate.after(effectiove_time)){//当前时间大于有效截止时间
                //                result.setMsg("手机号（"+PhoneAcctReq.getCardno()+"）已过有效期");
                result.setMsg("Số điện thoại（"+phoneAcctReq.getCardno()+"）đã hết hạn:"+phoneAcct.getEnd_effectiove_time());
                return result;
            }
        }

//        DepositReq depositReq = new DepositReq();
//        depositReq.setAccount(phoneAcctReq.getActivationAcct());
//        depositReq.setAmount(BigDecimal.valueOf(phoneAcct.getCardAmount()).add(BigDecimal.valueOf(phoneAcct.getAdditionalAmount())));
//        AuditReq auditReq = new AuditReq();
//        auditReq.setType(3);
//        auditReq.setAmount(BigDecimal.valueOf(phoneAcct.getCardAmount()).add(BigDecimal.valueOf(phoneAcct.getAdditionalAmount())));
//        depositReq.setAudit(auditReq);
//        depositReq.setIsReal(true);
//        depositReq.setMemo("SMS +88K Trải nghiệm miễn phí");
//        depositReq.setPortalMemo("SMS +88K Trải nghiệm miễn phí");
//        depositReq.setType(5);
//        log.error("调用api传参数==========》》》{}",JSONObject.toJSONString(depositReq));
//        try {
//            String resultString = HttpUtil.doProxyPostJson("https://ela.api.jdtmb.com/api/1.0/member/deposit",JSONObject.toJSONString(depositReq),"X-ApiKey","8d81f957");
//            log.error("收到第3方返回参数==========《《《《《《{}",resultString);
//            if(null!=resultString&&!"".equals(resultString)){
//                JSONObject apiResponseData = JSONObject.parseObject(resultString);
//                Integer code = apiResponseData.getInteger("Code");
//                if(code!=200){
//                    String message = apiResponseData.getString("Message");
//                    result.setMsg("Code:"+code+" "+"Message:"+errorCode(code));
//                    return result;
//                }else {
                    phoneacctMapper.updatePhoneacctIsHandleById(phoneAcct.getId(),DateUtils.getDateTime(),phoneAcctReq.getIpAddress(),phoneAcctReq.getDeviceInfo(),phoneAcctReq.getActivationAcct());
//            result.setMsg("手机号（"+phoneAcct.getCardno()+"）激活成功，充值金额（"+phoneAcct.getCardAmount()+"），增送金额（"+phoneAcct.getAdditionalAmount()+"）");
                    result.setMsg("Số điện thoại（"+phoneAcctReq.getCardno()+"）kích hoạt thành công, số tiền nạp（"+phoneAcct.getCardAmount()+"），số tiền thưởng（"+phoneAcct.getAdditionalAmount()+"）");
                    result.setSuccess(true);
//                }
//
//            }else {//调用api出现异常
////                result.setMsg("调用第3方服务异常，请到第3方平台核实确认。");
//                result.setMsg("Ngoại lệ khi gọi dịch vụ của bên thứ ba，Vui lòng truy cập nền tảng của bên thứ ba để xác minh và xác nhận");
//            }
//        } catch (Exception e) {
////            result.setMsg("调用第3方服务异常，请到第3方平台核实确认。");
//            result.setMsg("Ngoại lệ khi gọi dịch vụ của bên thứ ba，Vui lòng truy cập nền tảng của bên thứ ba để xác minh và xác nhận");
//        }
//
//
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result expPhoneAcct(List<PhoneAcctExport> phoneAcctExportList,User user){
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        boolean b = true;
        List<PhoneAcctExport> newPphoneAcctExportList = new ArrayList<>();
        List<PhoneAcct> phoneacctLists = new ArrayList<>();
        boolean add = true;
        for(PhoneAcctExport phoneAcctExport : phoneAcctExportList) {
            phoneAcctExport.setCardno(formatWithMakingup(phoneAcctExport.getCardno().trim()));
            String end_effectiove_time = null!=phoneAcctExport.getEnd_effectiove_time()&&!"".equals(phoneAcctExport.getEnd_effectiove_time())?phoneAcctExport.getEnd_effectiove_time().replace(",",""):"";
            PhoneAcct phoneAcct = phoneacctMapper.selectPhoneacctByCardnoOne(phoneAcctExport.getCardno());
            if (null == phoneAcct) {
                PhoneAcct newPhoneAcct = new PhoneAcct();
                newPhoneAcct.setCardno(phoneAcctExport.getCardno());//手机号
                newPhoneAcct.setIs_delete("0");//0否   1删除
                String cardAmount = null!=phoneAcctExport.getCardAmount()&&!"".equals(phoneAcctExport.getCardAmount())?phoneAcctExport.getCardAmount().replace(",",""):"";
                newPhoneAcct.setCardAmount(null!=cardAmount&&!"".equals(cardAmount)?Double.valueOf(cardAmount):0d);//卡面金额
                String additionalAmount = null!=phoneAcctExport.getAdditionalAmount()&&!"".equals(phoneAcctExport.getAdditionalAmount())?phoneAcctExport.getAdditionalAmount().replace(",",""):"";
                newPhoneAcct.setAdditionalAmount(null!=additionalAmount&&!"".equals(additionalAmount)?Double.valueOf(additionalAmount):0d);//增送金额
                newPhoneAcct.setIsHandle("0");//0否   1处理  2拒绝
                newPhoneAcct.setCreate_time(DateUtils.formatDate(new Date(),DateUtils.FORMAT_YYYY_MM_DD_HHMMSS));
                newPhoneAcct.setUserid(user.getId());//处理人ID
                newPhoneAcct.setUsername(user.getUsername());//处理人
                newPhoneAcct.setRemark(phoneAcctExport.getRemark());//备注

                String start_effectiove_time = null!=phoneAcctExport.getStart_effectiove_time()&&!"".equals(phoneAcctExport.getStart_effectiove_time())?phoneAcctExport.getStart_effectiove_time().replace(",",""):"";
                newPhoneAcct.setStart_effectiove_time(null!=start_effectiove_time&&!"".equals(start_effectiove_time)?DateUtils.formatDate(HSSFDateUtil.getJavaDate(Double.parseDouble(start_effectiove_time)),DateUtils.FORMAT_YYYY_MM_DD):"");

                newPhoneAcct.setEnd_effectiove_time(null!=end_effectiove_time&&!"".equals(end_effectiove_time)?DateUtils.formatDate(HSSFDateUtil.getJavaDate(Double.parseDouble(end_effectiove_time)),DateUtils.FORMAT_YYYY_MM_DD):"");
                phoneacctLists.add(newPhoneAcct);
            }
            newPphoneAcctExportList.add(phoneAcctExport);
        }
        if(null!=phoneacctLists && phoneacctLists.size()>0) {
            List<PhoneAcct> phoneAccts = new LinkedList<>();
            for (PhoneAcct phoneAcct:phoneacctLists){
                boolean bp = phoneAccts.stream().anyMatch(p -> p.getCardno().equals(phoneAcct.getCardno()));
                if (!bp) {
                    phoneAccts.add(phoneAcct);
                }
            }
            if(null!=phoneAccts && phoneAccts.size()>0) {
                phoneacctMapper.insertCollectList(phoneAccts);
            }
        }
        result.setSuccess(true);
        result.setMsg("导入成功");
        result.setDetail(newPphoneAcctExportList);
        return result;
    }
    public Result selectActivationCard(PhoneAcctManageReq phoneAcctReq, PageRequest pageRequest, User user){
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);


        try {
            String sql = "";
            if(null!=phoneAcctReq.getStart_create_time()&&!"".equals(phoneAcctReq.getStart_create_time())){
                sql += " and date_format(create_time,'%Y-%m-%d')>='"+DateUtils.formatDate(DateUtils.parseDate(phoneAcctReq.getStart_create_time(),DateUtils.FORMAT_YYYY_MM_DD),DateUtils.FORMAT_YYYY_MM_DD)+"'";
            }
            if(null!=phoneAcctReq.getEnd_create_time()&&!"".equals(phoneAcctReq.getEnd_create_time())){
                sql += " and date_format(create_time,'%Y-%m-%d')<='"+DateUtils.formatDate(DateUtils.parseDate(phoneAcctReq.getEnd_create_time(),DateUtils.FORMAT_YYYY_MM_DD),DateUtils.FORMAT_YYYY_MM_DD)+"'";
            }
            List<PhoneAcct> memberInfoList = phoneacctMapper.selectPhoneacct(phoneAcctReq.getActivationAcct(),phoneAcctReq.getIpAddress(),phoneAcctReq.getCardno(),phoneAcctReq.getIsHandle(),phoneAcctReq.getIs_delete(),user.getId(),sql);
            List<PhoneAcct> phoneAcctList1 = new ArrayList<PhoneAcct>();
            if(memberInfoList != null && memberInfoList.size()>0){
                //3.2计算startIndex
                int startIndex=(pageRequest.getPageNum()-1)*pageRequest.getPageSize();
                phoneAcctList1 = phoneacctMapper.selectPagePhoneacct(phoneAcctReq.getActivationAcct(),phoneAcctReq.getIpAddress(),phoneAcctReq.getCardno(),phoneAcctReq.getIsHandle(),phoneAcctReq.getIs_delete(),user.getId(),startIndex,pageRequest.getPageSize(),sql);
                result.setPageNum(pageRequest.getPageNum());//当前页码
                result.setPageSize(pageRequest.getPageSize());//每页数量
                result.setTotalSize(memberInfoList.size());//记录总数
                result.setTotalPages((memberInfoList.size()-1)/pageRequest.getPageSize()+1);//页码总数
            }
            result.setMsg("查询激活成功");
            result.setSuccess(true);
            result.setDetail(phoneAcctList1);
        } catch (Exception e) {
            result.setMsg("查询激活失败");
            e.printStackTrace();
        }
        return result;
    }

    public List<PhoneAcct> selectActivationCardexp(PhoneAcctManageReq phoneAcctReq,  User user){
        try {
            String sql = "";
            if(null!=phoneAcctReq.getStart_create_time()&&!"".equals(phoneAcctReq.getStart_create_time())){
                sql += " and date_format(create_time,'%Y-%m-%d')>='"+DateUtils.formatDate(DateUtils.parseDate(phoneAcctReq.getStart_create_time(),DateUtils.FORMAT_YYYY_MM_DD),DateUtils.FORMAT_YYYY_MM_DD)+"'";
            }
            if(null!=phoneAcctReq.getEnd_create_time()&&!"".equals(phoneAcctReq.getEnd_create_time())){
                sql += " and date_format(create_time,'%Y-%m-%d')<='"+DateUtils.formatDate(DateUtils.parseDate(phoneAcctReq.getEnd_create_time(),DateUtils.FORMAT_YYYY_MM_DD),DateUtils.FORMAT_YYYY_MM_DD)+"'";
            }
            return phoneacctMapper.selectPhoneacct(phoneAcctReq.getActivationAcct(),phoneAcctReq.getIpAddress(),phoneAcctReq.getCardno(),phoneAcctReq.getIsHandle(),phoneAcctReq.getIs_delete(),user.getId(),sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result isHandleCard(PhoneAcctHandleReq phoneAcctReq, User user){
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        if(null==phoneAcctReq.getCardno()||"".equals(phoneAcctReq.getCardno())){
            result.setMsg("手机号不能为空");
            return result;
        }
        List<PhoneAcct> list = phoneacctMapper.selectPhoneacctBycardno(phoneAcctReq.getCardno());
        if(null!=list && list.size()>0) {
            for (int i=0;i<list.size();i++) {
                PhoneAcct phoneAcct = list.get(i);
                if (i==list.size()-1) {
                    String isActivation;//0否   1激活
                    String isHandle;
                    if ("2".equals(phoneAcctReq.getIsHandle())) {//拒绝
                        isHandle = "2";
                        phoneAcctReq.setIsHandle("2");
                        phoneacctMapper.updateIsHandleBycardno(isHandle, phoneAcctReq.getRemark(), DateUtils.getDateTime(), user.getId(), user.getUsername(), phoneAcct.getId());
                        result.setMsg("拒绝成功");
                        result.setSuccess(true);
                    } else {
                        DepositReq depositReq = new DepositReq();
                        depositReq.setAccount(phoneAcct.getActivationAcct());
                        depositReq.setAmount(BigDecimal.valueOf(phoneAcct.getCardAmount()).add(BigDecimal.valueOf(phoneAcct.getAdditionalAmount())));
                        AuditReq auditReq = new AuditReq();
                        auditReq.setType(3);
                        auditReq.setAmount(BigDecimal.valueOf(phoneAcct.getCardAmount()).add(BigDecimal.valueOf(phoneAcct.getAdditionalAmount())));
                        depositReq.setAudit(auditReq);
                        depositReq.setIsReal(true);
                        depositReq.setMemo("SMS +88K Trải nghiệm miễn phí");
                        depositReq.setPortalMemo("SMS +88K Trải nghiệm miễn phí");
                        depositReq.setType(5);
                        log.error("调用api传参数==========》》》{}",JSONObject.toJSONString(depositReq));
                        try {
                            String resultString = HttpUtil.doProxyPostJson("https://ela.api.jdtmb.com/api/1.0/member/deposit",JSONObject.toJSONString(depositReq),"X-ApiKey","8d81f957");
                            log.error("收到第3方返回参数==========《《《《《《{}",resultString);
                            if(null!=resultString&&!"".equals(resultString)){
                                JSONObject apiResponseData = JSONObject.parseObject(resultString);
                                Integer code = apiResponseData.getInteger("Code");
                                if(code!=200){
                                    String message = apiResponseData.getString("Message");
                                    phoneAcctReq.setRemark(null!=phoneAcctReq.getRemark()?phoneAcctReq.getRemark():""+"\\r\\n"+"Code:"+"\\r\\n"+"Message:"+null!=message?message:"");
                                    if(code==5000||code==4000){//系统错误
                                        phoneacctMapper.updateIsHandleBycardno("0", phoneAcctReq.getRemark(), DateUtils.getDateTime(), user.getId(), user.getUsername(), phoneAcct.getId());
                                    }
                                    result.setMsg("Code:"+code+" "+"Message:"+null!=message?message:"");
                                    return result;
                                }else {
                                    isHandle = "1";
                                    phoneAcctReq.setIsHandle("1");
                                    result.setMsg("处理成功");
                                    result.setSuccess(true);
                                }
                                phoneacctMapper.updateIsHandleBycardno(isHandle, phoneAcctReq.getRemark(), DateUtils.getDateTime(), user.getId(), user.getUsername(), phoneAcct.getId());

                            }else {//调用api出现异常
    //                            isHandle = "1";
    //                            phoneAcctReq.setIsHandle("1");
                                result.setMsg("调用第3方服务异常，请到第3方平台核实确认。");
                                return result;
                            }
                        } catch (Exception e) {
                            result.setMsg("调用第3方服务异常，请到第3方平台核实确认。");
                            return result;
                        }
                    }
                }
            }
        }else {
            result.setMsg("手机号(" + phoneAcctReq.getCardno() + ")未激活");
            return result;
        }
        return result;
    }

    public Result selectStatiActivationCard(PhoneAcctReq phoneAcctReq, PageRequest pageRequest){
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        try {
            String sql = "";
            if(null!=phoneAcctReq.getStart_create_time()&&!"".equals(phoneAcctReq.getStart_create_time())){
                sql += " and date_format(create_time,'%Y-%m-%d')>='"+DateUtils.formatDate(DateUtils.parseDate(phoneAcctReq.getStart_create_time(),DateUtils.FORMAT_YYYY_MM_DD),DateUtils.FORMAT_YYYY_MM_DD)+"'";
            }
            if(null!=phoneAcctReq.getEnd_create_time()&&!"".equals(phoneAcctReq.getEnd_create_time())){
                sql += " and date_format(create_time,'%Y-%m-%d')<='"+DateUtils.formatDate(DateUtils.parseDate(phoneAcctReq.getEnd_create_time(),DateUtils.FORMAT_YYYY_MM_DD),DateUtils.FORMAT_YYYY_MM_DD)+"'";
            }
            List<PhoneAcctVo> memberInfovoList = phoneacctMapper.selectStatiActivationCard(phoneAcctReq.getCardno(),sql);
            List<PhoneAcctVo> memberInfovoList1 = new ArrayList<PhoneAcctVo>();
            if(memberInfovoList != null && memberInfovoList.size()>0){
                //3.2计算startIndex
                int startIndex=(pageRequest.getPageNum()-1)*pageRequest.getPageSize();
                memberInfovoList1 = phoneacctMapper.selectPageStatiActivationCard(phoneAcctReq.getCardno(),sql,startIndex,pageRequest.getPageSize());
                result.setPageNum(pageRequest.getPageNum());//当前页码
                result.setPageSize(pageRequest.getPageSize());//每页数量
                result.setTotalSize(memberInfovoList.size());//记录总数
                result.setTotalPages((memberInfovoList.size()-1)/pageRequest.getPageSize()+1);//页码总数
            }
            result.setMsg("查询统计激活信息成功");
            result.setSuccess(true);
            result.setDetail(memberInfovoList1);
        } catch (Exception e) {
            result.setMsg("查询统计激活信息失败");
            e.printStackTrace();
        }
        return result;

    }
    public String errorCode(Integer code) {
        switch (code) {
//            4000: 會員帳號不存在
            case 4000:
                return "tài khoản thành viên không tồn tại";
//            4001: 帳號不可為空
            case 4001:
                return "tài khoản không được trống";
//            4002: Memo長度必須在200字內
            case 4002:
                return "độ dài Memo phải trong vòng 200 ký tự";
//            4003: 無效的存入金額
            case 4003:
                return "số tiền gửi không hợp lệ";
//            4004: 無效的稽核金額
            case 4004:
                return "số tiền kiểm toán không hợp lệ";
//            4005: 無效的存款類型
            case 4005:
                return "loại tiền gửi không hợp lệ";
//            4006: 無效的稽核設定
            case 4006:
                return "cài đặt kiểm tra không hợp lệ";
//            4007: 無效的稽核類型
            case 4007:
                return "loại kiểm tra không hợp lệ";
//            4008: 無效的ApiKey
            case 4008:
                return "ApiKey không hợp lệ";
//            4009: 超過單次存款限額
            case 4009:
                return "vượt quá giới hạn tiền gửi một lần";
//            4010: 超過總存款限額
            case 4010:
                return "vượt quá tổng hạn mức tiền gửi";
//            4011: PortalMemo長度必須在500字內
            case 4011:
                return "độ dài PortalMemo phải trong vòng 500 ký tự ";
//            4101: 註冊失敗, 請聯絡API提供商
            case 4101:
                return "đăng ký không thành công, vui lòng liên hệ với nhà cung cấp API";
//            4102: 參數不可為空或格式錯誤
            case 4102:
                return "đổi số không thể rỗng hoặc không đúng định dạng";
//            4103: 不可重複
            case 4103:
                return "không thể lặp lại";
//            4104: 推薦人不存在
            case 4104:
                return "người giới thiệu không tồn tại";
//            4105: 會員註冊失敗, IP為黑名單
            case 4105:
                return "đăng ký thành viên không thành công, IP bị trong danh sách đen";
//            4106: 會員註冊失敗, IP異常
            case 4106:
                return "đăng ký thành viên không thành công , IP bất thường";
//            5000: 系統錯誤
            default:
                return "lỗi hệ thống";
        }
    }

}
