package com.xiaoi.south.manager.service;

import com.xiaoi.south.manager.entity.Crawler;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 爬虫数据入库
 */
public interface CrawlerService {
    int  insertForeach(List<Crawler> list);
    int  insertForeachSC(List<Crawler> list);
    int  insertForeachEN(List<Crawler> list);
    List<Crawler> getCrawlerList(String reptiledate,String classification,String object);
    List<Crawler> getCrawlerListSC(String reptiledate,String classification,String object);
    List<Crawler> getCrawlerListEN(String reptiledate,String classification,String object);
    List<Crawler> getCrawlerObjectList(String reptiledate);
    List<Crawler> getCrawlerObjectListSC(String reptiledate);
    List<Crawler> getCrawlerObjectListEN(String reptiledate);
}
