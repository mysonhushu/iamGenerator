/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamgenerator.config;

import com.iamgenerator.genrator.JavaBeanGenerator;
import com.iamgenerator.bean.SQLColumn;
import com.iamgenerator.bean.TableInfo;
import com.iamgenerator.dao.GeneratorDao;
import com.iamgenerator.db.ConnSource;
import com.iamgenerator.genrator.SqlBeanGenerator;
import java.util.ArrayList;
import org.logicalcobwebs.proxool.configuration.PropertyConfigurator;

public class SystemLoad {
    public static String properFile =  "properties/proxool.xml"; 
    public static String logPath = "properties/log.properties"; 
    /**
     * 初始化日志配置文件和数据库链接池
     */
    public static void init(){
            try {
                    PropertyConfigurator.configure(logPath); 
                    ConnSource.init(properFile);
            } catch (Exception e) {
                    e.printStackTrace();
            }
    }
    public static void main(String args[]) {
        SystemLoad.init();
        ArrayList<String> tables = GeneratorDao.getInstance().getTables();
        ArrayList<TableInfo> tableInfoList = new ArrayList<TableInfo>();
        for(int i=0; i<tables.size(); i++)
        {
//            System.out.println("table name = " + tables.get(i));
            TableInfo ti = new TableInfo();
            ti.tableName = tables.get(i);
            ti.columnList = new ArrayList<SQLColumn>();
            ArrayList<SQLColumn> cl = 
                    GeneratorDao.getInstance().getTableInfo(tables.get(i));
            ti.columnList = cl;
            tableInfoList.add(ti);
            System.out.println("---------------------------------------------");
            //String packageName,String tableName,ArrayList<SQLColumn> columns
//            JavaBeanGenerator javaBean = new JavaBeanGenerator("cn.iamaction.carbme.bean",
//                    tables.get(i).toLowerCase(),cl);
//            String javaBeanPackagePath = "D:\\myeclipseWorkspace\\carbme_bean\\src\\cn\\iamaction\\carbme\\bean";
//            //生成java bean类文件
//            javaBean.generateJavaBean(javaBeanPackagePath);
        }
        String sqlBeanPackage = "";
        String sqlBeanPath = "";
        String className = "CarbmeDaoSql";
        //生成DaoSql文件
        SqlBeanGenerator daoSql = new SqlBeanGenerator(sqlBeanPackage,
                                                            className,
                                                         tableInfoList,
                                                           sqlBeanPath);
        
//            public SqlBeanGenerator(String packageName, 
//                                String className, 
//                                ArrayList<TableInfo> tableInfoList,
//                                String path){
        
//        String strSize = "ArrayList<String> selectSqlList = new ArrayList<String>();";
//        System.out.println(strSize.length());
    }
         
}
