package com.xiaoi.south.manager.service;

import com.github.pagehelper.PageInfo;
import com.xiaoi.south.manager.entity.Click;
import com.xiaoi.south.manager.entity.ClickPercent;
import java.util.HashMap;
import java.util.List;

public interface ClickService {
    PageInfo<Click> getClickList(String starttime, String endtime, String u_question, String b_question, String catname, String answer, String userId, String statistics, String source, int page, int limit);
    PageInfo<Click> getClickListSC(String starttime, String endtime, String u_question, String b_question, String catname, String answer, String userId, String statistics, String source, int page, int limit);
    PageInfo<Click> getClickListEN(String starttime, String endtime, String u_question, String b_question, String catname, String answer, String userId, String statistics, String source, int page, int limit);
  //  int selectNums(String starttime,String endtime,String u_question,String b_question,String catname,String answer,String userId,String statistics,String source);
    List<HashMap> getCategoryName();
    List<HashMap> getCategoryNameSC();
    List<HashMap> getCategoryNameEN();
    List<ClickPercent> getPercent(String starttime,String endtime,String catname);
    List<ClickPercent> getPercentSC(String starttime,String endtime,String catname);
    List<ClickPercent> getPercentEN(String starttime,String endtime,String catname);
}
