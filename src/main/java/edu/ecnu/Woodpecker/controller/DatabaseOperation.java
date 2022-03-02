package edu.ecnu.Woodpecker.controller;

import org.apache.commons.cli.CommandLine;
/**
 * @description: 数据库操作的接口,想测试什么数据库，就实现对应的接口
 */
public interface DatabaseOperation
{
    public void initialize(String configFilePath);
    
    public void enter(CommandLine line);
}
