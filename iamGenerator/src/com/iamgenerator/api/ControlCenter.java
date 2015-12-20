/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamgenerator.api;

import com.iamgenerator.bean.TableInfo;
import com.iamgenerator.config.SystemLoad;
import com.iamgenerator.dao.GeneratorDao;
import com.iamgenerator.genrator.JavaBeanGenerator;
import com.iamgenerator.genrator.SqlBeanGenerator;
import com.iamgenerator.util.FileUtils;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author kado
 */
public class ControlCenter {
    public static HashMap<String,String> kv = null;
    public static ArrayList<TableInfo> tableInfoList = null;
    static {
        SystemLoad.init();
        kv = FileUtils.readKeyAndValueFromFile("D:\\my-git-workspace\\beanGenerator\\iamGenerator\\source\\carbme_sql.txt");
    }
    public void process(){
        tableInfoList = GeneratorDao.getInstance().getAllTableAndColumnsInfo();
//        this.generatJavaBean();
        this.generatSqlBean();
         
    }
    
    /**
     * 生成Java Bean 对象
     */
    public void generatJavaBean()
    {
        //生成Javabean对象
        String beanPackage = "cn.iamaction.carbme.bean";
        String beanPath = "D:\\myeclipseWorkspace\\carbme_bean\\src\\cn\\iamaction\\carbme\\bean";
        int size=tableInfoList.size();
        for(int i=0; i<size; i++)
        {
            JavaBeanGenerator javaBean = new JavaBeanGenerator(beanPackage,
                    tableInfoList.get(i).tableName.toLowerCase(),tableInfoList.get(i).columnList);
            //生成java bean类文件
            javaBean.generateJavaBean(beanPath);
        }
    }
    
    /**
     * 生成Sql Bean 对象
     */
    public void generatSqlBean()
    {
        String sqlBeanPackage = "";
        String sqlBeanPath = "";
        String className = "CarbmeDaoSql";
        //生成DaoSql文件
        SqlBeanGenerator daoSql = new SqlBeanGenerator(sqlBeanPackage,
                                                            className,
                                                         tableInfoList,
                                                           sqlBeanPath);
    }
    
    public static void main(String args[])
    {
        ControlCenter cc = new ControlCenter();
        cc.process();
    }
}
