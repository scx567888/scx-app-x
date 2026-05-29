package dev.scx.app.x.template;

import dev.scx.app.ScxApp;
import dev.scx.app.ScxAppModule;
import dev.scx.app.ScxAppModuleDefinition;
import dev.scx.app.environment.ScxEnvironment;
import dev.scx.app.environment.type.ConfiguredPath;
import dev.scx.app.x.web.ScxAppWebModule;

public final class ScxAppTemplateModule implements ScxAppModule {

    @Override
    public ScxAppModuleDefinition init(ScxEnvironment environment) {
        return ScxAppModuleDefinition.of()
            .require(ScxAppWebModule.class)
            .startBefore(ScxAppWebModule.class);
    }

    @Override
    public void start(ScxApp scxApp) throws Exception {
        var webModule = scxApp.getComponent(ScxAppWebModule.class);
        var templatePath = scxApp.environment().get("scx.template.path", ConfiguredPath.class, "AppRoot:templates");

        webModule.scxWeb().addReturnValueHandler(new TemplateReturnValueHandler(new TemplateEngine(templatePath.path())));
    }

}
