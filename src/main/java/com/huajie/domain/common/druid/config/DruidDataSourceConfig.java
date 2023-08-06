package com.huajie.domain.common.druid.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author zhuxiaofeng
 * @date 2023/8/5
 */
@Configuration
public class DruidDataSourceConfig {

    @Bean
    public DruidConfigProperties druidConfigProperties(){
        return new DruidConfigProperties();
    }

    @Bean("dataSource")
    public DataSource dataSource(DruidConfigProperties properties){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(properties.getDbUrl());
        druidDataSource.setUsername(properties.getUsername());
        druidDataSource.setPassword(properties.getPassword());
        druidDataSource.setDriverClassName(properties.getDriverClassName());
        druidDataSource.setInitialSize(properties.getInitialSize());
        druidDataSource.setMinIdle(properties.getMinIdle());
        druidDataSource.setMaxActive(properties.getMaxActive());
        druidDataSource.setMaxWait(properties.getMaxWait());

        try {
            druidDataSource.setFilters(properties.getFilters());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        druidDataSource.setConnectionProperties(properties.getConnectionProperties());

        return druidDataSource;

    }

}
