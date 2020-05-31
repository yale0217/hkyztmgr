package com.xiaoi.south.manager.service;

import com.github.pagehelper.PageInfo;
import com.xiaoi.south.manager.entity.AskDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AskDetailService {

    PageInfo<AskDetail> getAskDetails(String starttime, String endtime, String user_question,String platform, String tableName, int page, int limit);
    PageInfo<AskDetail> getAskDetailsSC(String starttime, String endtime, String user_question,String platform, String tableName, int page, int limit);
    PageInfo<AskDetail> getAskDetailsEN(String starttime, String endtime, String user_question,String platform, String tableName, int page, int limit);
    int updateQuestion(String starttime,String endtime,String user_question,String a_question,String tableName);
    int updateQuestionSC(String starttime,String endtime,String user_question,String a_question,String tableName);
    int updateQuestionEN(String starttime,String endtime,String user_question,String a_question,String tableName);
    int updateCilck(String starttime,String endtime,String user_question,String a_question);
    int updateCilckSC(String starttime,String endtime,String user_question,String a_question);
    int updateCilckEN(String starttime,String endtime,String user_question,String a_question);
    int updateEva(String starttime,String endtime,String user_question,String a_question);
    int updateEvaSC(String starttime,String endtime,String user_question,String a_question);
    int updateEvaEN(String starttime,String endtime,String user_question,String a_question);
    int addOperating(String id, String module, int type,String content, String user_name,String user_ip, String operate_time);
    int addOperatingSC(String id, String module, int type,String content, String user_name,String user_ip, String operate_time);
    int addOperatingEN(String id, String module, int type,String content, String user_name,String user_ip, String operate_time);
}
