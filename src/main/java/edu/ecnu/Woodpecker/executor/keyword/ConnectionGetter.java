package edu.ecnu.Woodpecker.executor.keyword;

import java.sql.Connection;

import edu.ecnu.Woodpecker.constant.ConfigConstant;
import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.constant.SQLConstant;
import edu.ecnu.Woodpecker.constant.SignConstant;
import edu.ecnu.Woodpecker.controller.MySQLOperation;
import edu.ecnu.Woodpecker.controller.PostgreSQLOperation;
import edu.ecnu.Woodpecker.controller.TestController;

import edu.ecnu.Woodpecker.executor.Executor;
import edu.ecnu.Woodpecker.executor.GrammarType;
import edu.ecnu.Woodpecker.log.WpLog;
import edu.ecnu.Woodpecker.sql.BasicSQLOperation;
import edu.ecnu.Woodpecker.sql.DbmsBrand;
import edu.ecnu.Woodpecker.util.Util;

/**
 * The class handles get_conn keyword
 * 关键字的执行类全部都是继承Executor
 *
 */
public class ConnectionGetter extends Executor implements Keyword
{
    public ConnectionGetter()
    {}

    @Override
    public void handle(String keyword, GrammarType type) throws Exception
    {
        switch (type)
        {
            case FIRST_GRAMMAR:
                WpLog.recordLog(LogLevelConstant.WARN, "Need variable to storage connection");
                break;
            case SECOND_GRAMMAR:
                WpLog.recordLog(LogLevelConstant.INFO, "GET_CONN: %s", keyword);
                // Index 0 is variable name, 1 is keyword
                String[] keywordParts = Util.removeBlankElement(keyword.split(SignConstant.ASSIGNMENT_STR, 2));
                handleSecondGrammar(keywordParts[0], keywordParts[1]);
                break;
            case THIRD_GRAMMAR:
                WpLog.recordLog(LogLevelConstant.INFO, "GET_CONN: %s", keyword);
                // Index 0 is data type and variable name, 1 is keyword
                keywordParts = Util.removeBlankElement(keyword.split(SignConstant.ASSIGNMENT_STR, 2));
                //keywordParts[0]是Connection conn2
                //keywordParts[1]是get_conn[master]
                // Index 0 is data type, 1 is variable name
                String[] decVar = Util.removeBlankElement(keywordParts[0].split("\\s"));
                //decVar[0]是Connection
                //decVar[1]是conn2
                handleThirdGrammar(decVar[0], decVar[1], keywordParts[1]);
                break;
            default:
                throw new Exception("Grammar error");
        }
    }

    /**
     * 
     * @param variableName  The name of variable which will be assigned by keyword
     * @param keyword   Doesn't include variableName
     */
    private void handleSecondGrammar(String variableName, String keyword) throws Exception
    {
        if (!varValueMap.containsKey(variableName))
        {
            WpLog.recordLog(LogLevelConstant.ERROR,
                    "Use variable without declaring in %s line %d", caseFileName, lineNumber);
            throw new Exception();
        }
        String category = Util.removeBlankElement(keyword.split("\\[|]"))[1];
        varValueMap.put(variableName, getConnection(category));
    }

    /**
     * 处理get_conn，把varValueMap中放入测试Case变量名称,比如conn2和得到的数据库连接引用，比如connection
     * varValueMap是继承父类Executor的静态变量，若子类没有重新定义静态变量，子类和父类共享静态变量
     * @param dataType  The data type of variable
     * @param variableName
     * @param keyword
     */
    private void handleThirdGrammar(String dataType, String variableName, String keyword) throws Exception
    {
        String category = Util.removeBlankElement(keyword.split("\\[|]"))[1];//master
        WpLog.recordLog(LogLevelConstant.INFO,"把测试Case变量名称"+variableName+"和得到的真实数据库连接引用放入varValueMap");
        varValueMap.put(variableName, getConnection(category));
    }
    
    /**
     * @param MSCategory The role of MS in cluster
     * @return JDBC connection which establish in specified MS
     * @throws Exception 
     */
    public static Connection getConnection(String MSCategory) throws Exception
    {
        String IP = null;
        String port = null;
        String DBName = SQLConstant.TEST_DB;//在数据库创建的用于测试的数据库名称：Woodpecker
        String userName = null;
        String password = null;
        DbmsBrand brand = null;
        String database = TestController.getDatabase().getBrand();
        switch (database)
        {
            case ConfigConstant.MYSQL:
                brand = DbmsBrand.MYSQL;
                IP = MySQLOperation.getIP();
                port = String.valueOf(MySQLOperation.getPort());
                userName = MySQLOperation.getDatabaseUser();
                password = MySQLOperation.getDatabasePassword();
                break;

            case ConfigConstant.TIDB:
                brand = DbmsBrand.TIDB;
                IP = MySQLOperation.getIP();
                port = String.valueOf(MySQLOperation.getPort());
                userName = MySQLOperation.getDatabaseUser();
                password = MySQLOperation.getDatabasePassword();
                break;
            case ConfigConstant.POSTGRESQL:
                brand = DbmsBrand.POSTGRESQL;
                IP =  PostgreSQLOperation.getIP();
                port = String.valueOf( PostgreSQLOperation.getPort());
                userName =  PostgreSQLOperation.getDatabaseUser();
                password =  PostgreSQLOperation.getDatabasePassword();
                break;
        }
        return connection = BasicSQLOperation.getConnection(IP, port, DBName, userName, password, brand);
    }
}
