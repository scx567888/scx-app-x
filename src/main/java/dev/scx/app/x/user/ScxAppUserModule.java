package dev.scx.app.x.user;

import dev.scx.app.ScxAppModule;
import dev.scx.app.ScxAppModuleDefinition;
import dev.scx.app.code_source.ScxCodeSource;
import dev.scx.app.environment.ScxEnvironment;

import java.io.IOException;

// todo 待处理 主要是 命名叫什么? 代码不用改

/// ScxAppUserModule
///
/// 用户应用模块基类.
///
/// 默认会扫描当前模块类所在包及其子包下的所有类,
/// 并将这些类加入 ScxApp candidates.
///
/// @author scx567888
/// @version 0.0.1
public abstract class ScxAppUserModule implements ScxAppModule {

    /// 根据 ScxModule 的 class 查找 所有 class
    public static Class<?>[] findClassListByScxModule(Class<? extends ScxAppModule> clazz) throws IOException, ClassNotFoundException {
        var classes = ScxCodeSource.of(clazz).loadClasses();
        var basePackageName = clazz.getPackageName();
        var p = basePackageName + ".";
        return classes.stream()
            .filter(c -> c.getPackageName().equals(basePackageName) || c.getPackageName().startsWith(p))
            .toArray(Class<?>[]::new);
    }

    @Override
    public ScxAppModuleDefinition init(ScxEnvironment environment) throws IOException, ClassNotFoundException {
        var classListByScxModule = findClassListByScxModule(this.getClass());
        // 注入
        return ScxAppModuleDefinition.of()
            .candidate(classListByScxModule);
    }

}
