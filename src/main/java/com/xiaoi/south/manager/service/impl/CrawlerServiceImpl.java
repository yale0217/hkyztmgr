package com.xiaoi.south.manager.service.impl;

import com.xiaoi.south.manager.common.ContextConst;
import com.xiaoi.south.manager.dao.CrawlerDao;
import com.xiaoi.south.manager.datasource.TargetDateSouce;
import com.xiaoi.south.manager.entity.Crawler;
import com.xiaoi.south.manager.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 爬虫数据入库
 */
@Service("CrawlerService")
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class CrawlerServiceImpl implements CrawlerService {
    @Autowired
    private CrawlerDao crawlerDao;
    @Override
    @TargetDateSouce(ContextConst.DataSourceType.HK)
    public int insertForeach(List<Crawler> list) {
        return crawlerDao.insertForeach(list);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.SC)
    public int insertForeachSC(List<Crawler> list) {
        return crawlerDao.insertForeach(list);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.EN)
    public int insertForeachEN(List<Crawler> list) {
        return crawlerDao.insertForeach(list);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.HK)
    public List<Crawler> getCrawlerList(String reptiledate,String classification,String object) {
        return crawlerDao.getCrawlerList(reptiledate,classification,object);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.SC)
    public List<Crawler> getCrawlerListSC(String reptiledate, String classification, String object) {
        return crawlerDao.getCrawlerList(reptiledate,classification,object);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.EN)
    public List<Crawler> getCrawlerListEN(String reptiledate, String classification, String object) {
        return crawlerDao.getCrawlerList(reptiledate,classification,object);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.HK)
    public List<Crawler> getCrawlerObjectList(String reptiledate) {
        return crawlerDao.getCrawlerObjectList(reptiledate);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.SC)
    public List<Crawler> getCrawlerObjectListSC(String reptiledate) {
        return crawlerDao.getCrawlerObjectList(reptiledate);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.EN)
    public List<Crawler> getCrawlerObjectListEN(String reptiledate) {
        return crawlerDao.getCrawlerObjectList(reptiledate);
    }
}
