package cool.scx.app.x.crud.exception;

import cool.scx.data.field_filter.FilterMode;
import cool.scx.http.exception.BadRequestException;
import cool.scx.web.vo.Result;


/**
 * EmptySelectColumnException
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class EmptySelectColumnException extends BadRequestException {

    public EmptySelectColumnException(FilterMode filterMode, String[] fieldNames) {
        super(Result.fail("empty-select-column").put("filter-mode", filterMode).put("field-names", fieldNames).toJson(""));
    }

}
