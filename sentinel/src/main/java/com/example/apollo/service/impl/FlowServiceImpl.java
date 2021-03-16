package com.example.apollo.service.impl;

import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.apollo.service.FlowService;
import org.springframework.stereotype.Service;

/**
 * @author eddie.lee
 * @ProjectName sentinel-apollo-demo
 * @Package com.example.apollo.service.impl
 * @ClassName FlowServiceImpl
 * @description
 * @date created in 2021-03-16 19:30
 * @modified by
 */
@Service
public class FlowServiceImpl implements FlowService {

    /**
     * blockHandler：流控降级的时候进入的兜底函数
     * fallback：抛出异常的时候进入的兜底函数
     * （1.6.0之前的版本 fallback 函数只针对降级异常（DegradeException）进行处理，不能针对业务异常进行处理）
     *
     * @return
     */
    @SentinelResource(
            value = "com.example.apollo.service.FlowServiceImpl:flow", // 权限命名
            entryType = EntryType.OUT,
            blockHandler = "flowBlockHandler",
            fallback = ""
    )
    @Override
    public String flow() {
        System.err.println("----> 正常执行flow方法");
        return "flow";
    }

    public String flowBlockHandler(BlockException ex) {
        System.err.println("----> 触发流控策略：" + ex);
        return "执行流控方法";
    }

}
