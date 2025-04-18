package cool.scx.app.x.crud;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.data.field_policy.FieldPolicy;
import cool.scx.data.query.Query;

import static cool.scx.data.field_policy.serializer.FieldPolicyDeserializer.FIELD_POLICY_DESERIALIZER;
import static cool.scx.data.query.serializer.QueryDeserializer.QUERY_DESERIALIZER;


/**
 * CRUDListParam
 *
 * @author scx567888
 * @version 0.0.1
 */
public class CRUDListParam {

    public JsonNode query;

    public JsonNode fieldFilter;

    /**
     * 拓展参数
     */
    public JsonNode extParams;

    public Query getQuery() {
        return QUERY_DESERIALIZER.deserializeQuery(query);
    }

    public FieldPolicy getFieldFilter() {
        //我们需要去掉表达式 防止客户端 sql 注入
        return FIELD_POLICY_DESERIALIZER.deserializeFieldFilter(fieldFilter).clearFieldExpressions();
    }

}
