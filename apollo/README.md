[TOC]

# 目录

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