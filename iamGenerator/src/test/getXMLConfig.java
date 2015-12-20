package test;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import javax.swing.filechooser.FileSystemView;
 
public class getXMLConfig {
    private static final String driver="com.mysql.jdbc.Driver";
    private static final String pwd="zhilixing";
    private static final String user="huxing";
    private static final String url = "jdbc:mysql://192.168.58.131/carbme"
            + "?user=" + user + "&password=" + pwd
            + "&useUnicode=true&characterEncoding=UTF-8";
    private static Connection getConnection=null;
 
    public static void main(String[] args) {
        getConnection=getConnections();
        try {
            DatabaseMetaData dbmd=getConnection.getMetaData();
            ResultSet resultSet = dbmd.getTables(null, "%", "%", new String[] { "TABLE" });
            while (resultSet.next()) {
                String tableName=resultSet.getString("TABLE_NAME");
                //System.out.println(tableName);
                if(tableName.equals("user")){
                    //ResultSet rs =getConnection.getMetaData().getColumns(null, getXMLConfig.getSchema(),tableName.toUpperCase(), "%");//其他数据库不需要这个方法的，直接传null，这个是oracle和db2这么用
                    ResultSet rs = dbmd.getColumns(null, "%", tableName, "%");
                    System.out.println("表名："+tableName+"\t\n表字段信息：");
                    while(rs.next()){
                        System.out.println(rs.getString("COLUMN_NAME")+"----"+rs.getString("REMARKS"));
                    }
                }else {
                    System.out.println("数据库表明："+tableName);
                     ResultSet rs = dbmd.getColumns(null, "%", tableName, "%");
                    System.out.println("表名："+tableName+"\t\n表字段信息：");
                    while(rs.next()){
                        System.out.println(rs.getString("COLUMN_NAME")+"----"+rs.getString("REMARKS"));
                    }
                }
            }
            FileSystemView fsv=FileSystemView.getFileSystemView();
            String path=fsv.getHomeDirectory().toString();//获取当前用户桌面路径
            File directory = new File(path);
            if (directory.exists()) {
            } else {
                directory.createNewFile();
            }
            /*FileWriter fw = new FileWriter(directory+ "\\"+dbname+".xml");
            PrintWriter pw = new PrintWriter(fw);
            pw.println("x");
            pw.flush();
            pw.close();
            System.out.println("生成成功！");*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnections(){
        try {
            //Properties props =new Properties();
            //props.put("remarksReporting","true");
            Class.forName(driver);
            getConnection=DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getConnection;
    }
     
     public static String getSchema() throws Exception {
            String schema;
            schema =getConnection.getMetaData().getUserName();
            if ((schema == null) || (schema.length() == 0)) {
                throw new Exception("ORACLE数据库模式不允许为空");
            }
            return schema.toUpperCase().toString();
 
    }
 
}
