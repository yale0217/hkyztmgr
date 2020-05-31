package com.xiaoi.south.manager.controller;

import com.alibaba.fastjson.JSON;
import com.xiaoi.south.manager.service.LoginOnService;
import com.xiaoi.south.manager.utlis.HttpClinetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginUserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginUserController.class);
    @Autowired
    private LoginOnService loginOnService;
    @ResponseBody
    @CrossOrigin(allowCredentials="true",allowedHeaders="*")
    @RequestMapping(value="/loginUser")
    public String login(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap){
         String userName = request.getParameter("username");
         String password = request.getParameter("password");
         String language = request.getParameter("language");
         HttpClinetUtils httpClinetUtils = new HttpClinetUtils();
         String json = null;
         String ip = httpClinetUtils.getIpAddress(request);
         LOGGER.info("userName：{"+userName+"},password:{"+password+"}，language：{"+language+"}"+",ip:{"+ip+"}");
        int i;
         if(language.endsWith("en")){
              i = loginOnService.getCountEN(userName,password);
             System.out.println("i====="+i);
         }else if(language.endsWith("hk")){
              i = loginOnService.getCount(userName,password);

         }else{
              i = loginOnService.getCountSC(userName,password);
             System.out.println("i====="+i);
        }
        System.out.println("i====="+i);
         if(i == 1){
             request.getSession().setAttribute("session_user", userName);
             request.getSession().setMaxInactiveInterval(120*60);
             modelMap.put("code", "0");
             modelMap.put("msg", "登录成功");
             json = JSON.toJSONString(modelMap);
         }
       return json;
    }

}
