package cool.scx.app.x.crud.parameter_handler.crud_list_param;

import cool.scx.app.x.crud.CRUDListParam;
import cool.scx.reflect.ParameterInfo;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.RequestInfo;

import static cool.scx.web.parameter_handler.from_body.FromBodyParameterHandler.getValueFromBody;

/**
 * CRUDListParamParameterHandler
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class CRUDListParamParameterHandler implements ParameterHandler {

    private final ParameterInfo parameter;

    public CRUDListParamParameterHandler(ParameterInfo parameter) {
        this.parameter = parameter;
    }

    @Override
    public Object handle(RequestInfo requestInfo) throws Exception {
        var javaType = parameter.type();
        var name = parameter.name();
        var required = false;
        var useAllBody = true;
        var crudListParam = getValueFromBody(name, useAllBody, required, javaType, requestInfo);
        //这里保证 方法上的 CRUDListParam 类型参数永远不为空
        return crudListParam != null ? crudListParam : new CRUDListParam();
    }

}
