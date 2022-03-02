package edu.ecnu.Woodpecker.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import edu.ecnu.Woodpecker.constant.FileConstant;
import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.constant.SignConstant;
import edu.ecnu.Woodpecker.log.WpLog;
/**
 * @author : Youshuhong
 * @create : 2021/12/16 13:30
 */
public interface Initializable
{
    /**
     * Initialize PostgreSQL parameters
     * @param <T>
     * @param targetObject
     * @param configFilePath
     */
    default <T> void initialize(T targetObject, String configFilePath) throws Exception
    {
        WpLog.recordLog(LogLevelConstant.INFO, "start to read %s", configFilePath);
        File confFile = new File(configFilePath);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(confFile), FileConstant.UTF_8)))
        {
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null)
            {
                // Empty line or annotation line
                if (line.matches("(\\s*#.*)|(\\s*)"))
                    continue;
                WpLog.recordLog(LogLevelConstant.INFO, "init parameter: %s", line);
                // Get the function name corresponding to config item
                stringBuilder.append("set").append(line.substring(0, line.indexOf(SignConstant.ASSIGNMENT_CHAR)).trim());
                stringBuilder.setCharAt(3, Character.toUpperCase(stringBuilder.charAt(3)));
                for (int fromIndex = 0; fromIndex < stringBuilder.length();)
                {
                    fromIndex = stringBuilder.indexOf(SignConstant.UNDERLINE_STR, fromIndex);
                    if (fromIndex == -1)
                        break;
                    stringBuilder.deleteCharAt(fromIndex);
                    stringBuilder.setCharAt(fromIndex, Character.toUpperCase(stringBuilder.charAt(fromIndex)));
                }
                String methodName = stringBuilder.toString();
                // Get config value
                String confValue = line.substring(line.indexOf(SignConstant.ASSIGNMENT_CHAR) + 1).trim();
                int index = confValue.indexOf(SignConstant.SHARP_CHAR);
                confValue = index == -1 ? confValue : confValue.substring(0, index).trim();
                stringBuilder.delete(0, stringBuilder.length());
                // Use reflect
                Method method = targetObject.getClass().getMethod(methodName, String.class);
                method.invoke(targetObject, confValue);
            }
        }
        catch (Exception e)
        {
            throw e;
        }
    }

}
