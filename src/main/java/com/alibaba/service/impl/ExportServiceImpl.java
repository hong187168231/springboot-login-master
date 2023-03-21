package com.alibaba.service.impl;

import com.alibaba.bean.Result;
import com.alibaba.bean.entity.CardInfo;
import com.alibaba.mapper.CardInfoMapper;
import com.alibaba.service.IExportService;
import com.alibaba.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ExportServiceImpl implements IExportService {

    @Autowired
    private CardInfoMapper cardInfoMapper;

    @Transactional(rollbackFor = Exception.class)
    public Result exportCardInfo(HttpServletResponse response,String cardNoPrefix){
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        if(null==cardNoPrefix||"".equals(cardNoPrefix)){
            result.setMsg("前缀不能为空");
            return result;
        }
        //导出txt文件
//        response.setContentType("text/plain;charset=UTF-8");

        String fileName = cardNoPrefix + DateUtils.date2Str(new Date(),DateUtils.FORMAT_YYYYMMDDHHMMSS)+".txt";
//        try {
//            fileName = URLEncoder.encode(fileName, "UTF-8");
//        } catch (Exception e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//            result.setMsg("导出文件失败");
//        }
//        response.setHeader("Content-Disposition","attachment; filename=" + fileName + ".txt");
//        BufferedOutputStream buff = null;
        BufferedWriter out = null;
        StringBuffer write = new StringBuffer();
        String enter = "\r\n";
        List<String> fineNameList = new ArrayList<>();
        fineNameList.add(fileName);
//        ServletOutputStream outSTr = null;
        try {
//            outSTr = response.getOutputStream(); // 建立
//            buff = new BufferedOutputStream(outSTr);
            out = new BufferedWriter(new FileWriter("/templates/"+fileName));
            List<CardInfo> list = cardInfoMapper.selectCardInfoByCardNoPrefix(cardNoPrefix);
            //把内容写入文件
            if(null!=list&&list.size()>0){
                for (int i = 0; i < list.size(); i++) {
                    CardInfo cardInfo = list.get(i);
                    write.append(cardInfo.getCardNo()+",");
                    write.append(cardInfo.getCardPwd());
                    write.append(enter);
                }
            }else {
                result.setMsg("导出文件失败,没有需要导出的数据");
                return result;
            }
            out.write(write.toString());

            cardInfoMapper.updateCardInfoByCardNoPrefix(cardNoPrefix, DateUtils.getDateTime());
            result.setMsg("导出文件成功");
            result.setSuccess(true);
            result.setDetail(fineNameList);
        } catch (Exception e) {
            result.setMsg("导出文件失败");
            e.printStackTrace();
        }
        finally {
            try {
                if(null!=out) {
                    out.flush();
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
