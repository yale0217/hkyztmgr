package com.xiaoi.south.manager;

import com.xiaoi.south.manager.crawler.CrawlerGOV;
import com.xiaoi.south.manager.utlis.CrawlerUtils;
import com.xiaoi.south.manager.utlis.HttpClinetUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HkyztmgrApplicationTests {
    HttpClinetUtils HttpClinetUtils = new HttpClinetUtils();
    CrawlerGOV jsoupUtils = new CrawlerGOV();
    @Test
    public void contextLoads() {
        UUID uuid=UUID.randomUUID();
        String uuidStr=uuid.toString().replaceAll("-","");
        System.out.println(uuidStr.length());
    }
    //康文署
    @Test
    public void getLSCD(){
        HttpClinetUtils HttpClinetUtils = new HttpClinetUtils();
        String html = HttpClinetUtils.doGet("https://www.lcsd.gov.hk/tc/aboutlcsd/forms.html","utf-8");
        String htmlEN = HttpClinetUtils.doGet("https://www.lcsd.gov.hk/en/aboutlcsd/forms.html","utf-8");
        String htmlSC = HttpClinetUtils.doGet("https://www.lcsd.gov.hk/sc/aboutlcsd/forms.html","utf-8");
        jsoupUtils.getLCSD(htmlSC,"cn");
    }
    //康文署 -- 图书馆
    @Test
    public void getLibrary(){
        HttpClinetUtils HttpClinetUtils = new HttpClinetUtils();
        String html = HttpClinetUtils.doGet("https://www.hkpl.gov.hk/tc/about-us/forms.html","utf-8");
        jsoupUtils.getLibrary(html);
    }
    //康文署 -- 图书馆
    @Test
    public void getMuseum(){
        HttpClinetUtils HttpClinetUtils = new HttpClinetUtils();
        String html = HttpClinetUtils.doGet("https://www.museums.gov.hk/zh_TW/web/portal/resource-centre.html","utf-8");
        jsoupUtils.getMuseum(html);
    }
    //劳工处
    @Test
    public void getLD(){
        HttpClinetUtils HttpClinetUtils = new HttpClinetUtils();
        String html = HttpClinetUtils.doGet("https://www.info.gov.hk/cgi-bin/forms/csearch201402.cgi?formnoinput=&keywordinput=&deptinput=LABD&dept=++%B7j+%B4M++","utf-8");
        String htmlEN = HttpClinetUtils.doGet("https://www.info.gov.hk/cgi-bin/forms/esearch201402.cgi?formnoinput=&keywordinput=&deptinput=LABD&dept=++%B7j+%B4M++","utf-8");
        jsoupUtils.getLD(htmlEN,"en");

    }
    //税务局
    @Test
    public void getIRDTest(){
        HttpClinetUtils HttpClinetUtils = new HttpClinetUtils();
        String html = HttpClinetUtils.doGet("https://www.ird.gov.hk/chi/paf/for.htm","utf-8");
        String htmlSC = HttpClinetUtils.doGet("https://www.ird.gov.hk/chs/paf/for.htm","utf-8");
        String htmlEN = HttpClinetUtils.doGet("https://www.ird.gov.hk/eng/paf/for.htm","utf-8");
        jsoupUtils.getIRD(htmlSC,"cn");

    }
    /**
     * 香港警務處
     */
    @Test
    public void getHKPF(){
        HttpClinetUtils HttpClinetUtils = new HttpClinetUtils();
        String html = HttpClinetUtils.doGet("https://www.police.gov.hk/ppp_tc/08_forms/","utf-8");
        String htmlSC = HttpClinetUtils.doGet("https://www.police.gov.hk/ppp_sc/08_forms/","utf-8");
        String htmlEN = HttpClinetUtils.doGet("https://www.police.gov.hk/ppp_en/08_forms/","utf-8");
       // CrawlerGOV jsoupUtils = new CrawlerGOV();
        jsoupUtils.getHKPF(htmlEN,"en");
    }

    /**
     * 入境处
     */
    @Test
    public void getIMD(){

        try {
            String residence = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/right-of-abode-in-hksar.html","utf-8");
            jsoupUtils.getIMD(residence,"居留權","tc");
            String ID_card = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/hkid.html","utf-8");
            jsoupUtils.getIMD(ID_card,"身份證","tc");
            String travel = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-travel-doc.html","utf-8");
            jsoupUtils.getIMD(travel,"旅行證件","tc");
            String Chinese_nationality = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/chinese-nationality-application-form.html","utf-8");
            jsoupUtils.getIMD(Chinese_nationality,"中國國籍","tc");
            String birthAndDeath = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/birth-death-marriage.html","utf-8");
            jsoupUtils.getIMD(birthAndDeath,"出生及死亡登記","tc");
            String matrimony = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/marriage.html","utf-8");
            jsoupUtils.getIMD(matrimony,"婚姻登記","tc");
            String other = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/other.html","utf-8");
            jsoupUtils.getIMD(other,"其他","tc");

            String Visa1 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/employment-as-professionals.html","utf-8");
            jsoupUtils.getIMD(Visa1,"簽證/專業人士來港就業","tc");
            String Visa2 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/techtas.html","utf-8");
            jsoupUtils.getIMD(Visa2,"簽證/科技人才入境計劃","tc");
            String Visa3 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/investment.html","utf-8");
            jsoupUtils.getIMD(Visa3,"簽證/企業家來港投資","tc");
            String Visa4 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/capital-investment-entrant.html","utf-8");
            jsoupUtils.getIMD(Visa4,"簽證/資本投資者入境計劃","tc");
            String Visa5 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/training.html","utf-8");
            jsoupUtils.getIMD(Visa5,"簽證/受訓","tc");
            String Visa6 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/foreign-domestic-helpers.html","utf-8");
            jsoupUtils.getIMD(Visa6,"簽證/從外國聘用家庭傭工","tc");
            String Visa7 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/employment-as-imported-workers.html","utf-8");
            jsoupUtils.getIMD(Visa7,"簽證/輸入勞工來港就業","tc");
            String Visa8 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/extension-of-stay.html","utf-8");
            jsoupUtils.getIMD(Visa8,"簽證/延長逗留期限及旅行證件加蓋簽注","tc");
            String Visa9 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/quality-migrant-admission.html","utf-8");
            jsoupUtils.getIMD(Visa9,"簽證/優秀人才入境計劃","tc");
            String Visa10 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/residence-as-dependants.html","utf-8");
            jsoupUtils.getIMD(Visa10,"簽證/受養人來港居留","tc");
            String Visa11 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/assg.html","utf-8");
            jsoupUtils.getIMD(Visa11,"簽證/輸入中國籍香港永久性居民第二代計劃","tc");
            String Visa12 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/study.html","utf-8");
            jsoupUtils.getIMD(Visa12,"簽證/就讀","tc");
            String Visa13 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/visit-transit.html","utf-8");
            jsoupUtils.getIMD(Visa13,"簽證/旅遊/過境","tc");
            String Visa14 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/working-holiday.html","utf-8");
            jsoupUtils.getIMD(Visa14,"簽證/工作假期計劃","tc");
            String Visa15 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/hksar-travel-pass.html","utf-8");
            jsoupUtils.getIMD(Visa15,"簽證/香港特別行政區旅遊通行證","tc");
            String Visa16 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/apec-business-travel-card.html","utf-8");
            jsoupUtils.getIMD(Visa16,"簽證/亞太經合組織商務旅遊證","tc");
            String Visa17 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hkt/forms/hk-visas/other.html","utf-8");
            jsoupUtils.getIMD(Visa17,"簽證/其他","tc");
            //List list = jsoupUtils.getIMD(ss.toString());
            //addKnowledgeUtils.addKnowledge(list,"","其他".replaceAll(" ","").trim(),String.valueOf(new Random().nextInt(899999) + 100000));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void getIMDEN(){

        try {
            String residence = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/right-of-abode-in-hksar.html","utf-8");
            jsoupUtils.getIMD(residence,"Right of Abode","en");
            String ID_card = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/hkid.html","utf-8");
            jsoupUtils.getIMD(ID_card,"Identity Cards","en");
            String travel = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-travel-doc.html","utf-8");
            jsoupUtils.getIMD(travel,"Travel Documents","en");
            String Chinese_nationality = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/chinese-nationality-application-form.html","utf-8");
            jsoupUtils.getIMD(Chinese_nationality,"Chinese Nationality","en");
            String birthAndDeath = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/birth-death-marriage.html","utf-8");
            jsoupUtils.getIMD(birthAndDeath,"Births and Deaths Registration","en");
            String matrimony = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/marriage.html","utf-8");
            jsoupUtils.getIMD(matrimony,"Marriage Registration","en");
            String other = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/other.html","utf-8");
            jsoupUtils.getIMD(other,"VISA/Others","en");

            String Visa1 = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/employment-as-professionals.html","utf-8");
            jsoupUtils.getIMD(Visa1,"Employment as Professionals (GEP, IANG, ASMTP)","en");
            String Visa2 = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/techtas.html","utf-8");
            jsoupUtils.getIMD(Visa2,"Technology Talent Admission Scheme (TechTAS)","en");
            String Visa3 = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/investment.html","utf-8");
            jsoupUtils.getIMD(Visa3,"Investment as Entrepreneurs","en");
            String Visa4 = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/capital-investment-entrant.html","utf-8");
            jsoupUtils.getIMD(Visa4,"Capital Investment Entrant Scheme","en");
            String Visa5 = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/training.html","utf-8");
            jsoupUtils.getIMD(Visa5,"Training","en");
            String Visa6 = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/foreign-domestic-helpers.html","utf-8");
            jsoupUtils.getIMD(Visa6,"Employment of Domestic Helpers from Abroad","en");
            String Visa7 = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/employment-as-imported-workers.html","utf-8");
            jsoupUtils.getIMD(Visa7,"Employment as Imported Workers","en");
            String Visa8 = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/extension-of-stay.html","utf-8");
            jsoupUtils.getIMD(Visa8,"Extension of Stay & Transfer of Endorsement","en");
            String Visa9 = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/quality-migrant-admission.html","utf-8");
            jsoupUtils.getIMD(Visa9,"Quality Migrant Admission Scheme","en");
            String Visa10 = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/residence-as-dependants.html","utf-8");
            jsoupUtils.getIMD(Visa10,"Residence as dependant","en");
            String Visa11 = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/assg.html","utf-8");
            jsoupUtils.getIMD(Visa11,"Admission Scheme for the Second Generation of Chinese Hong Kong Permanent Residents","en");
            String Visa12 = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/study.html","utf-8");
            jsoupUtils.getIMD(Visa12,"Study","en");
            String Visa13 = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/visit-transit.html","utf-8");
            jsoupUtils.getIMD(Visa13,"Visit / Transit","en");
            String Visa14 = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/working-holiday.html","utf-8");
            jsoupUtils.getIMD(Visa14,"Working Holiday Scheme","en");
            String Visa15 = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/hksar-travel-pass.html","utf-8");
            jsoupUtils.getIMD(Visa15,"Hong Kong Special Administrative Region Travel Pass","en");
            String Visa16 = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/apec-business-travel-card.html","utf-8");
            jsoupUtils.getIMD(Visa16,"APEC Business Travel Card","en");
            String Visa17 = HttpClinetUtils.doGet("https://www.immd.gov.hk/eng/forms/hk-visas/other.html","utf-8");
            jsoupUtils.getIMD(Visa17,"Other Applications","en");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void getIMDSC(){

        try {
            String residence = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/right-of-abode-in-hksar.html","utf-8");
            jsoupUtils.getIMD(residence,"居留权","cn");
            String ID_card = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/hkid.html","utf-8");
            jsoupUtils.getIMD(ID_card,"身份证","cn");
            String travel = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-travel-doc.html","utf-8");
            jsoupUtils.getIMD(travel,"旅行证件","cn");
            String Chinese_nationality = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/chinese-nationality-application-form.html","utf-8");
            jsoupUtils.getIMD(Chinese_nationality,"中国国籍","cn");
            String birthAndDeath = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/birth-death-marriage.html","utf-8");
            jsoupUtils.getIMD(birthAndDeath,"出生及死亡登记","cn");
            String matrimony = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/marriage.html","utf-8");
            jsoupUtils.getIMD(matrimony,"婚姻登记","cn");
            String other = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/other.html","utf-8");
            jsoupUtils.getIMD(other,"其他","cn");

            String Visa1 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/employment-as-professionals.html","utf-8");
            jsoupUtils.getIMD(Visa1,"签证/专业人士来港就业","cn");
            String Visa2 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/techtas.html","utf-8");
            jsoupUtils.getIMD(Visa2,"签证/科技人才入境计划","cn");
            String Visa3 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/investment.html","utf-8");
            jsoupUtils.getIMD(Visa3,"签证/企业家来港投资","cn");
            String Visa4 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/capital-investment-entrant.html","utf-8");
            jsoupUtils.getIMD(Visa4,"签证/资本投资者入境计划","cn");
            String Visa5 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/training.html","utf-8");
            jsoupUtils.getIMD(Visa5,"签证/受训","cn");
            String Visa6 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/foreign-domestic-helpers.html","utf-8");
            jsoupUtils.getIMD(Visa6,"签证/从外国聘用家庭佣工","cn");
            String Visa7 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/employment-as-imported-workers.html","utf-8");
            jsoupUtils.getIMD(Visa7,"签证/输入劳工来港就业","cn");
            String Visa8 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/extension-of-stay.html","utf-8");
            jsoupUtils.getIMD(Visa8,"签证/延长逗留期限及旅行证件加盖签注","cn");
            String Visa9 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/quality-migrant-admission.html","utf-8");
            jsoupUtils.getIMD(Visa9,"签证/优秀人才入境计划","cn");
            String Visa10 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/residence-as-dependants.html","utf-8");
            jsoupUtils.getIMD(Visa10,"签证/受养人来港居留","cn");
            String Visa11 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/assg.html","utf-8");
            jsoupUtils.getIMD(Visa11,"签证/输入中国籍香港永久居民第二代计划","cn");
            String Visa12 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/study.html","utf-8");
            jsoupUtils.getIMD(Visa12,"签证/就读","cn");
            String Visa13 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/visit-transit.html","utf-8");
            jsoupUtils.getIMD(Visa13,"签证/旅游/过境","cn");
            String Visa14 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/working-holiday.html","utf-8");
            jsoupUtils.getIMD(Visa14,"签证/工作假期计划","cn");
            String Visa15 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/hksar-travel-pass.html","utf-8");
            jsoupUtils.getIMD(Visa15,"签证/香港特别行政区旅游通行证","cn");
            String Visa16 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/apec-business-travel-card.html","utf-8");
            jsoupUtils.getIMD(Visa16,"签证/亚太经合组织商务旅游签证","cn");
            String Visa17 = HttpClinetUtils.doGet("https://www.immd.gov.hk/hks/forms/hk-visas/other.html","utf-8");
            jsoupUtils.getIMD(Visa17,"签证/其他","cn");
            //List list = jsoupUtils.getIMD(ss.toString());
            //addKnowledgeUtils.addKnowledge(list,"","其他".replaceAll(" ","").trim(),String.valueOf(new Random().nextInt(899999) + 100000));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  房屋署
     */
    @Test
    public void getHD(){
        HttpClinetUtils HttpClinetUtils = new HttpClinetUtils();
        String html = HttpClinetUtils.doGet("https://www.housingauthority.gov.hk/tc/global-elements/forms/index.html","utf-8");
        String htmlSC = HttpClinetUtils.doGet("https://www.housingauthority.gov.hk/sc/global-elements/forms/index.html","utf-8");
        String htmlEN = HttpClinetUtils.doGet("https://www.housingauthority.gov.hk/en/global-elements/forms/index.html","utf-8");
         jsoupUtils.getHD(htmlEN,"en");
    }
    /**
     *屋宇署
     */
    @Test
    public void BDtest(){
        HttpClinetUtils HttpClinetUtils = new HttpClinetUtils();
        String html = HttpClinetUtils.doGet("https://www.bd.gov.hk/tc/resources/forms/index.html","utf-8");
        String bd = HttpClinetUtils.doGet("https://www.bd.gov.hk/sc/resources/forms/index.html","utf-8");
        String bd1 = HttpClinetUtils.doGet("https://www.bd.gov.hk/en/resources/forms/index.html","utf-8");
        jsoupUtils.getBDUrl(html,"tc");
    }

    /**
     * 在職家庭及學生資助事務處
     */
    @Test
    public void getWFSFAA(){
        //todo
        HttpClinetUtils HttpClinetUtils = new HttpClinetUtils();
        String html = HttpClinetUtils.doGet("https://www.wfsfaa.gov.hk/sfo/tc/forms/form.htm","utf-8");
        jsoupUtils.getWFSFAA(html);
    }
    /**
     * 申訴專員公署
     */
    @Test
    public  void getOMB(){
        HttpClinetUtils HttpClinetUtils = new HttpClinetUtils();
        String html = HttpClinetUtils.doGet("https://www.ombudsman.hk/zh-hk/form_download.html","utf-8");
        String omb = HttpClinetUtils.doGet("https://www.ombudsman.hk/en-us/form_download.html","utf-8");
        String omb1 = HttpClinetUtils.doGet("https://www.ombudsman.hk/zh-cn/form_download.html","utf-8");
        jsoupUtils.getOMB(omb1,"cn");
    }
    /**
     * 個人資料私隱專員公署
     */
    @Test
    public void getPCPD(){
        HttpClinetUtils HttpClinetUtils = new HttpClinetUtils();
        String html = HttpClinetUtils.doGet("https://www.pcpd.org.hk/tc_chi/resources_centre/publications/forms/forms.html","utf-8");
        String pcpd = HttpClinetUtils.doGet("https://www.pcpd.org.hk/sc_chi/resources_centre/publications/forms/forms.html","utf-8");
        String pcpden = HttpClinetUtils.doGet("https://www.pcpd.org.hk/english/resources_centre/publications/forms/forms.html","utf-8");
         jsoupUtils.getPCPD(pcpden,"en");
    }

    /**
     * 运输署
     */
    @Test
    public void getTD(){
        HttpClinetUtils HttpClinetUtils = new HttpClinetUtils();
        //String html = HttpClinetUtils.doGet("https://www.td.gov.hk/tc/public_forms/td_forms/index.html","utf-8");
        String html = HttpClinetUtils.doGet("https://www.td.gov.hk/en/public_forms/td_forms/index.html","utf-8"); //英文
        String td = HttpClinetUtils.doGet("https://www.td.gov.hk/sc/public_forms/td_forms/index.html","utf-8");
        jsoupUtils.getTD(td,"cn");
    }
    @Test
    public void crawlerTasktest() throws Exception {
        CrawlerUtils crawlerUtils = new CrawlerUtils();
        //crawlerUtils.getCrawler();
        //List<Crawler> listObject = crawlerUtils.getCrawlerListObject();
        crawlerUtils.writeExcel("cn");
    }
@Test
    public void getYest(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();//new一个Calendar类,把Date放进去
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -15);
        System.out.println("昨天的日期为:" + sdf.format(calendar.getTime()));
     //   return sdf.format(calendar.getTime());
    }
    @Test
    public void getFirstDay(){
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        System.out.println(sdf.toString());
//        Calendar calendar1=Calendar.getInstance();
//        calendar1.set(Calendar.DAY_OF_MONTH, 1);
//        System.out.println("本月第一天: "+sdf.format(calendar1.getTime()));
        //return sdf.format(calendar1.getTime());
        // 获取当前年份、月份、日期
        Calendar cale = Calendar.getInstance();
       // cale = Calendar.getInstance();
        int year = cale.get(Calendar.YEAR);
        int month = cale.get(Calendar.MONTH) + 1;
        int day = cale.get(Calendar.DATE);
        String getY ;
        System.out.println("Current Date: " + cale.getTime());
        System.out.println("Year: " + year);
        System.out.println("Month: " + month);
        System.out.println("Day: " + day);

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

    }
    @Test
    public void testUrl(){
        HttpClinetUtils httpClinetUtils = new HttpClinetUtils();
        String language = "en";
        String url = "http://govhkpocr.demo.xiaoi.com/HKyztRobot/irobot/getCheckLine?language="+language;
        String json = httpClinetUtils.sendRequestBody(url,null,"utf-8");
        System.out.println(json);
    }
}
