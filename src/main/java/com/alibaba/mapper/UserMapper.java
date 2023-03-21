package com.alibaba.mapper;

import com.alibaba.bean.entity.Settings;
import com.alibaba.bean.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * mapper的具体表达式
 */
@Mapper //标记mapper文件位置，否则在Application.class启动类上配置mapper包扫描
@Repository
public interface UserMapper {

    /**
     * 查询用户名是否存在，若存在，不允许注册
     * 注解@Param(value) 若value与可变参数相同，注解可省略
     * 注解@Results  列名和字段名相同，注解可省略
     * @param username
     * @return
     */
    @Select(value = "select u.id,u.username,u.password,u.authority from user u where u.username=#{username}")
    @Results
            ({@Result(property = "username",column = "username"),
              @Result(property = "password",column = "password")})
    User findUserByName(@Param("username") String username);

    /**
     * 注册  插入一条user记录
     * @param user
     * @return
     */
    @Insert("insert into user values(#{id},#{username},#{password},#{authority})")
    //加入该注解可以保存对象后，查看对象插入id
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void regist(User user);

    /**
     * 登录
     * @param user
     * @return
     */
    @Select("select u.id,u.username,u.password,u.authority from user u where u.username = #{username} and password = #{password}")
    User login(User user);

    @Select("<script>"+
            "select id,username,password,authority from user  where 1=1 "
            +"<if test=\"username !=null and username != '' \"> and username = #{username} "
            +"</if>"
            +"<if test=\"authority !=null and authority != '' \"> and authority = #{authority} "
            +"</if>"
            +"</script>")
    @Results
            ({@Result(property = "id",column = "id"),
                    @Result(property = "username",column = "username"),
                    @Result(property = "password",column = "password"),
                    @Result(property = "authority",column = "authority")})
    List<User> findUser(User user);

    @Select("<script>"+
            "select id,username,password,authority from user  where 1=1 "
            +"<if test=\"username !=null and username != '' \"> and username = #{username} "
            +"</if>"
            +"<if test=\"authority !=null and authority != '' \"> and authority = #{authority} "
            +"</if>"
            +" limit #{startIndex},#{pageSize}"
            +"</script>")
    @Results
            ({@Result(property = "id",column = "id"),
                    @Result(property = "username",column = "username"),
                    @Result(property = "password",column = "password"),
                    @Result(property = "authority",column = "authority")})
    List<User> findPageUser(@Param("username")String username,
                            @Param("authority")String authority,@Param("startIndex")Integer startIndex,
                            @Param("pageSize")Integer pageSize);

    @Update("update user set username = #{username},password = #{password},authority = #{authority} where id = #{id}")
    void updateUser(User user);


}
