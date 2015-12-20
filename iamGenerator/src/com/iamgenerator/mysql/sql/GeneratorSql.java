package com.iamgenerator.mysql.sql;

/**
 *
 * @author kado
 */
public class GeneratorSql {
    //显示数据库里面的所有的表
    public static final String getTables_sql = "show tables;";
    //根据数据库表名显示列信息
    public static final String getColumnsByTableName_sql = "desc account";
}
