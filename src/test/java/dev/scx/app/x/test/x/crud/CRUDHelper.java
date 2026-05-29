package dev.scx.app.x.test.x.crud;

import dev.scx.app.x.base.BaseModel;
import dev.scx.app.x.base.BaseModelService;
import dev.scx.app.x.crud.exception.UnknownFieldNameException;
import dev.scx.data.sql.annotation.NoColumn;
import dev.scx.http.exception.BadRequestException;
import dev.scx.reflect.ClassInfo;

import java.lang.System.Logger;
import java.util.Map;

import static dev.scx.reflect.ScxReflect.typeOf;
import static dev.scx.serialize.ScxSerialize.convertObject;
import static java.lang.System.Logger.Level.ERROR;

/**
 * CRUDHelper
 *
 * @author scx567888
 * @version 0.0.1
 */
public final class CRUDHelper {

    /**
     * a
     */
    private static final Logger logger = System.getLogger(CRUDHelper.class.getName());

    /**
     * 获取 baseModel
     *
     * @param map            a
     * @param baseModelClass a
     * @param <B>            b
     * @return a
     */
    public static <B extends BaseModel> B mapToBaseModel(Map<String, Object> map, Class<B> baseModelClass) {
        try {
            return convertObject(map, baseModelClass);
        } catch (Exception e) {
            logger.log(ERROR, "将 Map 转换为 BaseModel 时发生异常 : ", e);
            //这里一般就是 参数转换错误
            throw new BadRequestException(e);
        }
    }

    /**
     * 检查 fieldName 是否合法
     *
     * @param modelClass m
     * @param fieldName  f
     * @return a {@link String} object
     * @throws UnknownFieldNameException c
     */
    public static String checkFieldName(Class<?> modelClass, String fieldName) throws UnknownFieldNameException {
        try {
            var field = modelClass.getField(fieldName);
            if (field.isAnnotationPresent(NoColumn.class)) {
                throw new UnknownFieldNameException(fieldName);
            }
        } catch (Exception e) {
            throw new UnknownFieldNameException(fieldName);
        }
        return fieldName;
    }

}
