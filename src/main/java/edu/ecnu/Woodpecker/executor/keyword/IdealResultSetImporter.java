package edu.ecnu.Woodpecker.executor.keyword;

import java.io.File;

import edu.ecnu.Woodpecker.constant.DataType;
import edu.ecnu.Woodpecker.constant.FileConstant;
import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.constant.SignConstant;
import edu.ecnu.Woodpecker.controller.TestController;
import edu.ecnu.Woodpecker.executor.Executor;
import edu.ecnu.Woodpecker.executor.GrammarType;
import edu.ecnu.Woodpecker.log.WpLog;
import edu.ecnu.Woodpecker.sql.IdealResultSet;
import edu.ecnu.Woodpecker.util.Util;

public class IdealResultSetImporter extends Executor implements Keyword
{
    public IdealResultSetImporter()
    {}

    @Override
    public void handle(String keyword, GrammarType type) throws Exception
    {
        WpLog.recordLog(LogLevelConstant.INFO, "IMPORT_IRS: %s", keyword);
        switch (type)
        {
            case FIRST_GRAMMAR:
                String[] parts = Util.removeBlankElement(keyword.split("\\[|]"));
                String[] variablePosition = Util.removeBlankElement(parts[2].split(SignConstant.COMMA_STR));
                importIRS(parts[1], variablePosition);
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
     * @param keyword Starts with "import_irs["
     * @throws Exception
     */
    private void handleSecondGrammar(String variableName, String keyword) throws Exception
    {

    }

    /**
     * 
     * @param dataType The data type of variable
     * @param variableName
     * @param keyword Starts with "import_irs["
     * @throws Exception
     */
    private void handleThirdGrammar(String dataType, String variableName, String keyword) throws Exception
    {

    }

    /**
     * 
     * @param variablePositions 元素是irs变量及其位置的字符串数组，变量和位置间以空格分隔
     * @throws Exception 用于向execute函数反馈执行是否成功
     */
    private void importIRS(String fileName, String[] variablePositions) throws Exception
    {
        // Build source file path
        String filePath = TestController.getIdealResultSetPath() + currentGroup + FileConstant.FILE_SEPARATOR
                + fileName.substring(1, fileName.length() - 1) + FileConstant.RESULT_FILE_SUFFIX;

        IdealResultSet[] idealResultSets = new IdealResultSet[variablePositions.length];
        int[] positions = new int[variablePositions.length];
        for (int i = 0; i < variablePositions.length; i++)
        {
            String[] variablePosition = variablePositions[i].split("\\s");
            String variable = variablePosition[0].trim();
            positions[i] = Integer.parseInt(variablePosition[1].trim());
            idealResultSets[i] = new IdealResultSet();
            // Set data type of an ideal result set
            String[] dataTypesStrs = Util.removeBlankElement(varTypeMap.get(variable).split("IdealResultSet|<|>|,"));
            //从写的测试CASE中解析IdealResultSet<int,int>
            // for (String test : dataTypesStrs)
            //     System.out.println(test);
            DataType[] dataTypes = new DataType[dataTypesStrs.length];
            for (int j = 0; j < dataTypesStrs.length; j++)
                dataTypes[j] = DataType.of(dataTypesStrs[j]);
            idealResultSets[i].setDataTypes(dataTypes);
        }
        // Import result set
        IdealResultSet.importIRS(new File(filePath), idealResultSets, positions);
        // Put value into variable table
        for (int i = 0; i < variablePositions.length; i++)
        {
            String variable = variablePositions[i].split("\\s")[0].trim();
            varValueMap.put(variable, idealResultSets[i]);
        }
    }
}
