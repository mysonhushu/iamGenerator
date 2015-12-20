package com.iamgenerator.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
 
/**
 * @description:文件处理帮助类
 * @author ID:71834
 */
public class FileUtils {
    boolean ifDebug = true;
    
    private BufferedWriter bw;
    private OutputStream os;
    private OutputStreamWriter osw;
    
        //写文件，支持中文字符，在linux redhad下测试过
    public static void writeFile(String str,String path)
    {
        try {
            File file=new File(path);
            if(!file.exists())
            {
                file.createNewFile();
            }
            FileOutputStream out=new FileOutputStream(file,false); //如果追加方式用true        
            StringBuffer sb=new StringBuffer();
            sb.append(str);
            out.write(sb.toString().getBytes("utf-8"));//注意需要转换对应的字符集
            out.close();
        } catch(IOException ex) {
            System.out.println(ex.getStackTrace());
        }
    }  
    
  
    public static StringBuilder read(String fileName)
    {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        FileReader filer = null;
        try
        {
            File file = new File(fileName);
            filer = new FileReader(file);
            reader = new BufferedReader(filer);
            String lineCons = null;
            while ((lineCons = reader.readLine()) != null)
            {
              sb.append(lineCons);
              lineCons = null;
            }
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        finally
        {
          close(reader, filer);
        }
        return sb;
    }
  
    public static StringBuilder readStream(String fileName)
    {
        StringBuilder sb = new StringBuilder();
        FileInputStream filer = null;
        try
        {
            filer = new FileInputStream(fileName);
            byte[] rb = new byte[1024];
            int k = 0;
            while ((k = filer.read(rb)) != -1) {
              sb.append(new String(rb, 0, k, "UTF-8"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            close(filer);
        }
        return sb;
    }
  
    public static boolean createFile(String fileUrl)
    {
        File file = new File(fileUrl);
        boolean end = false;
        if (file.exists())
        {
            end = false;
        }
        else
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            end = true;
        }
        return end;
    }
  
    public static boolean isExite(String fileUrl)
    {
        File pidFile = new File(fileUrl);
        boolean end = false;
        if (pidFile.exists()) {
            end = true;
        }
        return end;
    }
  
    public void getFileWriter(String fileName, String charsetName, boolean isAppend)
      throws Exception
    {
        this.os = new FileOutputStream(fileName, isAppend);
        this.osw = new OutputStreamWriter(this.os, charsetName);
        this.bw = new BufferedWriter(this.osw);
    }
  
    public void writer(String fileContent)
      throws Exception
    {
        this.bw.write(fileContent);
        this.bw.flush();
    }
  
    public static void updateFileContent(String fileName, String fileContent)
      throws Exception
    {
      FileUtils fp = new FileUtils();
      fp.getFileWriter(fileName, "UTF-8", false);
      fp.bw.write(fileContent);
      fp.bw.flush();
      fp.closeFileWriter();
    }
  
    public void closeFileWriter()
      throws Exception
    {
        if (this.bw != null)
        {
          this.bw.close();
          this.bw = null;
        }
        if (this.osw != null)
        {
          this.osw.close();
          this.osw = null;
        }
        if (this.os != null)
        {
          this.os.close();
          this.os = null;
        }
    }
  
    public static void close(BufferedReader reader, FileReader filer)
    {
        if (reader != null)
        {
            try
            {
              reader.close();
            }
            catch (Exception e)
            {
              e.printStackTrace();
            }
            reader = null;
        }
        if (filer != null)
        {
            try
            {
              filer.close();
            }
            catch (Exception e)
            {
              e.printStackTrace();
            }
            filer = null;
        }
    }
  
    public static void close(FileInputStream filer)
    {
        if (filer != null)
        {
            try
            {
              filer.close();
            }
            catch (Exception e)
            {
              e.printStackTrace();
            }
            filer = null;
        }
    }
 
    public static HashMap<String,String> readKeyAndValueFromFile(String filePath)
    {
        HashMap<String,String> kv = new HashMap<String,String>();
        BufferedReader reader = null;
        FileReader filer = null;
        try
        {
          File file = new File(filePath);
          filer = new FileReader(file);
          reader = new BufferedReader(filer);
          String line = null;
          while ((line = reader.readLine()) != null)
          {
              line = line.replaceAll("\"", "").replaceAll("'", "").trim();
              String[] a = line.split("=");
              kv.put(a[0], a[1]);
              line = null;
          }
        }
        catch (Exception e)
        {
           e.printStackTrace();
        }
        finally
        {
          close(reader, filer);
        }
        return kv;
    }
  }