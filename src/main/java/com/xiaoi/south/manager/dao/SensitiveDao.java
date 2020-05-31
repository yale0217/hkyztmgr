package com.xiaoi.south.manager.dao;

import com.xiaoi.south.manager.entity.FaqVote;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SensitiveDao {
    List<FaqVote> getFaqVoteList(@Param("starttime") String starttime, @Param("endtime") String endtime, @Param("user_question") String user_question, @Param("faqvote_type") String faqvote_type, @Param("platform") String platform, @Param("answer") String answer);
}
