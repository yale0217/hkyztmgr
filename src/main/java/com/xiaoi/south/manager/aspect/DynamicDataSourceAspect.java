package com.xiaoi.south.manager.aspect;

import com.xiaoi.south.manager.common.ContextConst;
import com.xiaoi.south.manager.datasource.DataSourceContextHolder;
import com.xiaoi.south.manager.datasource.TargetDateSouce;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @Auther: yale.ye
 * @Date: 2020/3/4 17:00
 * @Description:动态数据源通知
 */
@Component
@Aspect
@Order(1) //保证在@Transactional之前执行，必须加上，不然无法分辨是哪个数据源在执行事务
@Slf4j
public class DynamicDataSourceAspect {
    @Before("execution(* com.xiaoi.south.*.service..*.*(..))")
    public void before(JoinPoint point){
        try {
            TargetDateSouce annotationOfClass = point.getTarget().getClass().getAnnotation(TargetDateSouce.class);
            String methodName = point.getSignature().getName();
            Class[] parameterTypes = ((MethodSignature) point.getSignature()).getParameterTypes();
            Method method = point.getTarget().getClass().getMethod(methodName, parameterTypes);
            TargetDateSouce methodAnnotation = method.getAnnotation(TargetDateSouce.class);
            methodAnnotation = methodAnnotation == null ? annotationOfClass:methodAnnotation;
            ContextConst.DataSourceType dataSourceType = methodAnnotation != null && methodAnnotation.value() !=null ? methodAnnotation.value() :ContextConst.DataSourceType.HK ;
            DataSourceContextHolder.setDataSource(dataSourceType.name());
        } catch (NoSuchMethodException e) {
            log.error("error",e);
        }
    }

    @After("execution(* com.xiaoi.south.*.service..*.*(..))")
    public void after(JoinPoint point){
        DataSourceContextHolder.clearDataSource();
    }
}
