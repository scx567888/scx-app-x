package cool.scx.app.x.crud.exception;

import cool.scx.http.exception.BadRequestException;
import cool.scx.web.vo.Result;

/**
 * UnknownFilterModeException
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class UnknownFilterModeException extends BadRequestException {

    public UnknownFilterModeException(String filterMode) {
        super(Result.fail("unknown-filter-mode").put("filter-mode", filterMode).toJson(""));
    }

}
