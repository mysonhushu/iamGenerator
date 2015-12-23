package com.iamgenerator.genrator;

import com.iamgenerator.api.ControlCenter;
import com.iamgenerator.bean.SQLColumn;
import com.iamgenerator.bean.TableInfo;
import com.iamgenerator.util.CommentUtils;
import com.iamgenerator.util.DateUtils;
import com.iamgenerator.util.FileUtils;
import com.iamgenerator.util.StringUtils;
import java.util.ArrayList;

/**
 * Java 的Dao方法生成类
 * @author kado
 */
public class DaoBeanGenerator {
    
    /**java文件所在的包**/
    public String packageLine = "";
    /**表名和字段的数组信息**/
    public ArrayList<TableInfo> tableInfoList = null;
    public String classBeginLine = "";
    public String className = "";
    public String path="";
    //生成所有要导入的类
    public ArrayList<String> importClassList = null;
    //所有的PreparedStatement对象
    public ArrayList<String> statementList = null;
    //添加方法的集合
    public ArrayList<String> addMethodList = null;
    //修改方法的集合
    public ArrayList<String> modMethodList = null;
    //删除方法的集合
    public ArrayList<String> delMethodList = null;
    //查询方法的集合
    public ArrayList<String> qryMethodList = null;
    //根据ID查询数据的方法
    public ArrayList<String> qryByIdMethoddList = null;
    //失效方法的集合
    public ArrayList<String> expMethodList = null;
    //所有的getMapping方法
    public ArrayList<String> getMappingMethodList = null;
    //所有的setMapping方法
    public ArrayList<String> setMappingMethodList = null;
    
    public DaoBeanGenerator(String packageName, 
                                String className, 
                                ArrayList<TableInfo> tableInfoList,
                                String path){
       //如果有传入包名，则书写包名
        if(StringUtils.StringHasValue(packageName)){
            packageLine = "package " + packageName + ";";
        }
        this.className = className;
        this.path = path;
        //核实传入的tableInfo数组是否为空
        if(null == tableInfoList || tableInfoList.isEmpty())
        {
            StringUtils.println("调用SqlBeanGenerator时参数tableInfoList为null，无法生成SqlBeanInfo对象");
            System.exit(-1);
        }
        this.importClassList = new ArrayList<String>();
        this.importClassList.add("import java.sql.CallableStatement;");
        this.importClassList.add("import java.sql.Clob;");
        this.importClassList.add("import java.sql.PreparedStatement;");
        this.importClassList.add("import java.sql.ResultSet;");
        this.importClassList.add("import java.sql.SQLException;");
        this.importClassList.add("import java.util.ArrayList;");
        this.importClassList.add("import org.apache.log4j.Logger;");
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
            this.importClassList.add("import "+packageName.substring(0, packageName.lastIndexOf("."))+".bean."+tableInfoList.get(i).tableName.toUpperCase()+";");
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
        classBeginLine = "public class " + className + " extends Dao{";
        //开始生成构造查询sql
        this.generateDaoClass();
        this.generateDaoBean(packageName);
    }
    
    
    
    /**
     * 生成Dao类的方法
     */
    public void generateDaoClass(){
        //生成状态 ok
        this.statementList = this.generateStatementList();
        //添加方法 ok
        this.addMethodList = this.generateAddMethodList();
        //修改方法 
        this.modMethodList = this.generateModMethodList();
        //删除方法
        this.delMethodList = this.generateDelMethodList();
        //查询方法
        this.qryMethodList = this.generateQryMethodList();
        //根据ID查询数据的方法
        this.qryByIdMethoddList = this.generateQryMethodList();
        //失效方法
        this.expMethodList = this.generateExpMethodList();
        //从数据库获取数据的映射方法 ok
        this.getMappingMethodList = this.generateGetMapMethodList();
        //存入数据库数据的映射方法 ok
        this.setMappingMethodList = this.generateSetMapMethodList();
    }
    
