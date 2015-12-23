package com.iamgenerator.genrator;

import com.iamgenerator.api.ControlCenter;
import com.iamgenerator.bean.SQLColumn;
import com.iamgenerator.util.CommentUtils;
import com.iamgenerator.util.DateUtils;
import com.iamgenerator.util.FileUtils;
import com.iamgenerator.util.StringUtils;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author kado
 */
public class JavaBeanGenerator {
    /**java bean 属性信息**/
    public String packageLine = "";
    public ArrayList<String> importClassLines = new ArrayList<String>(); 
    public String classBeginLine = "";
    public ArrayList<String> propertyLines =  new ArrayList<String>();
    public ArrayList<String> propertyGetAndSetMethods = new ArrayList<String>();
    public String tablename = "";
    public boolean hasPrimaryKey = false;
    
    /**参数信息**/
    public ArrayList<SQLColumn> columnsList = null;
    
    /**
     * JavaBeanGenerator 类的构造函数
     * @param packageName 包名称
     * @param tableName   
     * @param columns 
     */
    public JavaBeanGenerator(String packageName,String tableName,
                                   ArrayList<SQLColumn> columns){
        if(StringUtils.StringHasValue(tableName)){
            this.tablename = tableName;
            packageLine = "public class " + tableName.toUpperCase() + " {";
        }else{
            StringUtils.println("表名为null，无法生成JavaBeanInfo对象");
            System.exit(-1);
        }
        if(StringUtils.StringHasValue(packageName)){
            packageLine = "package " + packageName + ";";
        }
        if(columns.isEmpty()){
            StringUtils.println("无法生成JavaBeanInfo对象，表" + tableName +
                                "没有属性");
            System.exit(-1);
        }
        //记录列名信息
        columnsList = columns;
        
        HashSet<String> hset = new HashSet<String>(); 
        //生成javabean的属性类
        for(int i=0; i<columns.size(); i++){
            hset.add(columns.get(i).colTypeName);
            propertyLines.add(CommentUtils.getLineStarComment(0, columns.get(i).remarks,false));
            if("VARCHAR".equals(columns.get(i).colTypeName.toUpperCase()) &&
                    StringUtils.StringHasValue(columns.get(i).colName)){
                propertyLines.add("public String "+columns.get(i).colName+";");
            } else if("DATE".equals(columns.get(i).colTypeName.toUpperCase()) &&
                    StringUtils.StringHasValue(columns.get(i).colName)){
                propertyLines.add("public Date "+columns.get(i).colName+";");
            } else if("INT".equals(columns.get(i).colTypeName.toUpperCase()) &&
                    StringUtils.StringHasValue(columns.get(i).colName)){
                propertyLines.add("public int "+columns.get(i).colName+";");
            } else if("DATETIME".equals(columns.get(i).colTypeName.toUpperCase()) &&
                    StringUtils.StringHasValue(columns.get(i).colName)){
                propertyLines.add("public Date "+columns.get(i).colName+";");
            } else {
                propertyLines.add("public String "+columns.get(i).colName+";");
            }
        }
        //要导入的包
        if(hset.contains("DATE") || hset.contains("DATETIME")){
            importClassLines.add("import java.util.Date;");
        }
        classBeginLine = "public class " + tableName.toUpperCase() + " {";
    }
    
    /**
     * 生成普通的JavaBean对象
     * @param path 
     */
    public void generateJavaBean(String packagePath){
        StringBuffer sb = new StringBuffer();
        sb.append(this.packageLine);
        sb.append("\r\n");
        sb.append("\r\n");
        for(int i=0; i<this.importClassLines.size(); i++)
        {
            sb.append(this.importClassLines.get(i));
            sb.append("\r\n");
        }
        sb.append("\r\n");
        sb.append(this.getclassComment());
        sb.append(this.classBeginLine);
        sb.append("\r\n");
        for(int j=0; j<this.propertyLines.size(); j++)
        {
            String primaryKey = this.tablename+"_id";
            if(this.propertyLines.get(j).toUpperCase().contains(primaryKey.toUpperCase()))
            {
                sb.append(StringUtils.getBlankString(4));
                sb.append(this.propertyLines.get(j));
                sb.append("\r\n");
            }else{
                sb.append(StringUtils.getBlankString(4));
                sb.append(this.propertyLines.get(j));
                sb.append("\r\n");
            }
        }
        sb.append("\r\n");
        for(int k=0; k<this.propertyGetAndSetMethods.size(); k++)
        {
            sb.append(this.propertyGetAndSetMethods.get(k));
        }
        sb.append("}");
        packagePath = packagePath + "\\" + tablename.toUpperCase()+".java";
        FileUtils.writeFile(sb.toString(), packagePath);
        StringUtils.println(sb.toString());
    }
    
    /**
     * 将传入的字符串的第一个字母大写
     * @param value 传入的字符串
     * @return 转换后的字符串
     */
    public static String getFirstUpperString(String value){
        if(StringUtils.StringHasValue(value)){
            return String.valueOf(value.charAt(0)).toUpperCase()+
                    value.substring(1);
        }else{
            StringUtils.println("call getFirstUpperString " + 
                    " method failed,value is null!"+value);
            System.exit(-1);
        }
        return value;
    }
    
    /**
     * 生成注释信息
     * @return 生成注释的信息
     */
    public String getclassComment(){
        String classComment = null;
        classComment =  CommentUtils.getClassComment(0, this.tablename.toUpperCase(), 
        ControlCenter.kv.get(this.tablename.toLowerCase()), "kado(631930821@qq.com)", 
        "成都市致力行科技有限公司(http://www.iamaction.cn)",DateUtils.getJustDate());
        return classComment;
    }
}
