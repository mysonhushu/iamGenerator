package com.iamgenerator.genrator;

import com.iamgenerator.bean.SQLColumn;
import com.iamgenerator.bean.TableInfo;
import com.iamgenerator.util.CommentUtils;
import com.iamgenerator.util.DateUtils;
import com.iamgenerator.util.FileUtils;
import com.iamgenerator.util.StringUtils;
import java.util.ArrayList;

/**
 * SqlBeanGenerator 用于生成Dao的JDBC所有的sql语句
 * @author kado
 */
public class SqlBeanGenerator {
    /**java文件所在的包**/
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
    //该状态为作废的sql语句
    ArrayList<String> efficacySqlList = null;
    
 
    
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
        this.className = className;
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
        //新增语句 ok
        this.insertSqlList = this.generateInsertSql();
        //删除语句 ok
        this.deleteSqlList = this.generateDeleteSql();
        //更新语句 ok
        this.updateSqlList = this.generateUpdateSql();
        //查询语句 ok
        this.selectSqlList = this.generateSelectSql();
        //改状态为作废的语句
        this.efficacySqlList = this.generateEfficacySql();
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
                                StringUtils.toCamelCase(ti.tableName) +"_sql" +
                                " = ";
            insertSql.append(StringUtils.getAFixedWidthString(propBegin, 75, false, 8, false, true));
            insertSql.append("\r\n");
            insertSql.append(StringUtils.getAFixedWidthString("insert into " + 
                                                     ti.tableName.toLowerCase() +"(",
                                                               75, false, 8, true, true));
            StringBuffer valuesSql = new StringBuffer();
            valuesSql.append(" values (");
            
            int columnSize = ti.columnList.size();
            for(int j=0; j<columnSize; j++)
            {
                if(j != columnSize-1)
                {
                    insertSql.append(StringUtils.getAFixedWidthString(ti.columnList.get(j).colName+",", 
                                    75, false, 8, true, true));
                    valuesSql.append(" ?,");   
                }else{
                    insertSql.append(StringUtils.getAFixedWidthString(ti.columnList.get(j).colName+")", 
                                    75, false, 8, true, true));
                    valuesSql.append(" ?)"); 
                }
            }
            insertSql.append(StringUtils.getAFixedWidthString(valuesSql.toString(), 75, true, 8, true, true));
            insertSqlList.add(insertSql.toString());
            System.out.println(insertSql.toString());
        }
        return insertSqlList;
    }
    
    /**
     * 产生将数据失效掉的sql
     * @return 拼接好的sql
     */
    public ArrayList<String> generateEfficacySql(){
        ArrayList<String> efficacySqlList = new ArrayList<String>();
        //将数据失效掉的sql
//        StringBuffer efficacySql = new StringBuffer();
        int size = this.tableInfoList.size();
        for(int i=0; i<size; i++)
        {
            TableInfo ti = this.tableInfoList.get(i);
            String stn = StringUtils.toAcronym(ti.tableName.toLowerCase());
            //生成普通的修改sql
            StringBuffer efficacySql = new StringBuffer();
            efficacySql.append(CommentUtils.getLineStarComment(8, ti.tableRemark+"("+ti.tableName+")"+"的更改sql", true));
             //开始
            String beginSql = "public static String exp"+ StringUtils.toCamelCase(ti.tableName)+ "_sql =";
            efficacySql.append(StringUtils.getAFixedWidthString(beginSql, 75, false, 8, false, true));
            efficacySql.append("\r\n");
            efficacySql.append(
                    StringUtils.getAFixedWidthString("update " + ti.tableName.toLowerCase() + " "+stn,
                            75, false, 8, true, true));
            int colSize = ti.columnList.size();
            efficacySql.append(StringUtils.getAFixedWidthString(" set "+stn+".status = '22' ", 75, false, 8, true, true));
          //  updateSql.append(StringUtils.getAFixedWidthString(" where "+stn+".app_id = ? ",75,false,true));
            String primary = ti.tableName.substring(ti.tableName.indexOf("_")+1)+"_id";
            if(ti.columnNameList.contains(primary.toLowerCase()))
            {
               efficacySql.append(StringUtils.getAFixedWidthString(" where "+stn+".app_id = ? ",75,false, 8, true, true));   
               efficacySql.append(StringUtils.getAFixedWidthString(" and "+stn+"."+primary.toLowerCase()+" = ? ",75,true, 8, true, true)); 
            } else {
               efficacySql.append(StringUtils.getAFixedWidthString(" where "+stn+".app_id = ? ",75,true, 8, true, true));  
               System.out.println("----------------"+ti.tableName+" has no prmary key!"+" the caculate primarykey = "+ primary.toLowerCase());
            }
            
            //临时测试，查看表当中是否包含字段status
            if(!ti.columnNameList.contains("status".toLowerCase()))
            {
                System.out.println("-------------------------------"+ti.tableName+" 没有status字段 ----------------");
            }else {
                for(SQLColumn a :ti.columnList)
                {
                    if(a.colName.toLowerCase().equals("status"))
                    {
                        if(a.colTypeName.toLowerCase().equals("varchar"))
                        {
                            continue;
                        }else {
                            System.out.println("===============table name="+ti.tableName+" status is not varchar!");
                        }
                        
                    }
                }
            }
            efficacySqlList.add(efficacySql.toString());
            System.out.println(efficacySql.toString());
        }
        
        return efficacySqlList;
    }
    
    /**
     * 产生普通的删除sql
     * @return 拼接好的sql
     */
    public ArrayList<String> generateDeleteSql(){
        ArrayList<String> deleteSqlList = new ArrayList<String>();
         //生成普通的删除语句
         StringBuffer deleteSql = new StringBuffer();
        int size = this.tableInfoList.size();
        for(int i=0; i<size; i++)
        {
            TableInfo ti = this.tableInfoList.get(i);
            String stn = StringUtils.toAcronym(ti.tableName.toLowerCase());
            deleteSql.append(CommentUtils.getLineStarComment(8, ti.tableRemark+"("+ti.tableName+")"+"的删除sql", true));
             //开始
            String beginSql = "public static String del"+ StringUtils.toCamelCase(ti.tableName)+ "_sql =";
            deleteSql.append(StringUtils.getAFixedWidthString(beginSql, 75, false, 8, false, true));
            deleteSql.append("\r\n");
            deleteSql.append(
                    StringUtils.getAFixedWidthString("delete from " + ti.tableName.toLowerCase(),
                            75, false, 8, true, true));
            deleteSql.append(
                    StringUtils.getAFixedWidthString(" where app_id = ? and status='22' and ",
                            75, false, 8, true, true));
            String primary = ti.tableName.substring(ti.tableName.indexOf("_")+1)+"_id";
            deleteSql.append(
                    StringUtils.getAFixedWidthString(" "+primary+" = ?",
                            75, true, 8, true, true));
        }
        System.out.println(deleteSql.toString());
        deleteSqlList.add(deleteSql.toString());
        return deleteSqlList;
    }
    
