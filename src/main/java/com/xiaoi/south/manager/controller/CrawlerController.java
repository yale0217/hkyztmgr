package com.xiaoi.south.manager.controller;

import com.alibaba.fastjson.JSON;
import com.xiaoi.south.manager.utlis.CrawlerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/crawler")
@PropertySource("classpath:/application.yml")
@Slf4j
public class CrawlerController {
    @Value("${manager.url}")
    private String url;
    /**
     * 打开知识对比页面
     * @return
     */
    @RequestMapping("/crawler")
    public String checkDemo() {
        return "crawler";
    }
    @RequestMapping("/crawlerEN")
    public String crawlerEN() {
        return "crawlerEN";
    }
    @RequestMapping("/crawlerSC")
    public String crawlerSC() {
        return "crawlerSC";
    }

    /**
     * 获取知识对比结果的URL
     * @param modelMap
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/list")
    public String list(ModelMap modelMap, HttpServletRequest request) {
        String rest;
        String language = request.getParameter("language");
        CrawlerUtils crawlerUtils = new CrawlerUtils();
        List list = crawlerUtils.getCheckLink(url,language);
//        List list = crawlerUtils.getCheckLink(url);
        try {
            modelMap.put("code", "0");
            modelMap.put("msg", "查询成功");
            modelMap.put("count", list.size());
            modelMap.put("data", list);
            rest = JSON.toJSONString(modelMap);
            log.info("list："+rest);
        }catch (Exception e){
            e.printStackTrace();
            modelMap.put("ren_code", "0");
            modelMap.put("ren_msg", "查询失败===>" + e);
            log.error("查询失败===>" + e);
            rest = JSON.toJSONString(modelMap);
        }
        return rest;
    }
}