    /**
     * 生成所有的PreparedStatement对象
     * @return 生成的preparedstatement对象的集合
     */
    public ArrayList<String> generateStatementList(){
        ArrayList<String> statementList = new ArrayList<String>();
        for(TableInfo ti : tableInfoList)
        {
            statementList.add("private PreparedStatement add"+StringUtils.toCamelCase(ti.tableName)+"_ps;");
        }
        for(TableInfo ti: tableInfoList)
        {
             statementList.add("private PreparedStatement qry"+StringUtils.toCamelCase(ti.tableName)+"_ps;");
        }
        for(TableInfo ti: tableInfoList)
        {
             statementList.add("private PreparedStatement mod"+StringUtils.toCamelCase(ti.tableName)+"_ps;");
        }
        for(TableInfo ti: tableInfoList)
        {
             statementList.add("private PreparedStatement del"+StringUtils.toCamelCase(ti.tableName)+"_ps;");
        }
        for(TableInfo ti: tableInfoList)
        {
             statementList.add("private PreparedStatement exp"+StringUtils.toCamelCase(ti.tableName)+"_ps;");
        }
        
        for(String str: statementList)
        {
            System.out.println(str);
        }
        return statementList;
    }
    
    
    /**
     * 生成添加的方法集合
     * @return 生成的所有新增方法的集合
     */
    public ArrayList<String> generateAddMethodList(){
        ArrayList<String> addMethodList = new ArrayList<String>();
        for(TableInfo ti: tableInfoList)
        {
             //类名
            String cName = ti.tableName.toUpperCase();
            //对象
            String obj = StringUtils.toAcronym(ti.tableName);
            //预处理参数名
            String psName = StringUtils.toCamelCase(cName)+"_ps";
            
            StringBuffer sb = new StringBuffer();
            ArrayList<String> params = new ArrayList<String>();
            params.add(cName+" 要添加的对象");
            sb.append(CommentUtils.getMethodComment(4, "add"+StringUtils.toCamelCase(cName), ti.tableRemark+"("+cName+")的添加方法",params , "int 失败返回-1,成功返回插入对象ID", null));
            String b = "public String add"+StringUtils.toCamelCase(cName)+"("+ cName+" "+obj+") {";
            sb.append(StringUtils.getAFixedWidthString(b,75, true, 4, false, false));
            sb.append(StringUtils.getAFixedWidthString("String ret = \"-1\";",75, true, 8, false, false));
            sb.append(StringUtils.getAFixedWidthString("this.getConnection();",75, true, 8, false, false));
            sb.append(StringUtils.getAFixedWidthString("try {",75, true, 8, false, false));
            String sql ="String sql = CarbmeDaoSql.add" + StringUtils.toCamelCase(cName)+"_sql;";
            sb.append(StringUtils.getAFixedWidthString(sql, 75, true, 12, false, false));
            String ps = "this.add"+StringUtils.toCamelCase(cName)+"_ps = this.conn.prepareStatement(sql);";
            sb.append(StringUtils.getAFixedWidthString(ps, 75, true, 12, false, false));
            sb.append(StringUtils.getAFixedWidthString("this.setMapping"+StringUtils.toCamelCase(cName)+"(add"+psName+", "+obj+");", 
                    75, true, 12, false, false));
            sb.append(StringUtils.getAFixedWidthString("Boolean a = this.add" + psName + ".execute();", 75, true, 12, false, false));
            sb.append(StringUtils.getAFixedWidthString("if(a){", 75, true, 12, false, false));
            String id = cName.substring(cName.indexOf("_")+1).toLowerCase()+"_id";
            sb.append(StringUtils.getAFixedWidthString("ret = \"\"+"+obj+"."+id+";", 75, true, 16, false, false));
            sb.append(StringUtils.getAFixedWidthString("}", 75, true, 12, false, false));
            sb.append(StringUtils.getAFixedWidthString("} catch (Exception e) {", 75, true, 8, false, false));
            sb.append(StringUtils.getAFixedWidthString("StackTraceElement [] messages=e.getStackTrace();", 75, true, 12, false, false));
            sb.append(StringUtils.getAFixedWidthString("int messagesSize = messages.length;", 75, true, 12, false, false));
            sb.append(StringUtils.getAFixedWidthString("for(int i=0; i<messagesSize; i++){", 75, true, 12, false, false));
            sb.append(StringUtils.getAFixedWidthString("log.error(messages[i]);", 75, true, 16, false, false));
            sb.append(StringUtils.getAFixedWidthString("}", 75, true, 12, false, false));
            sb.append(StringUtils.getAFixedWidthString("}finally{", 75, true, 8, false, false));
            sb.append(StringUtils.getAFixedWidthString("Dao.free(null, add" + psName +", conn);", 75, true, 12, false, false));
            sb.append(StringUtils.getAFixedWidthString("}", 75, true, 8, false, false));
            sb.append(StringUtils.getAFixedWidthString("return ret;", 75, true, 8, false, false));
            sb.append(StringUtils.getAFixedWidthString("}", 75, true, 4, false, false));
            
            System.out.println(sb.toString());
            addMethodList.add(sb.toString());
        }
        return addMethodList;
    }
    
    /**
     * 生成修改的方法集合
     * @return 生成的所有修改的方法的集合
     */
    public ArrayList<String> generateModMethodList(){
        ArrayList<String> modMethodList = new ArrayList<String>();
        return modMethodList;
    }
    
