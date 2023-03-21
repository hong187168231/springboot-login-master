package com.alibaba.service.impl;

import com.alibaba.bean.entity.CardInfo;
import com.alibaba.bean.Result;
import com.alibaba.bean.req.CardInfoAddReq;
import com.alibaba.bean.req.CardInfoReq;
import com.alibaba.mapper.CardInfoMapper;
import com.alibaba.service.ICardInfoService;
import com.alibaba.utils.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;
import java.util.*;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class CardInfoServiceImpl implements ICardInfoService {
    @Autowired
    private CardInfoMapper cardInfoMapper;

    /**
     * 批量新增卡
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result insertCardInfoBatch(CardInfoAddReq cardInfoReq){
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);

            Long serialNumber = 0l;
            if(cardInfoReq.getCardNoSerial()!=null&&!"".equals(cardInfoReq.getCardNoSerial())){
                serialNumber = Long.valueOf(cardInfoReq.getCardNoSerial());
            }
            int total = cardInfoReq.getTotal();
            String msg = this.verifyTotal(cardInfoReq);
            if(null!=msg&&!"".equals(msg)){
                result.setMsg(msg);
                return result;
            }
            CardInfo maxCardInfo = cardInfoMapper.selectMaxId(cardInfoReq.getCardNoPrefix());
            if(null!=maxCardInfo){
                if(serialNumber>0){
                    if(maxCardInfo.getCardNoPrefix()!=null&&!"".equals(maxCardInfo.getCardNoPrefix())){
                        if(maxCardInfo.getCardNoSerial()!=null&&!"".equals(maxCardInfo.getCardNoSerial())){
                            if(Long.valueOf(maxCardInfo.getCardNoSerial())>=Long.valueOf(serialNumber)){
                                result.setMsg("起始序号（"+serialNumber+"）必须大于已经存在序号（"+maxCardInfo.getCardNoSerial()+"）");
                                return result;
                            }
                        }
                    }
                }else {
                    result.setMsg("前缀已存在");
                    return result;
                }

            }
            List<CardInfo> cardInfoAllLists = new ArrayList<CardInfo>();

            int cycles = total/10000;//循环次数,最大单批新增1W笔
            int remainder = total%10000;
            if(remainder>0){
                cycles = cycles + 1;
            }
            Set<String> cardNumberSet;
            Object[] objects = new Object[0];
            if (null!=cardInfoReq.getLetterNumber()&&cardInfoReq.getLetterNumber().length>0){
                cardNumberSet = this.getCardNumber(cardInfoReq.getLetterNumber(),Integer.valueOf(total),cardInfoReq.getLetterNumberLength());
                objects = (Object[])cardNumberSet.toArray();
            }

            int l = 0;
            for (int c=0;c<cycles;c++){
                List<CardInfo> cardInfoLists = new ArrayList<CardInfo>();
                if(c==cycles-1&&remainder>0){
                    total = remainder;
                }else {
                    total = 10000;
                }
                for (int i=0;i<total;i++) {
                    CardInfo cardInfo = new CardInfo();
                    cardInfo.setUserId(1L);
                    String cardNoSerial = "";
                    if(serialNumber>0){
                        serialNumber = serialNumber + i;
                        cardNoSerial = this.getCardNoSerial(cardInfoReq.getSerialLength(), serialNumber);
                        cardInfo.setCardNoSerial(cardNoSerial);//序号
                    }
                    cardInfo.setCardNoPrefix(cardInfoReq.getCardNoPrefix());//前缀
                    String letterNumber = "";
                    if(null!=objects&&objects.length>0){
                        letterNumber = (String) objects[l];
                    }
                    cardInfo.setCardNo(cardInfoReq.getCardNoPrefix()+letterNumber+cardNoSerial);
                    l = l + 1;
                    String cardPwd = this.getCardPassword(cardInfoReq.getPwdLetterNumber(),cardInfoReq.getPwdLength());
                    cardInfo.setCardPwd(cardPwd);
                    cardInfo.setCardAmount(cardInfoReq.getCardAmount());//卡面金额
                    cardInfo.setAdditionalAmount(cardInfoReq.getAdditionalAmount());//增送金额
                    cardInfo.setIsExp("0");
                    cardInfo.setIs_delete("0");
                    cardInfo.setIsActivation("0");//0否   1激活
                    cardInfo.setIsHandle("0");//0否   1处理
                    cardInfo.setCreate_time(DateUtils.getDateTime());
                    cardInfo.setStart_effectiove_time(null!=cardInfoReq.getStart_effectiove_time()&&!"".equals(cardInfoReq.getStart_effectiove_time())?DateUtils.formatDate(DateUtils.dateStringToDate(cardInfoReq.getStart_effectiove_time()),DateUtils.FORMAT_YYYY_MM_DD):"");
                    cardInfo.setEnd_effectiove_time(null!=cardInfoReq.getEnd_effectiove_time()&&!"".equals(cardInfoReq.getEnd_effectiove_time())?DateUtils.formatDate(DateUtils.dateStringToDate(cardInfoReq.getEnd_effectiove_time()),DateUtils.FORMAT_YYYY_MM_DD):"");
                    cardInfoLists.add(cardInfo);
                }
                cardInfoAllLists.addAll(cardInfoLists);
                cardInfoMapper.insertCollectList(cardInfoLists);
            }

            result.setMsg("卡信息插入成功");
            result.setSuccess(true);
            result.setDetail(cardInfoAllLists);

        return result;
    }
    public Result selectCardInfoByisActivation(){
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);

        try {
            List<CardInfo> cardInfoLists = cardInfoMapper.selectCardInfoByisActivationAndIsHandle("0","1","1");
            result.setMsg("查询成功");
            result.setSuccess(true);
            result.setDetail(cardInfoLists);
        } catch (Exception e) {
            result.setMsg("查询失败");
            e.printStackTrace();
        }
        return result;
    }

    public Result selectCardNoPrefix(){
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);

        try {
            List<String> prefixLists = cardInfoMapper.selectCardNoPrefix();
            result.setMsg("查询前缀成功");
            result.setSuccess(true);
            result.setDetail(prefixLists);
        } catch (Exception e) {
            result.setMsg("查询前缀失败");
            e.printStackTrace();
        }
        return result;
    }

    private String getCardNoSerial(int strLength,Long number){
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMinimumIntegerDigits(strLength);
        format.setGroupingUsed(false);
        String numberStr = format.format(number);
//        System.out.println(numberStr);
        return numberStr;
    }
    private String getCardPassword(String pwdLetterNumber[],int digits){
        String str = "";
        Random random = new Random();
        if(pwdLetterNumber.length==2){//密码生成规则    0：字母 1：数字（字母数字都传表示数字与字母组合）
            char arr[] = new char[digits];
            int i = 0;
            while (i < digits) {
                char ch = (char) (int) (Math.random() * 124);
                if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' || ch >= '0' && ch <= '9') {
                    arr[i++] = ch;
                }
            }
            str = String.valueOf(arr);
        }else {
            if ("0".equals(pwdLetterNumber[0])){
                char arr[] = new char[digits];
                int i = 0;
                while (i < digits) {
                    char ch = (char) (int) (Math.random() * 124);
                    if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z') {
                        arr[i++] = ch;
                    }
                }
                str = String.valueOf(arr);
            }else {
                char arr[] = new char[digits];
                int i = 0;
                while (i < digits) {
                    char ch = (char) (int) (Math.random() * 124);
                    if (ch >= '0' && ch <= '9') {
                        arr[i++] = ch;
                    }
                }
                str = String.valueOf(arr);
            }
        }
        return  str;
    }
    private Set<String> getCardNumber(String letterNumberLength[],int total,int digits){

        Set r = new LinkedHashSet(total);
        Random random = new Random();

        if(letterNumberLength.length==2){//密码生成规则    0：字母 1：数字（字母数字都传表示数字与字母组合）
            while (r.size()<total){
                char arr[] = new char[digits];
                int i = 0;
                String str = "";
                while (i < digits) {
                    char ch = (char) (int) (Math.random() * 124);
                    if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z' || ch >= '0' && ch <= '9') {
                        arr[i++] = ch;
                    }
                }
                str = String.valueOf(arr);
                r.add(str);
            }
        }else {
            if ("0".equals(letterNumberLength[0])){
                while (r.size()<total){
                    char arr[] = new char[digits];
                    int i = 0;
                    String str = "";
                    while (i < digits) {
                        char ch = (char) (int) (Math.random() * 124);
                        if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z') {
                            arr[i++] = ch;
                        }
                    }
                    str = String.valueOf(arr);
                    r.add(str);
                }
            }else {
                while (r.size()<total){
                    char arr[] = new char[digits];
                    int i = 0;
                    String str = "";
                    while (i < digits) {
                        char ch = (char) (int) (Math.random() * 124);
                        if (ch >= '0' && ch <= '9') {
                            arr[i++] = ch;
                        }
                    }
                    str = String.valueOf(arr);
                    System.out.println(str);
                    r.add(str);
                }
            }
        }
        return r;
    }
    public String verifyTotal(CardInfoAddReq cardInfoReq){
        int total = cardInfoReq.getTotal();
        if(total>100000){
            return "卡号最大生成100000条";
        }
        int letterNumberLength = cardInfoReq.getLetterNumberLength();
        if (null!=cardInfoReq.getLetterNumber()&&cardInfoReq.getLetterNumber().length>0) {
            if (1 == letterNumberLength) {
                if (total > 10) {
                    return "随机字符长度最大生成10条";
                }
            }
            if (2 == letterNumberLength) {
                if (total > 100) {
                    return "随机字符长度最大生成100条";
                }
            }
            if (3 == letterNumberLength) {
                if (total > 1000) {
                    return "随机字符长度最大生成1000条";
                }
            }
            if (4 == letterNumberLength) {
                if (total > 10000) {
                    return "随机字符长度最大生成10000条";
                }
            }
            if (5 == letterNumberLength) {
                if (total > 100000) {
                    return "随机字符长度最大生成100000条";
                }
            }
            if (6 == letterNumberLength) {
                if (total > 1000000) {
                    return "随机字符长度最大生成1000000条";
                }
            }
        }
        if(null!=cardInfoReq.getCardNoSerial()&&!"".equals(cardInfoReq.getCardNoSerial())){
            if (1 == cardInfoReq.getSerialLength()) {
                if (total > 10) {
                    return "序号长度最大生成10条";
                }
            }
            if (2 == cardInfoReq.getSerialLength()) {
                if (total > 100) {
                    return "序号长度最大生成100条";
                }
            }
            if (3 == cardInfoReq.getSerialLength()) {
                if (total > 1000) {
                    return "序号长度最大生成1000条";
                }
            }
            if (4 == cardInfoReq.getSerialLength()) {
                if (total > 10000) {
                    return "序号长度最大生成10000条";
                }
            }
            if (5 == cardInfoReq.getSerialLength()) {
                if (total > 100000) {
                    return "序号长度最大生成100000条";
                }
            }
            if (6 == cardInfoReq.getSerialLength()) {
                if (total > 1000000) {
                    return "序号长度最大生成1000000条";
                }
            }
        }
        return "";
    }
}
