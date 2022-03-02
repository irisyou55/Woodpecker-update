package edu.ecnu.Woodpecker.util;

/**
 * @Author: Chen Lixiang
 * @Email: lixiang3608@outlook.com
 * @Date: 2021/11/9
 */
public class ProcessExecInfo {

    private int exitValue;

    private String errMsg;

    private String stdMsg;

    public ProcessExecInfo(int val, String errMsg, String stdMsg) {
        this.exitValue = val;
        this.errMsg = errMsg;
        this.stdMsg = stdMsg;
    }

    public int getExitValue() {
        return exitValue;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public String getStdMsg() {
        return stdMsg;
    }

}
