package com.company.project.framework.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 程序启动后通过ApplicationRunner处理一些事务
 * @author hyp
 * Project name is spring-boot-learn
 * Include in com.hyp.learn.shiro.framework.runner
 * hyp create at 20-3-29
 **/
@Slf4j
@Component
public class CustomApplicationRunner implements ApplicationRunner {
    @Value("${server.port}")
    private int port;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.error("程序部署完成，访问地址：http://localhost:" + port);
    }
}
