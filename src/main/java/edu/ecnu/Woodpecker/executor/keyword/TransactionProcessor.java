package edu.ecnu.Woodpecker.executor.keyword;

import java.sql.Connection;

import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.constant.SignConstant;
import edu.ecnu.Woodpecker.executor.Executor;
import edu.ecnu.Woodpecker.executor.GrammarType;
import edu.ecnu.Woodpecker.log.WpLog;
import edu.ecnu.Woodpecker.sql.BasicSQLOperation;
import edu.ecnu.Woodpecker.sql.TransactionOperator;
import edu.ecnu.Woodpecker.util.Util;

/**
 * The class handle tx keyword
 * 事务关键字的处理tx
 */
public class TransactionProcessor extends Executor implements Keyword
{
    public TransactionProcessor()
    {}

    @Override
    public void handle(String keyword, GrammarType type) throws Exception
    {
        WpLog.recordLog(LogLevelConstant.INFO, "TX: %s", keyword);
        switch (type)
        {
        case FIRST_GRAMMAR:
            // Index 0 is "tx", 1 is connection name, 2 is transaction operator
            String[] parts = Util.removeBlankElement(keyword.split("\\[|;|]"));
            executeTransaction(parts[1], parts[2]);
            break;
        case SECOND_GRAMMAR:
            // Index 0 is variable name, 1 is keyword
            String[] keywordParts = Util.removeBlankElement(keyword.split(SignConstant.ASSIGNMENT_STR, 2));
            handleSecondGrammar(keywordParts[0], keywordParts[1]);
            break;
        case THIRD_GRAMMAR:
            // Index 0 is data type and variable name, 1 is keyword
            keywordParts = Util.removeBlankElement(keyword.split(SignConstant.ASSIGNMENT_STR, 2));
            // Index 0 is data type, 1 is variable name
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
     * @param keyword Starts with "tx["
     * @throws Exception
     */
    private void handleSecondGrammar(String variableName, String keyword) throws Exception
    {

    }

    /**
     * 
     * @param dataType The data type of variable
     * @param variableName
     * @param keyword Starts with "tx["
     * @throws Exception
     */
    private void handleThirdGrammar(String dataType, String variableName, String keyword) throws Exception
    {

    }

    /**
     * 
     * @param connectionName
     * @param operator Transaction operator, include "start", "commit", "rollback"
     * @throws Exception
     */
    private void executeTransaction(String connectionName, String operator) throws Exception
    {
        //在对应的数据库连接中跑事务
        BasicSQLOperation.runTransaction((Connection) varValueMap.get(connectionName), TransactionOperator.valueOf(operator.toUpperCase()));
    }
}
