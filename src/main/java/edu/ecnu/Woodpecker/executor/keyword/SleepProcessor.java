package edu.ecnu.Woodpecker.executor.keyword;

import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.constant.SignConstant;
import edu.ecnu.Woodpecker.executor.Executor;
import edu.ecnu.Woodpecker.executor.GrammarType;
import edu.ecnu.Woodpecker.log.WpLog;
import edu.ecnu.Woodpecker.systemfunction.Shell;
import edu.ecnu.Woodpecker.util.Util;

/**
 * The class handle SLEEP keyword
 *
 */
public class SleepProcessor extends Executor implements Keyword
{
    public SleepProcessor()
    {}

    @Override
    public void handle(String keyword, GrammarType type) throws Exception
    {
        WpLog.recordLog(LogLevelConstant.INFO, "SLEEP: %s", keyword);
        switch (type)
        {
        case FIRST_GRAMMAR:
            String[] parts = Util.removeBlankElement(keyword.split("\\[|;|]"));
            executeSleep(Integer.parseInt(parts[1]), parts[2]);
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
     * @param keyword Starts with "sleep["
     * @throws Exception
     */
    private void handleSecondGrammar(String variableName, String keyword) throws Exception
    {

    }

    /**
     * 
     * @param dataType The data type of variable
     * @param variableName
     * @param keyword Starts with "sleep["
     * @throws Exception
     */
    private void handleThirdGrammar(String dataType, String variableName, String keyword) throws Exception
    {

    }

    /**
     * 
     * @param time Integer
     * @param timeUnit Second, minute, hour etc
     * @throws Exception
     */
    private void executeSleep(int time, String timeUnit) throws Exception
    {
        Shell.sleep(time, timeUnit);
    }
}
