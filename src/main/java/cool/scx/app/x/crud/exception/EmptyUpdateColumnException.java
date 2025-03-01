package cool.scx.app.x.crud.exception;

import cool.scx.http.exception.BadRequestException;
import cool.scx.web.vo.Result;

/**
 * EmptyUpdateColumnException
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class EmptyUpdateColumnException extends BadRequestException {

    public EmptyUpdateColumnException() {
        super(Result.fail("empty-update-column").toJson(""));
    }

}
