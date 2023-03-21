package com.alibaba.service.impl;

import com.alibaba.bean.PageRequest;
import com.alibaba.bean.Result;
import com.alibaba.bean.entity.User;
import com.alibaba.bean.req.UserReq;
import com.alibaba.mapper.UserMapper;
import com.alibaba.service.IUserService;
import com.alibaba.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;
    /**
     * 注册
     * @param userReq 参数封装
     * @return Result
     */
    @Transactional(rollbackFor = Exception.class)
    public Result regist(UserReq userReq) {
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);

            if(null==userReq.getUsername()||"".equals(userReq.getUsername())){
                result.setMsg("用户名不能为空");
                return result;
            }
            if(null==userReq.getPassword()||"".equals(userReq.getPassword())){
                result.setMsg("密码不能为空");
                return result;
            }
            if(null==userReq.getAuthority()||"".equals(userReq.getAuthority())){
                result.setMsg("用户类型不能为空");
                return result;
            }
            User existUser = userMapper.findUserByName(userReq.getUsername());
            if(existUser != null){
                //如果用户名已存在
                result.setMsg("用户名已存在");

            }else{
                User user = new User();
                user.setUsername(userReq.getUsername());
                user.setPassword(userReq.getPassword());
                user.setAuthority(userReq.getAuthority());
                userMapper.regist(user);
                //System.out.println(user.getId());
                result.setMsg("注册成功");
                result.setSuccess(true);
//                result.setDetail(user);
            }
        return result;
    }
    /**
     * 登录
     * @param userReq 用户名和密码
     * @return Result
     */
    public Result login(UserReq userReq) {
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        try {
            User user = new User();
            if(null==userReq.getUsername()||"".equals(userReq.getUsername())){
                result.setMsg("用户名不能为空");
                return result;
            }
            if(null==userReq.getPassword()||"".equals(userReq.getPassword())){
                result.setMsg("密码不能为空");
                return result;
            }
            user.setUsername(userReq.getUsername());
            user.setPassword(userReq.getPassword());
            User  user1 = userMapper.login(user);
            if(user1 == null){
                result.setMsg("用户名或密码错误");
            }else{
                UserContext.putCurrebtUser(user1);
                result.setMsg("登录成功");
                result.setSuccess(true);
                List<User> userList = new ArrayList<User>();
                userList.add(user1);
                result.setDetail(userList);
            }
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public Result queryUser(UserReq userReq, PageRequest pageRequest) {
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);
        try {
            User user = new User();
            user.setUsername(userReq.getUsername());
            user.setAuthority(userReq.getAuthority());
            List<User> userList = userMapper.findUser(user);
            List<User> userList1 = new ArrayList<User>();
            if(userList != null && userList.size()>0){
                //3.2计算startIndex
                int startIndex=(pageRequest.getPageNum()-1)*pageRequest.getPageSize();
                userList1 = userMapper.findPageUser(user.getUsername(),user.getAuthority(),startIndex,pageRequest.getPageSize());
                result.setPageNum(pageRequest.getPageNum());//当前页码
                result.setPageSize(pageRequest.getPageSize());//每页数量
                result.setTotalSize(userList.size());//记录总数
                result.setTotalPages((userList.size()-1)/pageRequest.getPageSize()+1);//页码总数
            }
            result.setMsg("查询用户成功");
            result.setSuccess(true);
            result.setDetail(userList1);
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public Result modifyUser(UserReq userReq) {
        Result result = new Result();
        result.setSuccess(false);
        result.setDetail(null);

            User user = new User();
            user.setId(userReq.getId());
            user.setUsername(userReq.getUsername());
            user.setPassword(userReq.getPassword());
            user.setAuthority(userReq.getAuthority());
            userMapper.updateUser(user);
            result.setMsg("修改用户成功");
            result.setSuccess(true);

        return result;
    }
}
