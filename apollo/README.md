[TOC]

# 目录

##  2-1 本章概述

- Sentinel 生产环境下如何使用
- 自定义规则持久化扩展实战 
  - 快速使用 Apollo
  - 开放平台
  - Apollo OpenAPI使用
  - Sentinel Dashboard 扩展实战

## 2-2 生产环境下如何使用Sentinel?

- 规则管理
- 规则推送
- 流量监控

### 规则管理及推送


推送模式 | 说明 | 优点 | 缺点
---|---|---|---
原始模式 | API将规则推送至客户端并直接更新到内存中, 扩展写数据源 (++WritableDateSource++) | 简单, 无任何依赖 | 不保证一致性; 规则保存在内存中, 重启立即消失. 严重不建议用于生产环境
Pull 模式 | 扩展写数据源 (++WritableDateSource++), 客户端主动向某个规则管理中心定期轮询拉取规则, 这个规则中心可以是 RDBMS、文件等 | 简单, 无任何依赖; 规则持久化 | 不保证一致性; 实时性不保证, 拉取过于频繁也可能会有性能问题.
Push 模式 | 扩展读数据源 (++ReadableDateSource++), 规则中心统一推送, 客户端通过注册监听器的方式时刻监听变化, 比如使用 Nacos、 Zookeeper 等配置中心. 这种方式有更好的实时性和一致性的保证, **生产环境下一般采用 push 模式的数据源** | 规则持久化; 一致性; 快速 | 引入第三方依赖

## 2-3 Apollo快速使用-1

