package com.iamgenerator.dao;

import com.iamgenerator.db.ConnSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 

public class Dao
{
    public Connection conn = null;
    public void setConn(Connection conn)
   {
        ConnSource.close(this.conn);
        this.conn = conn;
    }
    public void close()
    {
        ConnSource.close(this.conn);
    }
  
    public void rollback()
    {
       try
       {
          if (this.conn != null) {
            this.conn.rollback();
          }
        }
        catch (SQLException e)
        {
           e.printStackTrace();
        }
    }
  
    public void commit()
    {
      try
      {
        if (this.conn != null) {
          this.conn.commit();
        }
      }catch (SQLException e){
        e.printStackTrace();
      }
    }
    /**
     * close the ResultSet,Statement,Connection.
     * @param rs
     * @param st
     * @param conn
     */
    public static void free(ResultSet rs,Statement st,Connection conn){
        try {
            if(rs !=null)
            {
                rs.close();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            try{
                if(st != null)
                {
                    st.close();
                }
            }catch(SQLException e1){
                e1.printStackTrace();
            }finally{
                try {
                    if(conn != null)
                    {
                        conn.close();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
     }
  
    public void setAutoCommit(boolean is) throws Exception
    {
        if (this.conn != null) {
          this.conn.setAutoCommit(is);
        }
    }
  
    //setAttrToKIPForSales_sql
    public void getConnection(){
        if(this.conn == null){
            this.conn =  ConnSource.getConnection();
        } else{
             try {
                if(this.conn.isClosed()){
                    this.conn =  ConnSource.getConnection();
                }
            } catch (SQLException e) {
                  e.printStackTrace();
            }
        }
    } 
  
    public void getConnection(String dbType){
        ConnSource.close(this.conn);
        if(this.conn == null){
            this.conn =  ConnSource.getConnection(dbType);
        } else{
            try {
                if(this.conn.isClosed()){
                    this.conn =  ConnSource.getConnection(dbType);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    } 
}