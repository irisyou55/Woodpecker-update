/*
 * @Author: your name
 * @Date: 2021-03-01 20:33:33
 * @LastEditTime: 2021-03-03 09:57:06
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: /Woodpecker-209/src/main/java/edu/ecnu/Woodpecker/controller/WoodpeckerCommandLine.java
 */
package edu.ecnu.Woodpecker.controller;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import edu.ecnu.Woodpecker.constant.CLIParameterConstant;
import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.log.WpLog;

/**
 * Woodpecker command line class
 * 
 */
public class WoodpeckerCommandLine
{
    private static Options options = null;
    private static CommandLineParser parser = null; 
    private static CommandLine commandLine = null;
    
    public static void setOption(String[] args)
    {
        // use Apache Commons CLI to parse parameters
        options = new Options();
        options.addOption(CLIParameterConstant.HELP, CLIParameterConstant.HELP_DESC);
        options.addOption(CLIParameterConstant.SYNTAX_CHECK, true, CLIParameterConstant.SYNTAX_CHECK_DESC);

        // parse parameters
        parser = new DefaultParser();
        try
        {
            commandLine = parser.parse(options, args);
            WpLog.recordLog(LogLevelConstant.INFO, "开始执行CommandLine");
        }
        catch (ParseException e)
        {
            WpLog.recordLog(LogLevelConstant.ERROR, "解析命令行参数失败, 原因: %s", e.getMessage());
            System.exit(1);
        }
    }
    
    public static void printUsage()
    {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(CLIParameterConstant.USAGE, options);
    }
    
    public static CommandLine getCommandLine()
    {
        return commandLine;
    }
}
