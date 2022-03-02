package edu.ecnu.Woodpecker.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

import edu.ecnu.Woodpecker.tools.NmonTools;
import org.apache.commons.cli.CommandLine;

import edu.ecnu.Woodpecker.constant.FileConstant;
import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.constant.SignConstant;
import edu.ecnu.Woodpecker.executor.Executor;
import edu.ecnu.Woodpecker.executor.Parser;
import edu.ecnu.Woodpecker.log.WpLog;
import edu.ecnu.Woodpecker.util.Util;

/**
 * MySQL数据库的操作类
 * 
 */
public class MySQLOperation extends TestController implements DatabaseOperation
{
    /**
     * MySQL的账户和密码
     */
    private static String databaseUser = null;
    private static String databasePassword = null;

    /**
     * MySQL的driver名称，用于区分版本
     */
    private static String Driver = null;


    /**
     * MySQL服务器的IP和端口
     */
    private static String IP = null;
    private static int port;

    /**
     * mysqltest 的可执行文件路径
     */
    private static String mysqlTestPath = null;

    private MySQLOperation(){}
    
    private static class SingletonHolder
    {
        private static MySQLOperation instance = new MySQLOperation();
    }
    
    public static MySQLOperation getInstance()
    {
        return SingletonHolder.instance;
    }

