package com.example.apollo.service.impl;

import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.example.apollo.service.DegradeService;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author eddie.lee
 * @ProjectName sentinel-apollo-demo
 * @Package com.example.apollo.service.impl
 * @ClassName DegradeServiceImpl
 * @description
 * @date created in 2021-03-16 20:06
 * @modified by
 */
@Service
public class DegradeServiceImpl implements DegradeService {

    private AtomicInteger count = new AtomicInteger(0);

    /**
     * blockHandler：流控降级的时候进入的兜底函数
     * fallback：抛出异常的时候进入的兜底函数
     * （1.6.0之前的版本 fallback 函数只针对降级异常（DegradeException）进行处理，不能针对业务异常进行处理）
     *
     * @return
     */
    @SentinelResource(
            value = "com.example.apollo.service.DegradeService:degrade", // 权限命名
            entryType = EntryType.OUT,
            blockHandler = "degradeBlockHandler",
            fallback = "degradeFallback"
    )
    @Override
    public String degrade() {
        System.err.println("----> 正常执行degrade方法");

        // 1.2.4.7 不等于 0
        int i = count.incrementAndGet() % 3;
        System.out.println(i);
        if (i == 0) {
            throw new RuntimeException("1.2.4.7 不等于 0, 抛出业务异常：" + i);
            // 抛出业务异常就会走到这个方法 degradeFallback()
        }

        return "degrade";
    }

    public String degradeBlockHandler(BlockException ex) {
        System.err.println("----> 触发降级流控策略：" + ex);
        return "执行降级流控方法";
    }

    public String degradeFallback(Throwable ex) {
        System.err.println("----> 触发异常时的降级策略：" + ex);
        return "执行异常降级方法";
    }


}
