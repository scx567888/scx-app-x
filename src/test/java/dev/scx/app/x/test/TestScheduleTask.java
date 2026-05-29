package dev.scx.app.x.test;

import dev.scx.app.module.component.Component;
import dev.scx.app.module.scheduling.Scheduled;

import java.lang.System.Logger;

import static java.lang.System.Logger.Level.ERROR;

@Component
public class TestScheduleTask {

    private static final Logger logger = System.getLogger(TestScheduleTask.class.getName());

    @Scheduled(cron = "*/1 * * * * ?")
    public void oneSecondTasks() {
        logger.log(ERROR, "这是 通过注解的 定时任务打印的 !!!");
    }

}
