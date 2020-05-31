package com.xiaoi.south.manager.entity;

import lombok.Data;

/**
 * 点击报表
 */
@Data
public class Click {
//    private String  id;
    private String  userId;
    private String  sessionId;
    private String  u_question;//用户输入问题
    private String  b_question;//标准问题
    private String  catname;//分类名
    private String  objname;//实例名
    private String  answer;
//    private String  objectId;
//    private String  categoryId;
    private String  platform;
//    private String  nodeId;
    private String  statistics;//
    private String  trigger_time;//触发时间
    private String  trgger_url;//点击的URL
    private String  source;
    private int  number;

}