    /**
     * 生成删除的方法集合
     * @return 生成删除的方法集合
     */
    public ArrayList<String> generateDelMethodList(){
        ArrayList<String> delMethodList = new ArrayList<String>();
        return delMethodList;
    }
    
    
    /**
     * 生成查询的方法集合
     * @return 生成查询的方法集合
     */
    public ArrayList<String> generateQryMethodList(){
        ArrayList<String> qryMethodList = new ArrayList<String>();
        
        return qryMethodList;
    }
    
    /**
     * 生成根据ID查询数据的方法集合
     * @return 生成查询的方法集合
     */
    public ArrayList<String> generateQryByIdMethodList(){
        ArrayList<String> qryByIdMethodList = new ArrayList<String>();
        
        return qryByIdMethodList;
    }

    /**
     * 生成失效的方法集合
     * @return 生成失效的方法集合
     */
    public ArrayList<String> generateExpMethodList(){
        ArrayList<String> effMethodList = new ArrayList<String>();
        return effMethodList;
    }
    
    /**
     * 生成ResultSet结果集转换为JavaBean类的映射方法
     * @return 生成的方法的结果集
     */
    public ArrayList<String> generateGetMapMethodList(){
        ArrayList<String> mapMethodList = new ArrayList<String>();
        for(TableInfo ti: tableInfoList)
        {
            //类名
            String className = ti.tableName.toUpperCase();
            //对象名
            String objectName = StringUtils.toAcronym(ti.tableName);
            
            
            StringBuilder sb = new StringBuilder();
            ArrayList<String> params = new ArrayList<String>();
            params.add("ResultSet rs 查询返回结果集");
            sb.append(CommentUtils.getMethodComment(8, "getMapping"+StringUtils.toCamelCase(ti.tableName), ti.tableRemark+"("+ti.tableName+")查询返回结果集的映射方法",params , className+" 拼装好的对象", null));
            String a ="private "+className+" getMapping"+
                    StringUtils.toCamelCase(ti.tableName)+
                    "(ResultSet rs) throws Exception {";
            sb.append(StringUtils.getAFixedWidthString(a, 75, false, 8, false,false));
          //  sb.append(StringUtils.getAFixedWidthString("{", 75, false, false));
            sb.append("\r\n");
            sb.append(StringUtils.getAFixedWidthString(className + " " +
                    objectName + " = new "+ className + "();"  , 75, true, 12, false,false));
            for(SQLColumn sc : ti.columnList)
            {
                if(sc.colTypeName.toLowerCase().equals("varchar"))
                {
                    sb.append(StringUtils.getAFixedWidthString(objectName + "." +
                        sc.colName + " = rs.getString(\""+ sc.colName + "\");",
                            75, true, 12, false, false));
                }else if(sc.colTypeName.toLowerCase().equals("int")){
                    sb.append(StringUtils.getAFixedWidthString(objectName + "." +
                        sc.colName + " = rs.getInt(\""+ sc.colName + "\");",
                            75, true, 12,false, false));
                }else if(sc.colTypeName.toLowerCase().equals("datetime")){
                    sb.append(StringUtils.getAFixedWidthString(objectName + "." +
                        sc.colName + " = rs.getDate(\""+ sc.colName + "\");",
                            75, true, 12,false, false));
                }else{
                    sb.append(StringUtils.getAFixedWidthString(objectName + "." +
                        sc.colName + " = rs.getString(\""+ sc.colName + "\");",
                            75, true, 12, false, false));
                }
            }
             sb.append(StringUtils.getAFixedWidthString("return "+ objectName+";",
                            75, true, 12, false, false));
             sb.append(StringUtils.getAFixedWidthString("}",
                            75, true, 8, false,false));
             System.out.println(sb.toString());
             mapMethodList.add(sb.toString());
        }
        return mapMethodList;
    }
    
    
     /**
     * 将JavaBean对象设置到PreparedStatement里面的方法
     * @return 生成的方法的结果集
     */
    public ArrayList<String> generateSetMapMethodList(){
        ArrayList<String> mapMethodList = new ArrayList<String>();
        for(TableInfo ti: tableInfoList)
        {
            //对象名
            String objectName = StringUtils.toAcronym(ti.tableName);
            StringBuilder sb = new StringBuilder();
            ArrayList<String> params = new ArrayList<String>();
            params.add("PreparedStatement ps 预处理声明");
            params.add(ti.tableName+" "+objectName+" 操作对象");
            sb.append(CommentUtils.getMethodComment(8, "setMapping"+StringUtils.toCamelCase(ti.tableName), ti.tableRemark+"("+ti.tableName+")将对象设置到预处理标志中的方法",params , "PreparedStatement ps 设置好的预处理标志", null));
            String a ="private PreparedStatement setMapping"+
                    StringUtils.toCamelCase(ti.tableName)+
                    "(PreparedStatement ps," + 
                    ti.tableName.toUpperCase()+" "+objectName+") throws Exception {";
            sb.append(StringUtils.getAFixedWidthString(a, 75, true, 8, false,false));
            for(int i=0; i<ti.columnList.size(); i++)
            {
                if( "varchar".equals(ti.columnList.get(i).colTypeName.toLowerCase()))
                {
                    sb.append(StringUtils.getAFixedWidthString("this.add"+StringUtils.toCamelCase(ti.tableName)+"_ps.setString("+(i+1)+", "+objectName+"."+ti.columnList.get(i).colName+");",
                                75, true, 12, false,false));
                }else if("int".equals(ti.columnList.get(i).colTypeName.toLowerCase())){
                    sb.append(StringUtils.getAFixedWidthString("this.add"+StringUtils.toCamelCase(ti.tableName)+"_ps.setInt("+(i+1)+", "+objectName+"."+ti.columnList.get(i).colName+");",
                                75, true, 12, false,false));
                }else if("date".equals(ti.columnList.get(i).colTypeName.toLowerCase())){
                    sb.append(StringUtils.getAFixedWidthString("this.add"+StringUtils.toCamelCase(ti.tableName)+"_ps.setDate("+(i+1)+", "+objectName+"."+ti.columnList.get(i).colName+");",
                                75, true, 12, false,false));
                }
            }
            sb.append(StringUtils.getAFixedWidthString("return ps;",
                            75, true, 12, false, false));
            sb.append(StringUtils.getAFixedWidthString("}",
                            75, true, 8, false,false));
            mapMethodList.add(sb.toString());
            System.out.println(sb.toString());
        }
         return mapMethodList;
    }
    
