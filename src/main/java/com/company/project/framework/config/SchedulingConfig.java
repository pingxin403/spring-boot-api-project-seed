package com.company.project.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 定制任务
 *
 * @author hyp
 * Project name is blog
 * Include in com.hyp.learn.core.framework.config
 * hyp create at 20-3-18
 **/
@Component
@EnableScheduling
public class SchedulingConfig implements SchedulingConfigurer {

    private AtomicInteger integer = new AtomicInteger(0);

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        // 指定使用自定义的调度器
        scheduledTaskRegistrar.setScheduler(newExecutors());
    }

    /**
     * 实现多线程并行执行定时任务，防止串行带来了性能消耗
     */
    @Bean(destroyMethod = "shutdown")
    @SuppressWarnings("all")
    private Executor newExecutors() {
        return Executors.newScheduledThreadPool(5, r -> new Thread(r, String.format("APP-Task-%s", integer.incrementAndGet())));
    }
}
