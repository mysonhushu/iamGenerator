package com.iamgenerator.util;

import java.util.StringTokenizer;

/**
 * String对象工具类
 * @author kado
 */
public class StringUtils {
     public static void println(String value){
        System.out.println(value);
    }
    /**
     * 判断字符串的值是否为空
     * @param value
     * @return 
     */
    public static boolean StringHasValue(String value){
        if(value == null || "".equals(value)){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 判断String是否全为字母
     * @param str
     * @return 
     */
    public static boolean isCharacterAndUnderlineString(String str){
        boolean isCharacter = true;
        System.out.println("字母检验："+str);
        for(int i = str.length();--i>=0;)
        {
            int chr = str.charAt(i);
            if((chr>=65 && chr<=90)||(chr>=97 && chr<=122)||chr == 95)
            {
              isCharacter = isCharacter && true ;
            }else{
              isCharacter = isCharacter && false ;  
            }
        }
        return isCharacter;
    }
    /**
     * 判断String是否全为数字
     * @param str
     * @return 
     */
    public static boolean  isNumericString(String str){
        for ( int  i=str.length();--i>=0;){
           int  chr=str.charAt(i);
           if(chr<48 || chr>57)
           {
              return false ;
           }
        }
        return true ;
    }
    
     /**
       * 
      * @method name: stringContainsSpace
      * @Description: TODO(判断一个String对象是否含有空格)
      * @param s 
      * @return boolean   如果含有空格,返回true.没有则返回false。
      * @throws
       */
      public static boolean stringContainsSpace(String s)
      {
        return (s != null) && (s.indexOf(' ') != -1);
      }
      /**
       * 
      * @method name: escapeStringForJava
      * @Description: TODO(替换java当中不能正常识别的字符串)
      * @param s
      * @return String    返回类型替换后的字符串
       */
      public static String escapeStringForJava(String s)
      {
        StringTokenizer st = new StringTokenizer(s, "\"", true);
        StringBuffer sb = new StringBuffer();
        while (st.hasMoreTokens())
        {
          String token = st.nextToken();
          if ("\"".equals(token)) {
            sb.append("\\\"");
          } else {
            sb.append(token);
          }
        }
        return sb.toString();
      }
    /**
     * 
    * @method name: escapeStringForXml
    * @Description: TODO(替换xml不能识别的反斜杠)
    * @param s
    * @return    
    * @return String    返回类型
    * @throws
     */
    public static String escapeStringForXml(String s)
    {
      StringTokenizer st = new StringTokenizer(s, "\"", true);
      StringBuffer sb = new StringBuffer();
      while (st.hasMoreTokens())
      {
        String token = st.nextToken();
        if ("\"".equals(token)) {
          sb.append("&quot;");
        } else {
          sb.append(token);
        }
      }
      return sb.toString();
    }
    
    public static String toCamelCase(String value){
        if(!StringHasValue(value)){
            println("调用方法toCamelCase时传入的参数value没有值！");
            return null;
        }
        StringBuffer camelValue = new StringBuffer();
        boolean isUnderline = false;
        for(int i=0; i<value.length();i++)
        {
            if(i==0 && (value.charAt(i) != '_'))
            {
                camelValue.append(value.substring(0, 1).toUpperCase());
            }else if(value.charAt(i) == '_'){
                isUnderline = true;
            }else if(isUnderline){
                camelValue.append(value.substring(i, i+1).toUpperCase());
                isUnderline = false;
            }else {
                camelValue.append(value.substring(i, i+1).toLowerCase());
            }
        }
        return camelValue.toString();
    }
    
    /**
     * 获取短横线连接的每个单词的首字母缩写
     * @param value
     * @return 
     */
    public static String toAcronym(String value){
        if(!StringHasValue(value)){
            println("调用方法toCamelCase时传入的参数value没有值！");
            return null;
        }
        StringBuffer sbv = new StringBuffer();
        boolean isUnderline = false;
        for(int i=0; i<value.length();i++)
        {
            if(i==0 && (value.charAt(i) != '_'))
            {
                sbv.append(value.substring(0, 1).toLowerCase());
            }else if(value.charAt(i) == '_'){
                isUnderline = true;
            }else if(isUnderline){
                sbv.append(value.substring(i, i+1).toLowerCase());
                isUnderline = false;
            }else {
                continue;
            }
        }
        return sbv.toString();
    }
    
    public static String getBlankString(int size){
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<size;i++)
        {
            sb.append(" ");
        }
        return sb.toString();
    }
    
    /**
     * 根据给定的字符串获取固定宽度的字符串,用于产生sql格式化显示字段
     * @param value 字符串原来的值
     * @param width 想要获取的宽度
     * @param isEnd 是否是最后一个字符串
     * @param isSurround 是否被双引号包围
     * @return  格式化后的字符串
     */
    public static String getAFixedWidthString(String value,
                                                    int width,
                                                boolean isEnd,
                                               boolean isSurround)
    {
        return getAFixedWidthString(value,width,isEnd,8,"\"","+",isSurround);
    }
    /**
     * 根据给定的字符串获取固定宽度的字符串,用于产生sql格式化显示字段
     * @param value 字符串原来的值
     * @param width 想要获取的宽度
     * @param beforeBlankWidth 字符串前面的空格数
     * @param surroundStr 包围字符串的符号，比如说是冒号
     * @param connectStr 字符串连接符号
     * @param isEnd 是否是最后一个字符串
     * @param isSurround 是否被双引号包围
     * @return  格式化后的字符串
     */
    public static String getAFixedWidthString(String value,
                                                   int width,
                                                   Boolean isEnd,
                                                   int beforeBlankWidth,
                                                   String surroundStr,
                                                   String connectStr,
                                                   boolean isSurround){
        StringBuffer sb = new StringBuffer();
        //如果传入的字符串为null,就返回
        if(!StringHasValue(value))
        {
            return sb.toString();
        //如果传入的字符串长度大于给定的长度
        }else if(value.length()>= width){
            sb.append(getBlankString(beforeBlankWidth));
            //如果要包围就包围
            if(isSurround)
            {
                sb.append(surroundStr);
            }
           //添加空格
            sb.append(getBlankString(1));
            //添加值
            sb.append(value);
            //添加空格
            sb.append(getBlankString(1));
            //如果要被包围就包围
            if(isSurround)
            {
                sb.append(surroundStr);
                if(!isEnd){
                     sb.append(connectStr+"\r\n");
                }else{
                    sb.append("\";\r\n");
                }
            //否则换行
            }else{
                sb.append("\r\n");
            }
        //正常情况下
        }else{
            //借用一个局部变量
            StringBuffer pre = new StringBuffer();
            //添加前置空格
            pre.append(getBlankString(beforeBlankWidth));
            //如果要被包围就包围
            if(isSurround)
            {
                pre.append(surroundStr);
            }
            //添加空格
            pre.append(getBlankString(1));
            //添加值
            pre.append(value);
            //获取现有的字符串长度
            int length =  pre.length();
            //获取固定的字符串长度
            int fixed = width - length;
            sb.append(pre);
            sb.append(getBlankString(fixed));
            //如果要被类似的双引号包围
            if(isSurround)
            {
                sb.append(surroundStr);
                //如果没有结束，不添加分号
                if(!isEnd){
                     sb.append(connectStr+"\r\n");
                }else{
                    sb.append(";\r\n");
                }
            }else {
                //如果结束了，添加分号
                if(isEnd){
                    sb.append(";\r\n");
                } 
            }
        }
        return sb.toString();
    }
//    public static void main(String args[])
//    {
//        String a ="_huxing_is_a_person";
//        println(toAcronym(a));
//    }
}
