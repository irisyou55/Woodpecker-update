package edu.ecnu.Woodpecker.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Stream;

import edu.ecnu.Woodpecker.log.ReportGenerator;
import edu.ecnu.Woodpecker.tools.NmonTools;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;

import edu.ecnu.Woodpecker.constant.CLIParameterConstant;
import edu.ecnu.Woodpecker.constant.ConfigConstant;
import edu.ecnu.Woodpecker.constant.DataValueConstant;
import edu.ecnu.Woodpecker.constant.FileConstant;
import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.constant.SignConstant;
import edu.ecnu.Woodpecker.executor.Parser;
import edu.ecnu.Woodpecker.log.WpLog;
import edu.ecnu.Woodpecker.sql.DbmsBrand;
import edu.ecnu.Woodpecker.tools.SyntaxChecker;
import edu.ecnu.Woodpecker.util.ProxyInfo;
import edu.ecnu.Woodpecker.util.Util;

/**
 * 测试控制器 控制所有脚本文件（测试案例）组别间的执行顺序和组别内的执行顺序
 *
 */
public class TestController
{
    /**
     * 流程配置文件的相对路径，不能更改
     */
    private final static String WORKFLOW_CONFIG_PATH = "./config/Woodpecker.conf";

    /**
     * 解析器生成的中间结果集文件存放路径，不可更改
     */
    private final static String MIDDLE_RESULT_PATH = "./middle_result/";

    /**
     * 数据库名称，全小写，没有空格
     */
    private static DbmsBrand database = null;

    /**
     * 测试环境模块的配置文件路径，路径最后一定有'/'
     */
    protected static String testEnvironmentConfigPath = null;

    /**
     * 所有测试案例存放的路径，路径最后一定有'/'
     */
    protected static String testCasePath = null;

    /**
     * 退出测试标记，每个 case 运行前都会检查一下，exitFlag == true 则直接退出
     */
    public static AtomicBoolean exitFlag;


    /* 理想结果集文件存放的路径，路径最后一定有'/'
     */
    private static String idealResultSetPath = null;

    /*
     * 放监控信息的文件夹
     */
    private static String monitorInfoPath = null;



    /**
     * 数据库实例文件的路径，实例文件的后缀名为 .dbi
     */
    private static String databaseInstancePath = null;

    /**
     * Woodpecker所在机器的IP（当Woodpecker在服务器时此配置项才有用）
     */
    private static String WoodpeckerIP = null;

    /**
     * 测试报告存放路径
     */
    private static String reportPath = null;

    /**
     * 测试组别名
     */
    protected static String case_group_name;

    /**
     * case execute fail
     */
    protected static int retryCount;

    /**
     * Automatic test rounds
     */
    protected static int round;

    public static ReportGenerator reportGenerator;



    /**
     * 存放将执行的测试组别的名称；当前正在执行的案例的组别名称
     */
    protected static Set<String> groupSet = new HashSet<String>(10);
    protected static String currentGroup = null;

    private static DatabaseOperation dbOperation = null;

    /**
     * entry of test controller
     * 添加数据库在这里
     */

