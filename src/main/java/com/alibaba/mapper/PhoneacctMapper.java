package com.alibaba.mapper;

import com.alibaba.bean.entity.PhoneAcct;
import com.alibaba.bean.vo.PhoneAcctVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PhoneacctMapper {
    @Insert({
            "<script>",
            "insert into phone_acct(activationAcct,ipAddress,deviceInfo,cardno,is_delete,create_time,update_time,start_effectiove_time,end_effectiove_time,cardAmount,additionalAmount,isHandle,userid,username,remark) values ",
            "<foreach collection='phoneacctLists' item='item' index='index' separator=','>",
            "(#{item.activationAcct},#{item.ipAddress},#{item.deviceInfo},#{item.cardno},#{item.is_delete},#{item.create_time},#{item.update_time},#{item.start_effectiove_time},#{item.end_effectiove_time},#{item.cardAmount},#{item.additionalAmount},#{item.isHandle},#{item.userid},#{item.username},#{item.remark})",
            "</foreach>",
            "</script>"
    })
    int insertCollectList(@Param(value="phoneacctLists") List<PhoneAcct> phoneacctLists);
    @Insert("insert into phone_acct values(#{id},#{activationAcct},#{ipAddress},#{deviceInfo},#{cardno},#{is_delete},#{create_time},#{update_time},#{start_effectiove_time},#{end_effectiove_time},#{cardAmount},#{additionalAmount},#{isHandle},#{userid},#{username},#{remark})")
    //加入该注解可以保存对象后，查看对象插入id
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void insertPhoneAcct(PhoneAcct phoneAcct);

    @Update("update phone_acct set isHandle ='3',update_time =#{update_time},update_time =#{update_time},ipAddress =#{ipAddress},deviceInfo =#{deviceInfo},activationAcct =#{activationAcct} where id = #{id}")
    void updatePhoneacctIsHandleById(@Param(value="id")Long id,@Param(value="update_time")String update_time,@Param(value="ipAddress")String ipAddress,@Param(value="deviceInfo")String deviceInfo,@Param(value="activationAcct")String activationAcct);

    @Select("select m.id,m.activationAcct,m.ipAddress,m.deviceInfo,m.cardno,m.create_time,m.update_time,m.start_effectiove_time,m.end_effectiove_time,m.cardAmount,m.additionalAmount,m.isHandle from phone_acct m " +
            "where m.ipAddress = #{ipAddress} and m.is_delete != '1'")
    @Results
            ({@Result(property = "id",column = "id"),
                    @Result(property = "activationAcct",column = "activationAcct"),
                    @Result(property = "ipAddress",column = "ipAddress"),
                    @Result(property = "deviceInfo",column = "deviceInfo"),
                    @Result(property = "cardno",column = "cardno"),
                    @Result(property = "create_time",column = "create_time"),
                    @Result(property = "update_time",column = "update_time"),
                    @Result(property = "start_effectiove_time",column = "start_effectiove_time"),
                    @Result(property = "end_effectiove_time",column = "end_effectiove_time"),
                    @Result(property = "cardAmount",column = "cardAmount"),
                    @Result(property = "isHandle",column = "isHandle"),
                    @Result(property = "additionalAmount",column = "additionalAmount")})
    List<PhoneAcct> selectPhoneacctByIp(@Param(value="ipAddress")String ipAddress);

    @Select("select m.id,m.activationAcct,m.ipAddress,m.deviceInfo,m.cardno,m.create_time,m.update_time,m.start_effectiove_time,m.end_effectiove_time,m.cardAmount,m.additionalAmount,m.isHandle from phone_acct m " +
            "where m.activationAcct = #{activationAcct} and m.is_delete != '1'")
    @Results
            ({@Result(property = "id",column = "id"),
                    @Result(property = "activationAcct",column = "activationAcct"),
                    @Result(property = "ipAddress",column = "ipAddress"),
                    @Result(property = "deviceInfo",column = "deviceInfo"),
                    @Result(property = "cardno",column = "cardno"),
                    @Result(property = "create_time",column = "create_time"),
                    @Result(property = "update_time",column = "update_time"),
                    @Result(property = "start_effectiove_time",column = "start_effectiove_time"),
                    @Result(property = "end_effectiove_time",column = "end_effectiove_time"),
                    @Result(property = "cardAmount",column = "cardAmount"),
                    @Result(property = "isHandle",column = "isHandle"),
                    @Result(property = "additionalAmount",column = "additionalAmount")})
    List<PhoneAcct> selectPhoneacctByActivationAcct(@Param(value="activationAcct")String activationAcct);

    @Select("select m.id,m.activationAcct,m.ipAddress,m.deviceInfo,m.cardno,m.create_time,m.update_time,m.start_effectiove_time,m.end_effectiove_time,m.cardAmount,m.additionalAmount,m.isHandle from phone_acct m " +
            "where m.activationAcct = #{activationAcct} and TO_DAYS(m.create_time)=TO_DAYS(NOW()) and m.is_delete != '1'")
    @Results
            ({@Result(property = "id",column = "id"),
                    @Result(property = "activationAcct",column = "activationAcct"),
                    @Result(property = "ipAddress",column = "ipAddress"),
                    @Result(property = "deviceInfo",column = "deviceInfo"),
                    @Result(property = "cardno",column = "cardno"),
                    @Result(property = "create_time",column = "create_time"),
                    @Result(property = "update_time",column = "update_time"),
                    @Result(property = "start_effectiove_time",column = "start_effectiove_time"),
                    @Result(property = "end_effectiove_time",column = "end_effectiove_time"),
                    @Result(property = "cardAmount",column = "cardAmount"),
                    @Result(property = "isHandle",column = "isHandle"),
                    @Result(property = "additionalAmount",column = "additionalAmount")})
    List<PhoneAcct> selectPhoneacctByActivationAcctToDays(@Param(value="activationAcct")String activationAcct);

    @Select("<script>"+
            "select id,activationAcct,ipAddress,deviceInfo,cardno,create_time,update_time,start_effectiove_time,end_effectiove_time,cardAmount,additionalAmount,isHandle,is_delete,userid,username,remark from phone_acct " +
            "where 1=1 "
            +"<if test=\"activationAcct !=null and activationAcct !='' \"> and activationAcct = #{activationAcct} "
            +"</if>"
            +"<if test=\"ipAddress !=null and ipAddress !=''\"> and ipAddress = #{ipAddress} "
            +"</if>"
            +"<if test=\"cardno !=null and cardno !='' \"> and cardno = #{cardno} "
            +"</if>"
            +"<if test=\"isHandle !=null and isHandle !=''\"> and isHandle = #{isHandle} "
            +"</if>"
            +"<if test=\"is_delete !=null and is_delete !='' \"> and is_delete = #{is_delete} "
            +"</if>"
            +"<if test=\"userid !=null and userid !='' \"> and userid = #{userid} "
            +"</if>"
            +"${sql}"
            +"  order by create_time DESC "
            +"</script>"
    )
    @Results
            ({@Result(property = "id",column = "id"),
                    @Result(property = "activationAcct",column = "activationAcct"),
                    @Result(property = "ipAddress",column = "ipAddress"),
                    @Result(property = "deviceInfo",column = "deviceInfo"),
                    @Result(property = "cardno",column = "cardno"),
                    @Result(property = "create_time",column = "create_time"),
                    @Result(property = "update_time",column = "update_time"),
                    @Result(property = "start_effectiove_time",column = "start_effectiove_time"),
                    @Result(property = "end_effectiove_time",column = "end_effectiove_time"),
                    @Result(property = "cardAmount",column = "cardAmount"),
                    @Result(property = "isHandle",column = "isHandle"),
                    @Result(property = "is_delete",column = "is_delete"),
                    @Result(property = "additionalAmount",column = "additionalAmount"),
                    @Result(property = "userid",column = "userid"),
                    @Result(property = "username",column = "username"),
                    @Result(property = "remark",column = "remark")})
    List<PhoneAcct> selectPhoneacct(@Param("activationAcct")String activationAcct,@Param("ipAddress")String ipAddress,@Param("cardno")String cardno,@Param("isHandle")String isHandle,@Param("is_delete")String is_delete,@Param("userid")Long userid,String sql);

    @Select("<script>"+
            "select id,activationAcct,ipAddress,deviceInfo,cardno,create_time,update_time,start_effectiove_time,end_effectiove_time,cardAmount,additionalAmount,isHandle,is_delete,userid,username,remark from phone_acct " +
            "where 1=1 "
            +"<if test=\"activationAcct !=null and activationAcct !='' \"> and activationAcct = #{activationAcct} "
            +"</if>"
            +"<if test=\"ipAddress !=null and ipAddress !=''\"> and ipAddress = #{ipAddress} "
            +"</if>"
            +"<if test=\"cardno !=null and cardno !='' \"> and cardno = #{cardno} "
            +"</if>"
            +"<if test=\"isHandle !=null and isHandle !=''\"> and isHandle = #{isHandle} "
            +"</if>"
            +"<if test=\"is_delete !=null and is_delete !='' \"> and is_delete = #{is_delete} "
            +"</if>"
            +"<if test=\"userid !=null and userid !='' \"> and userid = #{userid} "
            +"</if>"
            +"${sql}"
            +"  order by create_time DESC "
            +" limit #{startIndex},#{pageSize}"
            +"</script>"
    )
    @Results
            ({@Result(property = "id",column = "id"),
                    @Result(property = "activationAcct",column = "activationAcct"),
                    @Result(property = "ipAddress",column = "ipAddress"),
                    @Result(property = "deviceInfo",column = "deviceInfo"),
                    @Result(property = "cardno",column = "cardno"),
                    @Result(property = "create_time",column = "create_time"),
                    @Result(property = "update_time",column = "update_time"),
                    @Result(property = "start_effectiove_time",column = "start_effectiove_time"),
                    @Result(property = "end_effectiove_time",column = "end_effectiove_time"),
                    @Result(property = "cardAmount",column = "cardAmount"),
                    @Result(property = "isHandle",column = "isHandle"),
                    @Result(property = "is_delete",column = "is_delete"),
                    @Result(property = "additionalAmount",column = "additionalAmount"),
                    @Result(property = "userid",column = "userid"),
                    @Result(property = "username",column = "username"),
                    @Result(property = "remark",column = "remark")})
    List<PhoneAcct> selectPagePhoneacct(@Param("activationAcct")String activationAcct,@Param("ipAddress")String ipAddress,@Param("cardno")String cardno,@Param("isHandle")String isHandle,@Param("is_delete")String is_delete,@Param("userid")Long userid,@Param("startIndex")Integer startIndex,
                                          @Param("pageSize")Integer pageSize,String sql);

    @Select("select m.id,m.activationAcct,m.ipAddress,m.deviceInfo,m.cardno,m.create_time,m.update_time,m.start_effectiove_time,m.end_effectiove_time,m.cardAmount,m.additionalAmount,m.isHandle from phone_acct m " +
            "where m.cardno = #{cardno}")
    @Results
            ({@Result(property = "id",column = "id"),
                    @Result(property = "activationAcct",column = "activationAcct"),
                    @Result(property = "ipAddress",column = "ipAddress"),
                    @Result(property = "deviceInfo",column = "deviceInfo"),
                    @Result(property = "cardno",column = "cardno"),
                    @Result(property = "create_time",column = "create_time"),
                    @Result(property = "update_time",column = "update_time"),
                    @Result(property = "start_effectiove_time",column = "start_effectiove_time"),
                    @Result(property = "end_effectiove_time",column = "end_effectiove_time"),
                    @Result(property = "cardAmount",column = "cardAmount"),
                    @Result(property = "isHandle",column = "isHandle"),
                    @Result(property = "additionalAmount",column = "additionalAmount")})
    PhoneAcct selectPhoneacctByCardnoOne(@Param("cardno")String cardno);
    @Select("select m.id,m.activationAcct,m.ipAddress,m.deviceInfo,m.cardno,m.create_time,m.update_time,m.start_effectiove_time,m.end_effectiove_time,m.cardAmount,m.additionalAmount,m.isHandle from phone_acct m " +
            "where m.cardno = #{cardno} and m.is_delete != '1' and m.isHandle='3'")
    @Results
            ({@Result(property = "id",column = "id"),
                    @Result(property = "activationAcct",column = "activationAcct"),
                    @Result(property = "ipAddress",column = "ipAddress"),
                    @Result(property = "deviceInfo",column = "deviceInfo"),
                    @Result(property = "cardno",column = "cardno"),
                    @Result(property = "create_time",column = "create_time"),
                    @Result(property = "update_time",column = "update_time"),
                    @Result(property = "start_effectiove_time",column = "start_effectiove_time"),
                    @Result(property = "end_effectiove_time",column = "end_effectiove_time"),
                    @Result(property = "cardAmount",column = "cardAmount"),
                    @Result(property = "isHandle",column = "isHandle"),
                    @Result(property = "additionalAmount",column = "additionalAmount")})
    List<PhoneAcct> selectPhoneacctBycardno(@Param("cardno")String cardno);

    @Update("update phone_acct set isHandle = #{isHandle},remark = #{remark},update_time = #{update_time},userid = #{userid},username = #{username} where id = #{id}")
    void updateIsHandleBycardno(@Param("isHandle")String isHandle,@Param("remark")String remark,@Param("update_time")String update_time,@Param("userid")Long userid,@Param("username")String username,@Param("id")Long id);

    @Update("DELETE FROM phone_acct where id = #{id}")
    void deleteMemberInfo(@Param("id")Long id);

    @Select("<script>"+
            "select cardno as cardno,COUNT(id) as countnumber,date_format(create_time,'%Y-%m-%d') as create_time from phone_acct " +
            "where is_delete = '0' "
            +"<if test=\"cardno !=null and cardno !='' \"> and cardno = #{cardno} "
            +"</if>"
            +"${sql}"
            +"  group by cardno,date_format(create_time,'%Y-%m-%d') "
            +"</script>"
    )
    @Results
            ({@Result(property = "cardno",column = "cardno"),
                    @Result(property = "countnumber",column = "countnumber"),
                    @Result(property = "create_time",column = "create_time")})
    List<PhoneAcctVo> selectStatiActivationCard(@Param("cardno")String cardno, String sql);

    @Select("<script>"+
            "select cardno as cardno,COUNT(id) as countnumber,date_format(create_time,'%Y-%m-%d') as create_time from phone_acct " +
            "where is_delete = '0' "
            +"<if test=\"cardno !=null and cardno !='' \"> and cardno = #{cardno} "
            +"</if>"
            +"${sql}"
            +"  group by cardno,date_format(create_time,'%Y-%m-%d') "
            +" limit #{startIndex},#{pageSize}"
            +"</script>"
    )
    @Results
            ({@Result(property = "cardno",column = "cardno"),
                    @Result(property = "countnumber",column = "countnumber"),
                    @Result(property = "create_time",column = "create_time")})
    List<PhoneAcctVo> selectPageStatiActivationCard(@Param("cardno")String cardno,String sql,@Param("startIndex")Integer startIndex,
                                                     @Param("pageSize")Integer pageSize);
}
