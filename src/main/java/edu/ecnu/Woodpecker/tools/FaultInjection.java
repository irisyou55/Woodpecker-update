package edu.ecnu.Woodpecker.tools;

import edu.ecnu.Woodpecker.constant.LogLevelConstant;
import edu.ecnu.Woodpecker.log.WpLog;
import edu.ecnu.Woodpecker.util.Util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Youshuhong
 * @create 2021/11/16 下午5:13
 */
public class FaultInjection {
    /**
     * servers' IP and SSH port
     */
    private int connectionPort = 22;

    public void seizeCPU(String keyword)
    {
        String[] parts = Util.removeBlankElement(keyword.split("\\[|;|]"));
        String ip = parts[1];
        String user = parts[2];
        Integer cores = Integer.parseInt(parts[3]);
        Integer time = Integer.parseInt(parts[4]);


        String src = "tools/FaultInjection/seizeCPU.sh";
        String dst = "/tmp/seizeCPU.sh";
        Util.put(ip, user, connectionPort, src, dst);

        String cmd = "sh " + dst + " " + cores + " " + time;
        Util.exec(ip, user, connectionPort, cmd);
    }
    public void seizeMEM(String keyword)
    {
        String[] parts = Util.removeBlankElement(keyword.split("\\[|;|]"));
        String ip = parts[1];
        String user = parts[2];
        Integer size = Integer.parseInt(parts[3]);
        Integer time = Integer.parseInt(parts[4]);

        String src = "tools/FaultInjection/seizeMEM";
        String dst = "/tmp/seizeMEM";
        Util.put(ip, user, connectionPort, src, dst);

        String cmd = dst + " " + time + " " + size;
        Util.exec(ip, user, connectionPort, cmd);
    }
    public void seizeDISK(String keyword)
    {
        String[] parts = Util.removeBlankElement(keyword.split("\\[|;|]"));
        String ip = parts[1];
        String user = parts[2];
        Integer IOPS = Integer.parseInt(parts[3]);
        Integer size = Integer.parseInt(parts[4]);
        Integer time = Integer.parseInt(parts[5]);

        String src = "tools/FaultInjection/seizeDISK.sh";
        String dst = "/tmp/seizeDISK.sh";
        Util.put(ip, user, connectionPort, src, dst);

        String cmd = "sh " + dst + " " + IOPS +" " + size + " " + time;
        Util.exec(ip, user, connectionPort, cmd);
    }
    public void seizeNET(String keyword)
    {
        String[] parts = Util.removeBlankElement(keyword.split("\\[|;|]"));
        String transmitIP = parts[1];
        String user1 = parts[2];
        Integer IOPS = Integer.parseInt(parts[3]);
        Integer size = Integer.parseInt(parts[4]);
        String receiveIP = parts[5];
        String user2 = parts[6];
        Integer time = Integer.parseInt(parts[7]);

        String transmitsrc = "tools/FaultInjection/netClient.jar";
        String transmitdst = "/tmp/netClient.jar";
        Util.put(transmitIP, user1, connectionPort, transmitsrc, transmitdst);

        String receivesrc = "tools/FaultInjection/netServer.jar";
        String receivedst = "/tmp/netServer.jar";
        Util.put(receiveIP, user2, connectionPort, receivesrc, receivedst);

        int port = 1234;
        String transmitcmd = "java -jar " + transmitdst + " " + receiveIP + " " + port + " " + IOPS + " " + size + " " + time;
        String receivecmd = "java -jar " + receivedst + " " + port + " " + (time + 5);
        Util.exec(receiveIP, user2, connectionPort, transmitcmd);
        Util.exec(transmitIP, user1, connectionPort, receivecmd);
    }

    public void seizeFILE(String keyword) throws IOException {
        String[] parts = Util.removeBlankElement(keyword.split("\\[|;|]"));
        String ip = parts[1];
        String user = parts[2];
        String path = parts[3];

        String src = "tools/FaultInjection/seizeFILE.sh";
        String dst = "/tmp/seizeFILE.sh";

        Util.put(ip, user, connectionPort, src, dst);
        String cmd = "sh " + dst + " " + path;

        WpLog.recordLog(LogLevelConstant.INFO, Util.exec(ip, user, connectionPort, cmd));
    }
}
