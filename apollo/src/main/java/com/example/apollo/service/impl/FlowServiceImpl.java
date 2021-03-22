package com.example.apollo.service.impl;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.apollo.service.FlowService;
import org.springframework.stereotype.Service;

/**
 * @author eddie.lee
 * @ProjectName sentinel-apollo-demo
 * @Package com.example.apollo.service
 * @ClassName FlowService
 * @description
 * @date created in 2021-03-22 13:36
 * @modified by
 */
@Service
public class FlowServiceImpl implements FlowService {

    /**
     * test
     *
     * @return str
     */
    @SentinelResource(
            value = "com.example.apollo.service.FlowService:test",
            blockHandler = "testFlowBlockHandler"
    )
    @Override
    public String testFlow() {
        System.out.println("正常执行");
        return "testFlow";
    }

    public String testFlowBlockHandler(BlockException e) {
        System.err.println("流控执行： " + e);
        return "流控执行";
    }

}
