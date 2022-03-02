package edu.ecnu.Woodpecker.executor.keyword;

import com.jcraft.jsch.Session;
import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.constant.SignConstant;
import edu.ecnu.Woodpecker.executor.Executor;
import edu.ecnu.Woodpecker.executor.GrammarType;
import edu.ecnu.Woodpecker.log.WpLog;
import edu.ecnu.Woodpecker.tools.SysbenchTools;
import edu.ecnu.Woodpecker.util.Util;

public class SysbenchOLTPProcessor extends Executor implements Keyword
{
    public SysbenchOLTPProcessor()
    {

    }

    @Override
    public void handle(String keyword, GrammarType type) throws Exception
    {
        // TODO Auto-generated method stub
        WpLog.recordLog(LogLevelConstant.INFO, "sysbench_oltp: %s", keyword);
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
     * sysbench关键字处理的地方
     * @param keyword Starts with "sysbench_oltp["
     */
    private void handleFirstGrammar(String keyword)
    {
        WpLog.recordLog(LogLevelConstant.INFO, "sysbench_oltp 1级文法");
        String[] parts = keyword.split("\\[|;|]");
        String sessionName = parts[1].trim();
        SysbenchTools sysbenchTools = new SysbenchTools();
        if(sessionName.equals("")){
            Session session = null;
            sysbenchTools.seizeSysbenchOLTP(session, keyword);
        }
        else {
            Session session = (Session)varValueMap.get(sessionName);
            sysbenchTools.seizeSysbenchOLTP(session, keyword);
        }

    }

    /**
     *
     * @param variableName
     * @param keyword Starts with "sysbench_oltp["
     * @throws Exception
     */
    private void handleSecondGrammar(String variableName, String keyword) throws Exception
    {

    }

    /**
     *
     * @param dataType The data type of variable
     * @param variableName
     * @param keyword Starts with "sysbench_oltp["
     * @throws Exception
     */
    private void handleThirdGrammar(String dataType, String variableName, String keyword) throws Exception
    {

    }
}
