package com.example.apollo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;

/**
 * @author eddie.lee
 * @ProjectName sentinel-apollo-demo
 * @Package com.example.apollo.controller
 * @ClassName DemoController
 * @description
 * @date created in 2021-03-15 10:45
 * @modified by
 */
@RestController
public class DemoController {

    @RequestMapping("/flow")
    public String flow() throws InterruptedException {
        Entry entry = null;
        try {
            //	2.1 定义资源名称
            entry = SphU.entry("helloworld");
            //	2.2 执行资源逻辑代码
            System.err.println("helloworld: 访问数据库");
            System.err.println("helloworld: 访问远程redis");
            System.err.println("helloworld: 数据库持久化操作");
            Thread.sleep(20);
        } catch (BlockException e) {
            System.err.println("要访问的资源被流控了, 执行流控逻辑！");
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
        return "flow";
    }

}
