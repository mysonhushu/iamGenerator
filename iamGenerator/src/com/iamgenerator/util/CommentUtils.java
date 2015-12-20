package com.iamgenerator.util;

import java.util.ArrayList;

/**
 * 注释帮助类
 * @author kado
 */
public class CommentUtils {
    
    /**
     * 生成多行注释当中的某一条单行
     * @param blankSize 空格的数量
     * @param name 标题名称
     * @param value 标题值
     * @param type 生成类型 0 = 开始， 1 = 中间， 2= 结束
     * @return 构造好的单行注释
     */
    public static String getLine(int blankSize,String name,String value)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(StringUtils.getBlankString(blankSize));
        sb.append("*");
        if(StringUtils.StringHasValue(name))
        {
            sb.append(" @" + name+ ": ");
        }
        sb.append(value);
        sb.append("\r\n");
        return sb.toString();
    }
    
    /**
     * 获取单行注释的方法
     * @param blankSize 空格的数量
     * @param comment 注释内容
     * @param isLineFeed 是否需要换行
     * @return 构建好的注释信息
     */
    public static String getLineComment(int blankSize,String comment,boolean isLineFeed){
        if(isLineFeed){
            return StringUtils.getBlankString(blankSize)+"// "+comment+" \r\n";
        }else {
            return StringUtils.getBlankString(blankSize)+"// "+comment+" ";
        }
        
    }
    
    /**
     * 获取单行注释的方法
     * @param blankSize 空格的数量
     * @param comment 注释内容
     * @param isLineFeed 是否需要换行
     * @return 构建好的注释信息
     */
    public static String getLineStarComment(int blankSize,String comment,boolean isLineFeed){
        if(isLineFeed){
            return StringUtils.getBlankString(blankSize)+"/** "+comment+" */\r\n";
        }else {
            return StringUtils.getBlankString(blankSize)+"/** "+comment+" */";
        }
        
    }
    
    /**
     * 获取多行注释的方法
     * @param blankSize 空格的数量
     * @param commentList 多行注释集合
     * @return 构造好的多行注释内容
     */
    public static String getMutilComment(int blankSize, ArrayList<String> commentList){
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<commentList.size(); i++)
        {
            if(i==0)
            {
                sb.append(StringUtils.getBlankString(blankSize));
                sb.append("/*");
                sb.append(commentList.get(i));
                sb.append("\r\n");
            }else {
                sb.append(StringUtils.getBlankString(blankSize));
                sb.append("*");
                sb.append(commentList.get(i));
                sb.append("\r\n");
            }
            sb.append("*/");
            sb.append(StringUtils.getBlankString(blankSize));
        }
        return sb.toString();
    }

    /**
     * 获取文档注释内容的方法
     * @param blankSize 空格数量
     * @param commentList 多行注释的内容
     * @return 构造好的多行注释内容
     */
    public static String getDocumentComment(int blankSize, ArrayList<String> commentList){
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<commentList.size(); i++)
        {
            if(i==0)
            {
                sb.append(StringUtils.getBlankString(blankSize));
                sb.append("/*");
                sb.append(commentList.get(i));
                sb.append("\r\n");
            }else {
                sb.append(getLine(blankSize,null,commentList.get(i)));
//                sb.append(StringUtils.getBlankString(blankSize));
//                sb.append("*");
//                sb.append(commentList.get(i));
//                sb.append("\r\n");
            }
            sb.append(StringUtils.getBlankString(blankSize));
            sb.append("*/");
        }
        return sb.toString();
    }
    
    /**
     * 获取文件注释类的方法
     * @param blankSize 空格数量
     * @param fileName  文件名称
     * @param packageName  包名称
     * @param todo  一句话描述该文件做什么
     * @param author 作者
     * @param date  日期
     * @param version  版本信息
     * @return 构造好的注释内容
     */
    public static String getFileComment(int blankSize,
                                    String fileName,
                                    String packageName,
                                    String todo,
                                    String author,
                                    String date,
                                    String version)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(StringUtils.getBlankString(blankSize));
        sb.append("/**\r\n");
        sb.append(getLine(blankSize,"Title",fileName));
        sb.append(getLine(blankSize,"Package",packageName));
        sb.append(getLine(blankSize,"Description",todo));
        sb.append(getLine(blankSize,"author",author));
        sb.append(getLine(blankSize,"date",DateUtils.getNow()));
        sb.append(getLine(blankSize,"version",version));
        sb.append(StringUtils.getBlankString(blankSize));
        sb.append("*/\r\n");
        return sb.toString();
    }
 
    /**
     * 获取类注释
     * @param blankSize 前置空格数量
     * @param className 类名
     * @param todo   这里用一句话描述这个类的作用
     * @param author 作者
     * @param date 创造时间
     * @return  构造号的类注释内容
     */
    public static String getClassComment(int blankSize,
                                            String className,
                                            String todo,
                                            String author,
                                            String company,
                                            String date)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(StringUtils.getBlankString(blankSize));
        sb.append("/**\r\n");
        sb.append(getLine(blankSize,"ClassName",className));
        sb.append(getLine(blankSize,"Description",todo));
        sb.append(getLine(blankSize,"author",author));
        sb.append(getLine(blankSize,"company",company));
        sb.append(getLine(blankSize,"date",date));
        sb.append(StringUtils.getBlankString(blankSize));
        sb.append("*/\r\n");
        return sb.toString();
    }
    
    public static String getFieldComment(int blankSize, String field,String todo)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(StringUtils.getBlankString(blankSize));
        sb.append("/**\r\n");
        sb.append(getLine(blankSize,"Fields",field));
        sb.append(getLine(blankSize,"todo",todo));
        sb.append(StringUtils.getBlankString(blankSize));
        sb.append("*/\r\n");
        return sb.toString();
    }
 
    /**
     * 构造方法的注释说明字符串
     * @param blankSize 缩进的空格数量
     * @param title 方法名称
     * @param todo 方法的作用
     * @param params 参数列表
     * @param returnType 返回类型
     * @param throwsStr  抛出的异常，如果为null就不抛出.
     * @return 本方法返回的类型
     */
    public static String getMethodComment(int blankSize, 
                                            String title,
                                            String todo,
                                            ArrayList<String> params,
                                            String returnType,
                                            String throwsStr)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(StringUtils.getBlankString(blankSize));
        sb.append("/**\r\n");
        sb.append(getLine(blankSize,"Title",title));
        sb.append(getLine(blankSize,"Description",todo));
        for(int i=0; i<params.size(); i++)
        {
            sb.append(getLine(blankSize,"param",params.get(i)));   
        }
        sb.append(getLine(blankSize,"return",returnType));
        if(StringUtils.StringHasValue(throwsStr))
        {
            sb.append(getLine(blankSize,"throws",throwsStr));    
        }
        sb.append(StringUtils.getBlankString(blankSize));
        sb.append("*/\r\n");
        return sb.toString();
    }
}
