package com.xiaoi.south.manager.entity;

import lombok.Data;

/**
 * 点击率报表
 * @author yale.ye
 */
@Data
public class ClickPercent {
    private  int sum_count;
    private int sum_trigger;
    private int sum_repeat;
    private String trigger_data;
    private String percent;
}
