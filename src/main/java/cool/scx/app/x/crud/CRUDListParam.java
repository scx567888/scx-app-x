package cool.scx.app.x.crud;

import com.fasterxml.jackson.databind.JsonNode;
import cool.scx.data.field_filter.FieldFilter;
import cool.scx.data.query.Query;

import static cool.scx.data.field_filter.serializer.FieldFilterDeserializer.FIELD_FILTER_DESERIALIZER;
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

    public FieldFilter getFieldFilter() {
        return FIELD_FILTER_DESERIALIZER.deserializeFieldFilter(fieldFilter);
    }

}
