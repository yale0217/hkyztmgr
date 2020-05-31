package com.xiaoi.south.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoi.south.manager.common.ContextConst;
import com.xiaoi.south.manager.dao.AskDetailDao;
import com.xiaoi.south.manager.datasource.TargetDateSouce;
import com.xiaoi.south.manager.entity.AskDetail;
import com.xiaoi.south.manager.service.AskDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("AskDetailService")
public class AskDetailServiceImpl implements AskDetailService {
    @Autowired
    AskDetailDao askDetailDao;
    @Override
    @TargetDateSouce(ContextConst.DataSourceType.HK)
    public PageInfo<AskDetail> getAskDetails(String starttime, String endtime, String user_question, String platform, String tableName, int page, int limit) {
        PageHelper.startPage(page, limit);
        PageHelper.orderBy("visit_time desc");//排序设置
        List<AskDetail> askDetails = askDetailDao.getAskDetails(starttime,endtime,user_question,platform,tableName);
        PageInfo<AskDetail> pageinfo = new PageInfo<AskDetail>(askDetails);
        return pageinfo;

    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.SC)
    public PageInfo<AskDetail> getAskDetailsSC(String starttime, String endtime, String user_question, String platform, String tableName, int page, int limit) {
        PageHelper.startPage(page, limit);
        PageHelper.orderBy("visit_time desc");//排序设置
        List<AskDetail> askDetails = askDetailDao.getAskDetails(starttime,endtime,user_question,platform,tableName);
        PageInfo<AskDetail> pageinfo = new PageInfo<AskDetail>(askDetails);
        return pageinfo;
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.EN)
    public PageInfo<AskDetail> getAskDetailsEN(String starttime, String endtime, String user_question, String platform, String tableName, int page, int limit) {
        PageHelper.startPage(page, limit);
        PageHelper.orderBy("visit_time desc");//排序设置
        List<AskDetail> askDetails = askDetailDao.getAskDetails(starttime,endtime,user_question,platform,tableName);
        PageInfo<AskDetail> pageinfo = new PageInfo<AskDetail>(askDetails);
        return pageinfo;
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.HK)
    public int updateQuestion(String starttime,String endtime,String user_question,String a_question,String tableName) {
        return askDetailDao.updateQuestion(starttime,endtime,user_question,a_question,tableName);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.SC)
    public int updateQuestionSC(String starttime, String endtime, String user_question, String a_question, String tableName) {
        return askDetailDao.updateQuestion(starttime,endtime,user_question,a_question,tableName);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.EN)
    public int updateQuestionEN(String starttime, String endtime, String user_question, String a_question, String tableName) {
        return askDetailDao.updateQuestion(starttime,endtime,user_question,a_question,tableName);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.HK)
    public int updateCilck(String starttime, String endtime, String user_question, String a_question) {
        return askDetailDao.updateCilck(starttime,endtime,user_question,a_question);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.SC)
    public int updateCilckSC(String starttime, String endtime, String user_question, String a_question) {
        return askDetailDao.updateCilck(starttime,endtime,user_question,a_question);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.EN)
    public int updateCilckEN(String starttime, String endtime, String user_question, String a_question) {
        return askDetailDao.updateCilck(starttime,endtime,user_question,a_question);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.HK)
    public int updateEva(String starttime, String endtime, String user_question, String a_question) {
        return askDetailDao.updateEva(starttime,endtime,user_question,a_question);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.SC)
    public int updateEvaSC(String starttime, String endtime, String user_question, String a_question) {
        return askDetailDao.updateEva(starttime,endtime,user_question,a_question);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.EN)
    public int updateEvaEN(String starttime, String endtime, String user_question, String a_question) {
        return askDetailDao.updateEva(starttime,endtime,user_question,a_question);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.HK)
    public int addOperating(String id, String module, int type, String content, String user_name, String user_ip, String operate_time) {
        return askDetailDao.addOperating(id,module,type,content,user_name,user_ip,operate_time);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.SC)
    public int addOperatingSC(String id, String module, int type, String content, String user_name, String user_ip, String operate_time) {
        return askDetailDao.addOperating(id,module,type,content,user_name,user_ip,operate_time);
    }

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.EN)
    public int addOperatingEN(String id, String module, int type, String content, String user_name, String user_ip, String operate_time) {
        return askDetailDao.addOperating(id,module,type,content,user_name,user_ip,operate_time);
    }


}
