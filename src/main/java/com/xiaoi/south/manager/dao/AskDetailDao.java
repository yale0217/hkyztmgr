package com.xiaoi.south.manager.dao;

import com.xiaoi.south.manager.entity.AskDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AskDetailDao {
    //@TargetDateSouce(ContextConst.DataSourceType.SC)
    List<AskDetail> getAskDetails(@Param("starttime")String starttime, @Param("endtime")String endtime, @Param("user_question")String user_question,@Param("platform")String platform , @Param("tableName")String tableName );
   // @TargetDateSouce(ContextConst.DataSourceType.SC)
    int updateQuestion(@Param("starttime")String starttime, @Param("endtime")String endtime,@Param("user_question")String user_question,@Param("a_question")String a_question,@Param("tableName")String tableName);
   // @TargetDateSouce(ContextConst.DataSourceType.SC)
    int updateCilck(@Param("starttime")String starttime, @Param("endtime")String endtime,@Param("user_question")String user_question,@Param("a_question")String a_question);
    //@TargetDateSouce(ContextConst.DataSourceType.SC)
    int updateEva(@Param("starttime")String starttime, @Param("endtime")String endtime,@Param("user_question")String user_question,@Param("a_question")String a_question);
   // @TargetDateSouce(ContextConst.DataSourceType.SC)
    int addOperating(@Param("id")String id, @Param("module")String module,@Param("type")int type,@Param("content")String content,@Param("user_name")String user_name, @Param("user_ip")String user_ip,@Param("operate_time")String operate_time);
}
