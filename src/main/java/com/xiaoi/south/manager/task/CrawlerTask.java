package com.xiaoi.south.manager.task;

import com.xiaoi.south.manager.utlis.CrawlerUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CrawlerTask {
    @Value("${scheduling.enabled}")
    private String scheduling;
    @Scheduled(cron = "0 53 0 * * ?")
    public void task01() throws InterruptedException{
        System.out.println(Thread.currentThread().getName() + "...............task01开始");
        CrawlerUtils crawlerUtils = new CrawlerUtils();
        if(!"false".equals(scheduling)){
            crawlerUtils.getCrawler();
        }
        System.out.println(Thread.currentThread().getName() + "...............task01结束");
    }
    @Scheduled(cron = "0 42 0 * * ?")
    public void task04() throws InterruptedException{
        System.out.println(Thread.currentThread().getName() + "...............task04开始");
        CrawlerUtils crawlerUtils = new CrawlerUtils();
        if(!"false".equals(scheduling)){
            crawlerUtils.getCrawlerCN();
        }
        System.out.println(Thread.currentThread().getName() + "...............task04结束");
    }
    //----------------34.92.172.44 不启用定时任务--------------
   // @Scheduled(cron = "0 07 0 1,16 * ?")
    @Scheduled(cron = "0 30 0 * * ?")
    public void task03() throws Exception {
        System.out.println(Thread.currentThread().getName() + "...............task03开始");
        CrawlerUtils crawlerUtils = new CrawlerUtils();
        if(!"false".equals(scheduling)){
            crawlerUtils.getCrawlerEN();
        }
        System.out.println(Thread.currentThread().getName() + "...............task03结束");
        //List<Crawler> listObject = crawlerUtils.getCrawlerListObject();

    }

//     @Scheduled(cron = "0 31 0 1,16 * ?")
   @Scheduled(cron = "0 50 0 * * ?")
    public void task02() throws Exception {
       CrawlerUtils crawlerUtils = new CrawlerUtils();
         crawlerUtils.writeExcel("cn");
        System.out.println(Thread.currentThread().getName() + "...............task02");
    }
//    @Scheduled(cron = "0 31 0 1,16 * ?")
@Scheduled(cron = "0 46 0 * * ?")
    public void task05() throws Exception {
    CrawlerUtils crawlerUtils = new CrawlerUtils();
        crawlerUtils.writeExcel("en");
        System.out.println(Thread.currentThread().getName() + "...............task05");
    }
//    @Scheduled(cron = "0 31 0 1,16 * ?")
@Scheduled(cron = "0 44 0 * * ?")
    public void task06() throws Exception {
    CrawlerUtils crawlerUtils = new CrawlerUtils();
        crawlerUtils.writeExcel("tc");
        System.out.println(Thread.currentThread().getName() + "...............task06");
    }
}
