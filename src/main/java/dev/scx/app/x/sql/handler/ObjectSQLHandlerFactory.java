package dev.scx.app.x.sql.handler;

import dev.scx.reflect.TypeInfo;
import dev.scx.sql.handler.TypeSQLHandler;
import dev.scx.sql.handler.TypeSQLHandlerFactory;

public final class ObjectSQLHandlerFactory implements TypeSQLHandlerFactory {

    @Override
    public TypeSQLHandler<?> createHandler(TypeInfo typeInfo) {
        return new ObjectSQLHandler(typeInfo);
    }

}
