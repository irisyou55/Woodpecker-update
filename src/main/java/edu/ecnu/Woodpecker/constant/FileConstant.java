package edu.ecnu.Woodpecker.constant;

/**
 * 文件常量类
 * 
 */
public final class FileConstant
{
    // 结果集文件后缀
    public final static String RESULT_FILE_SUFFIX = ".result";
    // DBI文件名后缀
    public final static String DBI_FILE_SUFFIX = ".dbi";
    // 目录分隔符
    public final static String FILE_SEPARATOR = "/";
    public final static char FILE_SEPARATOR_CHAR = '/';
    // 案例文件后缀
    public final static String CASE_FILE_SUFFIX = ".case";
    // 中间结果集文件后缀
    public final static String MIDDLE_RESULT_FILE_SUFFIX = ".mr";
    // 性能结果文件后缀
    public final static String PERFORMANCE_RESULT_FILE_SUFFIX = ".pr";
    // java jar文件后缀
    public final static String JAR_FILE_SUFFIX = ".jar";
    // UTF-8字符集
    public final static String UTF_8 = "utf-8";
    // 配置文件后缀
    public final static String CONFIG_FILE_SUFFIX = ".conf";
    // 负载配置文件后缀
    public final static String WORKLOAD_CONFIG_FILE_SUFFIX = ".wlconf";

    // MySQL配置文件名
    public final static String MYSQL_CONFIG_FILE_NAME = "MySQL_configuration.conf";
    // TiDB配置文件名
    public static final String TIDB_CONFIG_FILE_NAME = "TiDB_configuration.conf";
    // PostgreSQL配置文件名
    public final static String POSTGRESQL_CONFIG_FILE_NAME = "PostgreSQL_configuration.conf";

    // 第三方测试工具配置
    public static final String TOOLS_CONFIG_FILE_NAME = "tools_configuration.conf";

    // SyntaxChecker中间结果集文件路径
    public final static String SYNTAX_CHECKER_OUTPUT_PATH = "./middle_result/check.mr";
    // log4j 配置文件路径
    public final static String LOG4J_CONFIG_PATH = "./config/log4j.properties";
    // 默认测试环境配置路径
    public final static String DEF_TEST_ENV_CONF_PATH = "./config/";
    // 默认的测试Case路径
    public final static String DEF_TEST_CASE_PATH = "./test_case/";
    // 默认的理想结果集路径
    public final static String DEF_IDE_RES_PATH = "./ideal_result_set";
    // 默认数据库实例路径
    public final static String DEF_DB_INST_PATH = "./database_instance/";
    // 默认压力测试目录
    // public final static String DEF_STRESS_TEST_DIR = "./stress_test/"; //旧版压力测试的目录
    public final static String DEF_STRESS_TEST_DIR = "./st/"; //新版压力测试 性能测试的配置文件目录
    // 默认的IP地址
    public final static String DEF_IP = "localhost";
    // 默认的测试Report路径
    public final static String DEF_REPORT_PATH = "./";
    // 默认的测试重新测试次数
    public final static int DEF_RETRY_COUNT = 2;
    
    // default time for sending result from workload machine to dispatcher
    // 负载机向调度器发送结果的默认时间
    public final static int DEF_SEND_TIME = 8;
    
    // default testing directory on server is "/home/username/performancetest/st/", st includes config and nohup.out
    public final static String DEF_STRESS_TEST_DIR_ON_SERVER = "performancetest/st/";
    
    // default global confuration file in st is "config"
    public final static String DEF_GLOBAL_CONF_DIR_NAME_ON_SERVER = "config";
    
    // 默认压力测试配置
    // public final static String DEF_STRESS_TEST_CONFIG_FILE = "default";
    public final static String DEF_STRESS_TEST_CONFIG_FILE = "mysql";
//    public final static String DEF_STRESS_TEST_CONFIG_FILE = "postgresql";
    
    // default nullRatio for all data types
    public final static float NULL_RATIO = 0;
    
    //default cardinity for int(integer) type
    public final static int CARDINALITY = 30000;
    
    // default minValue, maxValue for all numeric types
    public final static double MIN_VALUE = -100000000;
    public final static double MAX_VALUE = 100000000;
    
    // default minLength, maxLength, seedNumber for all character types
    public final static int MIN_LENGTH = 1;
    public final static int MAX_LENGTH = 50;
    public final static int SEEDS_NUMBER = 10000;
    

    // default time unit(microsecond)
    public final static long MILLISECOND = 1000L;
    // default time unit for (millisecond)
    public final static long MICROSECOND = 1000000L;
    // default time unit(nanosecond)
    public final static long NANOSECOND = 1000000000L;
    
    // default mu in Gaussian distribution
    public final static double NORMAL_DIST_MU = 0;
    // default Gaussian distribution range
    public final static int NORMAL_DIST_RANGE = 3;
    
    // default Gaussian distribution array size
    public final static int NORMAL_DIST_LIST_SIZE = 10;
    // default Zipf distribution array size
    public final static int ZIPF_DIST_LIST_SIZE = 10;


    public final static char WIN_LINE_FEED_CHAR = '\n';
    public final static String WIN_LINE_FEED_STR = "\n";
    public final static String LINUX_LINE_FEED = "\r\n";
    public final static char MAC_LINE_FEED = '\r';
    // tab
    public final static char TAB_CHAR = '\t';
}
