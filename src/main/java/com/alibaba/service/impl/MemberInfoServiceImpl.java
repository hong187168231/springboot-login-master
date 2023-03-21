package com.alibaba.service.impl;

import com.alibaba.bean.PageRequest;
import com.alibaba.bean.entity.CardInfo;
import com.alibaba.bean.entity.MemberInfo;
import com.alibaba.bean.Result;
import com.alibaba.bean.entity.Settings;
import com.alibaba.bean.entity.User;
import com.alibaba.bean.req.*;
import com.alibaba.bean.vo.MemberInfoVo;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.mapper.CardInfoMapper;
import com.alibaba.mapper.MemberInfoMapper;
import com.alibaba.mapper.SettingsMapper;
import com.alibaba.service.IMemberInfoService;
import com.alibaba.utils.DateUtils;
import com.alibaba.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = RuntimeException.class)
@Slf4j
public class MemberInfoServiceImpl implements IMemberInfoService {
    @Autowired
    private MemberInfoMapper memberInfoMapper;
    @Autowired
    private CardInfoMapper cardInfoMapper;
    @Autowired
    private SettingsMapper settingsMapper;

    @Transactional(rollbackFor = Exception.class)
    public Result addMemberInfo(MemberInfoJHKReq memberInfoReq){
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        if(null==memberInfoReq.getCardNo()||"".equals(memberInfoReq.getCardNo())){
//            result.setMsg("充值卡卡号不能为空");
            result.setMsg("Mã seri không được để trống");
            return result;
        }
        if(null==memberInfoReq.getPhoneno()||"".equals(memberInfoReq.getPhoneno())){
//            result.setMsg("手机号不能为空");
            result.setMsg("Số điện thoại di động không được để trống");
            return result;
        }

        if(null==memberInfoReq.getCardPwd()||"".equals(memberInfoReq.getCardPwd())){
//            result.setMsg("充值卡密码不能为空");
            result.setMsg("Mật khẩu thẻ nạp không được để trống");
            return result;
        }
        if(null==memberInfoReq.getActivationAcct()||"".equals(memberInfoReq.getActivationAcct())){
//            result.setMsg("会员账号不能为空");
            result.setMsg("Tài khoản thành viên không được để trống");
            return result;
        }
        if(null==memberInfoReq.getIpAddress()||"".equals(memberInfoReq.getIpAddress())){
//            result.setMsg("IP地址不能为空");
            result.setMsg("Địa chỉ IP không được để trống");
            return result;
        }
        Settings settings = settingsMapper.selectSettingsByid("1");
        if("0".equals(settings.getSwhLitIpActi())){//限制同一个ip激活开关 0开 1关闭
            List<MemberInfo> IPList = memberInfoMapper.selectMemberInfoByIp(memberInfoReq.getIpAddress());
            if(null!=IPList){
                if(IPList.size()>=settings.getLimitIpActi()){;//限制同一个ip激活数量
//                    result.setMsg("同一个ip只能激活（"+settings.getLimitIpActi()+"）个卡密");
                    result.setMsg("Cùng một ip chỉ có thể kích hoạt（"+settings.getLimitIpActi()+"）mã hóa thẻ");
                    return result;
                }
            }
        }
        //终身
        if ("0".equals(settings.getSwhLitAcctActi())){//限制同一个id账号激活开关 0开 1关闭
            List<MemberInfo> activationAcctList = memberInfoMapper.selectMemberInfoByActivationAcct(memberInfoReq.getActivationAcct());
            if(null!=activationAcctList){
                if(activationAcctList.size()>=settings.getLitAcctActi()){//限制同一个id账号只能激活数量
//                    result.setMsg("同一个会员账号只能激活（"+settings.getLitAcctActi()+"）张卡密");
                    result.setMsg("Cùng một tài khoản thành viên chỉ có thể kích hoạt（"+settings.getLitAcctActi()+"）thẻ");
                    return result;
                }
            }
        }
        //每天
        if ("0".equals(settings.getSwhLitAcctActiDay())){//限制同一个id账号激活开关 0开 1关闭
            List<MemberInfo> activationAcctList = memberInfoMapper.selectMemberInfoByActivationAcctToDays(memberInfoReq.getActivationAcct());
            if(null!=activationAcctList){
                if(activationAcctList.size()>=settings.getLitAcctActiDay()){//限制同一个id账号只能激活数量
//                    result.setMsg("同一个会员账号只能激活（"+settings.getLitAcctActi()+"）张卡密");
                    result.setMsg("Cùng một tài khoản thành viên chỉ có thể kích hoạt ("+settings.getLitAcctActiDay()+") thẻ mỗi ngày");
                    return result;
                }
            }
        }


            CardInfo cardInfo = cardInfoMapper.selectCardInfoByCardNoAndCardPwd(memberInfoReq.getCardNo(),memberInfoReq.getCardPwd());
            if(null==cardInfo){
//                result.setMsg("充值卡（"+memberInfoReq.getCardNo()+"）不存在或者卡号密码错误");
                result.setMsg("Thẻ nạp tiền（"+memberInfoReq.getCardNo()+"） không tồn tại hoặc sai số thẻ và mật khẩu");
                return result;
            }
            if("1".equals(cardInfo.getIs_delete())){
//                result.setMsg("充值卡（"+memberInfoReq.getCardNo()+"）已经删除");
                result.setMsg("Thẻ nạp（"+memberInfoReq.getCardNo()+"）đã bị xóa");
                return result;
            }
            if("0".equals(cardInfo.getIsExp())){
//                result.setMsg("充值卡（"+memberInfoReq.getCardNo()+"）还未分发，暂不能激活");
                result.setMsg("Thẻ nạp（"+memberInfoReq.getCardNo()+"）chưa được phát hành và tạm thời không kích hoạt được");
                return result;
            }
            if("2".equals(cardInfo.getIsHandle())){
//                result.setMsg("充值卡（"+memberInfoReq.getCardNo()+"）已经拒绝，同一张卡不能重复激活");
                result.setMsg("Thẻ nạp（"+memberInfoReq.getCardNo()+"）đã từ chối, không thể kích hoạt nhiều lần");
                return result;
            }
            if("1".equals(cardInfo.getIsActivation())){
//                result.setMsg("充值卡（"+memberInfoReq.getCardNo()+"）已经激活，同一张卡不能重复激活");
                result.setMsg("Thẻ nạp（"+memberInfoReq.getCardNo()+"）đã được kích hoạt, không thể kích hoạt nhiều lần");
                return result;
            }
            if("1".equals(cardInfo.getIsHandle())){
//                result.setMsg("充值卡（"+memberInfoReq.getCardNo()+"）已经处理，同一张卡不能重复激活");
                result.setMsg("Thẻ nạp（"+memberInfoReq.getCardNo()+"）đã được xử lý, không thể kích hoạt nhiều lần");
                return result;
            }

        if(null!=cardInfo.getEnd_effectiove_time()&&!"".equals(cardInfo.getEnd_effectiove_time())){
            Date newDate = DateUtils.parseDate(DateUtils.formatDate(new Date(),DateUtils.FORMAT_YYYY_MM_DD),DateUtils.FORMAT_YYYY_MM_DD);
            Date effectiove_time = DateUtils.parseDate(cardInfo.getEnd_effectiove_time(),DateUtils.FORMAT_YYYY_MM_DD);
            if(newDate.after(effectiove_time)){//当前时间大于有效截止时间
                //                result.setMsg("充值卡（"+PhoneAcctReq.getCardno()+"）已过有效期");
                result.setMsg("Thẻ nạp tiền（"+memberInfoReq.getCardNo()+"）đã hết hạn:"+cardInfo.getEnd_effectiove_time());
                return result;
            }
        }
//==================================>调用第3方
//        DepositReq depositReq = new DepositReq();
//        depositReq.setAccount(memberInfoReq.getActivationAcct());
//        depositReq.setAmount(BigDecimal.valueOf(cardInfo.getCardAmount()).add(BigDecimal.valueOf(cardInfo.getAdditionalAmount())));
//        AuditReq auditReq = new AuditReq();
//        auditReq.setType(3);
//        auditReq.setAmount(BigDecimal.valueOf(cardInfo.getCardAmount()).add(BigDecimal.valueOf(cardInfo.getAdditionalAmount())));
//        depositReq.setAudit(auditReq);
//        depositReq.setIsReal(true);
//        depositReq.setMemo("100K Trải nghiệm miễn phí");
//        depositReq.setPortalMemo("100K Trải nghiệm miễn phí");
//        depositReq.setType(5);
//        log.error("调用api传参数==========》》》{}", JSONObject.toJSONString(depositReq));
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
                    cardInfo.setIsActivation("1");//0否   1激活
                    cardInfo.setActivationAcct(memberInfoReq.getActivationAcct());//激活账号
                    cardInfo.setUpdate_time(DateUtils.getDateTime());
                    cardInfoMapper.updateCardInfo(cardInfo);
                    MemberInfo memberInfo = new MemberInfo();
                    memberInfo.setActivationAcct(memberInfoReq.getActivationAcct());//激活账号
                    memberInfo.setIpAddress(memberInfoReq.getIpAddress());//IP地址
                    memberInfo.setDeviceInfo(memberInfoReq.getDeviceInfo());//设备信息
                    memberInfo.setCardNoPrefix(cardInfo.getCardNoPrefix());//前缀
                    memberInfo.setCardNo(memberInfoReq.getCardNo());//卡号=前缀+序号
                    memberInfo.setCardPwd(memberInfoReq.getCardPwd());
                    memberInfo.setIs_delete("0");//0否   1删除
                    memberInfo.setIsHandle("0");//0否   1处理
                    memberInfo.setCardAmount(cardInfo.getCardAmount());//卡面金额
                    memberInfo.setAdditionalAmount(cardInfo.getAdditionalAmount());//增送金额
                    memberInfo.setCreate_time(DateUtils.getDateTime());
                    memberInfo.setPhoneno(memberInfoReq.getPhoneno());
                    memberInfo.setStart_effectiove_time(cardInfo.getStart_effectiove_time());
                    memberInfo.setEnd_effectiove_time(cardInfo.getEnd_effectiove_time());
                    memberInfoMapper.insertMemberInfo(memberInfo);
//            result.setMsg("充值卡（"+memberInfoReq.getCardNo()+"）激活成功，充值金额（"+cardInfo.getCardAmount()+"），增送金额（"+cardInfo.getAdditionalAmount()+"）");
                    result.setMsg("Thẻ nạp（"+memberInfoReq.getCardNo()+"）kích hoạt thành công, số tiền nạp（"+cardInfo.getCardAmount()+"），số tiền thưởng（"+cardInfo.getAdditionalAmount()+"）");
                    result.setSuccess(true);
                    return result;
//                }
//
//            }else {//调用api出现异常
//                result.setMsg("调用第3方服务异常，请到第3方平台核实确认。");
//                result.setMsg("Ngoại lệ khi gọi dịch vụ của bên thứ ba，Vui lòng truy cập nền tảng của bên thứ ba để xác minh và xác nhận");
//            }
//        } catch (Exception e) {
//            result.setMsg("调用第3方服务异常，请到第3方平台核实确认。");
//            result.setMsg("Ngoại lệ khi gọi dịch vụ của bên thứ ba，Vui lòng truy cập nền tảng của bên thứ ba để xác minh và xác nhận");
//        }
//        return result;
    }
    public Result selectActivationCard(MemberInfoReq memberInfoReq, PageRequest pageRequest){
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        try {
            String sql = "";
            if(null!=memberInfoReq.getStartingTime()&&!"".equals(memberInfoReq.getStartingTime())){
                sql += " and date_format(create_time,'%Y-%m-%d')>='"+DateUtils.formatDate(DateUtils.parseDate(memberInfoReq.getStartingTime(),DateUtils.FORMAT_YYYY_MM_DD),DateUtils.FORMAT_YYYY_MM_DD)+"'";
            }
            if(null!=memberInfoReq.getEndTime()&&!"".equals(memberInfoReq.getEndTime())){
                sql += " and date_format(create_time,'%Y-%m-%d')<='"+DateUtils.formatDate(DateUtils.parseDate(memberInfoReq.getEndTime(),DateUtils.FORMAT_YYYY_MM_DD),DateUtils.FORMAT_YYYY_MM_DD)+"'";
            }
            List<MemberInfo> memberInfoList = memberInfoMapper.selectMemberInfo(memberInfoReq.getActivationAcct(),memberInfoReq.getCardNoPrefix(),memberInfoReq.getIpAddress(),memberInfoReq.getCardNo(),memberInfoReq.getIsHandle(),sql);
            List<MemberInfo> memberInfoList1 = new ArrayList<MemberInfo>();
            if(memberInfoList != null && memberInfoList.size()>0){
                //3.2计算startIndex
                int startIndex=(pageRequest.getPageNum()-1)*pageRequest.getPageSize();
                memberInfoList1 = memberInfoMapper.selectPageMemberInfo(memberInfoReq.getActivationAcct(),memberInfoReq.getCardNoPrefix(),memberInfoReq.getIpAddress(),memberInfoReq.getCardNo(),memberInfoReq.getIsHandle(),startIndex,pageRequest.getPageSize(),sql);
                result.setPageNum(pageRequest.getPageNum());//当前页码
                result.setPageSize(pageRequest.getPageSize());//每页数量
                result.setTotalSize(memberInfoList.size());//记录总数
                result.setTotalPages((memberInfoList.size()-1)/pageRequest.getPageSize()+1);//页码总数
            }
            result.setMsg("查询激活成功");
            result.setSuccess(true);
            result.setDetail(memberInfoList1);
        } catch (Exception e) {
            result.setMsg("查询激活失败");
            e.printStackTrace();
        }
        return result;
    }

