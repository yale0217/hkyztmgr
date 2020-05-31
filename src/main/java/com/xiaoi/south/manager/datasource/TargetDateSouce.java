package com.xiaoi.south.manager.datasource;

import com.alibaba.druid.support.json.JSONUtils;
import com.xiaoi.south.manager.common.ContextConst;

import java.lang.annotation.*;
/**
 * @Auther: yale.ye
 * @Date: 2020/3/4 17:00
 * @Description:
 * 此注解主要用在service实现类方法上
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDateSouce {
    ContextConst.DataSourceType value() default ContextConst.DataSourceType.HK;
}
