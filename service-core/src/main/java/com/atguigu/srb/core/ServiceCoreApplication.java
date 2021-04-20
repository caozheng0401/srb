package com.atguigu.srb.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author cz
 * @create 2021/4/18 14:14
 */
@SpringBootApplication
//把其他模块的项目配置自动注入到这里
@ComponentScan({"com.atguigu.srb","com.atguigu.common"})
public class ServiceCoreApplication {
    public static void main(String[] args) {
            SpringApplication.run(ServiceCoreApplication.class, args);
        }
}
