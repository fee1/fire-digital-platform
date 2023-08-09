package com.huajie.domain.common.mybatis.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.github.pagehelper.PageInterceptor;
import com.google.common.collect.Lists;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author zhuxiaofeng
 * @date 2023/8/5
 */
@Configuration
@MapperScan(basePackages = "com.huajie.infrastructure.mapper", annotationClass = Mapper.class)
public class MybatisConfig {

//    @Primary
//    @Bean
//    public ISqlInjector getSqlInjector() {
//        return new LogicSqlInjector();
//    }

    @Primary
    @Bean
    public MetaObjectHandler getMyMetaHandle() {
        return new MyMetaObjectHandler();
    }

    // 创建全局配置
    @Bean
    public GlobalConfig mpGlobalConfig() {
        // 全局配置文件
        GlobalConfig globalConfig = new GlobalConfig();
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        // 默认为自增
        dbConfig.setIdType(IdType.AUTO);
        // 手动指定db 的类型, 这里是mysql
        dbConfig.setDbType(DbType.MYSQL);
        globalConfig.setDbConfig(dbConfig);
//        globalConfig.setSqlInjector(getSqlInjector());
        globalConfig.setMetaObjectHandler(getMyMetaHandle());

        return globalConfig;
    }

    private Interceptor[] generateInterceptor(){
        List<Interceptor> interceptors = Lists.newArrayList();
        interceptors.add(getOptimisticLockerInterceptor());
        interceptors.add(pageInterceptor());
        return interceptors.toArray(new Interceptor[0]);

    }

    /**
     * 乐观锁是一种在并发环境下处理数据更新冲突的策略，它通过在更新操作
     * 时判断数据是否被其他线程修改过，从而避免数据不一致的情况
     * @return
     */
    @Primary
    @Bean
    public OptimisticLockerInterceptor getOptimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    /**
     * 分页拦截器
     * @return
     */
    @Primary
    @Bean
    public PageInterceptor pageInterceptor() {
        return new PageInterceptor();
    }

    @Primary
    @Bean(name = "sqlSessionFactory")
    public MybatisSqlSessionFactoryBean sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource,
                                                          @Value("classpath*:mapper/*Mapper.xml")
                                                                  Resource[] mapperLocations, GlobalConfig globalConfig)  throws Exception{
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        VFS.addImplClass(SpringBootVFS.class);
        sqlSessionFactoryBean.setTypeAliasesPackage("com.huajie.domain.entity");
        sqlSessionFactoryBean.setMapperLocations(mapperLocations);
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:mybatis-config.xml"));
        sqlSessionFactoryBean.setGlobalConfig(globalConfig);
        sqlSessionFactoryBean.setPlugins(generateInterceptor());
        return sqlSessionFactoryBean;
    }

}
