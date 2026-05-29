package dev.scx.app.x.fix_table;

import dev.scx.ansi.Ansi;
import dev.scx.app.ScxApp;
import dev.scx.data.sql.annotation.Table;
import dev.scx.data.sql.schema_mapping.AnnotationConfigTable;
import dev.scx.sql.SQLClient;

import java.util.List;

final class ScxAppFixTableModuleHelper {

    /// 获取所有 Table class
    public static List<Class<?>> getTableClassList(ScxApp scx) {
        return scx.candidates().stream()
            .filter(c -> c.isAnnotationPresent(Table.class))// 继承自 BaseModel
            .toList();
    }

    /// 检查数据源是否可用
    public static boolean checkDataSource(SQLClient sqlClient) {
        try (var connection = sqlClient.dataSource().getConnection()) {
            var dm = connection.getMetaData();
            Ansi.ansi().brightGreen("数据源连接成功 : 类型 [" + dm.getDatabaseProductName() + "]  版本 [" + dm.getDatabaseProductVersion() + "]").println();
            return true;
        } catch (Exception e) {
            Ansi.ansi().brightRed("数据源连接失败 !!!").println();
            return false;
        }
    }

    /// 检查是否有任何 (BaseModel) 类需要修复表
    ///
    /// @return 是否有
    public static boolean checkNeedFixTable(ScxApp scxApp, SQLClient sqlClient) {
        Ansi.ansi().brightGreen("检查数据表结构中...").println();
        var tableClassList = getTableClassList(scxApp);
        for (var tableClass : tableClassList) {
            //根据 class 获取 tableInfo
            var annotationConfigTable = new AnnotationConfigTable<>(tableClass);
            try {
                // 有任何需要修复的直接 返回 true
                if (TableSupport.checkNeedFixTable(annotationConfigTable, sqlClient)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public static void fixTable(ScxApp scx, SQLClient sqlClient) {
        Ansi.ansi().brightMagenta("修复数据表结构中...").println();
        //修复成功的表
        var fixSuccess = 0;
        //修复失败的表
        var fixFail = 0;
        //不需要修复的表
        var noNeedToFix = 0;
        var tableClassList = getTableClassList(scx);
        for (var tableClass : tableClassList) {
            // 根据 class 获取 annotationConfigTable
            var annotationConfigTable = new AnnotationConfigTable<>(tableClass);
            try {
                if (TableSupport.checkNeedFixTable(annotationConfigTable, sqlClient)) {
                    TableSupport.fixTable(annotationConfigTable, sqlClient);
                    fixSuccess = fixSuccess + 1;
                } else {
                    noNeedToFix = noNeedToFix + 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
                fixFail = fixFail + 1;
            }
        }

        if (fixSuccess != 0) {
            Ansi.ansi().brightGreen("修复成功 " + fixSuccess + " 张表...").println();
        }
        if (fixFail != 0) {
            Ansi.ansi().brightYellow("修复失败 " + fixFail + " 张表...").println();
        }
        if (fixSuccess + fixFail == 0) {
            Ansi.ansi().brightGreen("没有表需要修复...").println();
        }

    }

}
