package com.alibaba.controller;

import com.alibaba.bean.Result;
import com.alibaba.bean.entity.User;
import com.alibaba.bean.req.CardInfoAddReq;
import com.alibaba.bean.req.CardInfoReq;
import com.alibaba.service.ICardInfoService;
import com.alibaba.service.IExportService;
import com.alibaba.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/card")
@Api(tags = "成卡密")
public class CardController {
    @Autowired
    private ICardInfoService iCardInfoService;
    @Autowired
    private IExportService iExportService;

    /**
     * 批量生成卡密
     * @param cardInfoReq
     * @return
     */
    @PostMapping(value = "/addCardInfo")
    @ApiOperation("批量生成卡密")
    public Result addCardInfo(CardInfoAddReq cardInfoReq){
//        CardInfoReq cardInfoReq1 = new CardInfoReq();
//        cardInfoReq1.setSerialLength(0);//序号长度
//        cardInfoReq1.setTotal(100000);//生成条数
//        String[] pstr = {"1"};
//        cardInfoReq1.setPwdLetterNumber(pstr);//密码生成规则    0：字母 1：数字（字母数字都传表示数字与字母组合）
//        cardInfoReq1.setPwdLength(6);//密码长度
//        String[] lstr = {"1"};
//        cardInfoReq1.setLetterNumber(lstr);//卡号生成规则   0：字母 1：数字（字母数字都传表示数字与字母组合）
//        cardInfoReq1.setLetterNumberLength(6);//卡号随机数长度
        User user = UserContext.getCurrebtUser();
        if(null!=user){
            cardInfoReq.setUserId(user.getId());//登录ID
        }else {
            Result result = new Result();
            result.setSuccess(false);
            result.setMsg("用户登录已失效请重新登录");
            return result;
        }

//        cardInfoReq1.setCardNoPrefix("z");//前缀
//        cardInfoReq1.setCardAmount(10D);//卡面金额
//        cardInfoReq1.setAdditionalAmount(0D);//增送金额
//        cardInfoReq1.setUserId(1L);//登录ID
//        return iCardInfoService.insertCardInfoBatch(cardInfoReq1);
        return iCardInfoService.insertCardInfoBatch(cardInfoReq);
    }

    /**
     * 查询激活卡片，语音提醒
     * @return
     */
    @GetMapping(value = "/queryCardActi")
    @ApiOperation("查询激活卡片，语音提醒")
    public Result queryCardInfo(){

        return iCardInfoService.selectCardInfoByisActivation();
    }

    /**
     * 查询所有前缀
     * @return
     */
    @GetMapping(value = "/queryCardNoPrefix")
    @ApiOperation("查询所有前缀")
    public Result queryCardNoPrefix(){

        return iCardInfoService.selectCardNoPrefix();
    }

    /**
     * 导出卡号信息
     * @param response
     * @param cardInfoReq
     * @return
     */
    @RequestMapping(value = "/download",method = RequestMethod.POST)
    @ApiOperation("导出卡号信息")
    @ResponseBody
    public Result cardInfoDownload(HttpServletResponse response,CardInfoReq cardInfoReq){
        User user = UserContext.getCurrebtUser();
        if(null==user) {
            Result result = new Result();
            result.setSuccess(false);
            result.setMsg("用户登录已失效请重新登录");
            return result;
        }
        return iExportService.exportCardInfo(response,cardInfoReq.getCardNoPrefix());
    }
}
