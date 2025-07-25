package cool.scx.app.x.crud;

import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.query.Query;
import cool.scx.data.serialization.DeserializationException;
import cool.scx.http.exception.BadRequestException;
import cool.scx.object.node.Node;

import static cool.scx.data.field_policy.FieldPolicyBuilder.includeAll;
import static cool.scx.data.query.QueryBuilder.query;
import static cool.scx.data.serialization.FieldPolicyDeserializer.deserializeFieldPolicy;
import static cool.scx.data.serialization.QueryDeserializer.deserializeQuery;


/**
 * CRUDListParam
 *
 * @author scx567888
 * @version 0.0.1
 */
public class CRUDListParam {

    public Node query;

    public Node fieldPolicy;

    /**
     * 拓展参数
     */
    public Node extParams;

    public Query getQuery() {
        if (query == null || query.isNull()) {
            return query();
        }
        try {
            return deserializeQuery(query);
        } catch (Exception e) {
            throw new BadRequestException("query 参数错误", e);
        }
    }

    public FieldPolicy getFieldPolicy() {
        if (fieldPolicy == null || fieldPolicy.isNull()) {
            return includeAll();
        }
        //我们需要去掉表达式 防止客户端 sql 注入
        try {
            return deserializeFieldPolicy(fieldPolicy);
        } catch (DeserializationException e) {
            throw new BadRequestException("fieldPolicy 参数错误", e);
        }
    }

}
