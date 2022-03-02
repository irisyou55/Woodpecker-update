package edu.ecnu.Woodpecker.executor.keyword;

import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.constant.SignConstant;
import edu.ecnu.Woodpecker.executor.Executor;
import edu.ecnu.Woodpecker.executor.GrammarType;
import edu.ecnu.Woodpecker.log.WpLog;
import edu.ecnu.Woodpecker.tools.TouchstoneTools;
import edu.ecnu.Woodpecker.util.Util;

/**
 * @author Youshuhong
 * @create 2021/11/16 下午1:23
 */
public class TouchstoneProcessor extends Executor implements Keyword {

    public TouchstoneProcessor(){

    }

    @Override
    public void handle(String keyword, GrammarType type) throws Exception{
        WpLog.recordLog(LogLevelConstant.INFO, "TOUCHSTONE: %s", keyword);
        switch (type)
        {
            case FIRST_GRAMMAR:
                handleFirstGrammar(keyword);
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
     * @param keyword Starts with "touchstone["
     */
    private void handleFirstGrammar(String keyword) throws Exception {
        String[] parts = Util.removeBlankElement(keyword.split("\\[|;|]"));
        Integer round = Integer.parseInt(parts[2]);

        TouchstoneTools touchstoneTools = new TouchstoneTools();

        for(int i=0; i<round; i++) {
            int index = i+1;
            WpLog.recordLog(LogLevelConstant.INFO, "自动执行第"+index+"遍");
            touchstoneTools.SeizeTouchstone(keyword);
        }

    }

    /**
     *
     * @param variableName
     * @param keyword Starts with "touchstone["
     * @throws Exception
     */
    private void handleSecondGrammar(String variableName, String keyword) throws Exception
    {

    }

    /**
     *
     * @param dataType The data type of variable
     * @param variableName
     * @param keyword Starts with "touchstone["
     * @throws Exception
     */
    private void handleThirdGrammar(String dataType, String variableName, String keyword) throws Exception
    {

    }


}
