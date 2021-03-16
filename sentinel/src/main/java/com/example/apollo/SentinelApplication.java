package com.example.apollo;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eddie.lee
 * @ProjectName sentinel-apollo-demo
 * @Package apollo
 * @ClassName Application
 * @description
 * @date created in 2021-03-14 22:55
 * @modified by
 */
@SpringBootApplication
public class SentinelApplication {

//    public static void intFlowRules() {
//        List<FlowRule> rules = new ArrayList<FlowRule>();
//        FlowRule rule = new FlowRule();
//        //	注意： 我们的规则一定要绑定到对应的资源上，通过资源名称进行绑定
//        rule.setResource("helloworld");
//        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
//        // 流控规则 - 阈值
//        rule.setCount(10);
//        rules.add(rule);
//        // 规则管理器
//        FlowRuleManager.loadRules(rules);
//    }
//
//    	public static void intDegradeRules() {
//		List<DegradeRule> rules = new ArrayList<DegradeRule>();
//		DegradeRule rule = new DegradeRule();
//		//	注意： 我们的规则一定要绑定到对应的资源上，通过资源名称进行绑定
//		rule.setResource("com.example.apollo.controller.DemoController:degrade:selectDegrade");
//		rule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);
//        // 出现两次 Exception 就降级
//		rule.setCount(2);
//		rules.add(rule);
//		// 规则管理器
//		DegradeRuleManager.loadRules(rules);
//	}

    public static void main(String[] args) {
        SpringApplication.run(SentinelApplication.class, args);
//		intFlowRules();
//        intDegradeRules();
        System.err.println("规则加载完毕!");
    }
}
