package com.xiaoi.south.manager.utlis;

import com.alibaba.fastjson.JSONObject;
import com.xiaoi.south.manager.crawler.CrawlerGOV;
import com.xiaoi.south.manager.entity.Crawler;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 操作爬虫数据
 *
 */
public class CrawlerUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerGOV.class);

    CrawlerGOV jsoupUtils = new CrawlerGOV();
    HttpClinetUtils httpsUtils = new HttpClinetUtils();

    /**
     * 获取部门爬虫数据
     */
    public void getCrawler(){
        String language = "tc";
        String lscd = httpsUtils.doGet("https://www.lcsd.gov.hk/tc/aboutlcsd/forms.html","utf-8");
        jsoupUtils.getLCSD(lscd,language);//康文署

        String ld = httpsUtils.doGet("https://www.info.gov.hk/cgi-bin/forms/csearch201402.cgi?formnoinput=&keywordinput=&deptinput=LABD&dept=++%B7j+%B4M++","utf-8");
        jsoupUtils.getLD(ld,language);//劳工处

        String ird = httpsUtils.doGet("https://www.ird.gov.hk/chi/paf/for.htm","utf-8");
        jsoupUtils.getIRD(ird,language);//税务局

        String hkpf = httpsUtils.doGet("https://www.police.gov.hk/ppp_tc/08_forms/","utf-8");
        jsoupUtils.getHKPF(hkpf,language);//香港警務處
        //————————入境处—————start—————————————————————————
        String residence = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/right-of-abode-in-hksar.html","utf-8");
        jsoupUtils.getIMD(residence,"居留權",language);
        String ID_card = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/hkid.html","utf-8");
        jsoupUtils.getIMD(ID_card,"身份證",language);
        String travel = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-travel-doc.html","utf-8");
        jsoupUtils.getIMD(travel,"旅行證件",language);
        String Chinese_nationality = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/chinese-nationality-application-form.html","utf-8");
        jsoupUtils.getIMD(Chinese_nationality,"中國國籍",language);
        String birthAndDeath = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/birth-death-marriage.html","utf-8");
        jsoupUtils.getIMD(birthAndDeath,"出生及死亡登記",language);
        String matrimony = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/marriage.html","utf-8");
        jsoupUtils.getIMD(matrimony,"婚姻登記",language);
        String other = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/other.html","utf-8");
        jsoupUtils.getIMD(other,"其他",language);

        String Visa1 = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/employment-as-professionals.html","utf-8");
        jsoupUtils.getIMD(Visa1,"簽證/專業人士來港就業",language);
        String Visa2 = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/techtas.html","utf-8");
        jsoupUtils.getIMD(Visa2,"簽證/科技人才入境計劃",language);
        String Visa3 = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/investment.html","utf-8");
        jsoupUtils.getIMD(Visa3,"簽證/企業家來港投資",language);
        String Visa4 = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/capital-investment-entrant.html","utf-8");
        jsoupUtils.getIMD(Visa4,"簽證/資本投資者入境計劃",language);
        String Visa5 = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/training.html","utf-8");
        jsoupUtils.getIMD(Visa5,"簽證/受訓",language);
        String Visa6 = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/foreign-domestic-helpers.html","utf-8");
        jsoupUtils.getIMD(Visa6,"簽證/從外國聘用家庭傭工",language);
        String Visa7 = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/employment-as-imported-workers.html","utf-8");
        jsoupUtils.getIMD(Visa7,"簽證/輸入勞工來港就業",language);
        String Visa8 = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/extension-of-stay.html","utf-8");
        jsoupUtils.getIMD(Visa8,"簽證/延長逗留期限及旅行證件加蓋簽注",language);
        String Visa9 = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/quality-migrant-admission.html","utf-8");
        jsoupUtils.getIMD(Visa9,"簽證/優秀人才入境計劃",language);
        String Visa10 = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/residence-as-dependants.html","utf-8");
        jsoupUtils.getIMD(Visa10,"簽證/受養人來港居留",language);
        String Visa11 = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/assg.html","utf-8");
        jsoupUtils.getIMD(Visa11,"簽證/輸入中國籍香港永久性居民第二代計劃",language);
        String Visa12 = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/study.html","utf-8");
        jsoupUtils.getIMD(Visa12,"簽證/就讀",language);
        String Visa13 = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/visit-transit.html","utf-8");
        jsoupUtils.getIMD(Visa13,"簽證/旅遊/過境",language);
        String Visa14 = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/working-holiday.html","utf-8");
        jsoupUtils.getIMD(Visa14,"簽證/工作假期計劃",language);
        String Visa15 = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/hksar-travel-pass.html","utf-8");
        jsoupUtils.getIMD(Visa15,"簽證/香港特別行政區旅遊通行證",language);
        String Visa16 = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/apec-business-travel-card.html","utf-8");
        jsoupUtils.getIMD(Visa16,"簽證/亞太經合組織商務旅遊證",language);
        String Visa17 = httpsUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/other.html","utf-8");
        jsoupUtils.getIMD(Visa17,"簽證/其他",language);
        //————————入境处—————end—————————————————————————
        String hd = httpsUtils.doGet("https://www.housingauthority.gov.hk/tc/global-elements/forms/index.html","utf-8");
        jsoupUtils.getHD(hd,language); //房屋署

        String bd = httpsUtils.doGet("https://www.bd.gov.hk/tc/resources/forms/index.html","utf-8");
        jsoupUtils.getBDUrl(bd,language);//屋宇署

        String wfsfaa = httpsUtils.doGet("https://www.wfsfaa.gov.hk/sfo/tc/forms/form.htm","utf-8");
        jsoupUtils.getWFSFAA(wfsfaa);//在職家庭及學生資助事務處

        String omb = httpsUtils.doGet("https://www.ombudsman.hk/zh-hk/form_download.html","utf-8");
        jsoupUtils.getOMB(omb,language);//申訴專員公署

        String pcpd = httpsUtils.doGet("https://www.pcpd.org.hk/tc_chi/resources_centre/publications/forms/forms.html","utf-8");
        jsoupUtils.getPCPD(pcpd,language);//個人資料私隱專員公署

        String td = httpsUtils.doGet("https://www.td.gov.hk/tc/public_forms/td_forms/index.html","utf-8");
        jsoupUtils.getTD(td,language); //运输署
    }
    /**
     * 获取部门爬虫数据
     */
    public void getCrawlerEN(){
        String language = "en";
        String lscd = httpsUtils.doGet("https://www.lcsd.gov.hk/en/aboutlcsd/forms.html","utf-8");
        jsoupUtils.getLCSD(lscd,language);//康文署

        String ld = httpsUtils.doGet("https://www.info.gov.hk/cgi-bin/forms/esearch201402.cgi?formnoinput=&keywordinput=&deptinput=LABD&dept=++%B7j+%B4M++","utf-8");
        jsoupUtils.getLD(ld,language);//劳工处 需要重现处理

        String ird = httpsUtils.doGet("https://www.ird.gov.hk/eng/paf/for.htm","utf-8");
        jsoupUtils.getIRD(ird,language);//税务局

        String hkpf = httpsUtils.doGet("https://www.police.gov.hk/ppp_en/08_forms/","utf-8");
        jsoupUtils.getHKPF(hkpf,language);//香港警務處
        //————————入境处—————start—————————————————————————
        String residence = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/right-of-abode-in-hksar.html","utf-8");
        jsoupUtils.getIMD(residence,"Right of Abode",language);
        String ID_card = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/hkid.html","utf-8");
        jsoupUtils.getIMD(ID_card,"Identity Cards",language);
        String travel = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-travel-doc.html","utf-8");
        jsoupUtils.getIMD(travel,"Travel Documents",language);
        String Chinese_nationality = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/chinese-nationality-application-form.html","utf-8");
        jsoupUtils.getIMD(Chinese_nationality,"Chinese Nationality",language);
        String birthAndDeath = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/birth-death-marriage.html","utf-8");
        jsoupUtils.getIMD(birthAndDeath,"Births and Deaths Registration",language);
        String matrimony = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/marriage.html","utf-8");
        jsoupUtils.getIMD(matrimony,"Marriage Registration",language);
        String other = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/other.html","utf-8");
        jsoupUtils.getIMD(other,"VISA/Others",language);

        String Visa1 = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/employment-as-professionals.html","utf-8");
        jsoupUtils.getIMD(Visa1,"Employment as Professionals (GEP, IANG, ASMTP)",language);
        String Visa2 = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/techtas.html","utf-8");
        jsoupUtils.getIMD(Visa2,"Technology Talent Admission Scheme (TechTAS)",language);
        String Visa3 = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/investment.html","utf-8");
        jsoupUtils.getIMD(Visa3,"Investment as Entrepreneurs",language);
        String Visa4 = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/capital-investment-entrant.html","utf-8");
        jsoupUtils.getIMD(Visa4,"Capital Investment Entrant Scheme",language);
        String Visa5 = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/training.html","utf-8");
        jsoupUtils.getIMD(Visa5,"Training",language);
        String Visa6 = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/foreign-domestic-helpers.html","utf-8");
        jsoupUtils.getIMD(Visa6,"Employment of Domestic Helpers from Abroad",language);
        String Visa7 = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/employment-as-imported-workers.html","utf-8");
        jsoupUtils.getIMD(Visa7,"Employment as Imported Workers",language);
        String Visa8 = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/extension-of-stay.html","utf-8");
        jsoupUtils.getIMD(Visa8,"Extension of Stay & Transfer of Endorsement",language);
        String Visa9 = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/quality-migrant-admission.html","utf-8");
        jsoupUtils.getIMD(Visa9,"Quality Migrant Admission Scheme",language);
        String Visa10 = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/residence-as-dependants.html","utf-8");
        jsoupUtils.getIMD(Visa10,"Residence as dependant",language);
        String Visa11 = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/assg.html","utf-8");
        jsoupUtils.getIMD(Visa11,"Admission Scheme for the Second Generation of Chinese Hong Kong Permanent Residents",language);
        String Visa12 = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/study.html","utf-8");
        jsoupUtils.getIMD(Visa12,"Study",language);
        String Visa13 = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/visit-transit.html","utf-8");
        jsoupUtils.getIMD(Visa13,"Visit / Transit",language);
        String Visa14 = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/working-holiday.html","utf-8");
        jsoupUtils.getIMD(Visa14,"Working Holiday Scheme",language);
        String Visa15 = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/hksar-travel-pass.html","utf-8");
        jsoupUtils.getIMD(Visa15,"Hong Kong Special Administrative Region Travel Pass",language);
        String Visa16 = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/apec-business-travel-card.html","utf-8");
        jsoupUtils.getIMD(Visa16,"APEC Business Travel Card",language);
        String Visa17 = httpsUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/other.html","utf-8");
        jsoupUtils.getIMD(Visa17,"Other Applications",language);
        //————————入境处—————end—————————————————————————
        String hd = httpsUtils.doGet("https://www.housingauthority.gov.hk/en/global-elements/forms/index.html","utf-8");
        jsoupUtils.getHD(hd,language); //房屋署

        String bd = httpsUtils.doGet("https://www.bd.gov.hk/en/resources/forms/index.html","utf-8");
        jsoupUtils.getBDUrl(bd,language);//屋宇署

        String wfsfaa = httpsUtils.doGet("https://www.wfsfaa.gov.hk/sfo/en/forms/form.htm","utf-8");
        jsoupUtils.getWFSFAA(wfsfaa);//在職家庭及學生資助事務處

        String omb = httpsUtils.doGet("https://www.ombudsman.hk/en-us/form_download.html","utf-8");
        jsoupUtils.getOMB(omb,language);//申訴專員公署

        String pcpd = httpsUtils.doGet("https://www.pcpd.org.hk/english/resources_centre/publications/forms/forms.html","utf-8");
        jsoupUtils.getPCPD(pcpd,language);//個人資料私隱專員公署

        String td = httpsUtils.doGet("https://www.td.gov.hk/en/public_forms/td_forms/index.html","utf-8");
        jsoupUtils.getTD(td,language); //运输署
    }
    public void getCrawlerCN(){
        String language = "cn";
        String lscd = httpsUtils.doGet("https://www.lcsd.gov.hk/sc/aboutlcsd/forms.html","utf-8");
        jsoupUtils.getLCSD(lscd,language);//康文署

        String ld = httpsUtils.doGet("https://www.info.gov.hk/cgi-bin/forms/csearch201402.cgi?formnoinput=&keywordinput=&deptinput=LABD&dept=++%B7j+%B4M++","utf-8");
        jsoupUtils.getLD(ld,language);//劳工处

        String ird = httpsUtils.doGet("https://www.ird.gov.hk/chs/paf/for.htm","utf-8");
        jsoupUtils.getIRD(ird,language);//税务局

        String hkpf = httpsUtils.doGet("https://www.police.gov.hk/ppp_sc/08_forms/","utf-8");
        jsoupUtils.getHKPF(hkpf,language);//香港警務處
        //————————入境处—————start—————————————————————————
        String residence = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/right-of-abode-in-hksar.html","utf-8");
        jsoupUtils.getIMD(residence,"居留权",language);
        String ID_card = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/hkid.html","utf-8");
        jsoupUtils.getIMD(ID_card,"身份证",language);
        String travel = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-travel-doc.html","utf-8");
        jsoupUtils.getIMD(travel,"旅行证件",language);
        String Chinese_nationality = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/chinese-nationality-application-form.html","utf-8");
        jsoupUtils.getIMD(Chinese_nationality,"中国国籍",language);
        String birthAndDeath = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/birth-death-marriage.html","utf-8");
        jsoupUtils.getIMD(birthAndDeath,"出生及死亡登记",language);
        String matrimony = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/marriage.html","utf-8");
        jsoupUtils.getIMD(matrimony,"婚姻登记",language);
        String other = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/other.html","utf-8");
        jsoupUtils.getIMD(other,"其他",language);

        String Visa1 = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/employment-as-professionals.html","utf-8");
        jsoupUtils.getIMD(Visa1,"签证/专业人士来港就业",language);
        String Visa2 = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/techtas.html","utf-8");
        jsoupUtils.getIMD(Visa2,"签证/科技人才入境计划",language);
        String Visa3 = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/investment.html","utf-8");
        jsoupUtils.getIMD(Visa3,"签证/企业家来港投资",language);
        String Visa4 = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/capital-investment-entrant.html","utf-8");
        jsoupUtils.getIMD(Visa4,"签证/资本投资者入境计划",language);
        String Visa5 = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/training.html","utf-8");
        jsoupUtils.getIMD(Visa5,"签证/受训",language);
        String Visa6 = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/foreign-domestic-helpers.html","utf-8");
        jsoupUtils.getIMD(Visa6,"签证/从外国聘用家庭佣工",language);
        String Visa7 = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/employment-as-imported-workers.html","utf-8");
        jsoupUtils.getIMD(Visa7,"签证/输入劳工来港就业",language);
        String Visa8 = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/extension-of-stay.html","utf-8");
        jsoupUtils.getIMD(Visa8,"签证/延长逗留期限及旅行证件加盖签注",language);
        String Visa9 = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/quality-migrant-admission.html","utf-8");
        jsoupUtils.getIMD(Visa9,"签证/优秀人才入境计划",language);
        String Visa10 = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/residence-as-dependants.html","utf-8");
        jsoupUtils.getIMD(Visa10,"签证/受养人来港居留",language);
        String Visa11 = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/assg.html","utf-8");
        jsoupUtils.getIMD(Visa11,"签证/输入中国籍香港永久居民第二代计划",language);
        String Visa12 = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/study.html","utf-8");
        jsoupUtils.getIMD(Visa12,"签证/就读",language);
        String Visa13 = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/visit-transit.html","utf-8");
        jsoupUtils.getIMD(Visa13,"签证/旅游/过境",language);
        String Visa14 = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/working-holiday.html","utf-8");
        jsoupUtils.getIMD(Visa14,"签证/工作假期计划",language);
        String Visa15 = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/hksar-travel-pass.html","utf-8");
        jsoupUtils.getIMD(Visa15,"签证/香港特别行政区旅游通行证",language);
        String Visa16 = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/apec-business-travel-card.html","utf-8");
        jsoupUtils.getIMD(Visa16,"签证/亚太经合组织商务旅游签证",language);
        String Visa17 = httpsUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/other.html","utf-8");
        jsoupUtils.getIMD(Visa17,"签证/其他",language);
        //————————入境处—————end—————————————————————————
        String hd = httpsUtils.doGet("https://www.housingauthority.gov.hk/sc/global-elements/forms/index.html","utf-8");
        jsoupUtils.getHD(hd,language); //房屋署

        String bd = httpsUtils.doGet("https://www.bd.gov.hk/sc/resources/forms/index.html","utf-8");
        jsoupUtils.getBDUrl(bd,language);//屋宇署

        String wfsfaa = httpsUtils.doGet("https://www.wfsfaa.gov.hk/sfo/sc/forms/form.htm","utf-8");
        jsoupUtils.getWFSFAA(wfsfaa);//在職家庭及學生資助事務處

        String omb = httpsUtils.doGet("https://www.ombudsman.hk/zh-cn/form_download.html","utf-8");
        jsoupUtils.getOMB(omb,language);//申訴專員公署

        String pcpd = httpsUtils.doGet("https://www.pcpd.org.hk/sc_chi/resources_centre/publications/forms/forms.html","utf-8");
        jsoupUtils.getPCPD(pcpd,language);//個人資料私隱專員公署

        String td = httpsUtils.doGet("https://www.td.gov.hk/sc/public_forms/td_forms/index.html","utf-8");
        jsoupUtils.getTD(td,language); //运输署
    }

    /**
     * 昨天今天爬虫数据对比，并把结果写入Excel并保存
     * @param
     * @throws Exception
     */
    public void writeExcel(String language)  throws Exception {
        String filePath = getFilePath(language);
        //设置文件名
        String fileName = new SimpleDateFormat("yyyyMMdd").format(new Date()).toString() +").xlsx";
        String savePath;
        XSSFWorkbook workBook = new XSSFWorkbook();
        XSSFSheet sheet = workBook.createSheet();
        sheet.setDefaultColumnWidth(20); //设置默认宽度
        XSSFRow row = sheet.createRow(0); //第一行
        XSSFRow row1 = sheet.createRow(1);//第二行

        //合并单元格
        CellRangeAddress region1 = new CellRangeAddress(0, 0, (short) 0, (short) 4); //参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
        sheet.addMergedRegion(region1);
        CellRangeAddress region2 = new CellRangeAddress(0, 0, (short) 5, (short) 9); //参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
        sheet.addMergedRegion(region2);
        //自动换行
        XSSFCellStyle setWarp = workBook.createCellStyle();
        setWarp.setWrapText(true);
        XSSFCellStyle otherWarp = workBook.createCellStyle();
        otherWarp.setWrapText(true);
        //设置表格样式
        XSSFCellStyle setBorder = workBook.createCellStyle();
        setBorder.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中

        XSSFFont font = workBook.createFont();
        font.setFontName("仿宋_GB2312");
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        font.setFontHeightInPoints((short) 12);

        setBorder.setFont(font);//选择需要用到的字体格式
        //设置字体
        XSSFFont font1 = workBook.createFont();
        font1.setColor(XSSFFont.COLOR_RED);

        //设置样式
        XSSFCellStyle style = workBook.createCellStyle();
        style.setFont(font1);
        style.setWrapText(true);

        Cell cell0 = row.createCell(0);
        cell0.setCellStyle(setBorder);

        Cell cell1 = row.createCell(5);
        cell1.setCellStyle(setBorder);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String reptiledateT = sdf.format(new Date());
        //-----------------生产环境用----start---------------------------------------
        //String reptiledate = getYest();
        //-----------------生产环境用----end-----------------------------------------
        //-------------------------uat test 环境用------start------------------------
        Date d = new Date(System.currentTimeMillis()-1000*60*60*24);
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
        String reptiledate = sp.format(d);//获取昨天日期
        //-------------------------uat test 环境用-- end----------------------------
        List<Crawler> listObjectY = jsoupUtils.getObject(reptiledate,language);
        List<Crawler> listObject = jsoupUtils.getObject(reptiledateT,language);
//        List<Crawler> listObjectY = jsoupUtils.getObject(reptiledate);
//        List<Crawler> listObject = jsoupUtils.getObject(reptiledateT);
        if(listObjectY.size() > listObject.size()){
            listObject = listObjectY;
        }
        if("en".equals(language)){
            savePath = filePath + "Knowledge comparison("+fileName;
            cell0.setCellValue(reptiledate+" Data");
            cell1.setCellValue(reptiledateT+" Data");
            row1.createCell(0).setCellValue("ORGANIZATION");
            row1.createCell(1).setCellValue("CLASSIFICATION");
            row1.createCell(2).setCellValue("FORMNAME");
            row1.createCell(3).setCellValue("FORMID");
            row1.createCell(4).setCellValue("FORM_URL");
            row1.createCell(5).setCellValue("ORGANIZATION");
            row1.createCell(6).setCellValue("CLASSIFICATION");
            row1.createCell(7).setCellValue("FORMNAME");
            row1.createCell(8).setCellValue("FORMID");
            row1.createCell(9).setCellValue("FORM_URL");
        }else if("cn".equals(language)){
            savePath = filePath + "知识对比("+fileName;
            cell0.setCellValue(reptiledate+" 数据");
            cell1.setCellValue(reptiledateT+" 数据");
            row1.createCell(0).setCellValue("所属机构");
            row1.createCell(1).setCellValue("分类");
            row1.createCell(2).setCellValue("表格名称");
            row1.createCell(3).setCellValue("FORMID");
            row1.createCell(4).setCellValue("表格路径");
            row1.createCell(5).setCellValue("所属机构");
            row1.createCell(6).setCellValue("分类");
            row1.createCell(7).setCellValue("表格名称");
            row1.createCell(8).setCellValue("FORMID");
            row1.createCell(9).setCellValue("表格路径");
        }else{
            savePath = filePath + "知識對比("+fileName;
            cell0.setCellValue(reptiledate+" 数据");
            cell1.setCellValue(reptiledateT+" 数据");
            row1.createCell(0).setCellValue("所屬機構");
            row1.createCell(1).setCellValue("分類");
            row1.createCell(2).setCellValue("表格名稱");
            row1.createCell(3).setCellValue("FORMID");
            row1.createCell(4).setCellValue("表格路徑");
            row1.createCell(5).setCellValue("所屬機構");
            row1.createCell(6).setCellValue("分類");
            row1.createCell(7).setCellValue("表格名稱");
            row1.createCell(8).setCellValue("FORMID");
            row1.createCell(9).setCellValue("表格路徑");
        }

        int rowNum = 2;
        XSSFRow rrow = null;
        long startTime=System.currentTimeMillis();
        List<Crawler>   newCrawler = new ArrayList<Crawler>();
        for(int Obj = 0 ; Obj < listObject.size(); Obj++){
            String object= listObject.get(Obj).getObject();
            String catry = listObject.get(Obj).getClassification();
            List<Crawler> list = null;
            List<Crawler> listT = null;
            if(!catry.startsWith("學生資助處")){
                list = jsoupUtils.getCrawlerList(reptiledate,catry,object,language);//获取昨天天数据
                listT = jsoupUtils.getCrawlerList(reptiledateT,catry,object,language);//获取今天数据

                if(list.size() >= listT.size()){//昨天数据大于今天数据
                    for(int i = 0 ;i < list.size(); i ++){
                        rrow = sheet.createRow(rowNum);
                        Crawler crawlerY = list.get(i);
                        for (int j = 0; j < listT.size(); j++) {
                            Crawler crawlerT = listT.get(j);
                            //昨天数据和今天数据比较
                            if(crawlerY.getClassification().equals(crawlerT.getClassification()) && crawlerY.getObject().equals(crawlerY.getObject()) && crawlerY.getFormname().equals(crawlerT.getFormname())){
                                if( (crawlerY.getFormid() !=null && crawlerT.getFormid() != null) && crawlerY.getFormid().equals(crawlerT.getFormid())){ //formId 不为空，并且都相等
                                    rrow.createCell(5).setCellValue(listT.get(j).getClassification());
                                    rrow.createCell(6).setCellValue(listT.get(j).getObject());
                                    Cell rrowWarp7 = rrow.createCell(7);
                                    rrowWarp7.setCellStyle(otherWarp);
                                    rrowWarp7.setCellValue(listT.get(j).getFormname());
                                    rrow.createCell(8).setCellValue(listT.get(j).getFormid());
                                    if( (crawlerY.getFormurl() == null && crawlerT.getFormurl() == null) || (crawlerY.getFormurl() != null && crawlerY.getFormurl().equals(crawlerT.getFormurl()))){
                                        Cell rrowWarp1 = rrow.createCell(9);
                                        rrowWarp1.setCellStyle(setWarp);
                                        rrowWarp1.setCellValue(listT.get(j).getFormurl());
                                    }else{
                                        Cell rrowWarp9 = rrow.createCell(9);
                                        rrowWarp9.setCellStyle(style);
                                        rrowWarp9.setCellValue(listT.get(j).getFormurl());

                                    }
                                    listT.remove(crawlerT);
                                    break;
                                }else{
                                    if((crawlerY.getFormid() == null && crawlerT.getFormid() == null) || (crawlerY.getFormid() != null && crawlerY.getFormid().equals(crawlerT.getFormid()))){
                                        rrow.createCell(5).setCellValue(listT.get(j).getClassification());
                                        rrow.createCell(6).setCellValue(listT.get(j).getObject());
                                        Cell rrowWarp7 = rrow.createCell(7);
                                        rrowWarp7.setCellStyle(otherWarp);
                                        rrowWarp7.setCellValue(listT.get(j).getFormname());
                                        rrow.createCell(8).setCellValue(listT.get(j).getFormid());
                                        if( (crawlerY.getFormurl() == null && crawlerT.getFormurl() == null) || (crawlerY.getFormurl() != null && crawlerY.getFormurl().equals(crawlerT.getFormurl()))){
                                            Cell rrowWarp1 = rrow.createCell(9);
                                            rrowWarp1.setCellStyle(setWarp);
                                            rrowWarp1.setCellValue(listT.get(j).getFormurl());
                                        }else{
                                            Cell rrowWarp9 = rrow.createCell(9);
                                            rrowWarp9.setCellStyle(style);
                                            rrowWarp9.setCellValue(listT.get(j).getFormurl());
                                        }
                                        listT.remove(crawlerT);
                                        break;
                                    }

                                }


                            } else{//今天没数据，昨天有数据
                                if(j == list.size()-1 && !crawlerY.getFormname().equals(crawlerT.getFormname())){
                                    rrow.createCell(5).setCellValue("");
                                    rrow.createCell(6).setCellValue("");
                                    rrow.createCell(7).setCellValue("");
                                    rrow.createCell(8).setCellValue("");
                                    rrow.createCell(8).setCellValue("");
                                    break;
                                }
                            }
                        }
                        rrow.createCell(0).setCellValue(list.get(i).getClassification());
                        rrow.createCell(1).setCellValue(list.get(i).getObject());
                        Cell rrowWarp2 = rrow.createCell(2);
                        rrowWarp2.setCellStyle(otherWarp);
                        rrowWarp2.setCellValue(list.get(i).getFormname());
                        Cell rrowWarp3 = rrow.createCell(3);
                        rrowWarp3.setCellStyle(otherWarp);
                        rrowWarp3.setCellValue(list.get(i).getFormid());
                        Cell rrowWarp = rrow.createCell(4);
                        rrowWarp.setCellStyle(setWarp);
                        rrowWarp.setCellValue(list.get(i).getFormurl());
                        rowNum++;
                    }

                }else{//今天数据大于昨天数据
                    for(int i = 0 ;i < listT.size(); i ++){
                        rrow = sheet.createRow(rowNum);
                        Crawler crawlerT = listT.get(i);
                        for (int j = 0; j < list.size(); j++) {
                            Crawler crawlerY = list.get(j);
                            if(crawlerY.getClassification().equals(crawlerT.getClassification()) && crawlerY.getObject().equals(crawlerY.getObject()) && crawlerY.getFormname().equals(crawlerT.getFormname())){
                                if((crawlerY.getFormid() != null &&  crawlerT.getFormid() != null) && crawlerY.getFormid().equals(crawlerT.getFormid())){
                                    //昨天数据
                                    rrow.createCell(0).setCellValue(list.get(j).getClassification());
                                    rrow.createCell(1).setCellValue(list.get(j).getObject());
                                    Cell rrowWarp2 = rrow.createCell(2);
                                    rrowWarp2.setCellStyle(otherWarp);
                                    rrowWarp2.setCellValue(list.get(j).getFormname());
                                    Cell rrowWarp3 = rrow.createCell(3);
                                    rrowWarp3.setCellStyle(otherWarp);
                                    rrowWarp3.setCellValue(list.get(j).getFormid());
                                    Cell rrowWarp = rrow.createCell(4);
                                    rrowWarp.setCellStyle(setWarp);
                                    rrowWarp.setCellValue(list.get(j).getFormurl());
                                    //今天数据
                                    rrow.createCell(5).setCellValue(listT.get(i).getClassification());
                                    rrow.createCell(6).setCellValue(listT.get(i).getObject());
                                    Cell rrowWarp7 = rrow.createCell(7);
                                    rrowWarp7.setCellStyle(otherWarp);
                                    rrowWarp7.setCellValue(listT.get(i).getFormname());
                                    rrow.createCell(8).setCellValue(listT.get(i).getFormid());
                                    if( (crawlerY.getFormurl() == null && crawlerT.getFormurl() == null) || (crawlerY.getFormurl() != null && crawlerY.getFormurl().equals(crawlerT.getFormurl()))){
                                        Cell rrowWarp1 = rrow.createCell(9);
                                        rrowWarp1.setCellStyle(setWarp);
                                        rrowWarp1.setCellValue(listT.get(i).getFormurl());
                                    }else{
                                        Cell rrowWarp9 = rrow.createCell(9);
                                        rrowWarp9.setCellStyle(style);
                                        rrowWarp9.setCellValue(listT.get(i).getFormurl());

                                    }
                                    newCrawler.add(crawlerY);
                                    list.remove(crawlerY);
                                    break;
                                }else{
                                    if((crawlerY.getFormid() == null && crawlerT.getFormid() == null) || (crawlerY.getFormid() != null && crawlerY.getFormid().equals(crawlerT.getFormid()))){
                                        //昨天数据
                                        rrow.createCell(0).setCellValue(list.get(j).getClassification());
                                        rrow.createCell(1).setCellValue(list.get(j).getObject());
                                        Cell rrowWarp2 = rrow.createCell(2);
                                        rrowWarp2.setCellStyle(otherWarp);
                                        rrowWarp2.setCellValue(list.get(j).getFormname());
                                        Cell rrowWarp3 = rrow.createCell(3);
                                        rrowWarp3.setCellStyle(otherWarp);
                                        rrowWarp3.setCellValue(list.get(j).getFormid());
                                        Cell rrowWarp = rrow.createCell(4);
                                        rrowWarp.setCellStyle(setWarp);
                                        rrowWarp.setCellValue(list.get(j).getFormurl());
                                        //今天数据
                                        rrow.createCell(5).setCellValue(listT.get(i).getClassification());
                                        rrow.createCell(6).setCellValue(listT.get(i).getObject());
                                        Cell rrowWarp7 = rrow.createCell(7);
                                        rrowWarp7.setCellStyle(otherWarp);
                                        rrowWarp7.setCellValue(listT.get(i).getFormname());
                                        rrow.createCell(8).setCellValue(listT.get(i).getFormid());
                                        if( (crawlerY.getFormurl() == null && crawlerT.getFormurl() == null) || (crawlerY.getFormurl() != null && crawlerY.getFormurl().equals(crawlerT.getFormurl()))){
                                            Cell rrowWarp1 = rrow.createCell(9);
                                            rrowWarp1.setCellStyle(setWarp);
                                            rrowWarp1.setCellValue(listT.get(i).getFormurl());
                                        }else{
                                            Cell rrowWarp9 = rrow.createCell(9);
                                            rrowWarp9.setCellStyle(style);
                                            rrowWarp9.setCellValue(listT.get(i).getFormurl());

                                        }
                                        newCrawler.add(crawlerY);
                                        list.remove(crawlerY);
                                        break;
                                    }
                                }

                            }else{
                                if(j == list.size()-1 && !crawlerT.getFormname().equals(crawlerY.getFormname())){//今天有数据，昨天没数据
                                    rrow.createCell(0).setCellValue("");
                                    rrow.createCell(1).setCellValue("");
                                    rrow.createCell(2).setCellValue("");
                                    rrow.createCell(3).setCellValue("");
                                    rrow.createCell(4).setCellValue("");
                                    //rrow.createCell(5).setCellValue(listT.get(i).getClassification());
                                    Cell rrowWarp5 = rrow.createCell(5);
                                    rrowWarp5.setCellStyle(otherWarp);
                                    rrowWarp5.setCellValue(listT.get(i).getClassification());
                                    //rrow.createCell(6).setCellValue(listT.get(i).getObject());
                                    Cell rrowWarp6 = rrow.createCell(6);
                                    rrowWarp6.setCellStyle(otherWarp);
                                    rrowWarp6.setCellValue(listT.get(i).getObject());
                                    Cell rrowWarp7 = rrow.createCell(7);
                                    rrowWarp7.setCellStyle(otherWarp);
                                    rrowWarp7.setCellValue(listT.get(i).getFormname());
                                    Cell rrowWarp8 = rrow.createCell(8);
                                    rrowWarp8.setCellStyle(style);
                                    rrowWarp8.setCellValue(listT.get(i).getFormid());
                                    Cell rrowWarp9 = rrow.createCell(9);
                                    rrowWarp9.setCellStyle(style);
                                    rrowWarp9.setCellValue(listT.get(i).getFormurl());
                                    break;
                                }
                            }
                        }

                        rowNum++;
                    }
                    if(newCrawler.size() > 0 && list.size() > newCrawler.size()){//昨天有数据，今天没数据
                        for(int Y = 0 ; Y < list.size() ; Y ++){
                            rrow = sheet.createRow(rowNum);
                            for(int N = 0; N < newCrawler.size() ; N ++ ){
                                if(list.get(Y).getClassification().equals(newCrawler.get(N).getClassification()) && list.get(Y).getObject().equals(newCrawler.get(N).getObject()) && list.get(Y).getFormname().equals(newCrawler.get(N).getFormname())){
                                    break;
                                }else{
                                    if( N == newCrawler.size()-1 && !list.get(Y).getFormname().equals(newCrawler.get(N).getFormname())){
                                        rrow.createCell(0).setCellValue(list.get(Y).getClassification());
                                        rrow.createCell(1).setCellValue(list.get(Y).getObject());
                                        //rrow.createCell(2).setCellValue(list.get(Y).getFormname());
                                        Cell rrowWarp2 = rrow.createCell(2);
                                        rrowWarp2.setCellStyle(otherWarp);
                                        rrowWarp2.setCellValue(list.get(Y).getFormname());
                                        Cell rrowWarp3 = rrow.createCell(3);
                                        rrowWarp3.setCellStyle(otherWarp);
                                        rrowWarp3.setCellValue(list.get(Y).getFormid());
                                        Cell rrowWarp = rrow.createCell(4);
                                        rrowWarp.setCellStyle(setWarp);
                                        rrowWarp.setCellValue(list.get(Y).getFormurl());
                                        rowNum++;
                                    }

                                }

                            }

                        }
                    }
                }
            }

        }

      //  System.out.println("rowNum:"+rowNum);
      //  System.out.println("newCrawler:"+newCrawler);
        long endTime=System.currentTimeMillis(); //获取结束时间
        LOGGER.info("程序运行时间： "+(endTime-startTime)+"ms");
        OutputStream fileOut = new FileOutputStream(savePath);
        LOGGER.info("生产地址："+savePath);
        workBook.write(fileOut);
        fileOut.close();
    }
    public String getYest(){
        Calendar cale = Calendar.getInstance();
        int year = cale.get(Calendar.YEAR);
        int month = cale.get(Calendar.MONTH) + 1;
        int day = cale.get(Calendar.DATE);
        String getY ;
        if(day == 1){
            if(month == 1){
                getY = year-1 + "-12-16";
            }else{
                month = month-1;
                getY = year + "-"+ month  +"-"+"16";
            }
        }else{
            getY = year +"-"+month+"-"+"01";
        }
        return getY;
    }
    /**
     * 获取EXCEL文件路径
     * @return
     */
    public List getCheckLink(String url,String language){
        List list = new ArrayList();
        String path = getFilePath(language);
        File file = new File(path);
        File[] files = file.listFiles();
        String DirectoryName;
        if("en".equals(language)){
            DirectoryName = "crawlerlinkEN/";
        }else if("sc".equals(language)){
            DirectoryName = "crawlerlinkSC/";
        }else{
            DirectoryName = "crawlerlink/";
        }
        System.out.println("打开i的url:"+url + DirectoryName);
        for (int i = files.length-1 ; i >= 0 ; i--) {
            JSONObject map = new JSONObject();
            map.put("excelName",files[i].getName());
            map.put("URL",url + DirectoryName + files[i].getName());
            list.add(map);
        }

        LOGGER.info("Excel文件路径："+list);
        return list;
    }
    /**
     * 获取文件夹路径
     * @return
     */
    public String getFilePath(String language){
        String filePath = Crawler.class.getResource("").getPath();
        if ("en".equals(language)){
            filePath = filePath.split("WEB-INF")[0]+"crawlerlinkEN/";
        }else if("sc".equals(language) || "cn".equals(language)){
            filePath = filePath.split("WEB-INF")[0]+"crawlerlinkSC/";
        }else{
            filePath = filePath.split("WEB-INF")[0]+"crawlerlink/";
        }
        System.out.println("文件夹位置:" + filePath);
        File folder = new File(filePath);
        //文件夹路径不存在
        if (!folder.exists() && !folder.isDirectory()) {
            LOGGER.info("writeExcel文件夹路径不存在，创建路径:" + filePath);
            folder.mkdirs();
        }

        return filePath;
    }
}
