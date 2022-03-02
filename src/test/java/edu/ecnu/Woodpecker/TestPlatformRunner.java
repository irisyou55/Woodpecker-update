package edu.ecnu.Woodpecker;

import edu.ecnu.Woodpecker.manager.PlatformRunner;



/**
 * @Author: Chen Lixiang
 * @Email: lixiang3608@outlook.com
 * @Date: 2021/11/20
 */
public class TestPlatformRunner {

    public static void main(String[] args) {
        PlatformRunner runner = new PlatformRunner();
        runner.start(new String[]{"-syntax_check", "test_case/"});
        runner.stop();
    }

}
