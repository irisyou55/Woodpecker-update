package edu.ecnu.Woodpecker.constant;
/**
 * @description: 支持的Shell调用命令，添加Shell命令的时候，在这里添加
 */
public class ShellConstant
{
    public final static String SCP = "scp -r user@ip:path .; ";
    public final static String DELETE = "rm -rf path ";
    public final static String MKDIR = "mkdir -p dirName ;";
    public final static String OPENDIR = "cd dirName ";
    public final static String EXECRESULT = " && echo exec_successful ||echo exec_unsuccessful ;";
    public final static String LS = "ls ;";
    public final static String FORDISK = "for disk in {1..8}; do ";
    public final static String LN = "ln -s path ;";
    public final static String DONE = "done";

}
