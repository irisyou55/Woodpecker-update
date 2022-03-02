/*
 * @Author: silvaxiang
 * @Date: 2021-03-01 20:33:33
 * @LastEditTime: 2021-03-03 17:24:50
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: /Woodpecker-209/src/main/java/edu/ecnu/Woodpecker/executor/keyword/StatementGetter.java
 */
package edu.ecnu.Woodpecker.executor.keyword;

import java.sql.Connection;
import java.sql.Statement;

import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.constant.SignConstant;
import edu.ecnu.Woodpecker.executor.Executor;
import edu.ecnu.Woodpecker.executor.GrammarType;
import edu.ecnu.Woodpecker.log.WpLog;
import edu.ecnu.Woodpecker.sql.BasicSQLOperation;
import edu.ecnu.Woodpecker.util.Util;

/**
 * The class handle get_stat keyword
 *
 */
public class StatementGetter extends Executor implements Keyword
{
    public StatementGetter()
    {}

    @Override
    public void handle(String keyword, GrammarType type) throws Exception
    {
        switch (type)
        {
        case FIRST_GRAMMAR:
            WpLog.recordLog(LogLevelConstant.WARN, "Need variable to storage statement");
            break;
        case SECOND_GRAMMAR:
            WpLog.recordLog(LogLevelConstant.INFO, "GET_STAT: %s", keyword);
            // Index 0 is variable name, 1 is keyword
            String[] keywordParts = Util.removeBlankElement(keyword.split(SignConstant.ASSIGNMENT_STR, 2));
            handleSecondGrammar(keywordParts[0], keywordParts[1]);
            break;
        case THIRD_GRAMMAR:
            WpLog.recordLog(LogLevelConstant.INFO, "GET_STAT: %s", keyword);
            // index 0是数据类型和变量名，1是关键字
            //keywordParts[0]="Statement stmt" keywordParts[1]="get_stat[conn]"
            keywordParts = Util.removeBlankElement(keyword.split(SignConstant.ASSIGNMENT_STR, 2));
            // 索引0为数据类型，1为变量名
            //decVar[0]="Statement" decVar[1]="stmt"
            String[] decVar = Util.removeBlankElement(keywordParts[0].split("\\s"));
            handleThirdGrammar(decVar[0], decVar[1], keywordParts[1]);
            break;
        default:
            throw new Exception("Grammar error");
        }
    }

    /**
     * 
     * @param variableName
     * @param keyword Doesn't include variableName
     * @throws Exception
     */
    private void handleSecondGrammar(String variableName, String keyword) throws Exception
    {
        if (!varValueMap.containsKey(variableName))
        {
            WpLog.recordLog(LogLevelConstant.ERROR, "Use variable without declaring in %s line %d", caseFileName, lineNumber);
            throw new Exception();
        }
        String connectionName = Util.removeBlankElement(keyword.split("\\[|]"))[1];
        //varValueMap.put(variableName, getStatement(connectionName));  update for remove conn and stat in SQL
        varValueMap.put(variableName, getStatement(connectionName));

    }

    /**
     * 
     * @param dataType The data type of variable
     * @param variableName
     * @param keyword
     * @throws Exception
     */
    private void handleThirdGrammar(String dataType, String variableName, String keyword) throws Exception
    {
        String connectionName = Util.removeBlankElement(keyword.split("\\[|]"))[1];
      //varValueMap.put(variableName, getStatement(connectionName)); update for remove conn and stat in SQL
        //System.out.println(variableName+" "+connectionName);
        WpLog.recordLog(LogLevelConstant.INFO,"把测试Case变量名称"+variableName+"和得到的真实数据库Statement引用放入varValueMap");
        varValueMap.put(variableName, getStatement(connectionName));
    }

    /**
     * 
     * @param connectionName The name of JDBC connection
     * @return The statement from connection
     * @throws Exception
     */
    private Statement getStatement(String connectionName) throws Exception
    {
        //从已有的varValuemap中获取对应的数据库连接
        Connection conn = (Connection) varValueMap.get(connectionName);
        return BasicSQLOperation.getStatement(conn);//返回该数据库连接创建的Statement
    }
}
