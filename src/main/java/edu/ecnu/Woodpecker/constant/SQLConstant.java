/*
 * @Author: your name
 * @Date: 2021-03-01 20:33:33
 * @LastEditTime: 2021-03-01 22:11:48
 * @LastEditors: your name
 * @Description: In User Settings Edit
 * @FilePath: /Woodpecker-209/src/main/java/edu/ecnu/Woodpecker/constant/SQLConstant.java
 */
package edu.ecnu.Woodpecker.constant;

/**
 * 涉及SQL的关键字、操作等常量，包括事务的start commit rollback，存储过程的in out  inout，以及定义了测试数据库的名称为Woodpecker
 *
 */
public final class SQLConstant
{
    // 事务开始和提交
    public final static String START = "start";
    public final static String COMMIT = "commit";
    public final static String ROLLBACK = "rollback";
    // 存储过程参数输入和输出类型
    public final static String IN = "in";
    public final static String OUT = "out";
    public final static String IN_OUT = "inout";
    // MySQL中执行功能测试的数据库名
    public final static String TEST_DB = "Woodpecker";
}
