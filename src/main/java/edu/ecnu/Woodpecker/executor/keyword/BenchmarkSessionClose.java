package edu.ecnu.Woodpecker.executor.keyword;

import com.jcraft.jsch.Session;
import edu.ecnu.Woodpecker.constant.SignConstant;
import edu.ecnu.Woodpecker.executor.Executor;
import edu.ecnu.Woodpecker.executor.GrammarType;
import edu.ecnu.Woodpecker.util.Util;

public class BenchmarkSessionClose extends Executor implements Keyword{
    public BenchmarkSessionClose(){}

    @Override
    public void handle(String keyword, GrammarType type) throws Exception
    {
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
    private void handleFirstGrammar(String keyword)
    {
        String[] parts = Util.removeBlankElement(keyword.split("\\[|;|]"));
        Session session = (Session) varValueMap.get(parts[1]);
        if (session != null && session.isConnected())
            session.disconnect();
    }


    private void handleSecondGrammar(String variableName, String keyword) throws Exception
    {

    }

    private void handleThirdGrammar(String dataType, String variableName, String keyword) throws Exception
    {

    }

}
