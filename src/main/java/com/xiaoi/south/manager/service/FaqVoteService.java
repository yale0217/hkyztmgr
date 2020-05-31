package com.xiaoi.south.manager.service;

import com.github.pagehelper.PageInfo;
import com.xiaoi.south.manager.entity.FaqVote;

import java.util.List;

public interface FaqVoteService {
    /**
     * 查询所有的解决未解决
     *
     * @param
     * @param
     * @return
     */
    PageInfo<FaqVote> getFaqVoteList(String starttime, String endtime, String user_question, String faqvote_type, String platform, String answer, int page, int limit);
    PageInfo<FaqVote> getFaqVoteListEN(String starttime, String endtime, String user_question, String faqvote_type, String platform, String answer, int page, int limit);
    PageInfo<FaqVote> getFaqVoteListSC(String starttime, String endtime, String user_question, String faqvote_type, String platform, String answer, int page, int limit);
}
