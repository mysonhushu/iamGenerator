package com.iamgenerator.genrator;

import com.iamgenerator.bean.SQLColumn;
import com.iamgenerator.bean.TableInfo;
import com.iamgenerator.util.CommentUtils;
import com.iamgenerator.util.StringUtils;
import java.util.ArrayList;

/**
 * SqlBeanGenerator 用于生成Dao的JDBC所有的sql语句
 * @author kado
 */
public class SqlBeanGenerator {
    /**java bean 属性信息**/
    public String packageLine = "";
    /**表名和字段的数组信息**/
    public ArrayList<TableInfo> tableInfoList = null;
    public String classBeginLine = "";
    public String className = "";
    //普通增加sql语句
    ArrayList<String> insertSqlList = null;
    //普通删除sql语句
    ArrayList<String> deleteSqlList = null;
    //普通查询sql语句
    ArrayList<String> selectSqlList = null;
    //普通修改sql语句
    ArrayList<String> updateSqlList = null;
 
    
    /**
     * DaoSqlBeanGenerator的构造函数
     * @param packageName
     * @param tableName
     * @param columns 
     */
    public SqlBeanGenerator(String packageName, 
                                String className, 
                                ArrayList<TableInfo> tableInfoList,
                                String path){
        //如果有传入包名，则书写包名
        if(StringUtils.StringHasValue(packageName)){
            packageLine = "package " + packageName + ";";
        }
        //核实传入的tableInfo数组是否为空
        if(null == tableInfoList || tableInfoList.isEmpty())
        {
            StringUtils.println("调用SqlBeanGenerator时参数tableInfoList为null，无法生成SqlBeanInfo对象");
            System.exit(-1);
        }
        this.tableInfoList = tableInfoList;
         
        //遍历检验信息是否合法
        for(int i=0; i< tableInfoList.size(); i++)
        {
            if(!StringUtils.StringHasValue(tableInfoList.get(i).tableName))
            {
                StringUtils.println("表名为空，无法生成SqlBeanInfo对象 :"+i);
                System.exit(-1);
            }else {
                if(null == tableInfoList.get(i).columnList || 
                        tableInfoList.get(i).columnList.isEmpty())
                {
                    StringUtils.println("表"+tableInfoList.get(i).tableName+"对应的字段为空，无法产生对象！");
                    System.exit(-1);
                } 
            }
        }
        
        //检验类名的合法性
        if(StringUtils.StringHasValue(className)){
            this.className = className;
            packageLine = "package " + packageName + ";";
        }else{
            StringUtils.println("类名为null，无法生成SqlBeanGenerator对象");
            System.exit(-1);
        }
        //生成类声明的那一行数据
        classBeginLine = "public class " + className + " {";
        //开始生成构造查询sql
        this.generateJavaSql();
    }
    
    
    
    /**
     * 生成每张表的数据库操作sql，操作语句包括
     * insert语句
     * update语句
     * select语句
     * delete语句
     * @param path 
     */
    public void generateJavaSql(){
        //新增语句
//        this.insertSqlList = this.generateInsertSql();
        //删除语句
//        this.deleteSqlList = this.generateDeleteSql();
        //更新语句
        this.updateSqlList = this.generateUpdateSql();
        //查询语句
//        this.selectSqlList = this.generateSelectSql();
        
    }
    
    /**
     * 产生普通的新增sql
     * @return 
     */
    public ArrayList<String> generateInsertSql(){
        ArrayList<String> insertSqlList = new ArrayList<String>();
        //生成普通的新增语句
        int size = this.tableInfoList.size();
        //遍历数据表信息产生sql
        for(int i=0; i<size; i++)
        {
            TableInfo ti = this.tableInfoList.get(i);
            StringBuilder insertSql = new StringBuilder();
            insertSql.append(CommentUtils.getLineStarComment(8, ti.tableRemark+"("+ti.tableName+")"+"的新增sql", true));
            String propBegin = "public static final String add" +
                                StringUtils.toCamelCase(ti.tableName +"_sql" +
                                " = ");
            insertSql.append(StringUtils.getAFixedWidthString(propBegin, 75, false, false));
            insertSql.append("\r\n");
            insertSql.append(StringUtils.getAFixedWidthString("insert into " + 
                                                     ti.tableName.toLowerCase() +"(",
                                                               75, false,true));
            StringBuffer valuesSql = new StringBuffer();
            valuesSql.append(" values (");
            
            int columnSize = ti.columnList.size();
            for(int j=0; j<columnSize; j++)
            {
                if(j != columnSize-1)
                {
                    insertSql.append(StringUtils.getAFixedWidthString(ti.columnList.get(j).colName+",", 
                                    75, false, true));
                    valuesSql.append(" ?,");   
                }else{
                    insertSql.append(StringUtils.getAFixedWidthString(ti.columnList.get(j).colName+")", 
                                    75, false, true));
                    valuesSql.append(" ?)"); 
                }
            }
            insertSql.append(StringUtils.getAFixedWidthString(valuesSql.toString(), 75, true, true));
            insertSqlList.add(insertSql.toString());
            System.out.println(insertSql.toString());
        }
        return insertSqlList;
    }
    
