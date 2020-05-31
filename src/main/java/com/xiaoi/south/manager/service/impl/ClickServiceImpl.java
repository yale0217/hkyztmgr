package com.xiaoi.south.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoi.south.manager.common.ContextConst;
import com.xiaoi.south.manager.dao.ClickDao;
import com.xiaoi.south.manager.datasource.TargetDateSouce;
import com.xiaoi.south.manager.entity.Click;
import com.xiaoi.south.manager.entity.ClickPercent;
import com.xiaoi.south.manager.service.ClickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
@Service("ClickService")
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class ClickServiceImpl implements ClickService {
    @Autowired
    private ClickDao clickDao;
    @Override
    @TargetDateSouce(ContextConst.DataSourceType.HK)
    public PageInfo<Click> getClickList(String starttime, String endtime, String u_question, String b_question, String catname, String answer, String userId, String statistics, String source, int page, int limit) {
        if(page != 0 && limit != 0){
            PageHelper.startPage(page, limit);
        }
       // PageHelper.startPage(page, limit);
       // PageHelper.orderBy("visit_time desc");//排序设置
        List<Click> clickList = clickDao.getClickList(starttime,endtime,u_question,b_question,catname,answer,userId,statistics,source);
        PageInfo<Click> pageinfo = new PageInfo<>(clickList);
        return pageinfo;
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.SC)
    public PageInfo<Click> getClickListSC(String starttime, String endtime, String u_question, String b_question, String catname, String answer, String userId, String statistics, String source, int page, int limit) {
        if(page != 0 && limit != 0){
            PageHelper.startPage(page, limit);
        }

        List<Click> clickList = clickDao.getClickList(starttime,endtime,u_question,b_question,catname,answer,userId,statistics,source);
        PageInfo<Click> pageinfo = new PageInfo<>(clickList);
        return pageinfo;
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.EN)
    public PageInfo<Click> getClickListEN(String starttime, String endtime, String u_question, String b_question, String catname, String answer, String userId, String statistics, String source, int page, int limit) {
        if(page != 0 && limit != 0){
            PageHelper.startPage(page, limit);
        }
        List<Click> clickList = clickDao.getClickList(starttime,endtime,u_question,b_question,catname,answer,userId,statistics,source);
        PageInfo<Click> pageinfo = new PageInfo<>(clickList);
        return pageinfo;
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.HK)
    public  List<HashMap> getCategoryName() {
        return clickDao.getCategoryName();
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.SC)
    public List<HashMap> getCategoryNameSC() {
        return clickDao.getCategoryName();
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.EN)
    public List<HashMap> getCategoryNameEN() {
        return clickDao.getCategoryName();
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.HK)
    public List<ClickPercent> getPercent(String starttime,String endtime,String catname) {
        return clickDao.getPercent(starttime,endtime,catname);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.SC)
    public List<ClickPercent> getPercentSC(String starttime, String endtime, String catname) {
        return clickDao.getPercent(starttime,endtime,catname);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.EN)
    public List<ClickPercent> getPercentEN(String starttime, String endtime, String catname) {
        return clickDao.getPercent(starttime,endtime,catname);
    }
}
