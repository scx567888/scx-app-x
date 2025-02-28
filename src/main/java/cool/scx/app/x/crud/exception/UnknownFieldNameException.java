package cool.scx.app.x.crud.exception;

import cool.scx.http.exception.BadRequestException;
import cool.scx.web.vo.Result;

/**
 * UnknownFieldNameException
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class UnknownFieldNameException extends BadRequestException {

    public UnknownFieldNameException(String fieldName) {
        super(Result.fail("unknown-field-name").put("field-name", fieldName).toJson(""));
    }

}
