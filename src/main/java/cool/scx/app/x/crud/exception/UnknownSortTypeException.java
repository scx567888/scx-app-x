package cool.scx.app.x.crud.exception;

import cool.scx.http.exception.BadRequestException;
import cool.scx.web.vo.Result;

/**
 * UnknownSortTypeException
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class UnknownSortTypeException extends BadRequestException {

    public UnknownSortTypeException(String fieldName, String strSortType) {
        super(Result.fail("unknown-sort-type").put("field-name", fieldName).put("sort-type", strSortType).toJson(""));
    }

}
