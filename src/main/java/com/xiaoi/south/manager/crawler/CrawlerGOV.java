package com.xiaoi.south.manager.crawler;

import com.alibaba.fastjson.JSONObject;
import com.xiaoi.south.manager.entity.Crawler;
import com.xiaoi.south.manager.entity.TableBean;
import com.xiaoi.south.manager.service.CrawlerService;

import com.xiaoi.south.manager.utlis.HttpClinetUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xiaoi.south.manager.utlis.HttpClinetUtils.removeComments;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 政府网站爬虫
 */
@Component
public class CrawlerGOV {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerGOV.class);
    @Autowired
    private   CrawlerService crawlerService;
    public  static CrawlerGOV crawlerGOV ;
    HttpClinetUtils httpClinetUtils = new HttpClinetUtils();
    //初使化时将已静态化的crawlerService实例化
    @PostConstruct //通过@PostConstruct实现初始化bean之前进行的操作
    public void init() {
        crawlerGOV = this;
        crawlerGOV.crawlerService = this.crawlerService;

    }
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 屋宇署
     * @param html
     */
    public  void getBDUrl(String html,String language){
        //HttpClinetUtils HttpClinetUtils = new HttpClinetUtils();
        Document document = Jsoup.parse(html);
        Elements li = document.select("div[class=\"titleBox orange\"]").select("ul[class=\"plainBlueArrow\"]").select("li");
        Long id =6435412321L;
        for (int i = 0; i < li.size(); i++) {
            String url = li.get(i).select("a").attr("href");
            String content = li.get(i).select("a").text();
            System.out.println("【url】："+url+"【content】："+content);
            String result_html ;
            if("en".equals(language)){
                result_html = httpClinetUtils.doGet("https://www.bd.gov.hk/en/resources/forms/"+url,"utf-8");
            }else if("cn".equals(language)){
                result_html = httpClinetUtils.doGet("https://www.bd.gov.hk/sc/resources/forms/"+url,"utf-8");
            }else{
                result_html = httpClinetUtils.doGet("https://www.bd.gov.hk/tc/resources/forms/"+url,"utf-8");
            }

            id = id * 1234;
            getBDdate(result_html,id,content,language);
        }
    }
    public void getBDdate(String result_html,long id,String content,String language){
        Document document = Jsoup.parse(result_html);
        List list = new ArrayList();
        List<Crawler> listHK = new ArrayList<Crawler>();
        String object = null;
        Elements h4 = document.select("div[id=\"pane-D\"]").select("div[id=\"collapseFour\"]").select("div[class=\"card-body\"]").select("h4[class=\"blue\"]");
        Elements div = document.select("div[id=\"pane-D\"]").select("div[id=\"collapseFour\"]").select("div[class=\"card-body\"]").select("div[class=\"fullWidthWrap\"]");
        String catory = null;
        if("en".equals(language)){
            catory = "Buildings Department";
        }else if("cn".equals(language)){
            catory = "屋宇署";
        }else{
            catory = "屋宇署";
        }
        for (int i = 0; i < div.size(); i++) {
            if(h4.size() > 0){
                object = h4.get(i).text();
                id = id * 82;
            }
           // Elements tr = div.get(i).select("table[class=\"transformable mb-1\"]").select("tbody").select("tr");
            Elements tr = div.get(i).select("table").select("tbody").select("tr");
            for (int j = 0; j < tr.size(); j++) {
                String time = sdf.format(new Date());
                Crawler crawler = new Crawler();
                JSONObject map = new JSONObject();
                String formUrl = tr.get(j).select("td").get(0).select("a").attr("href");
                formUrl = formUrl.replaceAll("../../../","https://www.bd.gov.hk/");
                if(formUrl.startsWith("../../")){
                    if("en".equals(language)){
                        formUrl = formUrl.replaceAll("../../","https://www.bd.gov.hk/en/");
                    }else if("cn".equals(language)){
                        formUrl = formUrl.replaceAll("../../","https://www.bd.gov.hk/sc/");
                    }else{
                        formUrl = formUrl.replaceAll("../../","https://www.bd.gov.hk/tc/");
                    }

                }
                String formId = tr.get(j).select("td").get(0).select("a").text();
                String regex = "\\「.*?」";
                formId = formId.replaceAll("[\u4e00-\u9fa5]+", "");
                formId = formId.replaceAll(regex, "");
                formId = formId.replaceAll(" ","");
                String forName = tr.get(j).select("td").get(1).ownText();
                forName = forName.split("詳細資料")[0].replaceAll("\\(","（").replaceAll("\\)","）");
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                crawler.setFormname(forName);
                crawler.setFormurl(formUrl);
                crawler.setReptiletime(df.format(new Date()));
                crawler.setReptiledate(time);
                crawler.setId(uuid);

                map.put("id",id);
                map.put("restUrl",formUrl );
                if(formId.trim().length() > 0){
                    map.put("formId",formId);
                    crawler.setFormid(formId);
                }

                map.put("title",forName);
                if(object != null){
                    map.put("object",object.replaceAll(" ",""));
                    map.put("category",catory + "/" + content);
                    crawler.setClassification(catory +"/" + content);
                    crawler.setObject(object.replaceAll(" ",""));
                }else{
                    if("其他表格".equals(content) || "Other forms".equals(content)){
                        map.put("object",catory + content.replaceAll(" ",""));
                        crawler.setObject(catory + content.replaceAll(" ",""));
                    }else{
                        map.put("object",content.replaceAll(" ",""));
                        crawler.setObject(content.replaceAll(" ",""));
                    }
                    crawler.setClassification(catory);
                    map.put("category",catory);
                }
                listHK.add(crawler);
                list.add(map);
                //System.out.println("【object】："+object+"【formUrl】："+formUrl+"【formId】："+formId+"【forName】："+forName);
            }
        }
        System.out.println("listHK = " + listHK.size());
        if(listHK.size() > 0 ){
//            crawlerGOV.crawlerService.insertForeach(listHK);
            insertDB(listHK,language);
        }

        System.out.println("最终组合结果："+listHK);
    }
    /**
     * 房屋署 数据获取
     * @param html
     * @return
     */
    public List getHD(String html ,String language){
        List list = new ArrayList();
        Document document = Jsoup.parse(html);
        removeComments(document);
        Elements element = document.select("ul").select("li");
        int ids = 70870823;
        for(int i = 0 ; i < element.size()-1;i++){
            String id = ids+i+2*123+"";
            JSONObject map = new JSONObject();
            String title = element.get(i).text().replaceAll("\\(","（").replaceAll("\\)","）");
            String restUrl = "https://www.housingauthority.gov.hk" + element.get(i).select("a").attr("href");
            HttpClinetUtils HttpClinetUtils = new HttpClinetUtils();
            System.out.println("====restUrl=="+restUrl);
            String dates = httpClinetUtils.doGet(restUrl,"utf-8");
            getHDDates(dates,title,id,language);
        }
        //  System.out.println("list=="+list);
        return list;
    }

    public List getHDDates(String dates, String title, String id,String language){
        List list = new ArrayList();
        List<Crawler> listHK = new ArrayList<Crawler>();
        Document document = Jsoup.parse(dates);
        removeComments(document);
        String[] T  = null;
        Elements element = document.select("table[class=\"rwd-table\"]");
        String catory = null;
        if("en".equals(language)){
            catory = "Housing Department/";
            T  = getHDDFObjectEN(title);
        }else if("cn".equals(language)){
            catory = "房屋署/";
            T  = getHDDFObjectSC(title);
        }else{
            catory = "房屋署/";
            T  = getHDDFObject(title);
        }
        if(T != null){
            for(int i = 0 ; i < element.size();i++){
                id = id + i;
                Elements tr = element.get(i).select("tbody").select("tr");
                String obejct = null;
                String category =catory + title;
                if( T[i].indexOf(":") != -1){
                    category = category+"/"+T[i].split(":")[0];
                    obejct = T[i].split(":")[1];
                }else{
                    obejct = T[i];
                }
                for(int P = 0; P < tr.size(); P++){
                    String time = sdf.format(new Date());
                    Crawler crawler = new Crawler();
                    JSONObject map = new JSONObject();
                    String formName = tr.get(P).select("td").get(0).text();
                    String formId = tr.get(P).select("td").get(1).text();
                    if(formId.trim().equals("-")){
                        formId = "";
                    }
                    String restUrl ;
                    if(tr.get(P).select("td").get(3).select("a").size() != 1){
                        restUrl =  getHDDFormUrl(tr.get(P).select("td").get(3).toString());
                    }else{
                        restUrl = "https://www.housingauthority.gov.hk"+tr.get(P).select("td").get(3).select("a").attr("href");
                    }
                    map.put("title",formName);
                    map.put("restUrl",restUrl);
                    map.put("formId",formId);
                    map.put("object",obejct.replaceAll(" / ","，"));
                    map.put("id",id);
                    map.put("category",category.replaceAll(" / ","，"));
                    list.add(map);
                    String uuid = UUID.randomUUID().toString().replaceAll("-","");
                    crawler.setClassification(category.replaceAll(" / ","，"));
                    crawler.setObject(obejct.replaceAll(" / ","，"));
                    crawler.setFormid(formId);
                    crawler.setFormname(formName);
                    crawler.setFormurl(restUrl);
                    crawler.setReptiletime(df.format(new Date()));
                    crawler.setReptiledate(time);
                    crawler.setId(uuid);
                    listHK.add(crawler);
                }
            }
        }

        System.out.println("listHK = " + listHK.size());
        if(listHK.size() > 0 ){
           // crawlerGOV.crawlerService.insertForeach(listHK);
            insertDB(listHK,language);
        }

        System.out.println("最终结果："+listHK);
        return listHK;
    }
    public String[] getHDDFObject(String title){
        String[]  T1 ={"公屋申請表格及須知","公屋申請聲明書:入息聲明書","公屋申請聲明書:資產及入息聲明書","公屋申請聲明書:其他事項聲明書","更改資料"};
        String[]  T2 ={"更改戶籍資料","入息及資產申報","租約事宜","裝修及安裝","其他事宜"};
        String[]  T3 ={"更改戶籍資料及業權（只供簽立轉讓契據後使用）","買方資料變更（只供簽立轉讓契據前使用）:居者有其屋計劃","買方資料變更（只供簽立轉讓契據前使用）:綠表置居計劃","按揭及地價","置業資助貸款計劃"};
        String[]  T4 ={"購買資格證明書","可供出售證明書","臨時買賣合約","提名信","確認聲明"};
        String[]  T5 ={"投標及租賃"};
        String[]  T6 ={"商業 / 非住宅單位","工廠大廈"};
        String[]  T7 = null;
        String[]  T8 ={"其他人士"};
        String[] T = null;
        if(title.equals("公屋申請者")){
            T = T1;
        } else if (title.equals("公屋住戶")) {
            T = T2;
        }else if(title.equals("居屋 / 綠置居 / 租置計劃 / 置業資助貸款計劃單位業主")){
            T = T3;
        }else if(title.equals("居屋第二市場買家及賣家")){
            T = T4;
        }else if(title.equals("商業單位申請人")){
            T = T5;
        }else if(title.equals("商業單位租戶")){
            T = T6;
        }else if(title.equals("建築專業人士及承建商（認可人士 / 註冊結構工程師 / 註冊岩土工程師 / 註冊一般建築承建商 / 註冊專門建築承建商 / 註冊小型工程承建商 / 註冊檢驗人員 / 合資格人士）")){
            T = T7;
        }else if(title.equals("其他人士")){
            T = T8;
        }

        return T;
    }
    public String[] getHDDFObjectEN(String title){
        String[]  T1 ={"Application Form and Guide","Declarations for Application:Income Declaration Forms","Declarations for Application:Asset and Income Declaration Forms","Declarations for Application:Other Declaration Forms","Change of Information"};
        String[]  T2 ={"Changes of Household Particulars","Income and Assets Declaration","Tenancy Matter","Decoration and Installation","Other Businesses"};
        String[]  T3 ={"Change in Household and Ownership (After Execution of Assignment)","Change of Buyer's Information (Before Execution of Assignment):Home Ownership Scheme","Change of Buyer's Information (Before Execution of Assignment):Green Form Subsidised Home Ownership Scheme","Mortgage and Premium","Home Assistance Loan Scheme"};
        String[]  T4 ={"Certificate of Eligibility to Purchase","Certificate of Availability for Sale","Provisional Agreement for Sale and Purchase","Letter of Nomination"};
        String[]  T5 ={"Tendering and Leasing"};
        String[]  T6 ={"Commercial / Non-domestic Premises","Factory Building"};
        String[]  T7 = null;
        String[]  T8 ={"Others"};
        String[] T = null;
        if(title.equals("Public Rental Housing Applicant")){
            T = T1;
        } else if (title.equals("Public Rental Housing Tenant")) {
            T = T2;
        }else if(title.equals("Home Ownership Scheme / Green Form Subsidised Home Ownership Scheme / Tenants Purchase Scheme / Home Assistance Loan Scheme Flat Owner")){
            T = T3;
        }else if(title.equals("Buyer and Seller in HOS Secondary Market")){
            T = T4;
        }else if(title.equals("Commercial Properties Applicant")){
            T = T5;
        }else if(title.equals("Commercial Properties Tenant")){
            T = T6;
        }else if(title.equals("For Building Professionals and Contractors (AP / RSE / RGE / RGBC / RSC / RMWC / RI / QP)")){
            T = T7;
        }else if(title.equals("Others")){
            T = T8;
        }

        return T;
    }
    public String[] getHDDFObjectSC(String title){
        String[]  T1 ={"公屋申请表格及须知","公屋申请声明书:入息声明书","公屋申请声明书:资产及入息声明书","公屋申请声明书:其他事项声明书","更改资料"};
        String[]  T2 ={"更改户籍资料","入息及资产申报","租约事宜","装修及安装","其他事宜"};
        String[]  T3 ={"更改户籍资料及业权（只供签立转让契据后使用）","买方资料变更（只供签立转让契据前使用）:居者有其屋计划","买方资料变更（只供签立转让契据前使用）:绿表置居计划","按揭及地价","置业资助贷款计划"};
        String[]  T4 ={"购买资格证明书","可供出售证明书","临时买卖合约","提名信"};
        String[]  T5 ={"投标及租赁"};
        String[]  T6 ={"商业 / 非住宅单位","工厂大厦"};
        String[]  T7 = null;
        String[]  T8 ={"其他人士"};
        String[] T = null;
        if(title.equals("公屋申请者")){
            T = T1;
        } else if (title.equals("公屋住户")) {
            T = T2;
        }else if(title.equals("居屋 / 绿置居 / 租置计划 / 置业资助贷款计划单位业主")){
            T = T3;
        }else if(title.equals("居屋第二市场买家及卖家")){
            T = T4;
        }else if(title.equals("商业单位申请人")){
            T = T5;
        }else if(title.equals("商业单位租户")){
            T = T6;
        }else if(title.equals("建筑专业人士及承建商适用(认可人士 / 注册结构工程师 / 注册岩土工程师 / 注册一般建筑承建商 / 注册专门建筑承建商 / 注册小型工程承建商 / 注册检验人员 / 合资格人士)")){
            T = T7;
        }else if(title.equals("其他人士")){
            T = T8;
        }

        return T;
    }
    public String getHDDFormUrl(String a ){
        String restUrl = null;
        Document document = Jsoup.parse(a);
        Elements elements = document.select("a");
        for(int i = 0 ;i<elements.size(); i++ ){
            String isOnlie = elements.get(i).select("img").attr("alt");
            if(elements.size() == 3 && (isOnlie.indexOf("網上表格") != -1 || isOnlie.indexOf("Online Form") != -1 || isOnlie.indexOf("网上表格") != -1)){
                restUrl =  elements.get(i).select("a").attr("href");
            }
            if(elements.size() == 2){
                if(isOnlie.indexOf("網上表格") != -1 || isOnlie.indexOf("Online Form") != -1 || isOnlie.indexOf("网上表格") != -1){
                    restUrl =  elements.get(i).select("a").attr("href");
                }else if(isOnlie.indexOf("可輸入資料的PDF格式") != -1 || isOnlie.indexOf("可输入资料的PDF格式") != -1 || isOnlie.indexOf("Form in Fillable PDF Format") != -1){
                    restUrl =  "https://www.housingauthority.gov.hk"+elements.get(i).select("a").attr("href");
                }else{
                    restUrl =  "https://www.housingauthority.gov.hk"+elements.get(i).select("a").attr("href");
                }
            }
        }
        return  restUrl;
    }
    /**
     * 香港警務處 数据爬取
     * @param html
     * @return
     */
    public List getHKPF(String html,String language){
        List list = new ArrayList();
        List<Crawler> listHK = new ArrayList<Crawler>();
        Document document = Jsoup.parse(html);
        removeComments(document);
        Elements element = document.select("ul[id=\"acc\"]").select("li").select("h3");
        Elements ui = document.select("div[class=\"acc-content\"]").select("ul");
        String category = null;
        if("en".equals(language)){
            category = "Hong Kong Police Force";
        }else if("cn".equals(language)){
            category = "香港警务处";
        }else{
            category = "香港警務處";
        }
        int id = 312321312;
        for (int i = 0 ; i < element.size(); i++){

            String object = element.get(i).text();
            Elements dates = ui.get(i).select("li");
            id = id+i;
            for(int j = 0 ; j < dates.size() ; j++){
                String time = sdf.format(new Date());
                Crawler crawler = new Crawler();
//                JSONObject map = new JSONObject();
                Elements a = dates.get(j).select("li").select("a");
                String restUrl = null;
                String title = dates.get(j).select("li").text();
                title = title.replaceAll("([*網上表格]*)", "").replaceAll("PDF式","").replaceAll("(pdf)","").replaceAll("(doc)","").replaceAll("\\(\\)","");
                title = title.replaceAll("\\(","（").replaceAll("\\)","）");
                if(a.size() == 1){
                    restUrl = a.get(0).attr("href");
                    if(restUrl.startsWith("http://") || restUrl.startsWith("https://")){
                        restUrl  =  a.get(0).attr("href");
                    }else{
                        restUrl = "https://www.police.gov.hk"+restUrl;
                    }
                    System.out.println("title:"+title+"---restUrl:"+restUrl);
                }else{
                    //title =a.get(0).text();
                    if(a.get(0).toString().indexOf("onclick") != -1){
                        String tdst=a.get(0).attr("onclick");
                        String quStr=tdst.substring(tdst.indexOf("(")+1,tdst.indexOf(")"));
                        String ird = quStr.split(",")[0].split("-")[0];
                        String num = quStr.split(",")[0].split("-")[1];
                        String tc = quStr.split(",")[1];
                        restUrl = "https://eform.one.gov.hk/"+ird+"/"+num+"/index_"+tc+".html";
                        String regexp = "\'";
                        restUrl = restUrl.replaceAll(regexp, "");
                        System.out.println("startsWith-->href=="+restUrl);
                    }else{
                        restUrl = a.get(0).attr("href");
                        if(restUrl.startsWith("http://") || restUrl.startsWith("https://")){
                            restUrl  =  a.get(0).attr("href");
                        }else{
                            restUrl = "https://www.police.gov.hk"+restUrl;
                        }
                    }
                }
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                crawler.setClassification(category);
                crawler.setObject(object);
                crawler.setFormname(title);
                crawler.setFormurl(restUrl);
                crawler.setReptiletime(df.format(new Date()));
                crawler.setReptiledate(time);
                crawler.setId(uuid);
                listHK.add(crawler);
            }
        }
        System.out.println("listHK = " + listHK.size());
        System.out.println("listHK 结果 = " + listHK);
        if(listHK.size() > 0){
            insertDB(listHK,language);
        }
        //crawlerGOV.crawlerService.insertForeach(listHK);
        return listHK;
    }
    /**
     * 入境处
     * @param html
     * @param category
     * @return
     */
    public List getIMD(String html, String category,String language){
        List list = new ArrayList();
        String object;
        //  AddKnowledgeUtils addKnowledgeUtils = new AddKnowledgeUtils();
        Document document = Jsoup.parse(html);
        Elements contents1 = document.select("table[class=\"tableStyle03\"]");
        if (html.indexOf("<p class=\"font_size strong\">") != -1){
            Elements element = document.select("p[class=\"font_size strong\"]");
            // LOGGER.info("结果："+element.size());
            for (int s = 0 ; s < element.size(); s++){
                object = element.get(s).text();
                List list1 = getIMDRest(contents1.get(s).select("tr"),category,object,language);
                //     addKnowledgeUtils.addGenericKnowledge(list1);
                list.add(list1);

            }
        }else{
            object = document.select("div[class=\"headerWrapper template7\"]").select("h1").text();
            list = getIMDRest(contents1.get(0).select("tr"),category,object,language);
            //  addKnowledgeUtils.addGenericKnowledge(list);
            //  list.add(list1);
            //  list = list1;
        }

        LOGGER.info(category+"----》入境处数据getIMD-->结果："+ list);
        return list;
    }

    /**
     * 获得入境处表格数据
     * @param elements
     * @return
     */
    public List getIMDRest(Elements elements, String category,String object,String language){
        List list = new ArrayList();
        List<Crawler> listHK = new ArrayList<Crawler>();
        String categoryA = null;
        if("en".equals(language)){
            categoryA = "Immigration Department";
        }else if("cn".equals(language)){
            categoryA = "入境事务处";
        }else{
            categoryA = "入境事務處";
        }
        for (int i = 0 ; i < elements.size(); i++){
            String time = sdf.format(new Date());
            Crawler crawler = new Crawler();
            Elements td = elements.get(i).select("td");
            if ( td.size() == 2){
                JSONObject map = new JSONObject();
                String formId = td.get(0).text().replaceAll(" ","");
                String title = td.get(1).text().replaceAll("\\(","（").replaceAll("\\)","）");
                String regexp = " ";
                title = title.replaceAll(regexp, "");
                map.put("title",title.replaceAll(" ",""));
                formId = formId.replaceAll("\\(","（").replaceAll("\\)","）");
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                crawler.setClassification(categoryA);
                crawler.setObject(category);
                crawler.setFormname(title.replaceAll(" ",""));
                crawler.setFormurl("https://www.immd.gov.hk"+td.get(0).select("a").attr("href").trim());
                crawler.setReptiletime(df.format(new Date()));
                crawler.setReptiledate(time);
                crawler.setId(uuid);
                if(formId.replaceAll(" ","").equals("-")){
                    map.put("formId","");
                    crawler.setFormid("");
                }else{
                    map.put("formId",formId.replaceAll(" ",""));
                    crawler.setFormid(formId.replaceAll(" ",""));

                }
                listHK.add(crawler);

                //表格资料说明
                //map.put("explanaURL",explanaURL);
                map.put("restUrl","https://www.immd.gov.hk"+td.get(0).select("a").attr("href").trim());
                map.put("category",categoryA);
//                map.put("object",object.replaceAll(" ","").replaceAll("\\(","（").replaceAll("\\)","）"));
                map.put("object",category);
              //  map.put("id",id);
                list.add(map);
            }
        }
        System.out.println("listHK = " + listHK.size());
       // crawlerGOV.crawlerService.insertForeach(listHK);
        if(listHK.size() > 0){
            insertDB(listHK,language);
        }
        System.out.println("入境事務處--》结果："+listHK);
        return list;
    }

    public String getIMDpdfName(String html){
        Document document = Jsoup.parse(html);
        String pdfname = document.select("div.downloadBox").select("a").attr("href");
        String pdfname1 = pdfname.substring(pdfname.lastIndexOf("/")+1);
        return pdfname1;
    }
    public void getCategory(String html){
      //  HttpClinetUtils HttpClinetUtils = new HttpClinetUtils();
        //  AddKnowledgeUtils addKnowledgeUtils = new AddKnowledgeUtils();
//        List list1 = new ArrayList();
        Document document = Jsoup.parse(html);
//        JSONObject map = null;
        Elements element = document.select("div.template_content_container ").select("div.newsBox");
        for(int i= 1;i < element.size(); i++){
            String categoryName = document.select("div.template_content_container ").select("div.newsBox").get(i).select("span").text().replaceAll(" ","").replaceAll("/","");
            Elements object = document.select("div.template_content_container ").select("div.newsBox").get(i).select("ul").select("li");
            for(int j = 0 ; j < object.size(); j++){
//                map = new JSONObject();
                String objectName = object.get(j).select("a").text().replaceAll(" ","").replaceAll("/","");
                String url = object.get(j).select("a").attr("href");
                String rest = httpClinetUtils.doGet("https://www.immd.gov.hk"+url,"utf-8");
                //TODO 这个逻辑有待验证
                //List list = getIMD(rest);
                //addKnowledgeUtils.addKnowledge(list,categoryName,objectName,String.valueOf(j+i)+String.valueOf(new Random().nextInt(899999) + 100000));
            }


        }
//        LOGGER.info("返回大分类的链接："+list1);

    }
    /**
     * 税务局
     * @param html
     * @return
     */
    public List getIRD(String html,String language){
        String cateory = null;
        String mind = null;
        if("en".equals(language)){
            cateory = "Inland Revenue Department";
            mind = "eng";
        }else if("cn".equals(language)){
            cateory = "税务局";
            mind = "chs";
        }else{
            cateory = "稅務局";
            mind = "chi";
        }
        List<Crawler> listHK = new ArrayList<Crawler>();
        Document document = Jsoup.parse(html);
        removeComments(document);
        Elements element = document.select("table[border=\"3\"]");//document.select("div.contentstyle").select("div.blockTable");
        // System.out.println(element.toString());
        for(int i = 0; i < element.size();i++){
            String object =  element.get(i).select("caption").text();
            Elements tr =  element.get(i).select("tr");

            for(int j = 2; j < tr.size(); j++){
                String time = sdf.format(new Date());
                Crawler crawler = new Crawler();
                String href = null;
//                JSONObject map = new JSONObject();

                if( tr.get(j).select("td").size() == 4){

                    String formId = tr.get(j).select("td").get(0).text().replaceAll(" ","").replaceAll("\\(","（").replaceAll("\\)","）");
                    String title = tr.get(j).select("td").get(1).text();

                    Elements document_url = tr.get(j).select("td").get(2).select("a");
                    if(document_url.size() == 1){
                        href = document_url.get(0).attr("href").trim();
                        if(href.indexOf("../") > -1 && href.indexOf("download.htm") == -1){
                            href ="https://www.ird.gov.hk/"+ mind + href.substring(2, href.length());
                        }else if(href.indexOf("download.htm") == -1 && href.indexOf("../") == -1 && !href.startsWith("http")){
                          //  href ="https://www.ird.gov.hk"+href;
                            href ="https://www.ird.gov.hk/"+ mind +"/paf/"+href ;
                        }else if(href.startsWith("http")){
                            href = href;
                        }else{
                           // String URLname =  getIRDPDF(href);
                            href ="https://www.ird.gov.hk/"+ mind +"/paf/"+href ;
                        }
                    }else if(document_url.size() == 2){
                        String tdst=document_url.attr("onclick");
                        if(tdst.startsWith("openEform")){
                            String quStr=tdst.substring(tdst.indexOf("(")+1,tdst.indexOf(")"));
                            System.out.println("quStr.split(\",\")[0]=="+quStr.split(",")[0].split("-")[0]);
                            System.out.println("quStr.split(\",\")[0]=="+quStr.split(",")[0].split("-")[1]);
                            System.out.println("quStr.split(\",\")[1]=="+quStr.split(",")[1]);
                            String ird = quStr.split(",")[0].split("-")[0];
                            String num = quStr.split(",")[0].split("-")[1];
                            String tc = quStr.split(",")[1];
                            href = "http://www.eform.one.gov.hk/"+ird+"/"+num+"/index_"+tc+".html";
                            String regexp = "\'";
                            href = href.replaceAll(regexp, "");
                            System.out.println("替换后:" + href);
                            System.out.println("startsWith-->href=="+href);
                        }else{
                            href = document_url.get(0).attr("href").trim();
                            if(href.indexOf("../") > -1 && href.indexOf("download.htm") == -1){
                                href ="https://www.ird.gov.hk/" + mind + href.substring(2, href.length());
                            }else if(href.indexOf("download.htm") == -1 && href.indexOf("../") == -1 && !href.startsWith("http")){
                               // href ="https://www.ird.gov.hk"+href;
                                href ="https://www.ird.gov.hk/"+ mind +"/paf/"+href ;
                            }else if(href.startsWith("http")){
                                href = href;
                            }else{
                               // String URLname =  getIRDPDF(href);
                                href ="https://www.ird.gov.hk/"+ mind +"/paf/"+href ;

                            }
                        }
                        System.out.println(i+"====tdst=="+tdst);
                    }
                    String uuid = UUID.randomUUID().toString().replaceAll("-","");
                    crawler.setClassification(cateory);
                    crawler.setObject(object);
                    crawler.setFormid(formId);
                    crawler.setFormname(title);
                    crawler.setFormurl(href);
                    crawler.setReptiletime(df.format(new Date()));
                    crawler.setReptiledate(time);
                    crawler.setId(uuid);
                    listHK.add(crawler);
//                    map.put("object",object);
//                    map.put("formId",formId);
//                    map.put("title",title);
//                    map.put("restUrl",href);
//                    map.put("category","/專業知識點/稅務局/");
//
//                    map.put("id","201907261917"+i);
//                    System.out.println(j-2+"====formId=="+formId);
//                    System.out.println(j-2+"====title=="+title);
//                    System.out.println(j-2+"====href=="+href);
//                    list.add(map);
                }

            }

        }
        System.out.println("listHK = " + listHK.size());
      //  crawlerGOV.crawlerService.insertForeach(listHK);
        if(listHK.size() > 0){
            insertDB(listHK,language);
        }
        System.out.println("====list=="+listHK);
        return listHK;
    }
    private static Pattern NUMBER_PATTERN  = Pattern.compile(".*?(?=/)" );
    //获得税务局pdf文件路径
    public String  getIRDPDF(String html){

        //String text = "download.htm?irc3111a_flow/IRC3111A/業務地址變更通知書(資料讀入)";

        Matcher matcher = NUMBER_PATTERN.matcher(html);
        String URLname = null;
        if (matcher.find()) {
            System.out.println(matcher.group(0));
            URLname = matcher.group(0);
        }
        String rest = URLname.split("\\?")[1]+".pdf";
        System.out.println("url=="+rest);
        return  rest;

    }
    /**
     * 康文署
     * @param html
     * @return
     */
    public String getLCSD(String html,String language){

        List<Crawler> listHK = new ArrayList<Crawler>();
        // String regstr = "\\<!--(.+)--\\>";
        Document document = Jsoup.parse(html);
        removeComments(document);
        Elements element = document.select("div.richText ").select("h2");
        Elements table = document.select("div.richText ").select("table.table_resp").select("tbody");
        String formId = null;
        String title = null;
        String URL = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String time=sdf.format(new Date());
        String cateory = null;
        if("en".equals(language)){
            cateory = "Leisure and Cultural Services Department";
        }else if("cn".equals(language)){
            cateory = "康文署";
        }else{
            cateory = "康文署";
        }
        Long id = 0l;
        //LOGGER.info("table.tbody="+table.size());
        for(int i = 0 ; i < element.size(); i++){
            String text = element.get(i).text();
            Elements toby = table.get(i).select("tr");
            LOGGER.info("table.toby = " + toby.size());
            id = (2312321L * (i+1))+ (100*(i+1));
            for(int j = 0 ; j < toby.size(); j++){
                Crawler crawler = new Crawler();
                Elements td = toby.get(j).select("td");
                id = (id * (j+1)) + (10*(j+1));
                if( td.size() == 2){
                    // if(td.size() == 0 ){
                    formId = td.get(0).text();
                    //  }
                    //  if(td.size() == 1){
                    title  = td.get(1).text();
                    URL = "https://www.lcsd.gov.hk"+td.get(1).select("a").get(0).attr("href").trim();
                    //  }
                    LOGGER.info("formId = " + formId +";title="+title+"   URL="+URL+"  text= "+text);
                }
//                if(td.size() == 1){
//                    formId = td.get(0).text();
//                    LOGGER.info("formId = " + formId +";title="+title);
//                }
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                crawler.setClassification(cateory);
                crawler.setObject(text);
                crawler.setFormid(formId);
                crawler.setFormname(title);
                crawler.setFormurl(URL);
                crawler.setReptiletime(df.format(new Date()));
                crawler.setReptiledate(time);
                crawler.setId(uuid);
                listHK.add(crawler);
                LOGGER.info("table.td = " + td.size());
            }
            // LOGGER.info("table.tbody 11 ="+toby);
           // LOGGER.info("三生三世="+text);
        }
        System.out.println("listHK = " + listHK.size());
        System.out.println("listHK = " + listHK);
      //  crawlerGOV.crawlerService.insertForeach(listHK);
        if(listHK.size() > 0){
            insertDB(listHK,language);
        }
        return null;

    }

    /**
     * 康文署 图书馆
     * @param html
     */
    public void getLibrary(String html){
        Document document = Jsoup.parse(html);
        HttpClinetUtils HttpClinetUtils = new HttpClinetUtils();
        removeComments(document);
        String formId = null;
        String title = null;
        String URL = null;
        Elements element = document.select("div.wrapper ").select("div.content").select("div[class=\"content-container main_content\"]").select("table[style=\" width: 100%;\"]");
        for (int i = 0 ; i < element.size(); i++){
            String text = element.get(i).select("tbody").attr("title");
            Elements tr = element.get(i).select("tbody").select("tr");
            for (int j = 1; j < tr.size();j++){
                formId = tr.get(j).select("td").get(0).text();
                title = tr.get(j).select("td").get(1).text();
                URL = tr.get(j).select("td").get(1).select("a").get(0).attr("href");
                System.out.println("formId = " + formId + "->title = " + title+ "->URL = " + URL+ "->text = " + text);
            }
        }
    }
    /**
     * 康文署 博物馆
     * @param html
     */
    public void getMuseum(String html){
        Document document = Jsoup.parse(html);
        HttpClinetUtils HttpClinetUtils = new HttpClinetUtils();
        removeComments(document);
        String formId = null;
        String title = null;
        String URL = null;
        Elements element = document.select("div[class=\"portlet-boundary portlet-boundary_56_ portlet-static portlet-static-end portlet-journal-content\"]");
        System.out.println("博物馆element:"+element.size());
    }
    /**
     * 劳工处
     * @param html
     * @return
     */
    public List getLD(String html,String languageA){
        List list = new ArrayList();
        List<Crawler> listHK = new ArrayList<Crawler>();
        Document document = Jsoup.parse(html);
        removeComments(document);
        Elements element = document.select("table");
        String cateory = null;
        if("en".equals(languageA)){
            cateory = "Labour Department";
        }else if("cn".equals(languageA)){
            cateory = "劳工处";
        }else{
            cateory = "勞工處";
        }
        if(element.size() == 2){
            Elements table = element.get(1).select("tbody").select("tr");
            for(int i = 2;i < table.size(); i++){
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String time = sdf.format(new Date());
                Crawler crawler = new Crawler();
                JSONObject map = new JSONObject();
                String url_1 = null;
                String rest1 = null;
                String url_2 = null;
                String rest2 = null;
                String url_3 = null;
                String rest3 = null;
                String url_4 = null;
                String rest4 = null;
                String title = null;
                String formId = null;
                String explanaURL = null;
                String restUrl = null;
                Elements tb = table.get(i).select("td");
                //  LOGGER.info("table="+tb.size());
                String name = tb.get(0).text();
                String rest = getRest(name);
                if(rest.indexOf("%") > -1){
                    title = rest.split("%")[0];
                    title = title.replaceAll("/","／");
                    title = title.replaceAll("\\(","（").replaceAll("\\)","）");
                    formId = rest.split("%")[1].trim().replaceAll("\\(","（").replaceAll("\\)","）");
                    // formId = formId1.substring(0,formId1.length()-1);
                }else{
                   // title = rest.trim();
                    title = rest.replaceAll("/","／");
                    title = title.replaceAll("\\(","（").replaceAll("\\)","）");
                }
                String[] language = tb.get(1).text().split(" ");
                Elements a = tb.get(2).select("a");
                if(language.length == 2 && a.size() ==4){//中文和英文各两个文件
                    url_1 = a.get(0).attributes().toString();
                    rest1 = getRestUrl(url_1,languageA).trim();
                    url_2 = a.get(1).attributes().toString();
                    rest2 = getRestUrl(url_2,languageA).trim();
                    url_3 = a.get(2).attributes().toString();
                    rest3 = getRestUrl(url_3,languageA).trim();
                    url_4 = a.get(3).attributes().toString();
                    rest4 = getRestUrl(url_4,languageA).trim();
                    if("en".equals(languageA)){
                        if(rest3.indexOf("pdf") != -1){
                            restUrl = rest3;
                        }else if(rest4.indexOf("pdf") != -1){
                            restUrl = rest4;
                        }else{
                            restUrl = rest3;
                        }
                    }else{
                        if(rest1.indexOf("pdf") != -1){
                            restUrl = rest1;
                        }else if(rest2.indexOf("pdf") != -1){
                            restUrl = rest2;
                        }else{
                            restUrl = rest1;
                        }
                    }

                }else if(language.length == 2 && a.size() ==2){//中英文各一个文件
                    url_1 = a.get(0).attributes().toString();
                    rest1 = getRestUrl(url_1,languageA).trim();
                   // restUrl = rest1;
                    url_2 = a.get(1).attributes().toString();
                    rest2 = getRestUrl(url_2,languageA).trim();
                    if("en".equals(languageA)){
                        restUrl = rest2;
                    }else{
                        restUrl = rest1;
                    }
                }else if(language.length == 3 && a.size() ==3){//中英文各一个文件 加上中英夹杂的一个文件
                    url_1 = a.get(0).attributes().toString();
                    rest1 = getRestUrl(url_1,languageA).trim();
                    restUrl = rest1;
//                    url_2 = a.get(1).attributes().toString();
//                    rest2 = getRestUrl(url_2,languageA).trim();
//                    url_3 = a.get(2).attributes().toString();
//                    rest3 = getRestUrl(url_3,languageA).trim();
                }else if(language.length == 1){//中英夹杂文件
                    if(a.size() ==1){
                        url_1 = a.get(0).attributes().toString();
                        rest1 = getRestUrl(url_1,languageA).trim();
                        restUrl = rest1;
                    }else{
                        url_1 = a.get(0).attributes().toString();
                        rest1 = getRestUrl(url_1,languageA).trim();
                        restUrl = rest1;
                        url_2 = a.get(1).attributes().toString();
                        rest2 = getRestUrl(url_2,languageA).trim();
                    }
                }

                Element explanation = tb.get(3);
                if(explanation.toString().indexOf("href=\"") > -1){
                    explanaURL = explanation.select("a").attr("href").trim();
                }
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                crawler.setClassification(cateory);
                crawler.setObject(cateory);
                crawler.setFormid(formId);
                crawler.setFormname(title);
                crawler.setFormurl(restUrl);
                crawler.setReptiletime(df.format(new Date()));
                crawler.setReptiledate(time);
                crawler.setId(uuid);
                listHK.add(crawler);
//                map.put("title",title);
//                map.put("formId",formId);
//                //表格资料说明
//                map.put("explanaURL",explanaURL);
//                map.put("rest1",rest1);
//                map.put("rest2",rest2);
//                map.put("rest3",rest3);
//                map.put("rest4",rest4);
//                map.put("restUrl",restUrl);
//                map.put("category","/專業知識點/LD勞工處/");
//                map.put("object","勞工處");
//                map.put("id","20190726111120");
//                map.put("language",language.length);
//                LOGGER.info("第"+i+"个tr <---->rest1***"+rest1+"\n rest2---"+rest2+"\n rest3-->"+rest3+"\n rest4==="+rest4 +" \n title:::"+title+" \n formId...."+formId+" " +" \n explanaURL____"+explanaURL);
//                LOGGER.info("第"+i+"个tr ---->name="+name+";language="+language.length+";explanation="+explanation);
//                list.add(map);
            }
//            LOGGER.info("table="+table.size());
        }
//        LOGGER.info("list="+list);
        System.out.println("listHK = " + listHK.size());
        System.out.println("listHK 结果= " + listHK);
       // crawlerGOV.crawlerService.insertForeach(listHK);
        if(listHK.size() > 0){
            insertDB(listHK,languageA);
        }
        return list;
    }
    //处理劳工处的文件超链接
    public String getRestUrl(String url,String languageA){
        String restRul = null;
        if(url.indexOf("onclick") > -1){
            restRul = url.replaceAll("href=\"javascript:close\\(\\)\" onclick=\"return checkformat\\(&quot;acrobat&quot;,&quot;","").replaceAll("&quot;\\);\"","");
            if(restRul.indexOf("word&quot;") > -1){
                restRul = restRul.replaceAll("href=\"javascript:close\\(\\)\" onclick=\"return checkformat\\(&quot;word&quot;,&quot;","");
            }
            if(restRul.indexOf("fillable_form&quot;") > -1){
                restRul = restRul.replaceAll("href=\"javascript:close\\(\\)\" onclick=\"return checkformat\\(&quot;fillable_form&quot;,&quot;","");
            }
            if(restRul.indexOf("oform&quot;") > -1){
                restRul = restRul.replaceAll("href=\"javascript:close\\(\\)\" onclick=\"return checkformat\\(&quot;oform&quot;,&quot;","");
            }
        }
        if(url.indexOf("box4eform") > -1){
            restRul = url.replaceAll("href=\"javascript:box4eform\\(&quot;lv=e&amp;fname=ldform&amp;","").replaceAll("&amp;&quot;\\)\"","");
            if("en".equals(languageA)){
                restRul = "https://www.info.gov.hk/cgi-bin/forms/popup.cgi?lv=e&fname=ldform&"+restRul.trim();
            }else{
                restRul = "https://www.info.gov.hk/cgi-bin/forms/popup.cgi?lv=c&fname=ldform&"+restRul.trim();
            }
        }
        return restRul;
    }
    //获得劳工处的formId 和 表格名称
    public String getRest(String name){
        String rest= null;
        StringBuffer stringBuffer = new StringBuffer();
        String[] contetn = null;
        String rest1 = "";
        if(name.indexOf("((") != -1){
            contetn = name.split("\\(\\(");
            rest1 = "((";
        }else{
            contetn = name.split(" \\(");
        }

        if(contetn.length > 1){
            rest = contetn[contetn.length-1].toString();
            String restFormId = rest1 + rest.substring(0,rest.length()-1);
            for(int i = 0 ; i < contetn.length-1 ; i ++){
                if(i == 0){
                    stringBuffer.append(contetn[i]);
                }else{
                    stringBuffer.append("("+contetn[i]);
                }
            }
            //String title = stringBuffer.toString().replaceAll(" ","");
            rest = stringBuffer.toString() +"%"+ restFormId;
        }else{
            rest = name;
        }
        return rest;
    }
    /**
     * 申訴專員公署 数据爬取
     * @param html
     * @return
     */
    public List getOMB(String html,String language){
        List list = new ArrayList();
        List<Crawler> listHK = new ArrayList<Crawler>();
        Document document = Jsoup.parse(html);
        removeComments(document);
        Elements element = document.select("table").select("tbody").select("tr");
        String category = null;
        if("en".equals(language)){
            category = "Office Of The Ombudsman, Hong Kong";
        }else if("cn".equals(language)){
            category = "申诉专员公署";
        }else{
            category = "申訴專員公署";
        }
        for(int i = 0; i < element.size(); i++){
            JSONObject map = new JSONObject();
            String time = sdf.format(new Date());
            Crawler crawler = new Crawler();
            String title = element.get(i).select("td").get(0).text().replaceAll("\\(","（").replaceAll("\\)","）");
            String restUrl;
            if(element.get(i).select("td").get(3).text().equals("N/A")){
                restUrl = "https://www.ombudsman.hk" + element.get(i).select("td").get(2).select("a").attr("href");
            }else{
                restUrl = element.get(i).select("td").get(3).select("a").attr("href");
            }
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            crawler.setFormname(title);
            crawler.setFormurl(restUrl);
            crawler.setReptiletime(df.format(new Date()));
            crawler.setReptiledate(time);
            crawler.setId(uuid);
           // crawler.setFormid(formId);
            crawler.setClassification(category);
            crawler.setObject(category);
            listHK.add(crawler);
            map.put("title",title);
            map.put("restUrl",restUrl);
            map.put("object",category);
            map.put("id","8977825465432408988");
            map.put("category","/OMB申訴專員公署/OMB表格知識/申訴專員公署/");
            list.add(map);
        }
        System.out.println("list=="+listHK);
        System.out.println("listHK = " + listHK.size());
        if(listHK.size() > 0 ){
            insertDB(listHK,language);
        }
        return list;
    }
    /**
     * 个人资料隐私专员公署
     * @param html
     * @return
     */
    public List getPCPD(String html,String language){
        List list = new ArrayList();
        List<Crawler> listHK = new ArrayList<Crawler>();
        Document document = Jsoup.parse(html);
        removeComments(document);
        Elements element = document.select("ol[class=\"listStyle0\"]").select("li");
        String category = null;
        if("en".equals(language)){
            category = "A Quick Guide";
        }else if("cn".equals(language)){
            category = "香港个人资料私隐专员公署";
        }else {
            category = "個人資料私隱專員公署";
        }
        for(int i = 0 ; i < element.size();i++){
            String time = sdf.format(new Date());
            Crawler crawler = new Crawler();
            JSONObject map = new JSONObject();
            String title = element.get(i).text().replaceAll("\\(","（").replaceAll("\\)","）");
            String restUrl = element.get(i).select("a").attr("href");
            if(restUrl.startsWith("../../../../")){
                restUrl = restUrl.replaceAll("../../../../","/");
                restUrl = "https://www.pcpd.org.hk" + restUrl;
            }else{
                if("en".equals(language)){
                    restUrl = "https://www.pcpd.org.hk/english/resources_centre/publications/forms/" + restUrl;
                }else if("cn".equals(language)){
                    restUrl = "https://www.pcpd.org.hk/sc_chi/resources_centre/publications/forms/" + restUrl;
                }else {
                    restUrl = "https://www.pcpd.org.hk/tc_chi/resources_centre/publications/forms/" + restUrl;
                }

            }
            map.put("title",title);
            map.put("restUrl",restUrl);
            map.put("object",category);
            map.put("id","897776532werr8");
            map.put("category",category);
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            crawler.setFormname(title);
            crawler.setFormurl(restUrl);
            crawler.setReptiletime(df.format(new Date()));
            crawler.setReptiledate(time);
            crawler.setId(uuid);
           // crawler.setFormid(formId);
            crawler.setClassification(category);
            crawler.setObject(category);
            listHK.add(crawler);
            list.add(map);
        }
        System.out.println("list=="+listHK);
        System.out.println("listHK = " + listHK.size());
        if(listHK.size() > 0 ){
            insertDB(listHK,language);
        }
        return list;
    }
    /**
     * 运输署
     * @param html
     * @return
     */
    public List getTD(String html,String language) {
        List list1 = new ArrayList();
        Document document = Jsoup.parse(html);
        Elements elements = document.select("table[class=\"section_table1\"]").select("tbody").select("tr");
        for (int i = 0; i < elements.size(); i++) {
            String url = elements.get(i).select("td").get(1).select("a").attr("href");
            String value = url.substring(0, url.lastIndexOf("/") + 1);
            if (value.endsWith("_ap/")) {
                url = url.replaceAll("_ap", "");
            }
            String title = elements.get(i).select("td").get(1).text();
            String rest = httpClinetUtils.doGet("https://www.td.gov.hk"+url, "utf-8");
            getRest(rest,title,language);
        }
        return  list1;
    }

 public void getRest(String html ,String category,String language){
        List list1 = new ArrayList();
        Document document = Jsoup.parse(html);
        String masthead = document.select("#div_generated_pagename").text();
        Elements contents1 = document.select(".content_table1");
        String midtitle = null;
        if(contents1.size() == 1){
            Elements TLevel4 = contents1.select(".TLevel4");
            list1 = getTd(TLevel4,masthead,midtitle,language);
        }else{
            for(int k = 0; k < contents1.size(); k++){
                Element ss =  contents1.get(k);
                midtitle = ss.select("span[style=\"text-decoration: underline;\"]").text();
                Elements TLevel4 = ss.select(".TLevel4");
                List list2 = getTd(TLevel4,masthead,midtitle,language);
                //System.out.println("第"+k+"个中间标题：list2："+list2);
                list1.add(list2);
                //System.out.println("第"+k+"个中间标题：list："+list1);
            }
        }
       // return  list1;
    }

    public  List getTd(Elements TLevel4,String masthead,String midtitle,String language){
        List<TableBean> list  = new ArrayList();
        List<Crawler> listHK = new ArrayList<Crawler>();
        TableBean tableBean ;
        String categoryId = null;
        if("en".equals(language)){
            categoryId = "Transport Department";
        }else if("cn".equals(language)){
            categoryId = "运输署";
        }else{
            categoryId = "運輸署";
        }
        for(int i = 0 ; i < TLevel4.size();i++){
            tableBean = new TableBean();
            Crawler crawler = new Crawler();
            Element tr = TLevel4.get(i);
            // 获取该行的所有td节点
            Elements tds = tr.select("td");
            if(tds.size() > 2){
                String time = sdf.format(new Date());
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                crawler.setReptiletime(df.format(new Date()));
                crawler.setReptiledate(time);
                crawler.setId(uuid);
                crawler.setClassification(categoryId);
                crawler.setObject(masthead);
                for(int j = 0; j<tds.size();j++){
                    tableBean.setBigclassification(masthead);
                    tableBean.setClassification(categoryId);
                    if(j == 0){
                        String td = tds.get(0).text();
                        tableBean.setNumber(td);
                        crawler.setFormid(td);
                    }
                    if(j == 1){
                        if(tds.get(1).toString().indexOf("a href") > -1){
                            Elements link = tds.get(1).select("a");
                            String relHref = link.attr("href");
                            String title = tds.get(1).text();
                            tableBean.setPDF_url("https://www.td.gov.hk"+relHref);
                            tableBean.setTablename(title);
                            crawler.setFormname(title);
                            crawler.setFormurl("https://www.td.gov.hk"+relHref);
                        }else{
                            String version = tds.get(1).text();
                            tableBean.setVersion(version);
                        }
                    }
                    if(j == 2){
                        if(tds.get(2).toString().indexOf("href") > -1){
                            Elements link = tds.get(2).select("a");
                            String relHref = link.attr("href");
                            String title = tds.get(2).text();
                            tableBean.setPDF_url("https://www.td.gov.hk"+relHref);
                            tableBean.setTablename(title);
                            crawler.setFormname(title);
                            crawler.setFormurl("https://www.td.gov.hk"+relHref);
                        }
                    }
                    if(j == 3){
                        if(tds.get(3).toString().indexOf("img src") < 0){
                            Elements link = tds.get(3).select("a");
                            String relHref = link.attr("href");
                            tableBean.setElectron_url(relHref);
                        }
                    }
                   // listHK1.add(crawler);
                   // listHK.add(crawler);
                }

            }else if(tds.size() == 2 ) {
                tableBean.setBigclassification(masthead);
                tableBean.setClassification(midtitle);
                Elements link = tds.get(0).select("a");
                String relHref = link.attr("href");
                String title = tds.get(0).text();
                tableBean.setPDF_url("https://www.td.gov.hk"+relHref);
                tableBean.setTablename(title);
                String time1 = sdf.format(new Date());
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                crawler.setReptiletime(df.format(new Date()));
                crawler.setReptiledate(time1);
                crawler.setId(uuid);
                crawler.setClassification(categoryId);
                crawler.setObject(masthead);
                crawler.setFormname(title);
                crawler.setFormurl("https://www.td.gov.hk"+relHref);
            }

            list.add(tableBean);
            if(isNotBlank(crawler.getId())){
                listHK.add(crawler);
            }


        }
        System.out.println("listHK-----------"+listHK);
        System.out.println("listHK = " + listHK.size());
        if(listHK.size() > 0 ){
            //crawlerGOV.crawlerService.insertForeach(listHK);
            insertDB(listHK,language);
        }
        return  list;

    }
    /**
     * 在職家庭及學生資助事務處
     * @param html
     * @return
     */
    public List getWFSFAA(String html){
        HttpClinetUtils HttpClinetUtils = new HttpClinetUtils();
        List list = new ArrayList();
        Document document = Jsoup.parse(html);
        removeComments(document);
        Elements element = document.select("div[class=\"richtext\"]").select("dl");
        int ids = 45654654;
        String url_new = null;
        for(int i = 0 ; i < element.size(); i++){
            String category = "學生資助處/"+element.get(i).select("dt").text().replaceAll(":","");
            Elements dd = element.get(i).select("dd");
            for(int j = 0 ; j < dd.size();j++){
                String date = null;
                String  object = dd.get(j).text();
                ids = ids+1+j*123;
                String url = dd.get(j).select("a").attr("href");
                if(url.startsWith("../") &&  !"持續進修基金".equals(object)){
                    url = url.replace("../","https://www.wfsfaa.gov.hk/sfo/tc/");
                }else if( "持續進修基金".equals(object)){
                    url = url.replace("../../../","https://www.wfsfaa.gov.hk/");
                }else if("內地大學升學資助計劃".equals(object)){
                    url = "https://www.wfsfaa.gov.hk/sfo/tc/forms/"+url;
                }else{
                    url = "https://www.wfsfaa.gov.hk/sfo/tc/"+url;
                }
                Document document1 = Jsoup.parse(httpClinetUtils.doGet(url,"utf-8"));
                removeComments(document1);
                if("學生資助處/學前教育".equals(category) || "學生資助處/小學及中學程度".equals(category)){
                    //date = httpClinetUtils.doGet(url,"utf-8");
                    Elements element1 = document1.select("ul[class=\"faq-ulli\"]").select("li");
                    String rest = element1.get(1).select("a").attr("href");
                    url_new = url.replace("forms.htm",rest);
                    date = httpClinetUtils.doGet(url_new,"utf-8");
//                    System.out.println("url_new:"+url_new);
                    getE_WFSFAA(date,category,object,ids);
                }else{
                    Elements table;
                    if("內地大學升學資助計劃".equals(object)){
                        table =  document1.select("div[class=\"news-table2\"]").select("table");
                    }else if("學生資助處/持續進修".equals(category) && "持續進修基金".equals(object)){
                        table =  document1.select("div[class=\"list-table\"]").select("table");
                    }else{
                        table =  document1.select("div[class=\"list-table\"]").select("table");
                    }

                    getOtherWFSFAA(category,object,ids,table);
                }
                System.out.println("category::"+category+";;;object:"+object+";;;url:"+url+"   ;;;ids："+ids+"  ;;;url_new= "+url_new);
            }
        }
        return list;
    }
    public void getE_WFSFAA(String html,String category,String object,int id){
        List list = new ArrayList();

        Document document = Jsoup.parse(html);
        removeComments(document);
        String title = null;
        String restUrl = null;
        Elements element = document.select("div[class=\"form-download\"]").select("div[class=\"row02\"]");
        Elements table = document.select("div[class=\"list-table\"]").select("table");
        //System.out.println("table.size()："+table.size());
        for(int i = 0 ; i < element.size()-1;i++){
            title = element.get(i).select("div[style=\"display:table-cell; width:100%\"]").text();
            restUrl = element.get(i).select("div[style=\"display:table-cell\"]").select("a").attr("href").replaceAll("../../../../","https://www.wfsfaa.gov.hk/sfo/");
            //  System.out.println("title::"+title+";;;restUrl:"+restUrl+";;;第几条数据:"+i);
        }
        for(int j = 0 ; j < table.size(); j++){
            List<Crawler> listHK = new ArrayList<Crawler>();
            String konw = table.get(j).select("caption").text();
            if(!konw.contains("少數族裔語言")){
                Elements tr = table.get(j).select("tbody").select("tr");
                for(int T = 0; T < tr.size();T++){
                    String time = sdf.format(new Date());
                    Crawler crawler = new Crawler();
                    JSONObject map = new JSONObject();
                    Elements td = tr.get(T).select("td") ;
                    String title1 = null;
                    String resturl = null;
                    if(td.size() == 1 && tr.get(T).text().contains("中文 ☆") ){
                        title1 = konw;
                        resturl = tr.get(T).select("a").attr("href").replaceAll("../../../../","https://www.wfsfaa.gov.hk/sfo/");

                    }else if(td.size() == 4){
                        title1 = td.get(1).text();
                        resturl = td.get(3).select("a").attr("href").replaceAll("../../../../","https://www.wfsfaa.gov.hk/sfo/");
                    }else if(td.size() == 3){
                        title1 = td.get(0).text();
                        resturl = td.get(2).select("a").attr("href").replaceAll("../../../../","https://www.wfsfaa.gov.hk/sfo/");
                    }
                    if(title != null && title.trim().length() > 0){
                        map.put("title",title);
                        crawler.setFormname(title);
                    }
                    if(resturl != null && resturl.trim().length() > 0){
                        map.put("restUrl",resturl);
                        crawler.setFormurl(resturl);
                    }

                    String uuid = UUID.randomUUID().toString().replaceAll("-","");
                    crawler.setReptiletime(df.format(new Date()));
                    crawler.setReptiledate(time);
                    crawler.setId(uuid);
                   // crawler.setFormid(formId);
                    crawler.setClassification(category.replaceAll(" / ","，"));
                    crawler.setObject(object.replaceAll(" / ","，"));
                    listHK.add(crawler);
                    map.put("object",object.replaceAll(" / ","，"));
                    map.put("id",id);
                    map.put("category",category.replaceAll(" / ","，"));
                   // listHK.add(crawler);
                    list.add(map);
                }
                System.out.println("listHK = " + listHK.size());
                if(listHK.size() > 0 ){
                    crawlerGOV.crawlerService.insertForeach(listHK);
                }
            }
        }

    }
    public  void getOtherWFSFAA(String category,String object,int id,Elements table ){
        String caption = table.get(0).select("caption").text();
        List list = new ArrayList();
        List<Crawler> listHK = new ArrayList<Crawler>();
        Elements tr = null;
        if("更多表格及文件".equals(caption) && !"資助專上課程及專上學生車船津貼".equals(object) && !"持續進修基金".equals(object)) {
            tr = table.get(0).select("tbody").select("tr");
            for(int i = 1; i < tr.size(); i++){
                String time = sdf.format(new Date());
                Crawler crawler = new Crawler();
                JSONObject map = new JSONObject();
                String title;
                String tb5;
                String tb6 = null;
                if(tr.get(i).select("td").size() > 5){
                    title = tr.get(i).select("td").get(2).text();
                    tb5 = tr.get(i).select("td").get(4).select("a").attr("href").replaceAll("../../../../","https://www.wfsfaa.gov.hk/sfo/");
                    tb6 = tr.get(i).select("td").get(5).select("a").attr("href").replaceAll("../../../../","https://www.wfsfaa.gov.hk/sfo/");
                }else{
                    title = tr.get(i).select("td").get(2).text();
                    tb5 = tr.get(i).select("td").get(4).select("a").attr("href");
                    //String tb6 = tr.get(i).select("td").get(5).select("a").attr("href");
                }
                map.put("title",title);
                if(tb6 != null && tb6.trim().length() > 0){
                    map.put("restUrl",tb6);
                    crawler.setFormurl(tb6);
                }else{
                    map.put("restUrl",tb5);
                    crawler.setFormurl(tb5);
                }
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                crawler.setFormname(title);

                crawler.setReptiletime(df.format(new Date()));
                crawler.setReptiledate(time);
                crawler.setId(uuid);
                //crawler.setFormid(formId);
                crawler.setClassification(category.replaceAll(" / ","，"));
                crawler.setObject(object.replaceAll(" / ","，"));
                listHK.add(crawler);
                map.put("object",object.replaceAll(" / ","，"));
                map.put("id",id);
                map.put("category",category.replaceAll(" / ","，"));
                list.add(map);
                System.out.println("title:"+title+"  ;td5:"+tb5+"  ;td6:"+tb6+"   ;category="+category+"---object="+object);
            }
            //List lsit1 = addKnowledgeUtils.addGenericKnowledge(list);
            // System.out.println("lsit1 -- > "+ lsit1);
        }else if("內地大學升學資助計劃".equals(object)){
            tr = table.get(0).select("tbody").select("tr");
            for(int i = 0; i < tr.size(); i++){
                JSONObject map = new JSONObject();
                String time = sdf.format(new Date());
                Crawler crawler = new Crawler();
                String title = tr.get(i).select("td").get(0).text();
                String tb5 = tr.get(i).select("td").get(1).text();
                String  tb6 = tr.get(i).select("td").get(2).text();
                String tb = tr.get(i).select("td").get(3).select("a").attr("href").replaceAll("../../","https://www.wfsfaa.gov.hk/sfo/");;
                map.put("title",tb5);
                if(tb != null && tb.trim().length() > 0){
                    map.put("restUrl",tb);
                    crawler.setFormurl(tb);
                }/*else{
                  map.put("restUrl",tb);
              }*/

                map.put("object",object.replaceAll(" / ","，"));
                map.put("id",id);
                map.put("category",category.replaceAll(" / ","，"));
                System.out.println("內地大學升學資助計劃-->title:"+title+"  ;td5:"+tb5+"  ;td6:"+tb6+"=="+tb+"   ;category="+category+"---object="+object);
                list.add(map);
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                crawler.setFormname(tb5);

                crawler.setReptiletime(df.format(new Date()));
                crawler.setReptiledate(time);
                crawler.setId(uuid);
                //crawler.setFormid(formId);
                crawler.setClassification(category.replaceAll(" / ","，"));
                crawler.setObject(object.replaceAll(" / ","，"));
                listHK.add(crawler);
            }
            //List lsit1 = addKnowledgeUtils.addGenericKnowledge(list);
            // System.out.println("lsit1--內地大學升學資助計劃-->"+lsit1);
        }else if("持續進修基金".equals(object)){
            tr = table.get(0).select("tbody").select("tr");
            for(int i = 1; i < tr.size(); i++){
                String time = sdf.format(new Date());
                Crawler crawler = new Crawler();
                JSONObject map = new JSONObject();
                String title;
                String tb5;
                String tb6 = null;
                if(tr.get(i).select("td").size() > 2){
                    title = tr.get(i).select("td").get(0).text();
                    tb5 = tr.get(i).select("td").get(1).select("a").attr("href");
                    tb6 = tr.get(i).select("td").get(2).select("a").attr("href");
                }else{
                    title = tr.get(i).select("td").get(0).text();
                    tb5 = tr.get(i).select("td").get(1).select("a").attr("href");

                }
                map.put("title",title);
                if(tb6 != null && tb6.trim().length() > 0){
                    map.put("restUrl",tb6);
                    crawler.setFormurl(tb6);
                }else{
                    map.put("restUrl",tb5.replaceAll("../../","https://www.wfsfaa.gov.hk/cef/"));
                    crawler.setFormurl(tb5.replaceAll("../../","https://www.wfsfaa.gov.hk/cef/"));
                }
                map.put("object",object.replaceAll(" / ","，"));
                map.put("id",id);
                map.put("category",category.replaceAll(" / ","，"));
                System.out.println("持續進修基金-->title:"+title+"  ;td5:"+tb5+"  ;td6:"+tb6+"   ;category="+category+"---object="+object);
                list.add(map);
                String uuid = UUID.randomUUID().toString().replaceAll("-","");
                crawler.setFormname(title);

                crawler.setReptiletime(df.format(new Date()));
                crawler.setReptiledate(time);
                crawler.setId(uuid);
                //crawler.setFormid(formId);
                crawler.setClassification(category.replaceAll(" / ","，"));
                crawler.setObject(object.replaceAll(" / ","，"));
                listHK.add(crawler);
            }
            // List lsit1 = addKnowledgeUtils.addGenericKnowledge(list);
            // System.out.println("lsit1--持續進修基金-->"+lsit1);
        }else if("資助專上課程及專上學生車船津貼".equals(object)){
            tr = table.get(0).select("tbody").select("tr");
            for (int i = 0 ;i < tr.size(); i++){
                String time = sdf.format(new Date());
                Crawler crawler = new Crawler();
                JSONObject map = new JSONObject();
                Elements td = tr.get(i).select("td");
                if(td.size() > 3){
                    String title = td.get(1).text();
                    String tb5 = td.get(2).select("a").attr("href").replaceAll("../../../../","https://www.wfsfaa.gov.hk/sfo/");
//                    System.out.println("資助專上課程及專上學生車船津貼--->title="+title+";;tb5="+tb5);
                    map.put("title",title);
                    map.put("restUrl",tb5);
                    map.put("object",object.replaceAll(" / ","，"));
                    map.put("id",id);
                    map.put("category",category.replaceAll(" / ","，"));
                    list.add(map);
                    String uuid = UUID.randomUUID().toString().replaceAll("-","");
                    crawler.setFormname(title);
                    crawler.setFormurl(tb5);
                    crawler.setReptiletime(df.format(new Date()));
                    crawler.setReptiledate(time);
                    crawler.setId(uuid);
                    //crawler.setFormid(formId);
                    crawler.setClassification(category.replaceAll(" / ","，"));
                    crawler.setObject(object.replaceAll(" / ","，"));
                    listHK.add(crawler);
                }
            }
        }
        System.out.println("listHK = " + listHK.size());
        if(listHK.size() > 0 ){
            crawlerGOV.crawlerService.insertForeach(listHK);
        }
    }

    /**
     * 查询爬虫结果
     * @return
     */

    public List getCrawlerList(String reptiledate,String classification,String object,String language){
        //取今天日期,如果日期类型为String类型,可以使用df.parse()方法,转换为Date类型
        List<Crawler>  list ;
        if("en".equals(language)){
            list = crawlerGOV.crawlerService.getCrawlerListEN(reptiledate,classification,object);
        }else if("cn".equals(language)){
            list = crawlerGOV.crawlerService.getCrawlerListSC(reptiledate,classification,object);
        }else{
            list = crawlerGOV.crawlerService.getCrawlerList(reptiledate,classification,object);
        }
        return list;
    }
//public List getObject(String reptiledate){
//    List<Crawler>  list = crawlerGOV.crawlerService.getCrawlerObjectList(reptiledate);
//    return list;
//}
    public List getObject(String reptiledate,String language){
        LOGGER.info("reptiledate:{"+reptiledate+" }language{"+language+"}");
        List<Crawler>  list = null;
        if("en".equals(language)){
            list = crawlerGOV.crawlerService.getCrawlerObjectListEN(reptiledate);
        }else if("cn".equals(language)){
            list = crawlerGOV.crawlerService.getCrawlerObjectListSC(reptiledate);
        }else{
            list = crawlerGOV.crawlerService.getCrawlerObjectList(reptiledate);
        }
        return list;
    }
    public  void insertDB(List<Crawler> listHK ,String language){
        if("en".equals(language)){
            crawlerGOV.crawlerService.insertForeachEN(listHK);
        }else if("cn".equals(language)){
            crawlerGOV.crawlerService.insertForeachSC(listHK);
        }else{
            crawlerGOV.crawlerService.insertForeach(listHK);
        }
    }
}
