package com.xiaoi.south.manager.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.xiaoi.south.manager.entity.Click;
import com.xiaoi.south.manager.entity.ClickPercent;
import com.xiaoi.south.manager.entity.FaqVote;
import com.xiaoi.south.manager.service.ClickService;
import com.xiaoi.south.manager.service.FaqVoteService;
import com.xiaoi.south.manager.utlis.DataUtils;
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
import java.util.HashMap;
import java.util.List;

/**
 * 操作点击报表
 *
 * @author yale.ye
 */
@Controller
@RequestMapping("/click")
public class ClickController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClickController.class);
    @Autowired
    protected ClickService clickService;

    /**
     * 查询所有点击记录
     *
     * @param
     * @param
     * @param modelMap
     * @return
     */
    @ResponseBody
    @RequestMapping("/list")
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        String json = null;
        String starttime = request.getParameter("starttime");
        String endtime = request.getParameter("endtime");
        String u_question = request.getParameter("u_question");
        String b_question = request.getParameter("b_question");
        String catname = request.getParameter("catname");
        String answer = request.getParameter("answer");
        String userId = request.getParameter("userId");
        String statistics = request.getParameter("statistics");
        String source = request.getParameter("source");
        String language = request.getParameter("language");
        int page = 1;
        int limit = 10;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        if (request.getParameter("limit") != null) {
            limit = Integer.parseInt(request.getParameter("limit"));
        }
        DataUtils dataUtils = new DataUtils();
        if (starttime == null) {
            starttime = dataUtils.getFirstDay();
        }
        PageInfo<Click> clicksList;
        try {
            if ("sc".equals(language)) {
                clicksList = clickService.getClickListSC(starttime, endtime, u_question, b_question, catname, answer, userId, statistics, source, page, limit);
            } else if ("en".equals(language)) {
                clicksList = clickService.getClickListEN(starttime, endtime, u_question, b_question, catname, answer, userId, statistics, source, page, limit);
            } else {
                clicksList = clickService.getClickList(starttime, endtime, u_question, b_question, catname, answer, userId, statistics, source, page, limit);
            }

            for (int i = 0; i < clicksList.getList().size(); i++) {
                clicksList.getList().get(i).setNumber((1+i+((page-1)*limit)));
            }
            modelMap.put("code", "0");
            modelMap.put("msg", "查询成功");
            modelMap.put("count", clicksList.getTotal());
            modelMap.put("data", clicksList.getList());
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

    /**
     * 導出點擊率報表數據
     *
     * @param response
     * @param request
     * @throws IOException
     */
    @RequestMapping("/exceportClick")
    public void exceportClick(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String starttime = request.getParameter("starttime");
        String endtime = request.getParameter("endtime");
        String u_question = request.getParameter("u_question");
        String b_question = request.getParameter("b_question");
        String catname = request.getParameter("catname");
        String answer = request.getParameter("answer");
        String userId = request.getParameter("userId");
        String statistics = request.getParameter("statistics");
        String source = request.getParameter("source");
        String language = request.getParameter("language");
        DataUtils dataUtils = new DataUtils();
        if (starttime == null) {
            starttime = dataUtils.getFirstDay();
        }
        OutputStream outputStream = null;
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet();
        XSSFRow row = sheet.createRow(0);
        if ("sc".equals(language)) {
            row.createCell(0).setCellValue("会话Id");
            row.createCell(1).setCellValue("用户Id");
            row.createCell(2).setCellValue("访问时间");
            row.createCell(3).setCellValue("用户问题");
            row.createCell(4).setCellValue("标准问题");
            row.createCell(5).setCellValue("标准答案");
            row.createCell(6).setCellValue("来源");
            row.createCell(7).setCellValue("知识分类");
            row.createCell(8).setCellValue("点击URL");
            row.createCell(9).setCellValue("点击");
        } else if ("en".equals(language)) {
            row.createCell(0).setCellValue("sessionId");
            row.createCell(1).setCellValue("User id");
            row.createCell(2).setCellValue("Time Start");
            row.createCell(3).setCellValue("Utterance");
            row.createCell(4).setCellValue("Standard Intent");
            row.createCell(5).setCellValue("Standard Answer");
            row.createCell(6).setCellValue("Source");
            row.createCell(7).setCellValue("Intent Category");
            row.createCell(8).setCellValue("Click URL");
            row.createCell(9).setCellValue("Click");
        } else {
            row.createCell(0).setCellValue("會話Id");
            row.createCell(1).setCellValue("用戶Id");
            row.createCell(2).setCellValue("訪問時間");
            row.createCell(3).setCellValue("用戶問題");
            row.createCell(4).setCellValue("標準問題");
            row.createCell(5).setCellValue("標準答案");
            row.createCell(6).setCellValue("來源");
            row.createCell(7).setCellValue("知識分類");
            row.createCell(8).setCellValue("點擊URL");
            row.createCell(9).setCellValue("點擊");
        }

        int rowNum = 1;
        int page = 0;
        int limit = 0;
        PageInfo<Click> clicksList;
        if ("sc".equals(language)) {
            clicksList = clickService.getClickListSC(starttime, endtime, u_question, b_question, catname, answer, userId, statistics, source, page, limit);
        } else if ("en".equals(language)) {
            clicksList = clickService.getClickListEN(starttime, endtime, u_question, b_question, catname, answer, userId, statistics, source, page, limit);
        } else {
            clicksList = clickService.getClickList(starttime, endtime, u_question, b_question, catname, answer, userId, statistics, source, page, limit);
        }

        for (int i = 0; i < clicksList.getList().size(); i++) {
            XSSFRow rrow = sheet.createRow(rowNum);
            CellStyle style = workBook.createCellStyle();
            style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
            XSSFCell localXSSFCell = rrow.createCell(0);
            Cell cell0 = rrow.createCell(0);
            cell0.setCellStyle(style);
            //  tableBean.getBigclassification();
            cell0.setCellValue(clicksList.getList().get(i).getSessionId());
            rrow.createCell(1).setCellValue(clicksList.getList().get(i).getUserId());
            rrow.createCell(2).setCellValue(clicksList.getList().get(i).getTrigger_time());
            if (clicksList.getList().get(i).getU_question().startsWith("id=")) {
                rrow.createCell(3).setCellValue(clicksList.getList().get(i).getB_question());
            } else {
                rrow.createCell(3).setCellValue(clicksList.getList().get(i).getU_question());
            }

            rrow.createCell(4).setCellValue(clicksList.getList().get(i).getB_question());


            rrow.createCell(5).setCellValue(clicksList.getList().get(i).getAnswer());
            if ("sc".equals(language)) {
                if ("1".equals(clicksList.getList().get(i).getSource())) {
                    rrow.createCell(6).setCellValue("热门表格");
                } else {
                    rrow.createCell(6).setCellValue("用户输入");
                }
            } else if ("en".equals(language)) {
                if ("1".equals(clicksList.getList().get(i).getSource())) {
                    rrow.createCell(6).setCellValue("Popular Forms");
                } else {
                    rrow.createCell(6).setCellValue("User Input");
                }
            } else {
                if ("1".equals(clicksList.getList().get(i).getSource())) {
                    rrow.createCell(6).setCellValue("熱門表格");
                } else {
                    rrow.createCell(6).setCellValue("用戶輸入");
                }
            }

            rrow.createCell(7).setCellValue(clicksList.getList().get(i).getCatname());
            if (!"null".equals(clicksList.getList().get(i).getTrgger_url())) {
                rrow.createCell(8).setCellValue(clicksList.getList().get(i).getTrgger_url());
            } else {
                rrow.createCell(8).setCellValue("");
            }
            if ("sc".equals(language)) {
                if ("0".equals(clicksList.getList().get(i).getStatistics())) {
                    rrow.createCell(9).setCellValue("标准访问");
                } else if ("1".equals(clicksList.getList().get(i).getStatistics())) {
                    rrow.createCell(9).setCellValue("第一次点击");
                } else {
                    rrow.createCell(9).setCellValue("重复点击");
                }
            } else if ("en".equals(language)) {
                if ("0".equals(clicksList.getList().get(i).getStatistics())) {
                    rrow.createCell(9).setCellValue("NO click");
                } else if ("1".equals(clicksList.getList().get(i).getStatistics())) {
                    rrow.createCell(9).setCellValue("First click");
                } else {
                    rrow.createCell(9).setCellValue("Multiple clicks");
                }
            } else {
                if ("0".equals(clicksList.getList().get(i).getStatistics())) {
                    rrow.createCell(9).setCellValue("標準訪問");
                } else if ("1".equals(clicksList.getList().get(i).getStatistics())) {
                    rrow.createCell(9).setCellValue("第一次點擊");
                } else {
                    rrow.createCell(9).setCellValue("重複點擊");
                }
            }

            rowNum++;
        }
        OutputStream out = response.getOutputStream();
        String fileName;
        if ("sc".equals(language)) {
            fileName = "点击报表(" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString() + ").xlsx";
        } else if ("en".equals(language)) {
            fileName = "Click Report(" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString() + ").xlsx";
        } else {
            fileName = "點擊報表(" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString() + ").xlsx";
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "ISO-8859-1"));
        response.setContentType("application/msexcel;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
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
     * 获取知识分类名称
     *
     * @param modelMap
     * @return
     */
    @ResponseBody
    @RequestMapping("/getCategoryName")
    public String getCategoryName(ModelMap modelMap, HttpServletRequest request) {
        String language = request.getParameter("language");
        List<HashMap> list;
        if ("sc".equals(language)){
            list = clickService.getCategoryNameSC();
        }else if("en".equals(language)){
            list = clickService.getCategoryNameEN();
        }else{
            list = clickService.getCategoryName();
        }

        System.out.println(JSON.toJSONString(list));
        modelMap.put("code", "0");
        modelMap.put("msg", "查询成功");
        modelMap.put("count", list.size());
        modelMap.put("data", list);
        String json = JSON.toJSONString(modelMap);
        return json;
    }

    /**
     * 點擊率報表頁面
     *
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "click";
    }

    @RequestMapping("/indexSC")
    public String indexSC() {
        return "clickSC";
    }

    @RequestMapping("/indexEN")
    public String indexEN() {
        return "clickEN";
    }

    /**
     * 打開點擊率統計報表頁面
     *
     * @return
     */
    @RequestMapping("/percent")
    public String clickPercentDemo() {
        return "percent";
    }

    @RequestMapping("/percentSC")
    public String percentSC() {
        return "percentSC";
    }

    @RequestMapping("/percentEN")
    public String percentEN() {
        return "percentEN";
    }

    /**
     * 點擊率報表統計
     *
     * @param request
     * @param response
     * @param modelMap
     * @return
     */
    @ResponseBody
    @RequestMapping("/getPercentList")
    public String getPercentList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        String json = null;
        String starttime = request.getParameter("starttime");
        String endtime = request.getParameter("endtime");
        String catname = request.getParameter("catname");
        String language = request.getParameter("language");
        DataUtils dataUtils = new DataUtils();
        if (starttime == null || starttime == "") {
            starttime = dataUtils.getFirstDay();
        } else {
            starttime = starttime + " 00:00:00";

        }
        if (endtime != null && !"".equals(endtime)) {
            endtime = endtime + " 23:59:59";
        }
        try {
            List<ClickPercent> clicksList ;
            if ("sc".equals(language)){
                clicksList = clickService.getPercentSC(starttime, endtime, catname);
            }else if("en".equals(language)){
                clicksList = clickService.getPercentEN(starttime, endtime, catname);
            }else{
                clicksList = clickService.getPercent(starttime, endtime, catname);
            }
            modelMap.put("code", "0");
            modelMap.put("msg", "查询成功");
            modelMap.put("count", 0);
            modelMap.put("data", clicksList);
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

    @RequestMapping("/exceportClickPercent")
    public void exceportClickPercent(HttpServletResponse response, HttpServletRequest request) throws IOException {
        String starttime = request.getParameter("starttime");
        String endtime = request.getParameter("endtime");
        String catname = request.getParameter("catname");
        DataUtils dataUtils = new DataUtils();
        if (starttime == null) {
            starttime = dataUtils.getFirstDay();
        } else {
            starttime = starttime + " 00:00:00";

        }
        if (endtime != null && !"".equals(endtime)) {
            endtime = endtime + " 23:59:59";
        }
        OutputStream outputStream = null;
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet();
        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("日期");
        row.createCell(1).setCellValue("總訪問次數");
        row.createCell(2).setCellValue("第一次點擊次數");
//        row.createCell(3).setCellValue("重複點擊次數");
        row.createCell(3).setCellValue("百分比");
        int rowNum = 1;
        List<ClickPercent> clicksList = clickService.getPercent(starttime, endtime, catname);
        for (int i = 0; i < clicksList.size(); i++) {
            XSSFRow rrow = sheet.createRow(rowNum);
            CellStyle style = workBook.createCellStyle();
            style.setFillBackgroundColor(IndexedColors.AQUA.getIndex());
            XSSFCell localXSSFCell = rrow.createCell(0);
            Cell cell0 = rrow.createCell(0);
            cell0.setCellStyle(style);
            cell0.setCellValue(clicksList.get(i).getTrigger_data());
            rrow.createCell(1).setCellValue(clicksList.get(i).getSum_count());
            rrow.createCell(2).setCellValue(clicksList.get(i).getSum_trigger());
//            rrow.createCell(3).setCellValue(clicksList.get(i).getSum_repeat());
            rrow.createCell(3).setCellValue(clicksList.get(i).getPercent());

            rowNum++;
        }
        OutputStream out = response.getOutputStream();
        String fileName = "點擊率報表(" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString() + ").xlsx";

        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "ISO-8859-1"));
        response.setContentType("application/msexcel;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), "ISO-8859-1"));
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
}
