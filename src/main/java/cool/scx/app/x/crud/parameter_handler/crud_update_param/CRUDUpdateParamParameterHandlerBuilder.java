package cool.scx.app.x.crud.parameter_handler.crud_update_param;

import cool.scx.app.x.crud.CRUDUpdateParam;
import cool.scx.reflect.ParameterInfo;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.ParameterHandlerBuilder;

/**
 * CRUDUpdateParamParameterHandlerBuilder
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class CRUDUpdateParamParameterHandlerBuilder implements ParameterHandlerBuilder {

    @Override
    public ParameterHandler tryBuild(ParameterInfo parameter) {
        if (parameter.type().getRawClass() != CRUDUpdateParam.class) {
            return null;
        }
        return new CRUDUpdateParamParameterHandler(parameter);
    }

}