### 链接
- [Github 网址](https://github.com/ctripcorp/apollo)
- [快速开始](https://ctripcorp.github.io/apollo/#/zh/deployment/quick-start)

### 安装包方式

#### 1.1 准备工作
- java 1.8+
- Maven
- MySQL 5.6.5+

#### 1.2 下载安装包
- Github
  - checkout或下载apollo-build-scripts项目
- BaiduYunPan
  - https://pan.baidu.com/share/init?surl=Ieelw6y3adECgktO0ea0Gg 提取码: 9wwe

#### 1.3 创建数据库
- 解压安装包里面有sql文件夹
  - apolloconfigdb.sql  
  - apolloportaldb.sql
  - 运行语句即可

#### 1.4 配置数据库连接
```
#apollo config db info
apollo_config_db_url="jdbc:mysql://localhost:3306/ApolloConfigDB?characterEncoding=utf8&serverTimezone=Asia/Shanghai"
apollo_config_db_username=用户名
apollo_config_db_password=密码（如果没有密码，留空即可）

# apollo portal db info
apollo_portal_db_url="jdbc:mysql://localhost:3306/ApolloPortalDB?characterEncoding=utf8&serverTimezone=Asia/Shanghai"
apollo_portal_db_username=用户名
apollo_portal_db_password=密码（如果没有密码，留空即可）
```

> 注意：不要修改demo.sh的其它部分

#### 1.5 启动服务
查看端口是否占用, 分别是8070, 8080, 8090端口

#### 1.6 执行启动脚本

```shell
[root@k8s-master1 apollo]# pwd
/opt/apollo
[root@k8s-master1 apollo]# ./demo.sh start

==== starting service ====
Service logging file is ./service/apollo-service.log
Application is running as root (UID 0). This is considered insecure.
Started [24036]
Waiting for config service startup..
Config service started. You may visit http://localhost:8080 for service status now!
Waiting for admin service startup.
Admin service started
==== starting portal ====
Portal logging file is ./portal/apollo-portal.log
Application is running as root (UID 0). This is considered insecure.
Started [24335]
Waiting for portal startup..
Portal started. You can visit http://localhost:8070 now!
```

#### 1.7 访问项目

http://localhost:8070

## 2-4 Apollo快速使用-2

### Quick start

（1）添加Maven依赖
```xml
<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>com.alibaba.csp</groupId>
        <artifactId>sentinel-core</artifactId>
        <version>1.6.3</version>
    </dependency>

    <dependency>
        <groupId>com.alibaba.csp</groupId>
        <artifactId>sentinel-transport-simple-http</artifactId>
        <version>1.6.3</version>
    </dependency>

    <dependency>
        <groupId>com.alibaba.csp</groupId>
        <artifactId>sentinel-annotation-aspectj</artifactId>
        <version>1.6.3</version>
    </dependency>

    <dependency>
        <groupId>com.ctrip.framework.apollo</groupId>
        <artifactId>apollo-client</artifactId>
        <version>1.4.0</version>
    </dependency>

    <dependency>
        <groupId>com.alibaba.csp</groupId>
        <artifactId>sentinel-datasource-extension</artifactId>
        <version>1.6.3</version>
    </dependency>

    <dependency>
        <groupId>com.alibaba.csp</groupId>
        <artifactId>sentinel-datasource-apollo</artifactId>
        <version>1.6.3</version>
    </dependency>

</dependencies>
```

（2） Yaml配置文件
```yaml
###################################################
#
#    Spring基础配置
#
###################################################
spring:
  application:
    name: sentinel-apollo-demo
  http:
    encoding:
      charset: utf-8
  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: GMT+8
    default-property-inclusion: non_null
###################################################
#
#    Server基础配置
#
###################################################
server:
  port: 8081
  servlet:
    context-path: /
###################################################
#
#    Apollp基础配置
#
###################################################
app:
  id: sentinel-apollo-demo # apollo applicaiton.id
apollo:
  meta: http://localhost:8080 # apollo-config-service eureka.address
```

（3）打开 Apollo Web

http://localhost:8070

![](.README_images/e76b3309.png)

新增配置

![](.README_images/bb617ce5.png)

（4）创建配置实体与控制层请求

com.example.apollo.config.ApolloConfigBean
```java
@Data
@Configuration
public class ApolloConfigBean {

    @Value("${timeout:10}")
    private int timeout;

    @Value("${hello:'admin'}")
    private String hello;

}
```

com.example.apollo.controller.DemoController
```java
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
```

查看控制台信息
```text
## 请求接口
GET localhost:8081/index

## 日志打印 (无论请求多少次，得到也是代码里面的默认值)
2021-03-14 23:13:08.581  INFO 15704 --- [nio-8081-exec-2] c.e.com.example.apollo.controller.DemoController     : timout: [10], hello: ['admin']
```

查看Web端

![](.README_images/150ba816.png)

> 发布状态 -> "未发布" -> "已发布"


再次请求 GET localhost:8081/index

```text
2021-03-14 23:55:18.039  INFO 19396 --- [nio-8081-exec-2] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2021-03-14 23:55:18.039  INFO 19396 --- [nio-8081-exec-2] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2021-03-14 23:55:18.045  INFO 19396 --- [nio-8081-exec-2] o.s.web.servlet.DispatcherServlet        : Completed initialization in 6 ms
2021-03-14 23:55:18.061  INFO 19396 --- [nio-8081-exec-2] c.e.com.example.apollo.controller.DemoController     : timout: [20], hello: [eddie]
```

> 无论在 Apollo Web 怎么修改都好, 记得点击“发布”, 数据才会同步的


## 2-5 Apollo第三方授权

- 管理员工具 
  - 开放平台来对配置进行管理
  
![](.README_images/a3043267.png)

## 2-8 Sentinel整合Apollo配置文件解析与ApolloOpenApiClient创建-1

（一）[Download Sentinel](https://github.com/alibaba/Sentinel)

（二）sentinel-apollo-demo --> apollo --> 修改 jvm 参数
```java
-Dcsp.sentinel.dashboard.server=localhost:8080
-Dcsp.sentinel.api.port=8719
-Dproject.name=apollo-test
```

（三）修改 Sentinel --> sentinel-dashboard --> application.properties (最底部追加, 不修改原有配置)
```properties
########################################
## apollo配置信息
########################################

## apollo服务地址
apollo.portal.url=http://192.168.8.240:8070/

## 组合使用
## 应用服务名称：apollo-test (这个是 sentinel-apollo-demo --> apollo --> 修改 jvm 参数：-Dproject.name=apollo-test)
## apollo token: 540a316e923ad32669b0e3c12b5152165fe602f2
## apollo appId: sentinel-apollo-demo (这个是 sentinel-apollo-demo -> apollo -> application.yml # app.id 的参数)
## apollo thirdId：sentinel-apollo-demo-id (第三方应用ID)
apollo.portal.appNameConfigList[0]=apollo-test:540a316e923ad32669b0e3c12b5152165fe602f2:sentinel-apollo-demo：sentinel-apollo-demo-id

## 所属环境
apollo.portal.env=DEV

## 管理用户
#apollo.portal.userId=eddie

## 集群名称
apollo.portal.clusterName=default

## namespace
apollo.portal.nameSpace=application
```

> Apollo Web 查看 apollo token  <br> 1. 管理员工具 <br> 2. 开放平台授权管理

（四）Sentinel --> sentinel-dashboard 创建 package 

```text
com.alibaba.csp.sentinel.dashboard.rule.apollo
```

创建 com.alibaba.csp.sentinel.dashboard.rule.apollo.ApolloConfig
```java
package com.alibaba.csp.sentinel.dashboard.rule.apollo;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.*;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author eddie.lee
 * @ProjectName sentinel-parent
 * @Package com.alibaba.csp.sentinel.dashboard.rule.apollo
 * @ClassName ApolloConfig
 * @description
 * @date created in 2021-03-18 16:08
 * @modified by
 */
@Configuration
@ConfigurationProperties(prefix = "apollo.portal")
@ComponentScan("com.alibaba.csp.sentinel.dashboard.rule.apollo")
public class ApolloConfig implements InitializingBean {

    public static String USERNAME = "eddie";
    public static String PASSWORD = "eddie";
    public static String ENV = "dev";
    public static String URL = "";
    public static String CLUSTER_NAME = "default";
    public static String NAMESPACE = "application";

    private String url;

    private String env = ENV;

    private String username = USERNAME;

    private String password = PASSWORD;

    private String clusterName = CLUSTER_NAME;

    private String nameSpace = NAMESPACE;

    private List<String> appNameConfigList = new ArrayList<>();

    /**
     * volatile的解释：线程之间变量的可见性
     * https://blog.csdn.net/weixin_31479991/article/details/112446110
     */
    public static volatile ConcurrentHashMap<String /*appId*/, String /*thirdId*/> thirdIdMap = new ConcurrentHashMap<>();

    public static volatile ConcurrentHashMap<String /*applicationName*/, String /*appId*/> appIdMap = new ConcurrentHashMap<>();

    public static volatile ConcurrentHashMap<String /*applicationName*/, String /*token*/> tokenMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        ApolloConfig.ENV = env;
        ApolloConfig.USERNAME = username;
        ApolloConfig.CLUSTER_NAME = clusterName;
        ApolloConfig.NAMESPACE = nameSpace;
        ApolloConfig.URL = url;

        this.appNameConfigList.forEach(item -> {
            String[] items = url.split(":");
            if (items.length == 4) {
                String applicationName = items[0];
                String token = items[1];
                String appId = items[2];
                String thirdId = items[3];

                thirdIdMap.putIfAbsent(appId, thirdId);
                appIdMap.putIfAbsent(applicationName, token);
                tokenMap.putIfAbsent(applicationName, appId);
            }
        });
    }

    /**
     * 流控规则
     *
     * @return
     */
    @Bean
    public Converter<List<FlowRuleEntity>, String> flowRuleEntityEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<FlowRuleEntity>> flowRuleEntityDecoder() {
        return s -> JSON.parseArray(s, FlowRuleEntity.class);
    }

    /**
     * 授权规则
     *
     * @return
     */
    @Bean
    public Converter<List<AuthorityRuleEntity>, String> authorRuleEntityEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<AuthorityRuleEntity>> authorRuleEntityDecoder() {
        return s -> JSON.parseArray(s, AuthorityRuleEntity.class);
    }

    /**
     * 降级规则
     *
     * @return
     */
    @Bean
    public Converter<List<DegradeRuleEntity>, String> degradeRuleEntityEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<DegradeRuleEntity>> degradeRuleEntityDecoder() {
        return s -> JSON.parseArray(s, DegradeRuleEntity.class);
    }

    /**
     * 热点参数 规则
     *
     * @return
     */
    @Bean
    public Converter<List<ParamFlowRuleEntity>, String> paramRuleEntityEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<ParamFlowRuleEntity>> paramRuleEntityDecoder() {
        return s -> JSON.parseArray(s, ParamFlowRuleEntity.class);
    }

    /**
     * 系统规则
     *
     * @return
     */
    @Bean
    public Converter<List<SystemRuleEntity>, String> systemRuleEntityEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<SystemRuleEntity>> systemRuleEntityDecoder() {
        return s -> JSON.parseArray(s, SystemRuleEntity.class);
    }

    /**
     * 网关API
     *
     * @return
     * @throws Exception
     */
    @Bean
    public Converter<List<ApiDefinitionEntity>, String> apiDefinitionEntityEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<ApiDefinitionEntity>> apiDefinitionEntityDecoder() {
        return s -> JSON.parseArray(s, ApiDefinitionEntity.class);
    }

    /**
     * 网关flowRule
     *
     * @return
     * @throws Exception
     */
    @Bean
    public Converter<List<GatewayFlowRuleEntity>, String> gatewayFlowRuleEntityEncoder() {
        return JSON::toJSONString;
    }

    @Bean
    public Converter<String, List<GatewayFlowRuleEntity>> gatewayFlowRuleEntityDecoder() {
        return s -> JSON.parseArray(s, GatewayFlowRuleEntity.class);
    }

    //****************************
    //  get / set
    //****************************

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public List<String> getAppNameConfigList() {
        return appNameConfigList;
    }

    public void setAppNameConfigList(List<String> appNameConfigList) {
        this.appNameConfigList = appNameConfigList;
    }
}
```

## 2-9 Sentinel整合Apollo配置文件解析与ApolloOpenApiClient创建-2

- 屏蔽 sentinel-dashboard # pom.xml # apollo-openapi 的 scope
```xml
<!-- for Apollo rule publisher sample -->
<dependency>
    <groupId>com.ctrip.framework.apollo</groupId>
    <artifactId>apollo-openapi</artifactId>
    <version>1.2.0</version>
    <!-- <scope>test</scope> -->
</dependency>
```

- 创建工具类 com.alibaba.csp.sentinel.dashboard.rule.apollo.ApolloConfigUtil
```java
public final class ApolloConfigUtil {

    /**
     * 流控
     * *-flow-rules
     * apollo-test-flow-rules
     */
    private static final String FLOW_RULE_TYPE = "flow";
    private static final String FLOW_DATA_ID_POSTFIX = "-" + FLOW_RULE_TYPE + "-rules";

    public static String getFlowDataId(String appName) {
        return String.format("%s%s", appName, FLOW_DATA_ID_POSTFIX);
    }


    /**
     * 降级
     * *-degrade-rules
     * apollo-test-degrade-rules
     */
    private static final String DEGRADE_RULE_TYPE = "degrade";
    private static final String DEGRADE_DATA_ID_POSTFIX = "-" + DEGRADE_RULE_TYPE + "-rules";

    public static String getDegradeDataId(String appName) {
        return String.format("%s%s", appName, DEGRADE_DATA_ID_POSTFIX);
    }

    private static ConcurrentHashMap<String, ApolloOpenApiClient> APOLLOOPENAPICLIENTMAP = new ConcurrentHashMap<>();

    public static ApolloOpenApiClient createApolloOpenApiClient(String appName) {
        ApolloOpenApiClient client = APOLLOOPENAPICLIENTMAP.get(appName);
        if (client != null) {
            return client;
        } else {
            String token = ApolloConfig.tokenMap.get(appName);
            if (StringUtils.isNotBlank(token)) {
                client = ApolloOpenApiClient
                        .newBuilder()
                        .withPortalUrl(ApolloConfig.URL)
                        .withToken(token)
                        .build();
                APOLLOOPENAPICLIENTMAP.putIfAbsent(appName, client);
                return client;
            } else {
                System.out.println("根据指定的appName: " + appName + ", 找不到对应的token");
                return null;
            }
        }
    }
}
```