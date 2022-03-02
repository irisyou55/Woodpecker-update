package edu.ecnu.Woodpecker.manager;

import edu.ecnu.Woodpecker.controller.TestController;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Chen Lixiang
 * @Email: lixiang3608@outlook.com
 * @Date: 2021/11/19
 */
public class PlatformRunner implements Runnable {

    private Thread t;

    // 0 -> 未启动
    // 1 -> 运行中
    // -1 -> 上次运行出错，或被强制退出
    private int status;

    private String[] args;

    /**
     * PlatformRunner 构造函数，创建一个测试平台运行实例
     */
    public PlatformRunner() {
        this.status = 0;
        TestController.exitFlag = new AtomicBoolean(false);
    }

    /**
     * 获取当前平台运行状况 </br>
     * 0 -> 未启动 </br>
     * 1 -> 正在运行中 </br>
     * -1 -> 未启动，且上次运行失败; 或上次运行时被强制退出</br>
     * @return status
     */
    public int getStatus() {
        return status;
    }

    /**
     * 实现 Runnable 接口，请不要直接调用该 API，应当使用 start 方法。
     */
    @Override
    public void run() {
        TestController.exitFlag.set(false);
        try {
            TestController.main(this.args);
            status = 0;
        } catch (Exception e) {
            e.printStackTrace();
            status = -1;
        }
    }

    /**
     * 强制终止当前所有测试运行</br>
     * 可能会导致数据库连接未关闭等问题
     * @return 是否退出成功
     */
    @Deprecated
    public boolean forceStop() {
        if (status != 1) {
            return false;
        }

        t.interrupt();
        t.stop();
        join();
        status = -1;
        return true;
    }

    /**
     * 终止当前测试运行</br>
     * 等到当前 case 运行完成之后才退出，忽略后续所有测试 case
     * @return 是否退出成功
     */
    public boolean stop() {
        if (status != 1) {
            return false;
        }

        if (!TestController.exitFlag.compareAndSet(false, true)) {
            return false;
        }
        join();
        status = 0;
        return true;
    }

    /**
     * 异步启动 Woodpecker
     * @param args 启动命令行参数。目前命令行参数提供两个选项:</br>
     *             -help 打印命令行帮助信息</br>
     *             -syntax_check arg 对测试 case 文件做校验。例如要校验 test_case 下所有 .case 文件的合法性，
     *             则 args 应传入 {"-syntax_check", "test_case"}。
     *             也可以针对某个特定 case 进行校验，例如可以传入 {"-syntax_check", "test_case/test_tpch/1.case"}
     * @return true -> 启动成功; false -> 启动失败(platform已处于运行状态)
     */
    public boolean start(String[] args) {
        if (status == 1) {
            return false;
        }

        status = 1;
        this.args = args;
        t = new Thread(this);
        t.start();

        return true;
    }

    /**
     * 阻塞至 Benchmark Platform 运行结束
     */
    public void join() {
        try {
            if (t != null) t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
