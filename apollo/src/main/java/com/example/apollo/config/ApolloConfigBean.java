package com.example.apollo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author eddie.lee
 * @ProjectName sentinel-apollo-demo
 * @Package apollo
 * @ClassName ApolloConfigBean
 * @description
 * @date created in 2021-03-14 22:59
 * @modified by
 */
@Data
@Configuration
public class ApolloConfigBean {

    @Value("${timeout:10}")
    private int timeout;

    @Value("${hello:'admin'}")
    private String hello;

}
