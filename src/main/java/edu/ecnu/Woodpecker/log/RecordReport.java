package edu.ecnu.Woodpecker.log;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import edu.ecnu.Woodpecker.constant.FileConstant;
import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.controller.TestController;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 生成case_report.md报告
 */
public class RecordReport
{
    /**
     * 生成报告
     */
    public static void recordException(ArrayList<String> failedCase, ArrayList<String> parseErrorCase){

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        Map<String,List<String>> CaseMap = new HashMap<String,List<String>>();
        String filename = "case_report" + formatter.format(date);
        System.out.println("case_report" + formatter.format(date));

        CaseMap.put("run_fail_case",failedCase);
        CaseMap.put("syntax_error_case",parseErrorCase);

        JSONObject jsonObject = JSONObject.fromObject(CaseMap);
        String jsonString = jsonObject.toString();
        CreateFileUtil.createJsonFile(jsonString, TestController.getReportPath(), filename);

    }
    public static class CreateFileUtil {
        /**
         * 生成.json格式文件
         */
        public static boolean createJsonFile(String jsonString, String filePath, String fileName) {
            // 标记文件生成是否成功
            boolean flag = true;

            // 拼接文件完整路径
            String fullPath = filePath + File.separator + fileName + ".json";

            // 生成json格式文件
            try {
                // 保证创建一个新文件
                File file = new File(fullPath);
                if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
                    file.getParentFile().mkdirs();
                }
                if (file.exists()) { // 如果已存在,删除旧文件
                    file.delete();
                }
                file.createNewFile();

                if(jsonString.indexOf("'")!=-1){
                    //将单引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
                    jsonString = jsonString.replaceAll("'", "\\'");
                }
                if(jsonString.indexOf("\"")!=-1){
                    //将双引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
                    jsonString = jsonString.replaceAll("\"", "\\\"");
                }

                if(jsonString.indexOf("\r\n")!=-1){
                    //将回车换行转换一下，因为JSON串中字符串不能出现显式的回车换行
                    jsonString = jsonString.replaceAll("\r\n", "\\u000d\\u000a");
                }
                if(jsonString.indexOf("\n")!=-1){
                    //将换行转换一下，因为JSON串中字符串不能出现显式的换行
                    jsonString = jsonString.replaceAll("\n", "\\u000a");
                }

                // 格式化json字符串
                jsonString = JsonFormatTool.formatJson(jsonString);

                // 将格式化后的字符串写入文件
                Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
                write.write(jsonString);
                write.flush();
                write.close();
            } catch (Exception e) {
                flag = false;
                e.printStackTrace();
            }

            // 返回是否成功的标记
            return flag;
        }

    }
    public static class JsonFormatTool {
        /**
         * 单位缩进字符串。
         */
        private static String SPACE = "   ";

        /**
         * 返回格式化JSON字符串。
         *
         * @param json 未格式化的JSON字符串。
         * @return 格式化的JSON字符串。
         */
        public static String formatJson(String json) {
            StringBuffer result = new StringBuffer();

            int length = json.length();
            int number = 0;
            char key = 0;

            // 遍历输入字符串。
            for (int i = 0; i < length; i++) {
                // 1、获取当前字符。
                key = json.charAt(i);

                // 2、如果当前字符是前方括号、前花括号做如下处理：
                if ((key == '[') || (key == '{')) {
                    // （1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
                    if ((i - 1 > 0) && (json.charAt(i - 1) == ':')) {
                        result.append('\n');
                        result.append(indent(number));
                    }

                    // （2）打印：当前字符。
                    result.append(key);

                    // （3）前方括号、前花括号，的后面必须换行。打印：换行。
                    result.append('\n');

                    // （4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
                    number++;
                    result.append(indent(number));

                    // （5）进行下一次循环。
                    continue;
                }

                // 3、如果当前字符是后方括号、后花括号做如下处理：
                if ((key == ']') || (key == '}')) {
                    // （1）后方括号、后花括号，的前面必须换行。打印：换行。
                    result.append('\n');

                    // （2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
                    number--;
                    result.append(indent(number));

                    // （3）打印：当前字符。
                    result.append(key);

                    // （4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
                    if (((i + 1) < length) && (json.charAt(i + 1) != ',')) {
                        result.append('\n');
                    }

                    // （5）继续下一次循环。
                    continue;
                }

                // 4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
            /*if ((key == ',')) {
                result.append(key);
                result.append('\n');
                result.append(indent(number));
                continue;
            }*/

                // 5、打印：当前字符。
                result.append(key);
            }

            return result.toString();
        }

        /**
         * 返回指定次数的缩进字符串。每一次缩进三个空格，即SPACE。
         *
         * @param number 缩进次数。
         * @return 指定缩进次数的字符串。
         */
        private static String indent(int number) {
            StringBuffer result = new StringBuffer();
            for (int i = 0; i < number; i++) {
                result.append(SPACE);
            }
            return result.toString();
        }
    }

    public static void recordReport()
    {
        String filename = "case_report.md";
        writeHeaderFile(filename);//写入文件开始的一些格式
        ArrayList<ArrayList<String>> content = RecordAnalysis.readRecord();
        System.out.println("case_report.md");


        System.out.println(content.size());


        int rowNum = content.size();
        int time = 0;
        int cloumnNum = 0;
        for (int i = 0; i < rowNum; i++)
        {
            for (int j = 1; j <= 6; j++)
            {
                // System.out.print(content.get(i).get(j) + " ");
                write("|" + content.get(i).get(j), filename);
            }
            // System.out.println();
            write("|" + FileConstant.LINUX_LINE_FEED, filename);
            time = Integer.valueOf(content.get(i).get(2));
            cloumnNum = content.get(i).size();


        }
        write("\r\n\r\n\r\n", filename);
    }



    /**
     * 报告格式写入
     */
    public static void writeHeaderFile(String filename)
    {
        // String fieName;
        File file = new File(TestController.getReportPath(), filename);
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        String aa = "|case_id|execute_time|final_state|execute_order|state|timestamp|" + "\r\n";
        writeHead(aa, filename);
        aa = "| ------------- | ------------- | ------------- | ------------- | ------------- | ------------- |" + "\r\n";
        write(aa, filename);
    }

    /**
     * 写入报告文件
     * 
     * @param aa 写入信息
     */
    private static void write(String aa, String filename)
    {
        // String fieName;
        File file = new File(TestController.getReportPath() + filename );
        try
        {
            FileWriter fileWritter = new FileWriter(file.getName(), true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            // System.out.println("----------------" + aa);
            bufferWritter.write(aa);
            bufferWritter.flush();
            bufferWritter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }


    /**
     * 写入报告文件
     * 
     * @param aa 写入信息
     */
    private static void writeHead(String aa, String filename)
    {
        // String fieName;
        File file = new File(TestController.getReportPath() + filename);
        try
        {
            FileWriter fileWritter = new FileWriter(file.getName(), false);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            // System.out.println("----------------" + aa);
            bufferWritter.write(aa);
            bufferWritter.flush();
            bufferWritter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
