package com.example.apollo;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author eddie.lee
 * @ProjectName sentinel-apollo-demo
 * @Package apollo
 * @ClassName Application
 * @description
 * @date created in 2021-03-14 22:55
 * @modified by
 */
@EnableApolloConfig
@SpringBootApplication
public class ApolloApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ApolloApplication.class)
                .web(WebApplicationType.SERVLET)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

}