    /**
     * 产生普通的删除sql
     * @return 
     */
    public ArrayList<String> generateDeleteSql(){
        ArrayList<String> deleteSqlList = new ArrayList<String>();
         //生成普通的新增语句
        
        return deleteSqlList;
    }
    
    /**
     * 产生普通的修改sql
     * @return 
     */
    public ArrayList<String> generateUpdateSql(){
        ArrayList<String> updateSqlList = new ArrayList<String>();
        int size = this.tableInfoList.size();
        for(int i=0; i<size; i++)
        {
            TableInfo ti = this.tableInfoList.get(i);
            String stn = StringUtils.toAcronym(ti.tableName.toLowerCase());
            //生成普通的修改sql
            StringBuffer updateSql = new StringBuffer();
            updateSql.append(CommentUtils.getLineStarComment(8, ti.tableRemark+"("+ti.tableName+")"+"的更改sql", true));
             //开始
            String beginSql = "public static String upt"+ StringUtils.toCamelCase(ti.tableName)+ "_sql =";
            updateSql.append(StringUtils.getAFixedWidthString(beginSql, 75, false, true));
            updateSql.append(
                    StringUtils.getAFixedWidthString("update " + ti.tableName.toLowerCase() + " "+stn,
                            75, false,true));
            int colSize = ti.columnList.size();
            for(int j=0; j<colSize; j++)
            {
                if(j==0 && j != colSize-1)
                {
                   updateSql.append(StringUtils.getAFixedWidthString(" set "+stn+"."+ti.columnList.get(j).colName+" = ?,", 75, false, true));
                } else if( j == colSize-1){
                   updateSql.append(StringUtils.getAFixedWidthString(stn+"."+ti.columnList.get(j).colName+" = ?", 75, false, true)); 
                } else {
                   updateSql.append(StringUtils.getAFixedWidthString(stn+"."+ti.columnList.get(j).colName+" = ?", 75, false, true));  
                }
            }
          //  updateSql.append(StringUtils.getAFixedWidthString(" where "+stn+".app_id = ? ",75,false,true));
            String primary = ti.tableName.substring(ti.tableName.indexOf("_")+1)+"_id";
            if(ti.columnNameList.contains(primary.toLowerCase()))
            {
               updateSql.append(StringUtils.getAFixedWidthString(" where "+stn+".app_id = ? ",75,false,true));   
               updateSql.append(StringUtils.getAFixedWidthString(" and "+stn+"."+primary.toLowerCase()+" = ? ",75,true,true)); 
            } else {
               updateSql.append(StringUtils.getAFixedWidthString(" where "+stn+".app_id = ? ",75,true,true));  
               System.out.println("----------------"+ti.tableName+" has no prmary key!"+" the caculate primarykey = "+ primary.toLowerCase());
            }
            updateSqlList.add(updateSql.toString());
            System.out.println(updateSql.toString());
        }
        return updateSqlList;
    }
    
//    update ts_greens_spec tgs
//        set tgs.name = ?,
//            tgs.simple_spell = ?,
//                              tgs.isbn = ?,
//                              tgs.agreement_type_cd = ?,
//                              tgs.channel_id = ?,
//                              tgs.area_id = ?,
//                              tgs.summary = ?,
//                              tgs.start_time = ?,
//                              tgs.end_time = ?,
//                              tgs.status = ?,
//                              tgs.start_time = ?,
//                              tgs.create_time = ?
//              where tgs.app_id=?
//                              and tgs.greens_spec_id=?; 
    
    
    
    
    /**
     * 生成普通的查询sql
     * @return 
     */
    public ArrayList<String> generateSelectSql(){
        ArrayList<String> selectSqlList = new ArrayList<String>();
        
        int size = this.tableInfoList.size();
        //遍历数据表信息产生sql
        for(int i=0; i<size; i++)
        {
            TableInfo ti = this.tableInfoList.get(i);
            //生成普通查询sql
            StringBuffer normalSelect = new StringBuffer();
            normalSelect.append(CommentUtils.getLineStarComment(8, ti.tableRemark+"("+ti.tableName+")"+"的查询sql", true));
            //开始
            String beginSql = "public static String qry"+ StringUtils.toCamelCase(ti.tableName)+ "_sql =";
            normalSelect.append(StringUtils.getAFixedWidthString(beginSql,75, false,false));
            normalSelect.append("\r\n");
            normalSelect.append(StringUtils.getAFixedWidthString("select",75, false,true));
            int columnsSize = ti.columnList.size();
            for(int j=0;j< columnsSize; j++)
            {
                if(j != columnsSize-1)
                {
                    normalSelect.append(StringUtils.getAFixedWidthString(ti.columnList.get(j).colName.toLowerCase()+",",
                                                    75, false,true));
                }else if(j == columnsSize-1){
                    normalSelect.append(StringUtils.getAFixedWidthString(ti.columnList.get(j).colName.toLowerCase(), 
                                                    75, false,true));
                }
            }
            normalSelect.append(StringUtils.getAFixedWidthString(" from "+ti.tableName.toLowerCase(),75, false,true));
            normalSelect.append(StringUtils.getAFixedWidthString("  where app_id = ? ",75, true,true));
            selectSqlList.add(normalSelect.toString());
            System.out.println(normalSelect.toString());
        }
//        //生成id查询sql
//        
//        //生成名称查询sql
        return selectSqlList;
    }
    
}
