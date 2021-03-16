package com.example.apollo.controller;

import com.example.apollo.config.ApolloConfigBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eddie.lee
 * @ProjectName sentinel-apollo-demo
 * @Package apollo
 * @ClassName DemoController
 * @description
 * @date created in 2021-03-14 23:03
 * @modified by
 */
@Slf4j
@RestController
public class DemoController {

    @Autowired
    private ApolloConfigBean apolloConfigBean;

    @GetMapping("/index")
    public String index() {
        log.info("timout: [{}], hello: [{}]",
                apolloConfigBean.getTimeout(),
                apolloConfigBean.getHello()
        );
        return "timout: ["+apolloConfigBean.getTimeout()+"], hello: ["+apolloConfigBean.getHello()+"]";
    }

}
