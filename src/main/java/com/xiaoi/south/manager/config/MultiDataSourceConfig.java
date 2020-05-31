package com.xiaoi.south.manager.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.xiaoi.south.manager.common.ContextConst;
import com.xiaoi.south.manager.datasource.DynamicDataSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * @Auther: yale.ye
 * @Date: 2020/3/4 17:00
 * @Description:动态数据源配置类
 */
@Configuration
public class MultiDataSourceConfig {

    @Bean
    //2.0.0版本只需要修改pom文件中的依赖，yml的配置，和@ConfigurationProperties注解引入yml文件配置，其余不变
    //@ConfigurationProperties(prefix = "spring.datasource.druid.master")
    @ConfigurationProperties(prefix = "spring.datasource.hk")
    public DruidDataSource hkDataSource(){
        return DruidDataSourceBuilder.create().build();
    }
    @Bean
    //@ConfigurationProperties(prefix = "spring.datasource.druid.cluster")
    @ConfigurationProperties(prefix = "spring.datasource.sc")
    @ConditionalOnProperty(prefix = "spring.datasource.sc", name = "enabled", havingValue = "true")
    public DruidDataSource scDataSource(){
        return DruidDataSourceBuilder.create().build();
    }
    @Bean
    //@ConfigurationProperties(prefix = "spring.datasource.druid.cluster")
    @ConfigurationProperties(prefix = "spring.datasource.en")
    @ConditionalOnProperty(prefix = "spring.datasource.en", name = "enabled", havingValue = "true")
    public DruidDataSource enDataSource(){
        return DruidDataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public DataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        //配置默认数据源
        dynamicDataSource.setDefaultTargetDataSource(hkDataSource());
        //配置多数据源这里的key一定要是string类型，枚举类型并不支持，所以用到枚举中name()方法转成string，或者用toString方法。
        HashMap<Object, Object> dataSourceMap = new HashMap();
        dataSourceMap.put(ContextConst.DataSourceType.HK.name(),hkDataSource());
        dataSourceMap.put(ContextConst.DataSourceType.SC.name(),scDataSource());
        dataSourceMap.put(ContextConst.DataSourceType.EN.name(),enDataSource());
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        return dynamicDataSource;
    }
    /**
     * 配置@Transactional注解事务
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }

}
