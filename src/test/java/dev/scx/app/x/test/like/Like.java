package dev.scx.app.x.test.like;

import dev.scx.app.x.base.BaseModel;
import dev.scx.data.sql.annotation.Table;

//特殊表名
@Table("like")
public class Like extends BaseModel {

    public Order order;

}
