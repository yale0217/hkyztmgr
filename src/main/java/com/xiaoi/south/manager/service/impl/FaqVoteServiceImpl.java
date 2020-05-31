package com.xiaoi.south.manager.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoi.south.manager.common.ContextConst;
import com.xiaoi.south.manager.dao.FaqVoteDao;
import com.xiaoi.south.manager.datasource.TargetDateSouce;
import com.xiaoi.south.manager.entity.AskDetail;
import com.xiaoi.south.manager.entity.FaqVote;
import com.xiaoi.south.manager.service.FaqVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service("FaqVoteService")
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class FaqVoteServiceImpl implements FaqVoteService {
    @Autowired
    FaqVoteDao faqVoteDao;

    @Override
    @TargetDateSouce(ContextConst.DataSourceType.HK)
    public PageInfo<FaqVote> getFaqVoteList(String starttime, String endtime, String user_question, String faqvote_type, String platform, String answer, int page, int limit) {
        if(page != 0 && limit != 0){
            PageHelper.startPage(page, limit);
        }
        PageHelper.orderBy("vote_time desc");//排序设置
        List<FaqVote> faqVoteList = faqVoteDao.getFaqVoteList(starttime,endtime,user_question,faqvote_type,platform,answer);
        PageInfo<FaqVote> pageinfo = new PageInfo<FaqVote>(faqVoteList);
        return pageinfo;
    }
    @Override
    @TargetDateSouce(ContextConst.DataSourceType.SC)
    public PageInfo<FaqVote> getFaqVoteListSC(String starttime, String endtime, String user_question, String faqvote_type, String platform, String answer, int page, int limit) {
        if(page != 0 && limit != 0){
            PageHelper.startPage(page, limit);
        }
        PageHelper.orderBy("vote_time desc");//排序设置
        List<FaqVote> faqVoteList = faqVoteDao.getFaqVoteList(starttime,endtime,user_question,faqvote_type,platform,answer);
        PageInfo<FaqVote> pageinfo = new PageInfo<FaqVote>(faqVoteList);
        return pageinfo;
    }
    @Override
    @TargetDateSouce(ContextConst.DataSourceType.EN)
    public PageInfo<FaqVote> getFaqVoteListEN(String starttime, String endtime, String user_question, String faqvote_type, String platform, String answer, int page, int limit) {
        if(page != 0 && limit != 0){
            PageHelper.startPage(page, limit);
        }
        PageHelper.orderBy("vote_time desc");//排序设置
        List<FaqVote> faqVoteList = faqVoteDao.getFaqVoteList(starttime,endtime,user_question,faqvote_type,platform,answer);
        PageInfo<FaqVote> pageinfo = new PageInfo<FaqVote>(faqVoteList);
        return pageinfo;
    }
}
