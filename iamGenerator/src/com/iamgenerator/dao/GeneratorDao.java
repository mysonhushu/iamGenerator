package com.iamgenerator.dao;

import com.iamgenerator.api.ControlCenter;
import com.iamgenerator.bean.SQLColumn;
import com.iamgenerator.bean.TableInfo;
import com.iamgenerator.db.ConnSource;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author kado
 */
public class GeneratorDao extends Dao{
    public PreparedStatement getTableInfo_ps = null;
//    public PreparedStatement addTiUserType_ps = null;
    private static final Logger log = Logger.getLogger(GeneratorDao.class);
    private static final GeneratorDao instance = new GeneratorDao();
       
    private GeneratorDao (){}    
    public static final GeneratorDao getInstance() {   
       if(instance.conn == null){
           instance.conn = ConnSource.getConnection();
       } else{
           try {
               if (instance.conn.isClosed()){
                   instance.conn = ConnSource.getConnection();
               }
           } catch (SQLException ex) {
                ex.printStackTrace();
                StackTraceElement [] messages = ex.getStackTrace();
                int messagesSize = messages.length;
                for(int i=0;i<messagesSize;i++){
                    log.error(messages[i]);
                } 
           }
       }
       return instance;    
    }    
    /**
     * 获取数据库的所有表
     * @return 数据库的所有表的集合
     */
    public ArrayList<String> getTables(){
        ArrayList<String> tableNames = new ArrayList<String>();
        ResultSet rs = null;
        try {
            DatabaseMetaData dmd = conn.getMetaData();
            String[] types = {"TABLE"};
            rs = dmd.getTables(null, null, "%", types);
            while(rs.next()){
                //获取数据库表名
                tableNames.add(rs.getString("TABLE_NAME"));
                
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            StackTraceElement [] messages = ex.getStackTrace();
            int messagesSize = messages.length;
            for(int i=0;i<messagesSize;i++){
                log.error(messages[i]);
            } 
        } finally{
            free(rs, null, this.conn);
        }
        return tableNames;
    }
    
    
    /**
     * 获取数据库的所有表和相关的字段的信息
     * @return 数据库的所有表的集合
     */
    public ArrayList<TableInfo> getAllTableAndColumnsInfo(){
        ArrayList<TableInfo> tableInfoList = new ArrayList<TableInfo>();  
//      ArrayList<String> tableNames = new ArrayList<String>();
        ResultSet rs = null;
        ResultSet resSet = null;
        try {
            DatabaseMetaData dbmd = conn.getMetaData();
            String[] types = {"TABLE"};
            rs = dbmd.getTables(null, null, "%", types);
            while(rs.next()){
                TableInfo ti = new TableInfo();
                //获取表名称
                ti.tableName = rs.getString("TABLE_NAME");
                //如果在读取的配置里面有表名的注释，就添加注释
                if(ControlCenter.kv.containsKey(ti.tableName.toLowerCase()))
                {
                     ti.tableRemark = ControlCenter.kv.get(ti.tableName.toLowerCase());
                }
                ti.columnList = new ArrayList<SQLColumn>();
                ti.columnNameList = new ArrayList<String>();
                resSet = dbmd.getColumns(null, "%", ti.tableName, "%");
                //System.out.println("表名：" + ti.tableName);
                while(resSet.next())
                {
                    SQLColumn sc = new SQLColumn();
                    //字段序列号
                    sc.seq_id = resSet.getInt("ORDINAL_POSITION");
                    //字段名称
                    sc.colName = resSet.getString("COLUMN_NAME");
                    //字段类型
                    sc.colTypeName = resSet.getString("TYPE_NAME");
                    //字段注释
                    sc.remarks = resSet.getString("REMARKS");
                    //System.out.println("列序列：" + sc.seq_id + "   列名称：   " + sc.colName + "    列类型：    " + sc.colTypeName + "    列注释：    " + sc.remarks );
                    ti.columnList.add(sc);
                    ti.columnNameList.add(resSet.getString("COLUMN_NAME"));
                }
                tableInfoList.add(ti);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            StackTraceElement [] messages = ex.getStackTrace();
            int messagesSize = messages.length;
            for(int i=0;i<messagesSize;i++){
                log.error(messages[i]);
            } 
        } finally{
            free(rs, null, this.conn);
            free(resSet, null, this.conn);
        }
        return tableInfoList;
    }    
    /*
     * 根据提供的表名获取该表的表结构
     * @param tableName 
     */
    public ArrayList<SQLColumn> getTableInfo(String tableName){
        ArrayList<SQLColumn> columnList = new ArrayList<SQLColumn>();
        String sql = "select * from " + tableName + " where 1=2";
        ResultSetMetaData rsd = null;
        try {
            this.getTableInfo_ps = conn.prepareStatement(sql);
            rsd = this.getTableInfo_ps.executeQuery().getMetaData();
            for(int i=1; i<=rsd.getColumnCount(); i++)
            {
                String colName = rsd.getColumnName(i);
                String colTypeName = rsd.getColumnTypeName(i);
                SQLColumn sqlColumn = new SQLColumn();
                sqlColumn.seq_id = i;
                sqlColumn.colName = colName;
                sqlColumn.colTypeName = colTypeName;
//                System.out.println("  colName="+colName+"  colTypeName="+colTypeName );
                columnList.add(sqlColumn);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            StackTraceElement [] messages = ex.getStackTrace();
            int messagesSize = messages.length;
            for(int i=0;i<messagesSize;i++){
                log.error(messages[i]);
            } 
        } finally{
            free(null, this.getTableInfo_ps, this.conn);
        }
        return columnList;
    }
}
