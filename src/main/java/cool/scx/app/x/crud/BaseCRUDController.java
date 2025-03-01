package cool.scx.app.x.crud;

import cool.scx.app.base.BaseModelService;
import cool.scx.data.query.QueryOption;
import cool.scx.web.annotation.FromBody;
import cool.scx.web.annotation.FromPath;
import cool.scx.web.annotation.ScxRoute;
import cool.scx.web.vo.BaseVo;
import cool.scx.web.vo.Result;

import java.util.Map;

import static cool.scx.app.ScxContext.getBean;
import static cool.scx.app.x.crud.CRUDHelper.findBaseModelServiceClass;
import static cool.scx.data.query.QueryBuilder.and;
import static cool.scx.http.HttpMethod.*;

/**
 * 继承方式的 CRUD 的 controller
 *
 * @author scx567888
 * @version 0.0.1
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class BaseCRUDController<T extends BaseModelService> {

    protected final T service;

    public BaseCRUDController(T service) {
        this.service = service;
    }

    public BaseCRUDController() {
        this.service = getBean((Class<T>) findBaseModelServiceClass(this.getClass()));
    }

    @ScxRoute(methods = POST)
    public BaseVo list(CRUDListParam crudListParam) {
        var query = crudListParam.getQuery();
        var selectFilter = crudListParam.getFieldFilter();
        var list = service.find(query, selectFilter);
        var total = service.count(query);
        return Result.ok().put("items", list).put("total", total);
    }

    @ScxRoute(value = ":id", methods = GET)
    public BaseVo info(@FromPath Long id) {
        var info = service.get(id);
        return Result.ok(info);
    }

    @ScxRoute(value = "", methods = POST)
    public BaseVo add(@FromBody(useAllBody = true) Map<String, Object> saveModel) {
        var realObject = CRUDHelper.mapToBaseModel(saveModel, service.entityClass());
        var savedModel = service.add(realObject);
        return Result.ok(savedModel);
    }

    @ScxRoute(value = "", methods = PUT)
    public BaseVo update(CRUDUpdateParam crudUpdateParam) {
        var realObject = crudUpdateParam.getBaseModel(service.entityClass());
        var updateFilter = crudUpdateParam.getUpdateFilter(service.entityClass(), service.dao().tableInfo());
        var updatedModel = service.update(realObject, updateFilter);
        return Result.ok(updatedModel);
    }

    @ScxRoute(value = "", methods = DELETE)
    public BaseVo delete(CRUDListParam crudListParam) {
        var query = crudListParam.getQuery();
        var size = service.delete(query);
        return Result.ok(size);
    }

    @ScxRoute(value = "check-unique/:fieldName", methods = POST)
    public BaseVo checkUnique(@FromPath String fieldName, @FromBody Object value, @FromBody(required = false) Long id) {
        CRUDHelper.checkFieldName(service.entityClass(), fieldName);
        var query = and().eq(fieldName, value).ne("id", id, QueryOption.SKIP_IF_NULL);
        var isUnique = service.count(query) == 0;
        return Result.ok().put("isUnique", isUnique);
    }

    @ScxRoute(methods = POST)
    public BaseVo count(CRUDListParam crudListParam) {
        var query = crudListParam.getQuery();
        var total = service.count(query);
        return Result.ok(total);
    }

}
