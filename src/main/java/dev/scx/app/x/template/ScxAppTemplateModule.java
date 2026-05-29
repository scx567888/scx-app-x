package dev.scx.app.x.template;

import dev.scx.app.ScxApp;
import dev.scx.app.ScxAppModule;
import dev.scx.app.ScxAppModuleDefinition;
import dev.scx.app.environment.ScxEnvironment;
import dev.scx.app.environment.type.ConfiguredPath;
import dev.scx.app.x.web.ScxAppWebModule;

/// ScxAppTemplateModule
///
/// @author scx567888
/// @version 0.0.1
public final class ScxAppTemplateModule implements ScxAppModule {

    private TemplateEngine templateEngine;

    @Override
    public ScxAppModuleDefinition init(ScxEnvironment environment) {
        var templatePath = environment.get("scx.template.path", ConfiguredPath.class, "AppRoot:templates");
        this.templateEngine = new TemplateEngine(templatePath.path());

        return ScxAppModuleDefinition.of()
            .require(ScxAppWebModule.class)
            .startBefore(ScxAppWebModule.class);
    }

    @Override
    public void start(ScxApp scxApp) throws Exception {
        var webModule = scxApp.getComponent(ScxAppWebModule.class);

        webModule.scxWeb().addReturnValueHandler(new TemplateReturnValueHandler(templateEngine));
    }

    /// 暴漏 templateEngine, 允许外部添加指令
    public TemplateEngine templateEngine() {
        return templateEngine;
    }

}