    @Override
    /**
     * 读取MySQL配置文件信息并初始化
     * @param configFilePath MySQL配置文件路径
     */
    public void initialize(String configFilePath)
    {
        WpLog.recordLog(LogLevelConstant.INFO, "Initialize MySQL parameters");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(configFilePath), FileConstant.UTF_8)))
        {
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = br.readLine()) != null)
            {
                line = line.trim();
                // 空行或者注释行
                if (line.matches("^(#{1}.*)|(\\s*+)$"))
                    continue;
                // 获取配置项对应的函数名字
                stringBuilder.append("set").append(line.substring(0, line.indexOf(SignConstant.ASSIGNMENT_CHAR)).trim());
                stringBuilder.setCharAt(3, Character.toUpperCase(stringBuilder.charAt(3)));
                for (int fromIndex = 0; fromIndex < stringBuilder.length();)
                {
                    fromIndex = stringBuilder.indexOf(SignConstant.UNDERLINE_STR, fromIndex);
                    if (fromIndex == -1)
                        break;
                    stringBuilder.deleteCharAt(fromIndex);
                    stringBuilder.setCharAt(fromIndex, Character.toUpperCase(stringBuilder.charAt(fromIndex)));
                }
                String methodName = stringBuilder.toString();
                //System.out.println("methodName是"+methodName);
                // 获取配置项的值
                String confValue = line.substring(line.indexOf(SignConstant.ASSIGNMENT_CHAR) + 1).trim();
                int index = confValue.indexOf(SignConstant.SHARP_CHAR);
                confValue = index == -1 ? confValue : confValue.substring(0, index).trim();
                stringBuilder.delete(0, stringBuilder.length());
                // 使用反射，所有反射调用的set方法要求参数的类型是String
                Method method = MySQLOperation.class.getMethod(methodName, String.class);
                method.invoke(null, confValue);
            }
        }
        catch (Exception e)
        {
            WpLog.recordLog(LogLevelConstant.ERROR, WpLog.getExceptionInfo(e));
            System.err.println("There are errors when initializing parameters");
            exitWoodpecker(1);
        }
    }

    @Override
    public void enter(CommandLine line)
    {
        int caseExecuteNum = 0;
        int succFlag = 0;
        ArrayList<String> failedCase = new ArrayList<>();
        ArrayList<String> parseErrorCase = new ArrayList<>();

        TestController.reportGenerator.appendGeneralInfo("### 测试基本信息");
        TestController.reportGenerator.appendGeneralInfo("- 测试开始时间: " + new Date().toString())
                .appendGeneralInfo("- 数据库类型: MySQL")
                .appendGeneralInfo("- Host: " + getIP())
                .appendGeneralInfo("- Port: " + getPort())
                .appendGeneralInfo("- User: " + getDatabaseUser());

        for (String group : groupSet)
        {
            TestController.currentGroup = group;
            File[] caseFiles = new File(testCasePath + group).listFiles();
            int caseExecuteSucceedNum = 0;
            for (File caseFile : caseFiles)
            {
                if (TestController.exitFlag != null &&
                        TestController.exitFlag.compareAndSet(true, false)) {
                    WpLog.recordLog(LogLevelConstant.INFO, "用户终止当前测试进程");
                    return;
                }
                caseExecuteNum++;
                WpLog.recordTestflow("测试Case ID: "+group+"/"+caseFile.getName());
                WpLog.recordLog(LogLevelConstant.INFO, "开始将"+caseFile.getName()+"解析成中间结果形式");

                //调用Woodpecker解析器，将测试Case解析成中间结果形式。
                File midResult = Parser.parse(caseFile);

                if (midResult == null)
                {
                    // 解析失败，此案例跳过，继续解析 执行下一个测试Case
                    WpLog.recordLog(LogLevelConstant.ERROR, "%s 解析失败，请检查测试Case的语法！跳过并开始解析下一个测试Case", caseFile.getName());
                    parseErrorCase.add(group+"/"+caseFile.getName());
                    continue;
                }

                //中间结果集通过
                boolean retrySucceed = false;
                //这里执行
                WpLog.recordLog(LogLevelConstant.DEBUG, "解析器的值如下：");
                WpLog.recordLog(LogLevelConstant.DEBUG, Parser.getVarValueMap()+"");
                WpLog.recordLog(LogLevelConstant.DEBUG, Parser.getVarTypeMap()+"");
                


                //Woodpecker 执行中间结果集的地方
                boolean isPass = Executor.execute(midResult, Parser.getVarValueMap(), Parser.getVarTypeMap());


                if (!isPass)
                {
                    // 首次运行没通过
                    WpLog.recordLog(LogLevelConstant.ERROR,
                            "%s 首次运行未成功", caseFile.getName());
                    // 重试retryCount次
                    for (int i = 0; i < retryCount; i++)
                    {
                        // 重试执行
                        WpLog.recordTestflow("测试Case ID: "+group+"/"+caseFile.getName());
                        isPass = Executor.execute(midResult, Parser.getVarValueMap(), Parser.getVarTypeMap());
                        if (isPass)
                        {
                            // 第i+1次重试成功
                            WpLog.recordLog(LogLevelConstant.INFO,
                                    "%s execute successfully after retry %d times", caseFile.getName(), i + 1);
                            retrySucceed = true;
                            break;
                        }
                    }

                    if (!retrySucceed)
                    {
                        // 重试retryCount次依旧没有通过，跳过此case
                        WpLog.recordLog(LogLevelConstant.ERROR,
                                "%s execute unsuccessfully after retry %d times", caseFile.getName(), retryCount);
                        // 关闭MySQL，清数据，转储MySQL日志，重启MySQL
                        failedCase.add(group+"/"+caseFile.getName());
                        continue;
                    }
                }
                else
                {
                    // 此案例首次运行通过
                    WpLog.recordLog(LogLevelConstant.INFO,"%s execute successfully first time", caseFile.getName());
                    caseExecuteSucceedNum++;
                }
            }
            if (caseExecuteSucceedNum == caseFiles.length){
                succFlag = 1;
            }

            // 拉取可能存在的监控文件
            NmonTools.generateFiles();
        }

        TestController.reportGenerator.appendGeneralInfo("### 测试 Case 通过情况");
        // 记录日志“此次框架运行结束”
        if (succFlag == 1){
            WpLog.recordLog(LogLevelConstant.INFO, "执行了"+caseExecuteNum+"个Case，本次测试没有错误，Woodpecker停止运行");
            TestController.reportGenerator.appendGeneralInfo("执行了"+caseExecuteNum+"个 Case ，本次测试没有错误");
        }else{
            WpLog.recordLog(LogLevelConstant.ERROR, "执行了"+caseExecuteNum+"个Case，执行错误的case数为"+
            failedCase.size()+"，测试Case语法有误的个数为"+parseErrorCase.size()+"，Woodpecker停止运行");
            TestController.reportGenerator.appendGeneralInfo("执行了 "+caseExecuteNum+" 个 Case ，测试 Case 执行错误的个数为 "+
                    failedCase.size()+" ，测试 Case 语法有误的个数为"+parseErrorCase.size()).appendGeneralInfo("\n");
        
            if (failedCase.size()!=0){
                WpLog.recordLog(LogLevelConstant.ERROR,"执行错误的Case如下：");
                TestController.reportGenerator.appendGeneralInfo("执行错误的 Case 如下: ");
                for (String fail : failedCase) {
                    WpLog.recordLog(LogLevelConstant.ERROR,fail);
                    TestController.reportGenerator.appendGeneralInfo("- " + fail);
                }
            }
            if (parseErrorCase.size()!=0) {
                WpLog.recordLog(LogLevelConstant.ERROR,"存在语法错误的Case如下：");
                TestController.reportGenerator.appendGeneralInfo("存在语法错误的 Case 如下: ");
                for (String fail : parseErrorCase) {
                    WpLog.recordLog(LogLevelConstant.ERROR,fail);
                    TestController.reportGenerator.appendGeneralInfo("- " + fail);
                }
            }
        }
        // 初始化并生成报表
        if(!failedCase.isEmpty()||!parseErrorCase.isEmpty()) {
            WpLog.recordLog(LogLevelConstant.INFO, "生成测试报告:");
            WpLog.generateReport(failedCase, parseErrorCase);//生成测试报告
        }
    }

    public static String getIP()
    {
        return IP;
    }

    public static void setIP(String IP)
    {
        MySQLOperation.IP = IP;
    }

    public static int getPort()
    {
        return port;
    }

    public static void setPort(String port)
    {
        MySQLOperation.port = Integer.parseInt(port);
    }

    public static void setDatabaseUser(String databaseUser)
    {
        MySQLOperation.databaseUser = databaseUser;
    }

    public static void setDatabasePassword(String databasePassword)
    {
        MySQLOperation.databasePassword = databasePassword;
    }
    public static void setDriver(String driver){
        MySQLOperation.Driver = driver;
    }
    public static String getDriver(){
        return MySQLOperation.Driver;
    }

    public static String getDatabaseUser()
    {
        return databaseUser;
    }

    public static String getDatabasePassword()
    {
        return databasePassword;
    }

    public static void setMysqlTestPath(String mysqltestPath) {
        MySQLOperation.mysqlTestPath = mysqltestPath;
    }

    public static String getMysqlTestPath() {
        return MySQLOperation.mysqlTestPath;
    }
}
