package com.iamgenerator.bean;

import java.util.ArrayList;

/**
 * 数据库table的相关信息
 * @author kado
 */
public class TableInfo {
    /**表名**/
    public String tableName;
    /**表注释**/
    public String tableRemark;
    /**这张表所对应的所有列**/
    public ArrayList<SQLColumn> columnList = null; 
    /**表所对应的字段的所有名称集合**/
    public ArrayList<String> columnNameList = null;
}