    public List<MemberInfo> selectActivationCardexp(MemberInfoExpReq memberInfoReq){

        try {
            String sql = "";
            if(null!=memberInfoReq.getStartingTime()&&!"".equals(memberInfoReq.getStartingTime())){
                sql += " and date_format(create_time,'%Y-%m-%d')>='"+DateUtils.formatDate(DateUtils.parseDate(memberInfoReq.getStartingTime(),DateUtils.FORMAT_YYYY_MM_DD),DateUtils.FORMAT_YYYY_MM_DD)+"'";
            }
            if(null!=memberInfoReq.getEndTime()&&!"".equals(memberInfoReq.getEndTime())){
                sql += " and date_format(create_time,'%Y-%m-%d')<='"+DateUtils.formatDate(DateUtils.parseDate(memberInfoReq.getEndTime(),DateUtils.FORMAT_YYYY_MM_DD),DateUtils.FORMAT_YYYY_MM_DD)+"'";
            }
            return memberInfoMapper.selectMemberInfo(memberInfoReq.getActivationAcct(),memberInfoReq.getCardNoPrefix(),memberInfoReq.getIpAddress(),memberInfoReq.getCardNo(),memberInfoReq.getIsHandle(),sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result isHandleCard(MemberInfoReq memberInfoReq, User user){
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        if(null==memberInfoReq.getCardNo()||"".equals(memberInfoReq.getCardNo())){
            result.setMsg("充值卡卡号不能为空");
            return result;
        }
        List<MemberInfo> list = memberInfoMapper.selectMemberInfoByCardNo(memberInfoReq.getCardNo());
        if(null!=list && list.size()>0) {
            for (int i=0;i<list.size();i++) {
                MemberInfo memberInfo = list.get(i);
                if (i==list.size()-1) {
                    String isActivation;//0否   1激活
                    String isHandle;
                    if ("2".equals(memberInfoReq.getIsHandle())) {//拒绝
                        isActivation = "2";
                        isHandle = "2";
                        memberInfoReq.setIsHandle("2");
                        memberInfoReq.setIs_delete("1");
                        cardInfoMapper.updateIsHandleByCardNo(memberInfoReq.getIsHandle(), isActivation, DateUtils.getDateTime(), memberInfoReq.getCardNo());
                        memberInfoMapper.updateIsHandleByCardNo(isHandle, memberInfoReq.getIs_delete(), memberInfoReq.getRemark(), DateUtils.getDateTime(), user.getId(), user.getUsername(), memberInfo.getId());
                        result.setMsg("拒绝成功");
                        result.setSuccess(true);
                    } else {
                        DepositReq depositReq = new DepositReq();
                        depositReq.setAccount(memberInfo.getActivationAcct());
                        depositReq.setAmount(BigDecimal.valueOf(memberInfo.getCardAmount()).add(BigDecimal.valueOf(memberInfo.getAdditionalAmount())));
                        AuditReq auditReq = new AuditReq();
                        auditReq.setType(3);
                        auditReq.setAmount(BigDecimal.valueOf(memberInfo.getCardAmount()).add(BigDecimal.valueOf(memberInfo.getAdditionalAmount())));
                        depositReq.setAudit(auditReq);
                        depositReq.setIsReal(true);
                        depositReq.setMemo("100K Trải nghiệm miễn phí");
                        depositReq.setPortalMemo("100K Trải nghiệm miễn phí");
                        depositReq.setType(5);
                        log.error("调用api传参数==========》》》{}", JSONObject.toJSONString(depositReq));
                        try {
                            String resultString = HttpUtil.doProxyPostJson("https://ela.api.jdtmb.com/api/1.0/member/deposit",JSONObject.toJSONString(depositReq),"X-ApiKey","8d81f957");
                            log.error("收到第3方返回参数==========《《《《《《{}",resultString);
                            if(null!=resultString&&!"".equals(resultString)){
                                JSONObject apiResponseData = JSONObject.parseObject(resultString);
                                Integer code = apiResponseData.getInteger("Code");
                                if(code!=200){
                                    String message = apiResponseData.getString("Message");
                                    memberInfo.setRemark(null!=memberInfo.getRemark()?memberInfo.getRemark():""+"\\r\\n"+"Code:"+"\\r\\n"+"Message:"+null!=message?message:"");
                                    isActivation = "0";
                                    memberInfoReq.setIsHandle("0");
                                    memberInfoReq.setIs_delete("0");
                                    if(code==5000||code==4000){//系统错误
                                        cardInfoMapper.updateIsHandleByCardNo(memberInfoReq.getIsHandle(), isActivation, DateUtils.getDateTime(), memberInfoReq.getCardNo());
                                        memberInfoMapper.deleteMemberInfo(memberInfo.getId());
                                    }
                                    result.setMsg("Code:"+code + " "+"Message:"+null!=message?message:"");
                                    return result;
                                }else {
                                    isHandle = "1";
                                    memberInfoReq.setIsHandle("1");
                                    isActivation = "1";
                                    memberInfoReq.setIs_delete("0");

                                }
                                cardInfoMapper.updateIsHandleByCardNo(memberInfoReq.getIsHandle(), isActivation, DateUtils.getDateTime(), memberInfoReq.getCardNo());
                                memberInfoMapper.updateIsHandleByCardNo(isHandle, memberInfoReq.getIs_delete(), memberInfoReq.getRemark(), DateUtils.getDateTime(), user.getId(), user.getUsername(), memberInfo.getId());
                                result.setMsg("处理成功");
                                result.setSuccess(true);

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
                }else {
                    memberInfoMapper.deleteMemberInfo(memberInfo.getId());
                }
            }
        }else {
            result.setMsg("充值卡卡号(" + memberInfoReq.getCardNo() + ")未激活");
            return result;
        }
        return result;
    }

    public Result selectStatiActivationCard(MemberInfoReq memberInfoReq, PageRequest pageRequest){
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        try {
            String sql = "";
            if(null!=memberInfoReq.getStartingTime()&&!"".equals(memberInfoReq.getStartingTime())){
                sql += " and date_format(create_time,'%Y-%m-%d')>='"+DateUtils.formatDate(DateUtils.parseDate(memberInfoReq.getStartingTime(),DateUtils.FORMAT_YYYY_MM_DD),DateUtils.FORMAT_YYYY_MM_DD)+"'";
            }
            if(null!=memberInfoReq.getEndTime()&&!"".equals(memberInfoReq.getEndTime())){
                sql += " and date_format(create_time,'%Y-%m-%d')<='"+DateUtils.formatDate(DateUtils.parseDate(memberInfoReq.getEndTime(),DateUtils.FORMAT_YYYY_MM_DD),DateUtils.FORMAT_YYYY_MM_DD)+"'";
            }
            List<MemberInfoVo> memberInfovoList = memberInfoMapper.selectStatiActivationCard(memberInfoReq.getCardNoPrefix(),sql);
            List<MemberInfoVo> memberInfovoList1 = new ArrayList<MemberInfoVo>();
            if(memberInfovoList != null && memberInfovoList.size()>0){
                //3.2计算startIndex
                int startIndex=(pageRequest.getPageNum()-1)*pageRequest.getPageSize();
                memberInfovoList1 = memberInfoMapper.selectPageStatiActivationCard(memberInfoReq.getCardNoPrefix(),sql,startIndex,pageRequest.getPageSize());
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
