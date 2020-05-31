package com.xiaoi.south.manager.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xiaoi.south.manager.entity.FaqVote;
import com.xiaoi.south.manager.service.FaqVoteService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/faqvote")
public class FaqVoteController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FaqVoteController.class);
    @Autowired
    protected FaqVoteService faqVoteService;
    /**
     * 查询所有的解决未解决记录
     *
     * @param
     * @param
     * @param modelMap
     * @return
     */
    @ResponseBody
    @GetMapping("/list")
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        String json = null;
        String starttime = request.getParameter("starttime");
        String endtime = request.getParameter("endtime");
        String user_question = request.getParameter("user_question");
        String faqvote_type = request.getParameter("faqvote_type");
        String platform = request.getParameter("platform");
        String answer = request.getParameter("answer");
        String language = request.getParameter("language");
        if(platform != null && platform.equals("0")){
            platform = "";
        }
        // 获取当月第一天和最后一天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String firstday;
        // 获取前月的第一天
       // Calendar cale = null;
        Calendar cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format.format(cale.getTime())+ " 00:00:00";
        if(starttime == null){
            starttime  = firstday;
        }
        int page = 1;
        int limit = 10;
        if(request.getParameter("page") != null){
            page = Integer.parseInt(request.getParameter("page"));
        }
        if(request.getParameter("limit") != null){
            limit = Integer.parseInt(request.getParameter("limit"));
        }
        PageInfo<FaqVote> FaqVoteList ;
        try {
            if("en".equals(language)){
                FaqVoteList = faqVoteService.getFaqVoteListEN(starttime,endtime,user_question,faqvote_type,platform,answer, page,  limit);
            }else if("sc".equals(language)){
                FaqVoteList = faqVoteService.getFaqVoteListSC(starttime,endtime,user_question,faqvote_type,platform,answer, page,  limit);
            }else{
                FaqVoteList = faqVoteService.getFaqVoteList(starttime,endtime,user_question,faqvote_type,platform,answer, page,  limit);
            }
            for (int i = 0; i < FaqVoteList.getList().size(); i++) {//组装序号
                FaqVoteList.getList().get(i).setNumber((1+i+((page-1)*limit)));
            }
            modelMap.put("code", "0");
            modelMap.put("msg", "查询成功");
            modelMap.put("count", FaqVoteList.getTotal());
            modelMap.put("data", FaqVoteList.getList());
            json = JSON.toJSONString(modelMap);
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("ren_code", "0");
            modelMap.put("ren_msg", "查询失败===>" + e);
            LOGGER.error("查询失败===>" + e);
            json = JSON.toJSONString(modelMap);
        }
        return json;
    }
    @RequestMapping("/exceportFaqvote")
    public void exceportFaqvote(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String starttime = request.getParameter("starttime");
        String endtime = request.getParameter("endtime");
        String user_question = request.getParameter("user_question");
        String faqvote_type = request.getParameter("faqvote_type");
        String platform = request.getParameter("platform");
        String answer = request.getParameter("answer");
        String language = request.getParameter("language");
        if(platform != null && platform.equals("0")){
            platform = "";
        }
        // 获取当月第一天和最后一天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String firstday;
        // 获取前月的第一天
        Calendar cale = null;
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format.format(cale.getTime())+ " 00:00:00";
        if(starttime == null){
            starttime  = firstday;
        }
        int page = 0;
        int limit = 0;
        OutputStream outputStream = null;
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet();
        XSSFRow row = sheet.createRow(0);
        PageInfo<FaqVote> FaqVoteList = null;
        String fileName;
        if("en".equals(language)){
            row.createCell(0).setCellValue("User Id");
            row.createCell(1).setCellValue("Utterance");
            row.createCell(2).setCellValue("Standard Intent");
            row.createCell(3).setCellValue("Answer");
            row.createCell(4).setCellValue("Time Start");
            row.createCell(5).setCellValue("Type");
            row.createCell(6).setCellValue("Reason");
            row.createCell(7).setCellValue("Platform");
            FaqVoteList = faqVoteService.getFaqVoteListEN(starttime,endtime,user_question,faqvote_type,platform,answer, page,  limit);
            fileName = "解決未解決報表("+new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString()+ ").xlsx";
        }else if("sc".equals(language)){
            row.createCell(0).setCellValue("用户Id");
            row.createCell(1).setCellValue("用户问题");
            row.createCell(2).setCellValue("标志问题");
            row.createCell(3).setCellValue("标准答案");
            row.createCell(4).setCellValue("存取时间");
            row.createCell(5).setCellValue("类型");
            row.createCell(6).setCellValue("原因");
            row.createCell(7).setCellValue("平台");
            FaqVoteList = faqVoteService.getFaqVoteListSC(starttime,endtime,user_question,faqvote_type,platform,answer, page,  limit);
            fileName = "解决未解决("+new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString()+ ").xlsx";
        }else{
            row.createCell(0).setCellValue("用戶Id");
            row.createCell(1).setCellValue("用戶問題");
            row.createCell(2).setCellValue("標準問題");
            row.createCell(3).setCellValue("標準答案");
            row.createCell(4).setCellValue("存取時間");
            row.createCell(5).setCellValue("類型");
            row.createCell(6).setCellValue("原因");
            row.createCell(7).setCellValue("平台");
            FaqVoteList = faqVoteService.getFaqVoteList(starttime,endtime,user_question,faqvote_type,platform,answer, page,  limit);
            fileName = "解決未解決報表("+new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString()+ ").xlsx";
        }

        int rowNum = 1;
        if(FaqVoteList.getList().size() > 0){
            for(int i = 0 ; i < FaqVoteList.getList().size(); i++){
                XSSFRow rrow = sheet.createRow(rowNum);
                CellStyle style = workBook.createCellStyle();
                style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
                XSSFCell localXSSFCell = rrow.createCell(0);
                Cell cell0 = rrow.createCell(0);
                cell0.setCellStyle(style);
                cell0.setCellValue(FaqVoteList.getList().get(i).getUser_id());
                rrow.createCell(1).setCellValue(FaqVoteList.getList().get(i).getUser_question());
                rrow.createCell(2).setCellValue(FaqVoteList.getList().get(i).getQuestion());
                rrow.createCell(3).setCellValue(FaqVoteList.getList().get(i).getAnswer());
                rrow.createCell(4).setCellValue(FaqVoteList.getList().get(i).getVote_time());
                if("en".equals(language)){
                    if(FaqVoteList.getList().get(i).getAction().equals("1")){
                        rrow.createCell(5).setCellValue("Helpful");
                    }else{
                        rrow.createCell(5).setCellValue("NoHelpful");
                    }
                }else if("sc".equals(language)){
                    if(FaqVoteList.getList().get(i).getAction().equals("1")){
                        rrow.createCell(5).setCellValue("解决");
                    }else{
                        rrow.createCell(5).setCellValue("未解决");
                    }
                }else{
                    if(FaqVoteList.getList().get(i).getAction().equals("1")){
                        rrow.createCell(5).setCellValue("解決");
                    }else{
                        rrow.createCell(5).setCellValue("未解決");
                    }
                }

                rrow.createCell(6).setCellValue(FaqVoteList.getList().get(i).getReason());
                rrow.createCell(7).setCellValue(FaqVoteList.getList().get(i).getPlatform());
                rowNum++;
            }
        }

        OutputStream out = response.getOutputStream();
        response.setHeader("Content-Disposition" ,"attachment;filename="+new String(fileName.getBytes("gbk"),"ISO-8859-1"));
        response.setContentType("application/msexcel;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition" ,"attachment;filename="+new String(fileName.getBytes(),"ISO-8859-1"));
        response.setContentType("application/msexcel;charset=GBK");
        workBook.write(out);
        out.flush();
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                LOGGER.error("", e);
            } finally {
                outputStream = null;
            }
        }
    }

    /**
     * 繁体页面
     * @return
     */
    @RequestMapping("/index")
    public String demo() {
        return "faqvote";
    }
    /**
     * 简体页面
     * @return
     */
    @RequestMapping("/indexSC")
    public String demoSC() {
        return "faqvoteSC";
    }
    /**
     * 英文页面
     * @return
     */
    @RequestMapping("/indexEN")
    public String demoEN() {
        return "faqvoteEN";
    }
}
