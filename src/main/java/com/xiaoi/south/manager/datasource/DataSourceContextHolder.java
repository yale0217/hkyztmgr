package com.xiaoi.south.manager.datasource;

public class DataSourceContextHolder {
    /**
     * CONTEXT_HOLDER代表一个可以存放String类型的ThreadLocal对象，
     * 此时任何一个线程可以并发访问这个变量，
     * 对它进行写入、读取操作，都是线程安全的。
     * 比如一个线程通过CONTEXT_HOLDER.set(“aaaa”);将数据写入ThreadLocal中，
     * 在任何一个地方，都可以通过CONTEXT_HOLDER.get();将值获取出来。
     * 这里写入的就是数据库名，
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<String>();

    public static void setDataSource(String dbType){
        System.out.println("切换到["+dbType+"]数据源");
        CONTEXT_HOLDER.set(dbType);
    }

    public static String getDataSource(){
        return CONTEXT_HOLDER.get();
    }

    public static void clearDataSource(){
        CONTEXT_HOLDER.remove();
    }
}
