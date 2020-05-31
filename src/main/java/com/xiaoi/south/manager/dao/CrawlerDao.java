package com.xiaoi.south.manager.dao;

import com.xiaoi.south.manager.entity.Crawler;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 爬虫数据入库
 */
public interface CrawlerDao {
    /**
     * 插入数据到om_crawler表
     * @param list
     * @return
     */
    int insertForeach(@Param("list") List<Crawler> list);

    /**
     * 查询结果
     * @param reptiledate
     * @return
     */
    List<Crawler> getCrawlerList(@Param("reptiledate")String reptiledate,@Param("classification")String classification,@Param("object")String object);

    /**
     * 获得分类
     * @param reptiledate
     * @return
     */
    List<Crawler> getCrawlerObjectList(@Param("reptiledate")String reptiledate);
}