//    DELETE
//    FROM
//            ti_user_type
//    WHERE
//            app_id = '1'
//    AND user_type_id = '2';
    
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
            updateSql.append(StringUtils.getAFixedWidthString(beginSql, 75, false, 8, false, true));
            updateSql.append("\r\n");
            updateSql.append(
                    StringUtils.getAFixedWidthString("update " + ti.tableName.toLowerCase() + " "+stn,
                            75, false, 8, true, true));
            int colSize = ti.columnList.size();
            for(int j=0; j<colSize; j++)
            {
                if(j==0 && j != colSize-1)
                {
                   updateSql.append(StringUtils.getAFixedWidthString(" set "+stn+"."+ti.columnList.get(j).colName+" = ?,", 75, false, 8, true, true));
                } else if( j == colSize-1){
                   updateSql.append(StringUtils.getAFixedWidthString(stn+"."+ti.columnList.get(j).colName+" = ?", 75, false, 8, true, true)); 
                } else {
                   updateSql.append(StringUtils.getAFixedWidthString(stn+"."+ti.columnList.get(j).colName+" = ?", 75, false, 8, true, true));  
                }
            }
          //  updateSql.append(StringUtils.getAFixedWidthString(" where "+stn+".app_id = ? ",75,false,true));
            String primary = ti.tableName.substring(ti.tableName.indexOf("_")+1)+"_id";
            if(ti.columnNameList.contains(primary.toLowerCase()))
            {
               updateSql.append(StringUtils.getAFixedWidthString(" where "+stn+".app_id = ? ",75,false, 8, true, true));   
               updateSql.append(StringUtils.getAFixedWidthString(" and "+stn+"."+primary.toLowerCase()+" = ? ",75,true, 8, true, true)); 
            } else {
               updateSql.append(StringUtils.getAFixedWidthString(" where "+stn+".app_id = ? ",75,true, 8, true, true));  
               System.out.println("----------------"+ti.tableName+" has no prmary key!"+" the caculate primarykey = "+ primary.toLowerCase());
            }
            
            //临时测试，查看表当中是否包含字段status
            if(!ti.columnNameList.contains("status".toLowerCase()))
            {
                System.out.println("-------------------------------"+ti.tableName+" 没有status字段 ----------------");
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
            normalSelect.append(StringUtils.getAFixedWidthString(beginSql,75, false, 8, false, true));
            normalSelect.append("\r\n");
            normalSelect.append(StringUtils.getAFixedWidthString("select",75, false, 8, true, true));
            int columnsSize = ti.columnList.size();
            for(int j=0;j< columnsSize; j++)
            {
                if(j != columnsSize-1)
                {
                    normalSelect.append(StringUtils.getAFixedWidthString(ti.columnList.get(j).colName.toLowerCase()+",",
                                                    75, false, 8, true, true));
                }else if(j == columnsSize-1){
                    normalSelect.append(StringUtils.getAFixedWidthString(ti.columnList.get(j).colName.toLowerCase(), 
                                                    75, false, 8, true, true));
                }
            }
            normalSelect.append(StringUtils.getAFixedWidthString(" from "+ti.tableName.toLowerCase(),75, false, 8, true, true));
            normalSelect.append(StringUtils.getAFixedWidthString("  where app_id = ? ",75, true, 8, true, true));
            selectSqlList.add(normalSelect.toString());
            System.out.println(normalSelect.toString());
        }
