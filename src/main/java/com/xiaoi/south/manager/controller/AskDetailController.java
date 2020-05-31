package com.xiaoi.south.manager.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.xiaoi.south.manager.entity.AskDetail;
import com.xiaoi.south.manager.entity.Click;
import com.xiaoi.south.manager.entity.FaqVote;
import com.xiaoi.south.manager.service.AskDetailService;
import com.xiaoi.south.manager.service.ClickService;
import com.xiaoi.south.manager.service.FaqVoteService;
import com.xiaoi.south.manager.utlis.DataUtils;
import com.xiaoi.south.manager.utlis.HttpClinetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 *处理敏感信息
 */
@Controller
@RequestMapping("/sensitive")
public class AskDetailController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AskDetailController.class);
    private static String DB_NAME_PREFIX = "om_log_ask_detail_";
    @Autowired
    protected AskDetailService askDetailService;
    @Autowired
    protected FaqVoteService faqVoteService;
    @Autowired
    protected ClickService clickService;
    //加载页面
    @RequestMapping("/index")
    public String askdetail() {
        return "askdetail";
    }
    @RequestMapping("/indexSC")
    public String askdetailSC() {
        return "askdetailSC";
    }
    @RequestMapping("/indexEN")
    public String askdetailEN() {
        return "askdetailEN";
    }
    //获取数据
    @ResponseBody
    @RequestMapping("/list")
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        String json = null;
        String starttime = request.getParameter("starttime");
        String endtime = request.getParameter("endtime");
        String user_question = request.getParameter("user_question");
        String selectType = request.getParameter("selectType");
        String platform = request.getParameter("platform");
        String language = request.getParameter("language");
        int page = 1;
        int limit = 10;
        if(request.getParameter("page") != null){
             page = Integer.parseInt(request.getParameter("page"));
        }
        if(request.getParameter("limit") != null){
            limit = Integer.parseInt(request.getParameter("limit"));
        }
        DataUtils dataUtils = new DataUtils();
        if(starttime == null){
            starttime  = dataUtils.getFirstDay();
        }

        try {
            if("1".equals(selectType)){//获取评价报表数据
                PageInfo<FaqVote> FaqVoteList ;
                if("sc".equals(language)){
                    FaqVoteList = faqVoteService.getFaqVoteListSC(starttime,endtime,user_question,null,null,null, page,  limit);
                }else if("en".equals(language)){
                    FaqVoteList = faqVoteService.getFaqVoteListEN(starttime,endtime,user_question,null,null,null, page,  limit);
                }else{
                    FaqVoteList = faqVoteService.getFaqVoteList(starttime,endtime,user_question,null,null,null, page,  limit);
                }
                for (int i = 0; i < FaqVoteList.getList().size(); i++) {//组装序号
                    FaqVoteList.getList().get(i).setNumber((1+i+((page-1)*limit)));
                }
                modelMap.put("count", FaqVoteList.getTotal());
                modelMap.put("data", FaqVoteList.getList());
            }else if("2".equals(selectType)){//获取点击报表数据
                PageInfo<Click> clicksList;
                if("sc".equals(language)){
                    clicksList = clickService.getClickListSC(starttime,endtime,user_question,null,null,null,null,null,null,page,limit);
                }else if("en".equals(language)){
                    clicksList = clickService.getClickListEN(starttime,endtime,user_question,null,null,null,null,null,null,page,limit);
                }else{
                    clicksList = clickService.getClickList(starttime,endtime,user_question,null,null,null,null,null,null,page,limit);
                }
                for (int i = 0; i < clicksList.getList().size(); i++) {//组装序号
                    clicksList.getList().get(i).setNumber((1+i+((page-1)*limit)));
                }
                modelMap.put("count", clicksList.getTotal());
                modelMap.put("data", clicksList.getList());
            }else{//获取自动问答明细报表数据
                //获取数据库自动问答明细表
                String tableName = DB_NAME_PREFIX + starttime.split(" ")[0].substring(0,7).replaceAll("-","");
                PageInfo<AskDetail> askDetail ;
                if("sc".equals(language)){
                    askDetail = askDetailService.getAskDetailsSC(starttime,endtime,user_question,platform,tableName,page,limit);
                }else if("en".equals(language)){
                    askDetail = askDetailService.getAskDetailsEN(starttime,endtime,user_question,platform,tableName,page,limit);
                }else{
                    askDetail = askDetailService.getAskDetails(starttime,endtime,user_question,platform,tableName,page,limit);
                }
                for (int i = 0; i < askDetail.getList().size(); i++) {//组装序号
                    askDetail.getList().get(i).setNumeber((1+i+((page-1)*limit)));
                }
                modelMap.put("count", askDetail.getTotal());
                modelMap.put("data", askDetail.getList());
            }

            modelMap.put("code", "0");
            modelMap.put("msg", "查询成功");
            json = JSON.toJSONString(modelMap);
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("ren_code", "110");
            modelMap.put("ren_msg", "查询失败===>" + e);
            LOGGER.error("查询失败===>" + e);
            json = JSON.toJSONString(modelMap);
        }
        return json;
    }
    @ResponseBody
    @RequestMapping("/update")
    public int update(HttpServletRequest request, HttpServletResponse response) {
        String starttime = request.getParameter("starttime");
        String endtime = request.getParameter("endtime");
        String user_question = request.getParameter("user_question");
        String a_question = request.getParameter("a_question");
        String selectType = request.getParameter("selectType");
        String language = request.getParameter("language");
        DataUtils dataUtils = new DataUtils();
        int i = 0;
        if(starttime == null){
            starttime  = dataUtils.getFirstDay();
        }
        String typeName = null;
        if("1".equals(selectType)){//评价报表数据
            if("sc".equals(language)){
                i = askDetailService.updateEvaSC(starttime,endtime,user_question,a_question);
                typeName = "修改了评价报表敏感数据";
            }else if("en".equals(language)){
                i = askDetailService.updateEvaEN(starttime,endtime,user_question,a_question);
                typeName = "修改了評價報表敏感數據";
            }else{
                i = askDetailService.updateEva(starttime,endtime,user_question,a_question);
                typeName = "修改了評價報表敏感數據";
            }
        }else if("2".equals(selectType)){//点击报表数据
//            i = askDetailService.updateCilck(starttime,endtime,user_question,a_question);
//            typeName = "修改了點擊報表敏感數據";
            if("sc".equals(language)){
                i = askDetailService.updateCilckSC(starttime,endtime,user_question,a_question);
                typeName = "修改了点击报表敏感数据";
            }else if("en".equals(language)){
                i = askDetailService.updateCilckEN(starttime,endtime,user_question,a_question);
                typeName = "修改了點擊報表敏感數據";
            }else{
                i = askDetailService.updateCilck(starttime,endtime,user_question,a_question);
                typeName = "修改了點擊報表敏感數據";
            }
        }else{//自動問答明細
            String tableName = DB_NAME_PREFIX + starttime.split(" ")[0].substring(0,7).replaceAll("-","");
//            i = askDetailService.updateQuestion(starttime,endtime,user_question,a_question,tableName);
//            typeName = "修改了問答明細報表敏感數據";
            if("sc".equals(language)){
                i = askDetailService.updateQuestionSC(starttime,endtime,user_question,a_question,tableName);
                typeName = "修改了问答明细报表敏感数据";
            }else if("en".equals(language)){
                i = askDetailService.updateQuestionEN(starttime,endtime,user_question,a_question,tableName);
                typeName = "修改了問答明細報表敏感數據";
            }else{
                i = askDetailService.updateQuestion(starttime,endtime,user_question,a_question,tableName);
                typeName = "修改了問答明細報表敏感數據";
            }
        }
        if(i > 0 ){
            Object user_name = request.getSession().getAttribute("session_user");
            HttpClinetUtils httpClinetUtils = new HttpClinetUtils();
            String user_ip = httpClinetUtils.getIpAddress(request);
            UUID uuid=UUID.randomUUID();
            String uuidStr=uuid.toString().replaceAll("-","");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String operate_time = df.format(new Date());// new Date()为获取当前系统时间
            int j ;
            if("sc".equals(language)){
                 j = askDetailService.addOperatingSC(uuidStr,"0",3,typeName+"：共"+i+"條",user_name.toString(),user_ip,operate_time);
            }else if("en".equals(language)){
                 j = askDetailService.addOperatingEN(uuidStr,"0",3,typeName+"：共"+i+"條",user_name.toString(),user_ip,operate_time);
            }else{
                 j = askDetailService.addOperating(uuidStr,"0",3,typeName+"：共"+i+"條",user_name.toString(),user_ip,operate_time);
            }
            System.out.println(j);
        }

       return  i;
    }
}
