package com.example.apollo.controller;

import com.example.apollo.config.ApolloConfigBean;
import com.example.apollo.service.DegradeService;
import com.example.apollo.service.FlowService;
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

    @Autowired
    private FlowService flowService;

    @Autowired
    private DegradeService degradeService;

    @GetMapping("/index")
    public String index() {
        log.info("timout: [{}], hello: [{}]",
                apolloConfigBean.getTimeout(),
                apolloConfigBean.getHello()
        );
        return "timout: [" + apolloConfigBean.getTimeout() + "], hello: [" + apolloConfigBean.getHello() + "]";
    }

    @GetMapping("/testFlow")
    public String testFlow() {
        return flowService.testFlow();
    }

    @GetMapping("/testDegrade")
    public String testDegrade() {
        return degradeService.testDegrade();
    }

}
