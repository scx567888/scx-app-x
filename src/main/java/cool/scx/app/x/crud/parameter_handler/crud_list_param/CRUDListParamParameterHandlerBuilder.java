package cool.scx.app.x.crud.parameter_handler.crud_list_param;

import cool.scx.app.x.crud.CRUDListParam;
import cool.scx.reflect.ParameterInfo;
import cool.scx.web.parameter_handler.ParameterHandler;
import cool.scx.web.parameter_handler.ParameterHandlerBuilder;

/**
 * CRUDListParamParameterHandlerBuilder
 *
 * @author scx567888
 * @version 0.0.1
 */
public class CRUDListParamParameterHandlerBuilder implements ParameterHandlerBuilder {

    @Override
    public ParameterHandler tryBuild(ParameterInfo parameter) {
        if (parameter.type().getRawClass() != CRUDListParam.class) {
            return null;
        }
        return new CRUDListParamParameterHandler(parameter);
    }

}
