package com.example.apollo.config;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author eddie.lee
 * @ProjectName sentinel-apollo-demo
 * @Package com.example.apollo.config
 * @ClassName Sentinel4ApolloConfig
 * @description
 * @date created in 2021-03-22 12:38
 * @modified by
 */
@Configuration
public class SentinelApolloConfig {

    @Value("${spring.application.name}")
    private String applicationName = "";

    @Bean
    @ConditionalOnMissingBean
    public SentinelResourceAspect sentinelresourceaspect() {
        return new SentinelResourceAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    public ApolloDataSourceListener apolloDataSourceListener() {
        return new ApolloDataSourceListener(applicationName);
    }

}
