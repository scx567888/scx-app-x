package cool.scx.app.x.crud.parameter_handler.crud_update_param;

import cool.scx.app.x.crud.CRUDUpdateParam;
import cool.scx.reflect.ParameterInfo;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.RequestInfo;

import static cool.scx.web.parameter_handler.from_body.FromBodyParameterHandler.getValueFromBody;

/**
 * CRUDUpdateParamParameterHandler
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class CRUDUpdateParamParameterHandler implements ParameterHandler {

    private final ParameterInfo parameter;

    public CRUDUpdateParamParameterHandler(ParameterInfo parameter) {
        this.parameter = parameter;
    }

    @Override
    public Object handle(RequestInfo requestInfo) throws Exception {
        var javaType = parameter.type();
        var name = parameter.name();
        var required = false;
        var useAllBody = true;
        var crudUpdateParam = getValueFromBody(name, useAllBody, required, javaType, requestInfo);
        //这里保证 方法上的 CRUDUpdateParam 类型参数永远不为空
        return crudUpdateParam != null ? crudUpdateParam : new CRUDUpdateParam();
    }

}
