package dev.scx.app.x.test.car;

import dev.scx.app.module.component.Component;
import dev.scx.app.x.base.BaseModelService;
import dev.scx.di.annotation.Value;

@Component
public class CarService extends BaseModelService<Car> {

    @Value("scx.http.port")
    public String port;

}
