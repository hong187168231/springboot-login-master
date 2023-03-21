package com.alibaba.mapper;

import com.alibaba.bean.entity.CardInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CardInfoMapper{

    @Insert({
            "<script>",
            "insert into cardInfo(id, userId,cardNoPrefix,cardNoSerial, cardNo,cardPwd,isExp,is_delete,create_time,update_time,cardAmount,additionalAmount,isActivation,isHandle,activationAcct,start_effectiove_time,end_effectiove_time) values ",
            "<foreach collection='cardInfoLists' item='item' index='index' separator=','>",
            "(#{item.id}, #{item.userId},#{item.cardNoPrefix},#{item.cardNoSerial}, #{item.cardNo}, #{item.cardPwd}, #{item.isExp}, #{item.is_delete}, #{item.create_time}, #{item.update_time}, #{item.cardAmount}, #{item.additionalAmount}, #{item.isActivation}, #{item.isHandle}, #{item.activationAcct}, #{item.start_effectiove_time}, #{item.end_effectiove_time})",
            "</foreach>",
            "</script>"
    })
    int insertCollectList(@Param(value="cardInfoLists") List<CardInfo> cardInfoLists);

    @Select("select c.cardNoPrefix,MAX(c.cardNoSerial) as cardNoSerial from cardInfo c where c.cardNoPrefix=#{cardNoPrefix}")
    @Results
            ({@Result(property = "cardNoPrefix",column = "cardNoPrefix"),
                    @Result(property = "cardNoSerial",column = "cardNoSerial")})
    CardInfo selectMaxId(@Param(value="cardNoPrefix")String cardNoPrefix);

    @Select("select c.id, c.userId,c.cardNoPrefix,c.cardNoSerial, c.cardNo,c.cardPwd,c.isExp,c.is_delete,c.create_time,c.update_time,c.cardAmount,c.additionalAmount,c.isActivation,c.isHandle,c.activationAcct,c.start_effectiove_time,c.end_effectiove_time from cardInfo c where c.cardNo=#{cardNo} and c.cardPwd=#{cardPwd}")
    @Results
            ({@Result(property = "id",column = "id"),
                    @Result(property = "userId",column = "userId"),
                    @Result(property = "cardNoPrefix",column = "cardNoPrefix"),
                    @Result(property = "cardNoSerial",column = "cardNoSerial"),
                    @Result(property = "cardNo",column = "cardNo"),
                    @Result(property = "cardPwd",column = "cardPwd"),
                    @Result(property = "isExp",column = "isExp"),
                    @Result(property = "is_delete",column = "is_delete"),
                    @Result(property = "create_time",column = "create_time"),
                    @Result(property = "update_time",column = "update_time"),
                    @Result(property = "start_effectiove_time",column = "start_effectiove_time"),
                    @Result(property = "end_effectiove_time",column = "end_effectiove_time"),
                    @Result(property = "cardAmount",column = "cardAmount"),
                    @Result(property = "additionalAmount",column = "additionalAmount"),
                    @Result(property = "isActivation",column = "isActivation"),
                    @Result(property = "isHandle",column = "isHandle"),
                    @Result(property = "activationAcct",column = "activationAcct")})
    CardInfo selectCardInfoByCardNoAndCardPwd(@Param(value="cardNo")String cardNo,@Param(value="cardPwd")String cardPwd);

    @Update("update CardInfo set userId = #{userId},cardNoPrefix = #{cardNoPrefix} , cardNoSerial = #{cardNoSerial}, cardNo = #{cardNo},cardPwd =#{cardPwd},isExp =#{isExp},is_delete =#{is_delete},create_time =#{create_time},update_time =#{update_time},cardAmount =#{cardAmount},additionalAmount =#{additionalAmount},isActivation =#{isActivation},isHandle =#{isHandle},activationAcct =#{activationAcct}  where id = #{id}")
    void updateCardInfo(CardInfo cardInfo);

    @Select("select c.id, c.userId,c.cardNoPrefix,c.cardNoSerial, c.cardNo,c.cardPwd,c.isExp,c.is_delete,c.create_time,c.update_time,c.cardAmount,c.additionalAmount,c.isActivation,c.isHandle,c.activationAcct from cardInfo c where c.is_delete='0' and c.isHandle=#{isHandle} and c.isActivation=#{isActivation} and c.isExp=#{isExp}")
    @Results
            ({@Result(property = "id",column = "id"),
                    @Result(property = "userId",column = "userId"),
                    @Result(property = "cardNoPrefix",column = "cardNoPrefix"),
                    @Result(property = "cardNoSerial",column = "cardNoSerial"),
                    @Result(property = "cardNo",column = "cardNo"),
                    @Result(property = "cardPwd",column = "cardPwd"),
                    @Result(property = "isExp",column = "isExp"),
                    @Result(property = "is_delete",column = "is_delete"),
                    @Result(property = "create_time",column = "create_time"),
                    @Result(property = "update_time",column = "update_time"),
                    @Result(property = "cardAmount",column = "cardAmount"),
                    @Result(property = "additionalAmount",column = "additionalAmount"),
                    @Result(property = "isActivation",column = "isActivation"),
                    @Result(property = "isHandle",column = "isHandle"),
                    @Result(property = "activationAcct",column = "activationAcct")})
    List<CardInfo> selectCardInfoByisActivationAndIsHandle(@Param(value="isHandle")String isHandle,@Param(value="isActivation")String isActivation,@Param(value="isExp")String isExp);

    @Select("select c.id, c.userId,c.cardNoPrefix,c.cardNoSerial, c.cardNo,c.cardPwd,c.isExp,c.is_delete,c.create_time,c.update_time,c.cardAmount,c.additionalAmount,c.isActivation,c.isHandle,c.activationAcct from cardInfo c where c.is_delete='0' and c.isHandle='0' and c.isActivation='0' and c.cardNoPrefix=#{cardNoPrefix}")
    @Results
            ({@Result(property = "id",column = "id"),
                    @Result(property = "userId",column = "userId"),
                    @Result(property = "cardNoPrefix",column = "cardNoPrefix"),
                    @Result(property = "cardNoSerial",column = "cardNoSerial"),
                    @Result(property = "cardNo",column = "cardNo"),
                    @Result(property = "cardPwd",column = "cardPwd"),
                    @Result(property = "isExp",column = "isExp"),
                    @Result(property = "is_delete",column = "is_delete"),
                    @Result(property = "create_time",column = "create_time"),
                    @Result(property = "update_time",column = "update_time"),
                    @Result(property = "cardAmount",column = "cardAmount"),
                    @Result(property = "additionalAmount",column = "additionalAmount"),
                    @Result(property = "isActivation",column = "isActivation"),
                    @Result(property = "isHandle",column = "isHandle"),
                    @Result(property = "activationAcct",column = "activationAcct")})
    List<CardInfo> selectCardInfoByCardNoPrefix(@Param(value="cardNoPrefix")String cardNoPrefix);

    @Update("update CardInfo set isExp ='1',update_time =#{update_time} where cardNoPrefix = #{cardNoPrefix}")
    void updateCardInfoByCardNoPrefix(@Param(value="cardNoPrefix")String cardNoPrefix,@Param(value="update_time")String update_time);

    @Update("update CardInfo set isHandle = #{isHandle},isActivation = #{isActivation} ,update_time = #{update_time} where cardNo = #{cardNo}")
    void updateIsHandleByCardNo(@Param("isHandle")String isHandle,@Param("isActivation")String isActivation,@Param("update_time")String update_time,@Param("cardNo")String cardNo);

    @Select("select c.cardNoPrefix from cardInfo c where c.is_delete='0' GROUP BY cardNoPrefix")
    List<String> selectCardNoPrefix();
}
