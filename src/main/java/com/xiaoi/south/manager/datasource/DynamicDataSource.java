package com.xiaoi.south.manager.datasource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @Auther: yale.ye
 * @Date: 2020/3/4 17:00
 * @Description:数据源路由实现类
 * AbstractRoutingDataSource(每执行一次数据库，动态获取DataSource)
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSource();
    }
}
