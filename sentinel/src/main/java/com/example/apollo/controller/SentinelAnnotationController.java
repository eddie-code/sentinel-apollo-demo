package com.example.apollo.controller;

import com.example.apollo.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eddie.lee
 * @ProjectName sentinel-apollo-demo
 * @Package com.example.apollo.controller
 * @ClassName SentinelAnnotationController
 * @description
 * @date created in 2021-03-16 19:31
 * @modified by
 */
@RestController
public class SentinelAnnotationController {

    @Autowired
    private FlowService flowService;

    @GetMapping("/flow-test")
    public String flowTest() {
        return flowService.flow();

    }

}
