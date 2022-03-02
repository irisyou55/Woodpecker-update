package edu.ecnu.Woodpecker.executor.keyword;

import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.constant.SignConstant;
import edu.ecnu.Woodpecker.executor.Executor;
import edu.ecnu.Woodpecker.executor.GrammarType;
import edu.ecnu.Woodpecker.log.WpLog;
import edu.ecnu.Woodpecker.util.Util;

public class CalculateProcessor extends Executor implements Keyword
{
    public CalculateProcessor()
    {}

    @Override
    public void handle(String keyword, GrammarType type) throws Exception
    {
        WpLog.recordLog(LogLevelConstant.INFO, "CAL: %s", keyword);
        switch (type)
        {
            case FIRST_GRAMMAR:
                calculate(keyword);
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
     * @param keyword Starts with "CAL["
     * @throws Exception
     */
    private void handleSecondGrammar(String variableName, String keyword) throws Exception
    {

    }

    /**
     * 
     * @param dataType The data type of variable
     * @param variableName
     * @param keyword Starts with "CAL["
     * @throws Exception
     */
    private void handleThirdGrammar(String dataType, String variableName, String keyword) throws Exception
    {

    }

    /**
     * 
     * @param input The whole line in middle result except line number and grammar type
     */
    private static void calculate(String input) throws Exception
    {
        String expression = input.substring(4, input.length() - 1).trim();
        int assignIndex = expression.indexOf(SignConstant.ASSIGNMENT_CHAR);
        String targetVariable = expression.substring(0, assignIndex).trim();
        if (!varValueMap.containsKey(targetVariable))
            throw new Exception("undefined variable in " + input);
        varValueMap.put(targetVariable, calExpression(expression.substring(assignIndex + 1)));
    }
}
