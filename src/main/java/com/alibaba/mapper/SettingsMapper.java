package com.alibaba.mapper;

import com.alibaba.bean.entity.Settings;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface SettingsMapper {

    @Select("select id,swhLitIpActi,limitIpActi,swhLitAcctActi,litAcctActi,swhLitAcctActiDay,litAcctActiDay from settings ")
    @Results
            ({@Result(property = "id",column = "id"),
                    @Result(property = "swhLitIpActi",column = "swhLitIpActi"),
                    @Result(property = "limitIpActi",column = "limitIpActi"),
                    @Result(property = "swhLitAcctActi",column = "swhLitAcctActi"),
                    @Result(property = "litAcctActi",column = "litAcctActi"),
                    @Result(property = "swhLitAcctActiDay",column = "swhLitAcctActiDay"),
                    @Result(property = "litAcctActiDay",column = "litAcctActiDay")})
    List<Settings> selectSettings();

    @Select("select id,swhLitIpActi,limitIpActi,swhLitAcctActi,litAcctActi,swhLitAcctActiDay,litAcctActiDay from settings where id=#{id}")
    @Results
            ({@Result(property = "id",column = "id"),
                    @Result(property = "swhLitIpActi",column = "swhLitIpActi"),
                    @Result(property = "limitIpActi",column = "limitIpActi"),
                    @Result(property = "swhLitAcctActi",column = "swhLitAcctActi"),
                    @Result(property = "litAcctActi",column = "litAcctActi"),
                    @Result(property = "swhLitAcctActiDay",column = "swhLitAcctActiDay"),
                    @Result(property = "litAcctActiDay",column = "litAcctActiDay")})
    Settings selectSettingsByid(@Param("id") String id);

    @Update("update settings set swhLitIpActi = #{swhLitIpActi},limitIpActi = #{limitIpActi} , " +
            "swhLitAcctActi = #{swhLitAcctActi}, litAcctActi = #{litAcctActi} ," +
            " swhLitAcctActiDay = #{swhLitAcctActiDay} , litAcctActiDay = #{litAcctActiDay} " +
            "where id = #{id}")
    void updateSettings(Settings settings);
}
