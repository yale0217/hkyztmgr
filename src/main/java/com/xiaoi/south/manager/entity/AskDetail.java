package com.xiaoi.south.manager.entity;

import lombok.Data;


@Data
public class AskDetail {
    private String user_id;
    private String user_question;
    private String question;
    private String answer;
    private String visit_time;
    private String platform;
    private int numeber;
}
