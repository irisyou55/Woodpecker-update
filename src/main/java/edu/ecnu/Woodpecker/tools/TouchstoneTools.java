package edu.ecnu.Woodpecker.tools;

import edu.ecnu.Woodpecker.util.Util;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * @author Youshuhong
 * @create 2021/11/16 下午1:27
 */
public class TouchstoneTools {
    public void SeizeTouchstone(String keyword){
        String cmd = "cd tools/BenchmarkTools/Touchstone/running_examples && bash runAll.sh";
        String netsString = exec(cmd).toString();
        System.out.println("==========获得值=============");
        System.out.println(netsString);

    }


    public static String exec(String cmd) {
        try {
            String[] cmdA = { "/bin/sh", "-c", cmd };
            Process process = Runtime.getRuntime().exec(cmdA);
            LineNumberReader br = new LineNumberReader(new InputStreamReader(
                    process.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
