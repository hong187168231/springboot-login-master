package com.alibaba.mapper;

import com.alibaba.bean.entity.MemberInfo;
import com.alibaba.bean.vo.MemberInfoVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MemberInfoMapper {
    @Insert("insert into memberInfo values(#{id},#{activationAcct},#{ipAddress},#{deviceInfo},#{cardNo},#{is_delete},#{create_time},#{update_time},#{cardAmount},#{additionalAmount},#{isHandle},#{userid},#{username},#{cardPwd},#{cardNoPrefix},#{remark},#{phoneno},#{start_effectiove_time},#{end_effectiove_time})")
    //加入该注解可以保存对象后，查看对象插入id
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void insertMemberInfo(MemberInfo memberInfo);

    @Select("select m.id,m.activationAcct,m.ipAddress,m.deviceInfo,m.cardNo,m.create_time,m.update_time,m.cardAmount,m.additionalAmount,m.isHandle from memberInfo m where m.ipAddress = #{ipAddress} and m.is_delete != '1'")
    @Results
            ({@Result(property = "id",column = "id"),
                    @Result(property = "activationAcct",column = "activationAcct"),
                    @Result(property = "ipAddress",column = "ipAddress"),
                    @Result(property = "deviceInfo",column = "deviceInfo"),
                    @Result(property = "cardNo",column = "cardNo"),
                    @Result(property = "create_time",column = "create_time"),
                    @Result(property = "update_time",column = "update_time"),
                    @Result(property = "cardAmount",column = "cardAmount"),
                    @Result(property = "isHandle",column = "isHandle"),
                    @Result(property = "additionalAmount",column = "additionalAmount")})
    List<MemberInfo> selectMemberInfoByIp(@Param(value="ipAddress")String ipAddress);

    @Select("select m.id,m.activationAcct,m.ipAddress,m.deviceInfo,m.cardNo,m.create_time,m.update_time,m.cardAmount,m.additionalAmount,m.isHandle from memberInfo m " +
            "where m.activationAcct = #{activationAcct} and m.is_delete != '1'")
    @Results
            ({@Result(property = "id",column = "id"),
                    @Result(property = "activationAcct",column = "activationAcct"),
                    @Result(property = "ipAddress",column = "ipAddress"),
                    @Result(property = "deviceInfo",column = "deviceInfo"),
                    @Result(property = "cardNo",column = "cardNo"),
                    @Result(property = "create_time",column = "create_time"),
                    @Result(property = "update_time",column = "update_time"),
                    @Result(property = "cardAmount",column = "cardAmount"),
                    @Result(property = "isHandle",column = "isHandle"),
                    @Result(property = "additionalAmount",column = "additionalAmount")})
    List<MemberInfo> selectMemberInfoByActivationAcct(@Param(value="activationAcct")String activationAcct);

    @Select("select m.id,m.activationAcct,m.ipAddress,m.deviceInfo,m.cardNo,m.create_time,m.update_time,m.cardAmount,m.additionalAmount,m.isHandle from memberInfo m " +
            "where m.activationAcct = #{activationAcct} and TO_DAYS(m.create_time)=TO_DAYS(NOW()) and m.is_delete != '1'")
    @Results
            ({@Result(property = "id",column = "id"),
                    @Result(property = "activationAcct",column = "activationAcct"),
                    @Result(property = "ipAddress",column = "ipAddress"),
                    @Result(property = "deviceInfo",column = "deviceInfo"),
                    @Result(property = "cardNo",column = "cardNo"),
                    @Result(property = "create_time",column = "create_time"),
                    @Result(property = "update_time",column = "update_time"),
                    @Result(property = "cardAmount",column = "cardAmount"),
                    @Result(property = "isHandle",column = "isHandle"),
                    @Result(property = "additionalAmount",column = "additionalAmount")})
    List<MemberInfo> selectMemberInfoByActivationAcctToDays(@Param(value="activationAcct")String activationAcct);

    @Select("<script>"+
            "select id,activationAcct,cardNoPrefix,ipAddress,deviceInfo,cardNo,create_time,update_time,cardAmount,additionalAmount,isHandle,userid,username,cardPwd,phoneno,start_effectiove_time,end_effectiove_time,remark from memberInfo " +
            "where 1=1 "
            +"<if test=\"activationAcct !=null and activationAcct !='' \"> and activationAcct = #{activationAcct} "
            +"</if>"
            +"<if test=\"cardNoPrefix !=null and cardNoPrefix !='' \"> and cardNoPrefix = #{cardNoPrefix} "
            +"</if>"
            +"<if test=\"ipAddress !=null and ipAddress !='' \"> and ipAddress = #{ipAddress} "
            +"</if>"
            +"<if test=\"cardNo !=null and cardNo !='' \"> and cardNo = #{cardNo} "
            +"</if>"
            +"<if test=\"isHandle !=null and isHandle !='' \"> and isHandle = #{isHandle} "
            +"</if>"
            +"${sql}"
            +"  order by create_time DESC "
            +"</script>"
    )
    @Results
            ({@Result(property = "id",column = "id"),
                    @Result(property = "activationAcct",column = "activationAcct"),
                    @Result(property = "cardNoPrefix",column = "cardNoPrefix"),
                    @Result(property = "ipAddress",column = "ipAddress"),
                    @Result(property = "deviceInfo",column = "deviceInfo"),
                    @Result(property = "cardNo",column = "cardNo"),
                    @Result(property = "create_time",column = "create_time"),
                    @Result(property = "update_time",column = "update_time"),
                    @Result(property = "cardAmount",column = "cardAmount"),
                    @Result(property = "isHandle",column = "isHandle"),
                    @Result(property = "additionalAmount",column = "additionalAmount"),
                    @Result(property = "userid",column = "userid"),
                    @Result(property = "username",column = "username"),
                    @Result(property = "phoneno",column = "phoneno"),
                    @Result(property = "start_effectiove_time",column = "start_effectiove_time"),
                    @Result(property = "end_effectiove_time",column = "end_effectiove_time"),
                    @Result(property = "remark",column = "remark"),
                    @Result(property = "cardPwd",column = "cardPwd")})
    List<MemberInfo> selectMemberInfo(@Param("activationAcct")String activationAcct,@Param("cardNoPrefix")String cardNoPrefix,@Param("ipAddress")String ipAddress,@Param("cardNo")String cardNo,@Param("isHandle")String isHandle,String sql);

    @Select("<script>"+
            "select id,activationAcct,cardNoPrefix,ipAddress,deviceInfo,cardNo,create_time,update_time,cardAmount,additionalAmount,isHandle,userid,username,cardPwd,remark,phoneno,start_effectiove_time,end_effectiove_time from memberInfo " +
            "where 1=1 "
            +"<if test=\"activationAcct !=null and activationAcct !='' \"> and activationAcct = #{activationAcct} "
            +"</if>"
            +"<if test=\"cardNoPrefix !=null and cardNoPrefix !='' \"> and cardNoPrefix = #{cardNoPrefix} "
            +"</if>"
            +"<if test=\"ipAddress !=null and ipAddress !=''\"> and ipAddress = #{ipAddress} "
            +"</if>"
            +"<if test=\"cardNo !=null and cardNo !='' \"> and cardNo = #{cardNo} "
            +"</if>"
            +"<if test=\"isHandle !=null and isHandle !=''\"> and isHandle = #{isHandle} "
            +"</if>"
            +"${sql}"
            +"  order by create_time DESC "
            +" limit #{startIndex},#{pageSize}"
            +"</script>"
    )
    @Results
            ({@Result(property = "id",column = "id"),
                    @Result(property = "activationAcct",column = "activationAcct"),
                    @Result(property = "cardNoPrefix",column = "cardNoPrefix"),
                    @Result(property = "ipAddress",column = "ipAddress"),
                    @Result(property = "deviceInfo",column = "deviceInfo"),
                    @Result(property = "cardNo",column = "cardNo"),
                    @Result(property = "create_time",column = "create_time"),
                    @Result(property = "update_time",column = "update_time"),
                    @Result(property = "cardAmount",column = "cardAmount"),
                    @Result(property = "isHandle",column = "isHandle"),
                    @Result(property = "additionalAmount",column = "additionalAmount"),
                    @Result(property = "userid",column = "userid"),
                    @Result(property = "username",column = "username"),
                    @Result(property = "phoneno",column = "phoneno"),
                    @Result(property = "start_effectiove_time",column = "start_effectiove_time"),
                    @Result(property = "end_effectiove_time",column = "end_effectiove_time"),
                    @Result(property = "cardPwd",column = "cardPwd"),
                    @Result(property = "remark",column = "remark")})
    List<MemberInfo> selectPageMemberInfo(@Param("activationAcct")String activationAcct,@Param("cardNoPrefix")String cardNoPrefix,@Param("ipAddress")String ipAddress,@Param("cardNo")String cardNo,@Param("isHandle")String isHandle,@Param("startIndex")Integer startIndex,
                                          @Param("pageSize")Integer pageSize,String sql);

    @Select("select m.id,m.activationAcct,m.ipAddress,m.deviceInfo,m.cardNo,m.create_time,m.update_time,m.cardAmount,m.additionalAmount,m.isHandle from memberInfo m " +
            "where m.cardNo = #{cardNo} and m.is_delete != '1' and m.isHandle='0'")
    @Results
            ({@Result(property = "id",column = "id"),
                    @Result(property = "activationAcct",column = "activationAcct"),
                    @Result(property = "ipAddress",column = "ipAddress"),
                    @Result(property = "deviceInfo",column = "deviceInfo"),
                    @Result(property = "cardNo",column = "cardNo"),
                    @Result(property = "create_time",column = "create_time"),
                    @Result(property = "update_time",column = "update_time"),
                    @Result(property = "cardAmount",column = "cardAmount"),
                    @Result(property = "isHandle",column = "isHandle"),
                    @Result(property = "additionalAmount",column = "additionalAmount")})
    List<MemberInfo> selectMemberInfoByCardNo(@Param("cardNo")String cardNo);

    @Update("update memberInfo set isHandle = #{isHandle},is_delete = #{is_delete},remark = #{remark},update_time = #{update_time},userid = #{userid},username = #{username} where id = #{id}")
    void updateIsHandleByCardNo(@Param("isHandle")String isHandle,@Param("is_delete")String is_delete,@Param("remark")String remark,@Param("update_time")String update_time,@Param("userid")Long userid,@Param("username")String username,@Param("id")Long id);

    @Update("DELETE FROM memberInfo where id = #{id}")
    void deleteMemberInfo(@Param("id")Long id);

    @Select("<script>"+
            "select cardNoPrefix as cardNoPrefix,COUNT(id) as countnumber,date_format(create_time,'%Y-%m-%d') as create_time from memberInfo " +
            "where is_delete = '0' "
            +"<if test=\"cardNoPrefix !=null and cardNoPrefix !='' \"> and cardNoPrefix = #{cardNoPrefix} "
            +"</if>"
            +"${sql}"
            +"  group by cardNoPrefix,date_format(create_time,'%Y-%m-%d') "
            +"</script>"
    )
    @Results
            ({@Result(property = "cardNoPrefix",column = "cardNoPrefix"),
                    @Result(property = "countnumber",column = "countnumber"),
                    @Result(property = "create_time",column = "create_time")})
    List<MemberInfoVo> selectStatiActivationCard(@Param("cardNoPrefix")String cardNoPrefix,String sql);

    @Select("<script>"+
            "select cardNoPrefix as cardNoPrefix,COUNT(id) as countnumber,date_format(create_time,'%Y-%m-%d') as create_time from memberInfo " +
            "where is_delete = '0' "
            +"<if test=\"cardNoPrefix !=null and cardNoPrefix !='' \"> and cardNoPrefix = #{cardNoPrefix} "
            +"</if>"
            +"${sql}"
            +"  group by cardNoPrefix,date_format(create_time,'%Y-%m-%d') "
            +" limit #{startIndex},#{pageSize}"
            +"</script>"
    )
    @Results
            ({@Result(property = "cardNoPrefix",column = "cardNoPrefix"),
                    @Result(property = "countnumber",column = "countnumber"),
                    @Result(property = "create_time",column = "create_time")})
    List<MemberInfoVo> selectPageStatiActivationCard(@Param("cardNoPrefix")String cardNoPrefix,String sql,@Param("startIndex")Integer startIndex,
                                                     @Param("pageSize")Integer pageSize);
}
