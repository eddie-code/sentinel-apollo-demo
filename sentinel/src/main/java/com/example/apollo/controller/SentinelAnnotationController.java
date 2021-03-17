package com.example.apollo.controller;

import com.example.apollo.service.DegradeService;
import com.example.apollo.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eddie.lee
 * @ProjectName sentinel-apollo-demo
 * @Package com.example.apollo.controller
 * @ClassName SentinelAnnotationController
 * @description 注解方式 - 降级、流控
 * @date created in 2021-03-16 19:31
 * @modified by
 */
@RestController
public class SentinelAnnotationController {

    @Autowired
    private FlowService flowService;

    @Autowired
    private DegradeService degradeService;

    @GetMapping("/flow-test")
    public String flowTest() {
        return flowService.flow();
    }

    @GetMapping("/degrade-test")
    public String degradeTest() {
        return degradeService.degrade();
    }

}
