package edu.ecnu.Woodpecker.executor.keyword;

import java.io.File;
import java.sql.Connection;

import edu.ecnu.Woodpecker.constant.ConfigConstant;
import edu.ecnu.Woodpecker.constant.FileConstant;
import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.constant.SignConstant;
import edu.ecnu.Woodpecker.controller.TestController;
import edu.ecnu.Woodpecker.executor.Executor;
import edu.ecnu.Woodpecker.executor.GrammarType;
import edu.ecnu.Woodpecker.log.WpLog;
import edu.ecnu.Woodpecker.sql.BasicSQLOperation;
import edu.ecnu.Woodpecker.util.Util;

/**
 *  The class handle CLEAR_DBI keyword
 *  清理数据库实例的处理
 *
 */
public class DBICleaner extends Executor implements Keyword
{
    public DBICleaner()
    {}

    @Override
    public void handle(String keyword, GrammarType type) throws Exception
    {
        WpLog.recordLog(LogLevelConstant.INFO, "CLEAR_DBI: %s", keyword);
        switch (type)
        {
            case FIRST_GRAMMAR:
                String[] parts = Util.removeBlankElement(keyword.split("\\[|]"));
                clearDBI(parts[1]);
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
     * @param keyword Starts with "clear_dbi["
     * @throws Exception
     */
    private void handleSecondGrammar(String variableName, String keyword) throws Exception
    {

    }

    /**
     * 
     * @param dataType The data type of variable
     * @param variableName
     * @param keyword Starts with "clear_dbi["
     * @throws Exception
     */
    private void handleThirdGrammar(String dataType, String variableName, String keyword) throws Exception
    {

    }

    /**
     * 
     * @param DBIName DBI file name without suffix
     * @throws Exception
     */
    private void clearDBI(String DBIName) throws Exception
    {
        String filePath = TestController.getDatabaseInstancePath() + currentGroup + FileConstant.FILE_SEPARATOR
                + DBIName.substring(1, DBIName.length() - 1) + FileConstant.DBI_FILE_SUFFIX;
        Connection conn = ConnectionGetter.getConnection(ConfigConstant.MASTER_LOWER);
        boolean isSucceful = BasicSQLOperation.clearDBI(new File(filePath), FileConstant.UTF_8, conn);
        if (!isSucceful)
            throw new Exception(String.format("Clean database instance unsuccessfully in %s line %d", caseFileName, lineNumber));
    }
}
