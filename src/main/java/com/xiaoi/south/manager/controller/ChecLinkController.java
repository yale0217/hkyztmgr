package com.xiaoi.south.manager.controller;

import com.alibaba.fastjson.JSON;
import com.xiaoi.south.manager.utlis.HttpClinetUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 标准答案超链接检查结果展示
 */
@Controller
@RequestMapping("/checkLink")
@PropertySource("classpath:/application.yml")
public class ChecLinkController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChecLinkController.class);
    @Value("${checklink.url}")
    private String url;
    @RequestMapping("/check")
    public String checkDemo() {
        return "check";
    }
    @RequestMapping("/checkSC")
    public String checkCN() {
        return "checkSC";
    }
    @RequestMapping("/checkEN")
    public String checkEN() {
        return "checkEN";
    }
    @ResponseBody
    @RequestMapping("/list")
    public String list( ModelMap modelMap,HttpServletRequest request) {
        String language = request.getParameter("language");
        HttpClinetUtils httpClinetUtils = new HttpClinetUtils();
        String rest = null;
        LOGGER.info("url："+url+"?language="+language);
        if("sc".equals(language)){
            language = "cn";
        }
        try {
            String json = httpClinetUtils.sendRequestBody(url+"?language="+language,null,"utf-8");
            if(json != null){
                List<Object>  list = JSON.parseArray(json);
                modelMap.put("code", "0");
                modelMap.put("msg", "查询成功");
                modelMap.put("count", list.size());
                modelMap.put("data", list);
                rest = JSON.toJSONString(modelMap);
            }

            LOGGER.info("list："+rest);
        }catch (Exception e){
            e.printStackTrace();
            modelMap.put("ren_code", "0");
            modelMap.put("ren_msg", "查询失败===>" + e);
            LOGGER.error("查询失败===>" + e);
            rest = JSON.toJSONString(modelMap);
        }
        return rest;
    }

}