    /**
     * 生成普通的DaoBean对象
     * @param path 
     */
    public void generateDaoBean(String packagePath){
        StringBuffer sb = new StringBuffer();
        sb.append(this.packageLine);
        sb.append("\r\n");
        sb.append("\r\n");
        for(int i=0; i<this.importClassList.size(); i++)
        {
            sb.append(this.importClassList.get(i));
            sb.append("\r\n");
        }
        sb.append("\r\n");
        sb.append(this.getclassComment());
        sb.append(this.classBeginLine);
        sb.append("\r\n");
        sb.append(StringUtils.getBlankString(4));
        sb.append("private static final Logger log = Logger.getLogger("+this.className+".class);");
        sb.append("\r\n");
        for(int j=0; j<this.statementList.size(); j++)
        {
            sb.append(StringUtils.getBlankString(4));
            sb.append(this.statementList.get(j));
            sb.append("\r\n");
        }
        sb.append("\r\n");
        for(int j=0; j<this.addMethodList.size(); j++)
        {
            sb.append(this.addMethodList.get(j));
            sb.append("\r\n");
        }
        sb.append("\r\n");
        for(int j=0; j<this.modMethodList.size(); j++)
        {
            sb.append(this.modMethodList.get(j));
            sb.append("\r\n");
        }
        sb.append("\r\n");
        for(int j=0; j<this.delMethodList.size(); j++)
        {
            sb.append(this.delMethodList.get(j));
            sb.append("\r\n");
        }
        sb.append("\r\n");
        for(int j=0; j<this.qryMethodList.size(); j++)
        {
            sb.append(this.qryMethodList.get(j));
            sb.append("\r\n");
        }
        sb.append("\r\n");
        for(int j=0; j<this.qryByIdMethoddList.size(); j++)
        {
            sb.append(this.qryByIdMethoddList.get(j));
            sb.append("\r\n");
        }
        sb.append("\r\n");
        for(int j=0; j<this.expMethodList.size(); j++)
        {
            sb.append(this.expMethodList.get(j));
            sb.append("\r\n");
        }
        sb.append("\r\n");
        for(int j=0; j<this.getMappingMethodList.size(); j++)
        {
            sb.append(this.getMappingMethodList.get(j));
            sb.append("\r\n");
        }
        sb.append("\r\n");
        for(int j=0; j<this.setMappingMethodList.size(); j++)
        {
            sb.append(this.setMappingMethodList.get(j));
            sb.append("\r\n");
        }
        sb.append("\r\n");
        sb.append("}");
        String m =  path+ "\\" + this.className+".java";
        System.out.println("packagePath =" + m );
        FileUtils.writeFile(sb.toString(), m);
        StringUtils.println(sb.toString());
    }
    
     /**
     * 生成注释信息
     * @return 生成注释的信息
     */
    public String getclassComment(){
        String classComment = null;
        classComment =  CommentUtils.getClassComment(0, this.className, 
        "所有的增删改查方法", "kado(631930821@qq.com)", 
        "成都市致力行科技有限公司(http://www.iamaction.cn)",DateUtils.getJustDate());
        return classComment;
    }
}
