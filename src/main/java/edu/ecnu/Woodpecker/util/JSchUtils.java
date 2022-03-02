package edu.ecnu.Woodpecker.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class JSchUtils {


    /**
     * 连接到指定的IP
     *
     * @throws JSchException
     */
    public static Session connect(String user, String passwd, String host, int port) throws JSchException {
        JSch jsch = new JSch();// 创建JSch对象
        Session session = jsch.getSession(user, host, port);// 根据用户名、主机ip、端口号获取一个Session对象
        session.setPassword(passwd);// 设置密码

        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);// 为Session对象设置properties
        session.setTimeout(60000000);// 设置超时
        session.connect();// 通过Session建立连接
        return session;
    }

    /**
     * 关闭连接
     */
    public static void close(Session session) {
        session.disconnect();
    }

    /**
     * 执行相关的命令
     *
     * @throws JSchException
     */
    public static void execCmd(Session session, String command) throws JSchException {
        BufferedReader reader = null;
        Channel channel = null;
        try {
            if (command != null) {
                channel = session.openChannel("exec");
                ((ChannelExec) channel).setCommand(command);
                // ((ChannelExec) channel).setErrStream(System.err);
                channel.connect();

                InputStream in = channel.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in));
                String buf = null;
                while ((buf = reader.readLine()) != null) {
                    System.out.println(buf);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            channel.disconnect();
        }
    }





    public static void main(String[] args) {
        try {

            // 1.连接到指定的服务器
            Session session = connect("lyqu", "lyqu", "10.11.6.120", 22);

            // 2.执行相关的命令
            execCmd(session, "sysbench --version");

            execCmd(session, "sysbench --test=oltp_read_only --mysql-host=10.11.6.120 --mysql-port=5515 --mysql-user=qswang --mysql-password=qswang --mysql-db=testsysbench --tables=2 --table_size=5000 --threads=20 --max-requests=0 --time=30 --report-interval=3 run");

            // 3.下载文件
            //download("/data/nginx_log.20160707.txt", "D:\\temp");

            // 4.关闭连接
            close(session);
        } catch (JSchException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}