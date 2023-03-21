package com.alibaba.utils;

import com.alibaba.bean.entity.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

public class UserContext {
    private static final  String user_session = "logininfo";

    private static HttpSession getSession(){
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
    }
    public static void putCurrebtUser(User user){
        getSession().setAttribute(user_session,user);
    }
    public static User getCurrebtUser(){
        return (User)getSession().getAttribute(user_session);
    }
}
