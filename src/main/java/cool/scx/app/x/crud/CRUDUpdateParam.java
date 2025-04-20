package cool.scx.app.x.crud;

import cool.scx.app.base.BaseModel;
import cool.scx.app.x.crud.exception.EmptyUpdateColumnException;
import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.jdbc.mapping.AnnotationConfigTable;
import cool.scx.data.jdbc.sql_builder.SQLBuilderHelper;

import java.util.Arrays;
import java.util.Map;

import static cool.scx.data.field_policy.FieldPolicyBuilder.excluded;
import static cool.scx.data.field_policy.FieldPolicyBuilder.included;

/**
 * 更新实体类的封装
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class CRUDUpdateParam {

    /**
     * 更新的所有内容 可以转换为对应的 实体类
     */
    public Map<String, Object> updateModel;

    /**
     * 需要更新的字段列
     */
    public String[] needUpdateFieldNames;

    public <B extends BaseModel> B getBaseModel(Class<B> modelClass) {
        return CRUDHelper.mapToBaseModel(this.updateModel, modelClass);
    }

    public FieldPolicy getUpdatePolicy(Class<? extends BaseModel> modelClass, AnnotationConfigTable scxDaoTableInfo) {
        if (needUpdateFieldNames == null) {
            return excluded();
        }
        var legalFieldName = Arrays.stream(needUpdateFieldNames).map(fieldName -> CRUDHelper.checkFieldName(modelClass, fieldName)).toArray(String[]::new);
        var updateFilter = included(legalFieldName).ignoreNull(false);
        //防止空列更新
        if (SQLBuilderHelper.filterByFieldPolicy(updateFilter, scxDaoTableInfo).length == 0) {
            throw new EmptyUpdateColumnException();
        }
        return updateFilter;
    }

}
