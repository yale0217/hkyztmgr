package com.xiaoi.south.manager.utlis;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataUtils {

    public String getFirstDay(){
        // 获取当月第一天和最后一天
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String firstday;
        // 获取前月的第一天
        Calendar cale = null;
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format.format(cale.getTime())+ " 00:00:00";
        return firstday;
    }
}
