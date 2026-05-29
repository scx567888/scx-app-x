package dev.scx.app.x.scheduling;

import dev.scx.ansi.Ansi;
import dev.scx.app.ScxApp;
import dev.scx.app.ScxAppModule;
import dev.scx.reflect.ClassInfo;
import dev.scx.reflect.ScxReflect;
import dev.scx.scheduling.ScheduleHandle;
import dev.scx.scheduling.ScxScheduling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static dev.scx.reflect.AccessModifier.PUBLIC;

public final class ScxAppSchedulingModule implements ScxAppModule {

    private final List<ScheduleHandle> scheduleHandleList;

    public ScxAppSchedulingModule() {
        this.scheduleHandleList = new ArrayList<>();
    }

    @Override
    public void start(ScxApp scxApp) throws Exception {
        var componentContainer = scxApp.componentContainer();
        var componentDefinitions = componentContainer.componentDefinitions().values();

        root:
        for (var componentDefinition : componentDefinitions) {
            var component = componentContainer.getComponent(componentDefinition.componentName());

            var typeInfo = ScxReflect.typeOf(component.getClass());

            // 只处理 classInfo
            if (!(typeInfo instanceof ClassInfo classInfo)) {
                continue root;
            }

            methods:
            for (var method : classInfo.methods()) {
                // 只处理 public 方法
                if (method.accessModifier() != PUBLIC) {
                    continue methods;
                }
                var scheduledList = Arrays.stream(method.annotations())
                    .flatMap(c -> switch (c) {
                        case Scheduled s -> Stream.of(s);
                        case Scheduled.List f -> Stream.of(f.value());
                        default -> Stream.of();
                    }).toList();

                for (Scheduled scheduled : scheduledList) {
                    if (method.parameters().length != 0) {
                        throw new IllegalArgumentException("被 Scheduled 注解标识的方法不可以有参数 Class [" + classInfo.name() + "] , Method [" + method.name() + "]");
                    }
                    if (method.isStatic()) {
                        ScheduleHandle handle = ScxScheduling.cron()
                            .cronExpression(scheduled.cron())
                            .start(c -> {
                                method.invoke(null);
                            });
                        this.scheduleHandleList.add(handle);
                    } else {
                        ScheduleHandle handle = ScxScheduling.cron()
                            .cronExpression(scheduled.cron())
                            .start(c -> {
                                method.invoke(component);
                            });
                        this.scheduleHandleList.add(handle);
                    }
                }

            }
        }

        Ansi.ansi()
            .brightBlue("已注册 " + scheduleHandleList.size() + " 个 Scheduled Task !!!")
            .println();

    }

    @Override
    public void stop(ScxApp scxApp) {
        for (var handle : scheduleHandleList) {
            handle.cancel();
        }
    }

}
