package com.example.apollo.config;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.apollo.ApolloDataSource;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * @author eddie.lee
 * @ProjectName sentinel-apollo-demo
 * @Package com.example.apollo.config
 * @ClassName ApolloDataSourceListener
 * @description
 * @date created in 2021-03-22 12:42
 * @modified by
 */
public class ApolloDataSourceListener implements InitializingBean {

    private String applicationName;

    // *-flow-rules
    private static final String FLOW_RULE_TYPE = "flow";
    private static final String FLOW_DATA_ID_POSTFIX = "-" + FLOW_RULE_TYPE + "-rules";

    private static final String DEGRADE_RULE_TYPE = "degrade";
    private static final String DEGRADE_DATA_ID_POSTFIX = "-" + DEGRADE_RULE_TYPE + "-rules";

    public ApolloDataSourceListener(String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initFlowRules();
        initDegradeRules();
    }

    /**
     * 流控监听
     */
    private void initFlowRules() {
        // apollo-demo-flow-rules
        String flowRuleKey = applicationName + FLOW_DATA_ID_POSTFIX;
        // 动态监听
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource =
                new ApolloDataSource<>("application",
                        flowRuleKey,
                        "[]",
                        source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
                        }));
        // 刷新内存
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }

    /**
     * 降级监听
     */
    private void initDegradeRules() {
        // apollo-demo-degrade-rules
        String degradeKey = applicationName + DEGRADE_DATA_ID_POSTFIX;
        // 动态监听
        ReadableDataSource<String, List<DegradeRule>> flowRuleDataSource =
                new ApolloDataSource<>("application",
                        degradeKey,
                        "[]",
                        source -> JSON.parseObject(source, new TypeReference<List<DegradeRule>>() {
                        }));
        // 刷新内存
        DegradeRuleManager.register2Property(flowRuleDataSource.getProperty());
    }
}