    public static void start(CommandLine line) {
        // 初始化参数
        initSysParameter();
        initFlowParameter();
        //用于自定义案例测试的回归测试
        WpLog.recordLog(LogLevelConstant.INFO, "开始自动执行" + round + "遍");
        // 生成与测试案例文件夹一样的目录结构，用于存放中间结果集
        try {
            Parser.geneMidResultDirectory(testCasePath, MIDDLE_RESULT_PATH);
        } catch (Exception e) {
            WpLog.recordLog(LogLevelConstant.ERROR, WpLog.getExceptionInfo(e));
            System.err.println("There are errors when generating directory structure");
            exitWoodpecker(2);
        }

        // 初始化报告文件
        reportGenerator = new ReportGenerator(getReportPath());

        if (database.getBrand().equals(ConfigConstant.MYSQL)) {
            // MySQL
            WpLog.recordLog(LogLevelConstant.INFO, "本次测试的数据库是MySQL");
            dbOperation = MySQLOperation.getInstance();
            dbOperation.initialize(testEnvironmentConfigPath + FileConstant.MYSQL_CONFIG_FILE_NAME);
            for (int i = 0; i < round; i++) {
                WpLog.recordLog(LogLevelConstant.INFO, "执行第" + (i+1) + "遍");
                dbOperation.enter(line);
            }
        } else if (database.getBrand().equals(ConfigConstant.TIDB)) {
            WpLog.recordLog(LogLevelConstant.INFO, "本次测试的数据库是TiDB");
            dbOperation = MySQLOperation.getInstance();
            //读取TiDB的配置文件
            dbOperation.initialize(testEnvironmentConfigPath + FileConstant.TIDB_CONFIG_FILE_NAME);
            for (int i = 0; i < round; i++) {
                WpLog.recordLog(LogLevelConstant.INFO, "执行第" + (i+1) + "遍");
                dbOperation.enter(line);
            }
        }
        else if (database.getBrand().equals(ConfigConstant.POSTGRESQL))
        {
            // PostgreSQL
            WpLog.recordLog(LogLevelConstant.INFO, "DBMS is PostgreSQL");
            dbOperation = PostgreSQLOperation.getInstance();
            dbOperation.initialize(testEnvironmentConfigPath + FileConstant.POSTGRESQL_CONFIG_FILE_NAME);
            for (int i = 0; i < round; i++) {
                WpLog.recordLog(LogLevelConstant.INFO, "执行第" + (i+1) + "遍");
                dbOperation.enter(line);
            }
        }
//        else if (database.getBrand().equals(ConfigConstant.OtherDBMSBrand)){
//            //其他添加的数据库
//            WpLog.recordLog(LogLevelConstant.INFO, "DBMS is xxx");
//            dbOperation = xxxDBOperation.getInstance();
//            dbOperation.initialize(testEnvironmentConfigPath + FileConstant.xxxDB_CONFIG_FILE_NAME);
//        for (int i = 0 ; i < round ; i++) {
//            dbOperation.enter(line);
//          }
//        }
        else
        {
            // wrong dbms type
            WpLog.recordLog(LogLevelConstant.ERROR, "DBMS type in config file is wrong");
            exitWoodpecker(1);
        }

    }
    /**
     * 初始化创建必须的文件夹
     */
    private static void initFile(){
        String[] files = new String[]{"log","middle_result","test_case","config"};
        for(String file : files){
            File createDir = new File(file);
            if(!createDir.exists()){
                createDir.mkdir();
            }
        }
    }
    /**
     * 根据系统配置文件里的内容，初始化系统参数
     */
    private static void initSysParameter()
    {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(WORKFLOW_CONFIG_PATH), FileConstant.UTF_8)))
        {
            WpLog.recordLog(LogLevelConstant.INFO, "初始化Woodpecker的系统参数");
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = br.readLine()) != null)
            {
                line = line.trim();
                // 空行或者注释行
                if (line.matches("^(#{1}.*)|(\\s*+)$"))
                    continue;
                // 匹配到"---------"分隔行，说明系统参数读取结束
                if (line.matches("\\s*[-]+\\s*"))
                    break;
                WpLog.recordLog(LogLevelConstant.INFO, "初始化系统参数: %s", line);
                // 获取配置项对应的函数名字和配置项的值
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
                //System.out.println(methodName);
                String confValue = line.substring(line.indexOf(SignConstant.ASSIGNMENT_CHAR) + 1).trim();
                int index = confValue.indexOf(SignConstant.SHARP_CHAR);
                confValue = index == -1 ? confValue : confValue.substring(0, index).trim();
                stringBuilder.delete(0, stringBuilder.length());
                // 使用反射，所有反射调用的set方法要求参数的类型是String
                Method method = TestController.class.getMethod(methodName, String.class);
                method.invoke(null, confValue);
            }
        }
        catch (Exception e)
        {
            WpLog.recordLog(LogLevelConstant.ERROR, WpLog.getExceptionInfo(e));
            System.err.println("初始化参数时出错，退出Woodpecker");
            exitWoodpecker(3);
        }
        // Initialize some parameters which aren't setted in Woodpecker.conf
        if (WoodpeckerIP == null)
            WoodpeckerIP = "127.0.0.1";
    }

    /**
     * 初始化流程参数 求此次框架运行所需要执行的所有组别的名称，结果存放于类成员变量groupSet中 初始化重试次数
     */
    private static void initFlowParameter()
    {
        WpLog.recordLog(LogLevelConstant.INFO, "初始化工作流程参数(重试次数(retry_count),测试Group(case_group_name)等)");
        // 是否按照默认流程执行，true为默认流程
        boolean defaultOrNot = true;
        // 读取流程配置文件并决定是按照默认流程还是指定流程执行
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(WORKFLOW_CONFIG_PATH), FileConstant.UTF_8));)
        {
            String line = null;
            while ((line = br.readLine()) != null)
            {
                line = line.trim();
                // 匹配到"-----------"分隔行
                if (line.matches("^\\s*+-+\\s*+"))
                    break;
            }
            // 读取到流程参数部分
            while ((line = br.readLine()) != null)
            {
                line = line.trim();
                // 当前读取行是注释或空行
                if (line.matches("^(#{1}.*)|(\\s*+)$"))
                    continue;
                WpLog.recordLog(LogLevelConstant.INFO, "初始化测试流程参数: %s", line);
                // 初始化重试次数
                if (line.startsWith(ConfigConstant.RETRY_COUNT))
                {
                    String[] parts = line.split("=|#");
                    retryCount = Integer.parseInt(parts[1].trim());
                    continue;
                }
                // 自动化回归测试次数
                if (line.startsWith(ConfigConstant.ROUND))
                {
                    String[] parts = line.split("=|#");
                    round = Integer.parseInt(parts[1].trim());
                    continue;
                }
                // 求报告路径
                if (line.startsWith(ConfigConstant.REPORT_PATH))
                {
                    String[] parts = line.split("=");
                    setReportPath(parts[1].trim().substring(1, parts[1].length()-1));
                }
                // 求监控数据存储路径
                if (line.startsWith(ConfigConstant.MONITOR_INFO_PATH))
                {
                    String[] parts = line.split("=");
                    System.out.println("xxx " + parts[1]);
                    setMonitorInfoPath(parts[1].trim().substring(1, parts[1].length()-1));
                }
                // 求测试组别
                if (defaultOrNot && line.startsWith(ConfigConstant.DEFAULT_OR_NOT))
                {
                    int index = line.indexOf(SignConstant.SHARP_CHAR);
                    line = index == -1 ? line : line.substring(0, index).trim();
                    if (line.endsWith(DataValueConstant.TRUE))
                        break;
                    else
                    {
                        defaultOrNot = false;
                        continue;
                    }
                }
                // 不按照默认流程，求指定组别的名字
                if (line.startsWith(ConfigConstant.CASE_GROUP_NAME))
                {
                    line = line.substring(line.indexOf(SignConstant.ASSIGNMENT_CHAR) + 1);
                    int index = line.indexOf(SignConstant.DOUBLE_SHARP);
                    line = index == -1 ? line : line.substring(0, index).trim();
                    case_group_name = line;
                    String[] names = line.split(SignConstant.COMMA_STR);
                    for (String name : names)
                        groupSet.add(name.trim());
                }
            }
        }
        catch (Exception e)
        {
            WpLog.recordLog(LogLevelConstant.ERROR, WpLog.getExceptionInfo(e));
            System.err.println("There are errors when initializing parameters");
            exitWoodpecker(3);
        }

        if (defaultOrNot)
        {
            // 求默认流程，即所有组别的名称
            File[] files = new File(testCasePath).listFiles();
            for (File file : files)
            {
                if (file.isDirectory())
                    groupSet.add(file.getName().trim());
            }
        }
        WpLog.recordLog(LogLevelConstant.INFO, "execute case group: %s", groupSet.toString());
    }

    /**
     * use tools provided by Woodpecker
     * 如果是help，打印出使用方法
     * @param line
     */
    private static void useTools(CommandLine line) throws Exception
    {
        // Check conflict between options
        if (OptionsHasConflict(line))
        {
            WpLog.recordLog(LogLevelConstant.ERROR, "There is conflict between input options");
            exitWoodpecker(1);
        }
        if(line.hasOption(CLIParameterConstant.HELP))
        {
            WoodpeckerCommandLine.printUsage();
            exitWoodpecker(0);
        }

        if (line.hasOption(CLIParameterConstant.SYNTAX_CHECK))
        {
            initializeParameter();
            new SyntaxChecker().check(line.getOptionValue(CLIParameterConstant.SYNTAX_CHECK).trim());
            exitWoodpecker(0);
        }
    }

    /**
     * Check conflict between options, can't ensure that find all conflicts
     *
     * @return true if exist conflicts
     */
    private static boolean OptionsHasConflict(CommandLine line)
    {
        // For multiple usage
        Supplier<Stream<Option>> streamSupplier = () -> Stream.of(line.getOptions());
        // Exclusive option
        if (streamSupplier.get()
                .anyMatch(ele -> ele.getOpt().equals(CLIParameterConstant.HELP)
                        || ele.getOpt().equals(CLIParameterConstant.SYNTAX_CHECK)))
        {
            return streamSupplier.get().count() > 1 ? true : false;
        }
        return false;
    }

    /**
     * Exit Woodpecker framework
     *
     * @param status
     */
    public static void exitWoodpecker(int status)
    {
        WpLog.recordLog(LogLevelConstant.INFO, "-------------stop Woodpecker-------------");
        Thread.currentThread().interrupt();
        if (reportGenerator != null)
            reportGenerator.generatePDF(case_group_name);
    }

    public static void initializeParameter() throws Exception
    {
        initSysParameter();
        initFlowParameter();
    }

    public static String getMonitorInfoPath() {
        if (monitorInfoPath.startsWith("\"")) {
            return monitorInfoPath.substring(1, monitorInfoPath.length() - 1);
        } else {
            return monitorInfoPath;
        }
    }

    public static void setMonitorInfoPath(String path) {
        TestController.monitorInfoPath = path;
    }

    public static DbmsBrand getDatabase()
    {
        return database;
    }

    public static String getTestEnvironmentConfigPath()
    {
        return testEnvironmentConfigPath;
    }

    public static String getReportPath()
    {
        return reportPath;
    }

    public static String getCurrentGroup()
    {
        return currentGroup;
    }

    public static Set<String> getGroupSet()
    {
        return groupSet;
    }

    public static String getDatabaseInstancePath()
    {
        return databaseInstancePath;
    }

    public static String getIdealResultSetPath()
    {
        return idealResultSetPath;
    }

    public static String getWoodpeckerIP()
    {
        return WoodpeckerIP;
    }

    public static void setIdealResultSetPath(String idealResultSetPath)
    {
        if (idealResultSetPath.matches("\".*\""))
            idealResultSetPath = idealResultSetPath.substring(1, idealResultSetPath.length() - 1);
        TestController.idealResultSetPath = idealResultSetPath + FileConstant.FILE_SEPARATOR_CHAR;
    }


    public static void setCurrentGroup(String currentGroup)
    {
        TestController.currentGroup = currentGroup;
    }

    public static void setDatabase(String database)
    {
        TestController.database = DbmsBrand.of(database);
    }

    public static void setTestEnvironmentConfigPath(String testEnvironmentConfigPath)
    {
        if (testEnvironmentConfigPath.matches("\".*\""))
            testEnvironmentConfigPath = testEnvironmentConfigPath.substring(1, testEnvironmentConfigPath.length() - 1);
        TestController.testEnvironmentConfigPath = testEnvironmentConfigPath + FileConstant.FILE_SEPARATOR_CHAR;
    }

    public static void setDatabaseInstancePath(String databaseInstancePath)
    {
        if (databaseInstancePath.matches("\".*\""))
            databaseInstancePath = databaseInstancePath.substring(1, databaseInstancePath.length() - 1);
        TestController.databaseInstancePath = databaseInstancePath + FileConstant.FILE_SEPARATOR_CHAR;
    }

    public static void setTestCasePath(String testCasePath)
    {
        if (testCasePath.matches("\".*\""))
            testCasePath = testCasePath.substring(1, testCasePath.length() - 1);
        TestController.testCasePath = testCasePath + FileConstant.FILE_SEPARATOR_CHAR;
    }


    public static void setWoodpeckerIP(String WoodpeckerIP)
    {
        TestController.WoodpeckerIP = WoodpeckerIP;
    }

    public static void setReportPath(String reportPath)
    {
        if (reportPath.matches("\".*\""))
            reportPath = reportPath.substring(1, reportPath.length() - 1);
        TestController.reportPath = reportPath + FileConstant.FILE_SEPARATOR_CHAR;
    }

    /**
     * Set the value of ProxyInfo class
     *
     * @param input Include all values
     */
    public static void setProxyInfo(String input) throws Exception
    {
        // Index 0 is proxy type, 1 is host, 2 is port, 3 is user, 4 is password
        String[] values = Util.removeBlankElement(input.split(SignConstant.COMMA_STR));
        ProxyInfo.setProxyType(values[0]);
        ProxyInfo.setProxyHost(values[1].substring(1, values[1].length() - 1));
        ProxyInfo.setProxyPort(values[2]);
        ProxyInfo.setUser(values[3]);
        ProxyInfo.setPassword(values[4]);
        ProxyInfo.setGlobalProxyServer();
    }

    /**
     * Woodpecker的入口
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        initFile();
        // use Apache Commons CLI parse parameters
        WoodpeckerCommandLine.setOption(args);
        CommandLine line = WoodpeckerCommandLine.getCommandLine();
        if (line.getOptions().length > 0) {
            useTools(line);
        } else {
            WpLog.recordLog(LogLevelConstant.INFO, "-------------Woodpecker开始运行-------------");
            TestController.start(line);
            exitWoodpecker(0);
        }

    }
}
