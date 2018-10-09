package com.peas.hsf.pool;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

public class DruidDataSourceFactoryAdapter extends UnpooledDataSourceFactory {
    public DruidDataSourceFactoryAdapter() {
        this.dataSource = new DruidDataSource();
    }
}