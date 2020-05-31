package com.xiaoi.south.manager.dao;

import com.xiaoi.south.manager.entity.Click;
import com.xiaoi.south.manager.entity.ClickPercent;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * 查询点击数据
 */
public interface ClickDao {
    /**
     * 查詢點擊報表數據
     * @param starttime
     * @param endtime
     * @param u_question
     * @param b_question
     * @param catname
     * @param answer
     * @param userId
     * @return
     */
    List<Click> getClickList(@Param("starttime")String starttime,@Param("endtime")String endtime, @Param("u_question")String u_question, @Param("b_question")String b_question,
                             @Param("catname")String catname, @Param("answer")String answer, @Param("userId")String userId,@Param("statistics")String statistics,@Param("source")String source);


    /**
     * 查詢知識分類
     * @return
     */
    List<HashMap>  getCategoryName();

    /**
     * 点击率统计
     * @return
     */
    List<ClickPercent> getPercent(@Param("starttime")String starttime,@Param("endtime")String endtime,@Param("catname")String catname);
}
