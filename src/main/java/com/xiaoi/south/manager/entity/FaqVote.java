package com.xiaoi.south.manager.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.expression.ParseException;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class FaqVote {
//    private static final String DDFormat = "yyyy-MM-dd HH:mm:ss";
//    private static final String TIME_ZONE = "GMT+8";
    private String id;
    private String session_id;
    private String user_id;
    private String faq_id;
    private String  platform;
//    @JsonFormat(pattern=DDFormat, timezone = TIME_ZONE)
//    private Timestamp vote_time;
    private String action;
    private String city;
    private String user_question;
    private String question;
    private String answer;
    private String  reason;
    private String brand;
    private String stat_key;
    private String custom1;
    private String custom2;
    private String custom3;
    private Date vote_time;
    private int  number;
    //settime的set和get方法
    public String getVote_time() {
        String value = null;
        //将Date类型的时间转换成指定格式的字符串
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        value = dateFormat.format(vote_time);
        return value;
    }
    public void setVote_time(String vote_time) {
        //将字符串类型的日期转换成Date类型的指定格式的日期
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);//从第一个字符开始解析
        try {
            this.vote_time = f.parse(vote_time,pos);/*对参数msg_create_date（String类型）从第一个字符开始解析（由pos），转换成java.util.Date类型，
		而这个Date的格式为"yyyy-MM-dd"（因为SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");）*/
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
