package cool.scx.app.x.crud.exception;

import cool.scx.http.exception.BadRequestException;
import cool.scx.web.vo.Result;

/**
 * PaginationParametersErrorException
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class PaginationParametersErrorException extends BadRequestException {

    public PaginationParametersErrorException(Long currentPage, Long pageSize) {
        super(Result.fail("pagination-parameters-error").put("info", "currentPage 和 pageSize 均不能小于 0").put("currentPage", currentPage).put("pageSize", pageSize).toJson(""));
    }

}
