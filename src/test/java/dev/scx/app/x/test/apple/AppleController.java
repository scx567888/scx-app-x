package dev.scx.app.x.test.apple;

import dev.scx.app.x.base.BaseModelService;
import dev.scx.app.x.crud.CRUDHelper;
import dev.scx.app.x.crud.CRUDListParam;
import dev.scx.app.x.crud.CRUDUpdateParam;
import dev.scx.app.x.web.Result;
import dev.scx.web.annotation.*;
import dev.scx.web.result.WebResult;

import java.util.Map;

import static dev.scx.data.query.BuildControl.SKIP_IF_NULL;
import static dev.scx.data.query.QueryBuilder.and;
import static dev.scx.http.method.HttpMethod.*;
import static dev.scx.http.method.HttpMethod.POST;

@Routes("/api/apple")
public class AppleController  {

    protected final BaseModelService<Apple> service;

    public AppleController(BaseModelService<Apple> service) {
        this.service = service;
    }

    @Route(value = "/list", methods = POST)
    public WebResult list(CRUDListParam crudListParam) {
        var query = crudListParam.getQuery();
        var selectFilter = crudListParam.getFieldPolicy();
        var list = service.find(query, selectFilter);
        var total = service.count(query);
        return Result.ok().put("items", list).put("total", total);
    }

    @Route(value = "/:id", methods = GET)
    public WebResult info(@PathCapture Long id) {
        var info = service.get(id);
        return Result.ok(info);
    }

    @Route(value = "/", methods = POST)
    public WebResult add(@Body Apple saveModel) {
        var savedModel = service.add(saveModel);
        return Result.ok(savedModel);
    }

    @Route(value = "/", methods = PUT)
    public WebResult update(CRUDUpdateParam crudUpdateParam) {
        var realObject = crudUpdateParam.getBaseModel(service.entityClass());
        var updatePolicy = crudUpdateParam.getUpdatePolicy(service.entityClass(), service.dao().table());
        var updatedModel = service.update(realObject, updatePolicy);
        return Result.ok(updatedModel);
    }

    @Route(value = "/", methods = DELETE)
    public WebResult delete(CRUDListParam crudListParam) {
        var query = crudListParam.getQuery();
        var size = service.delete(query);
        return Result.ok(size);
    }

    @Route(value = "/check-unique/:fieldName", methods = POST)
    public WebResult checkUnique(@PathCapture String fieldName, @BodyField Object value, @BodyField(required = false) Long id) {
        CRUDHelper.checkFieldName(service.entityClass(), fieldName);
        var query = and().eq(fieldName, value).ne("id", id, SKIP_IF_NULL);
        var isUnique = service.count(query) == 0;
        return Result.ok().put("isUnique", isUnique);
    }

    @Route(value = "/count", methods = POST)
    public WebResult count(CRUDListParam crudListParam) {
        var query = crudListParam.getQuery();
        var total = service.count(query);
        return Result.ok(total);
    }

}