//        //生成id查询sql
//        
//        //生成名称查询sql
        return selectSqlList;
    }
    
     /**
     * 生成普通的JavaBean对象
     * @param path 
     */
    public void generateSqlBean(String packagePath){
        StringBuffer sb = new StringBuffer();
        sb.append(this.packageLine);
        sb.append("\r\n");
        sb.append("\r\n");
        sb.append(this.getclassComment());
        sb.append(this.classBeginLine);
        sb.append("\r\n");
        sb.append("\r\n");
        sb.append(CommentUtils.getLineStarComment(8, "所有的新增sql", true));
        sb.append("\r\n");
        for(int k=0; k<this.insertSqlList.size(); k++)
        {
            sb.append(this.insertSqlList.get(k));
            sb.append("\r\n");
        }
        sb.append("\r\n");
        sb.append("\r\n");
        sb.append(CommentUtils.getLineStarComment(8, "所有的失效sql", true));
        sb.append("\r\n");
        for(int n=0; n<this.efficacySqlList.size();n++)
        {
            sb.append(this.efficacySqlList.get(n));
            sb.append("\r\n");
        }
        sb.append("\r\n");
        sb.append("\r\n");
        sb.append(CommentUtils.getLineStarComment(8, "所有的删除sql", true));
        sb.append("\r\n");
        for(int u=0; u<this.deleteSqlList.size();u++)
        {
            sb.append(this.deleteSqlList.get(u));
            sb.append("\r\n");
        } 
        sb.append("\r\n");
        sb.append("\r\n");
        sb.append(CommentUtils.getLineStarComment(8, "所有的更新sql", true));
        sb.append("\r\n");
        for(int m=0; m<this.updateSqlList.size(); m++)
        {
            sb.append(this.updateSqlList.get(m));
            sb.append("\r\n");
        }  
        sb.append("\r\n");
        sb.append("\r\n");
        sb.append(CommentUtils.getLineStarComment(8, "所有的查询sql", true));
        sb.append("\r\n");
        for(int j=0; j<this.selectSqlList.size(); j++)
        {
            sb.append(this.selectSqlList.get(j));
            sb.append("\r\n");
        }

        sb.append("}");
        packagePath = packagePath + "\\" + this.className+".java";
        FileUtils.writeFile(sb.toString(), packagePath);
        StringUtils.println(sb.toString());
    }
    
     /**
     * 生成注释信息
     * @return 生成注释的信息
     */
    public String getclassComment(){
        String classComment = null;
        classComment =  CommentUtils.getClassComment(0, this.className, 
        "数据库所有表的操作sql", "kado(631930821@qq.com)", 
        "成都市致力行科技有限公司(http://www.iamaction.cn)",DateUtils.getJustDate());
        return classComment;
    }
}
