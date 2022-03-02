package edu.ecnu.Woodpecker.test;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * java在linux环境下执行linux命令，然后返回命令返回值。
 * @author lee
 */
public class ExecLinuxCMD {

    public static Object exec(String cmd) {
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

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //String pwdString = exec("pwd").toString();
        String netsString = exec("cd /Users/irisyou/oltpbench &&./oltpbenchmark -b smallbank -c config/sample_smallbank_config.xml --create=True --load=True --execute=True -s 5 -o outputfile").toString();

        System.out.println("==========获得值=============");
        //System.out.println(pwdString);
        System.out.println(netsString);
    }

}
