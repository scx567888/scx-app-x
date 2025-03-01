package cool.scx.app.x.crud.exception;

import cool.scx.http.exception.BadRequestException;
import cool.scx.web.vo.Result;

/**
 * UnknownWhereTypeException
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class UnknownWhereTypeException extends BadRequestException {

    public UnknownWhereTypeException(String fieldName, String strWhereType) {
        super(Result.fail("unknown-where-type").put("field-name", fieldName).put("where-type", strWhereType).toJson(""));
    }

}
